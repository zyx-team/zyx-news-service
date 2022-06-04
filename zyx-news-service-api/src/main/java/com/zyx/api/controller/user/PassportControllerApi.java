package com.zyx.api.controller.user;

import com.zyx.grace.result.GraceJSONResult;
import com.zyx.pojo.bo.RegistLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "用户注册登录PassportController", tags = {"用户注册登录的PassportController"})
@RequestMapping("passport")
public interface PassportControllerApi {

    /**
     * @Description: 获得短信验证码
     * @Param [mobile, request]
     * @Return com.zyx.grace.result.GraceJSONResult
     * @Author: zhangyaxin
     * @Create: 2022/6/1 22:18
     */
    @ApiOperation(value = "获得短信验证码", notes = "获得短信验证码", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);

    /**
     * @Description: 一键注册登录接口
     * @Param [registLoginBO, request, response]
     * @Return com.zyx.grace.result.GraceJSONResult
     * @Author: zhangyaxin
     * @Create: 2022/6/1 22:18
     */
    @ApiOperation(value = "一键注册登录接口", notes = "一键注册登录接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    public GraceJSONResult doLogin(@RequestBody @Valid RegistLoginBO registLoginBO,
//                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response);

    /**
     * @Description: 用户退出登录
     * @Param [userId, request, response]
     * @Return com.zyx.grace.result.GraceJSONResult
     * @Author: zhangyaxin
     * @Create: 2022/6/1 22:18
     */
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public GraceJSONResult logout(@RequestParam String userId,
                                   HttpServletRequest request,
                                   HttpServletResponse response);
}
