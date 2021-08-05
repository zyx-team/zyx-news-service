//package com.zyx.demo;
//
//import com.halo.core.init.InitializingTask;
//import com.newcore.pcms.outreach.service.api.cloumq.SmdsCloudMqListenService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//
//public class CloudMqServiceImpl extends InitializingTask {
//
//    @Autowired
//    SmdsCloudMqListenService smdsCloudMqListenService;
//    @Override
//    public void execute(ApplicationContext context) {
//        smdsCloudMqListenService.start("personinfo");
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//
//    static {
//
//    }
//}
