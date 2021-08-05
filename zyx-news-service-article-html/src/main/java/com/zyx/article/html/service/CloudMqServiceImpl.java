package com.zyx.article.html.service;

import com.halo.core.init.InitializingTask;
import com.newcore.pcms.outreach.service.api.cloumq.SmdsCloudMqListenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class CloudMqServiceImpl extends InitializingTask {


    @Override
    public void execute(ApplicationContext context) {
        System.out.println("ceshi InitializingBean");
    }

    @Override
    public int getOrder() {
        return 0;
    }

    static {

    }
}
