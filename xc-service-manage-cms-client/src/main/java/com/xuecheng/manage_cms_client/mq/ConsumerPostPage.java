package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听MQ，接收页面发布消息
 * @author Administrator
 * @version 1.0
 **/
@Component
public class ConsumerPostPage {

    private static  final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);
    @Autowired
    PageService pageService;

    @RabbitListener(queues = {"queue_cms_postpage_01"})
    public void postPage(String msg){
        //解析消息
        Map map = JSON.parseObject(msg,Map.class);
        //得到消息中的pageId
        String pageId = (String) map.get("pageId");
        //验证pageId是否合法
        CmsPage cmsPage = pageService.findCmsPageById(pageId);
        if (cmsPage==null){
            LOGGER.error("receive postPage msg,cmsPage is null,pageId:{}",pageId);
            return;
        }
        pageService.savePageToServerPath(pageId);
    }
}
