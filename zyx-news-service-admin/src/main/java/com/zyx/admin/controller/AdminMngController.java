package com.zyx.admin.controller;

import com.zyx.admin.service.AdminUserService;
import com.zyx.api.BaseController;
import com.zyx.api.controller.admin.AdminMngControllerApi;
import com.zyx.api.controller.user.HelloControllerApi;
import com.zyx.enums.FaceVerifyType;
import com.zyx.exception.GraceException;
import com.zyx.grace.result.GraceJSONResult;
import com.zyx.grace.result.ResponseStatusEnum;
import com.zyx.pojo.AdminUser;
import com.zyx.pojo.bo.AdminLoginBO;
import com.zyx.pojo.bo.NewAdminBO;
import com.zyx.utils.FaceVerifyUtils;
import com.zyx.utils.PagedGridResult;
import com.zyx.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AdminMngController.class);

    @Autowired
    private RedisOperator redis;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FaceVerifyUtils faceVerifyUtils;

    /**
     * @Description: admin用户登录
     * @Param [adminLoginBO, request, response]
     * @Return com.zyx.grace.result.GraceJSONResult
     * @Author: zhangyaxin
     * @Create: 2021/7/3 19:52
     */
    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        // 0. TODO 验证BO中的用户名和密码不为空

        // 1. 查询admin用户的信息
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        // 2. 判断admin不为空，如果为空则登录失败
        if (admin == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }

        // 3. 判断密码是否匹配
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword());
        if (isPwdMatch) {
            doLoginSettings(admin, request, response);
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    /**
     * @Description: 用于admin用户登录过后的基本信息设置
     * @Param [admin, request, response]
     * @Return void
     * @Author: zhangyaxin
     * @Create: 2021/7/3 19:52
     */
    private void doLoginSettings(AdminUser admin,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        // 保存token放入到redis中
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(), token);

        // 保存admin登录基本token信息到cookie中
        setCookie(request, response, "atoken", token, COOKIE_MONTH);
        setCookie(request, response, "aid", admin.getId(), COOKIE_MONTH);
        setCookie(request, response, "aname", admin.getAdminName(), COOKIE_MONTH);
    }

    /**
     * @Description: 判断admin用户名是否存在
     * @Param [username]
     * @Return com.zyx.grace.result.GraceJSONResult
     * @Author: zhangyaxin
     * @Create: 2022/6/5 14:36
     */
    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceJSONResult.ok();
    }

    private void checkAdminExist(String username) {
        AdminUser admin = adminUserService.queryAdminByUsername(username);

        if (admin != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO newAdminBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        // 0. TODO 验证BO中的用户名和密码不为空

        // 1. base64不为空，则代表人脸入库，否则需要用户输入密码和确认密码
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword()) ||
                    StringUtils.isBlank(newAdminBO.getConfirmPassword())
            ) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }

        // 2. 密码不为空，则必须判断两次输入一致
        if (StringUtils.isNotBlank(newAdminBO.getPassword())) {
            if (!newAdminBO.getPassword()
                    .equalsIgnoreCase(newAdminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        // 3. 校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());

        // 4. 调用service存入admin信息
        adminUserService.createAdminUser(newAdminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = adminUserService.queryAdminList(page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        // 从redis中删除admin的会话token
        redis.del(REDIS_ADMIN_TOKEN + ":" + adminId);

        // 从cookie中清理adming登录的相关信息
        deleteCookie(request, response, "atoken");
        deleteCookie(request, response, "aid");
        deleteCookie(request, response, "aname");

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminFaceLogin(AdminLoginBO adminLoginBO,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {

        // 0. 判断用户名和人脸信息不能为空
        if (StringUtils.isBlank(adminLoginBO.getUsername())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }
        String tempFace64 = adminLoginBO.getImg64();
        if (StringUtils.isBlank(tempFace64)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }

        // 1. 从数据库中查询出faceId
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        String adminFaceId = admin.getFaceId();

        if (StringUtils.isBlank(adminFaceId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }

        // 2. 请求文件服务，获得人脸数据的base64数据
        String fileServerUrlExecute
                = "http://files.imoocnews.com:8004/fs/readFace64InGridFS?faceId=" + adminFaceId;
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(fileServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        String base64DB = (String)bodyResult.getData();


        // 3. 调用阿里ai进行人脸对比识别，判断可信度，从而实现人脸登录
        boolean result = faceVerifyUtils.faceVerify(FaceVerifyType.BASE64.type,
                tempFace64,
                base64DB,
                60);

        if (!result) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }

        // 4. admin登录后的数据设置，redis与cookie
        doLoginSettings(admin, request, response);

        return GraceJSONResult.ok();
    }
}
