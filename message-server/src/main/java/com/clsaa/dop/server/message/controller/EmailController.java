package com.clsaa.dop.server.message.controller;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.message.model.dto.EmailDtoV1;
import com.clsaa.dop.server.message.mq.MessageQueueException;
import com.clsaa.dop.server.message.mq.MessageSender;
import com.clsaa.dop.server.message.service.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 邮件发送接口实现类
 * 邮件发送的REST API接口将在第三个迭代改为消息队列接收的方式
 *
 * @author joyren
 * @deprecated
 */
@RestController
@CrossOrigin
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageSender messageSender;

    @PostMapping("/test1")
    public String test1() {
        System.out.println("test1");
        return "test1";
    }

    @PostMapping("/test2")
    public String test1(@RequestParam("from") String from) {
        System.out.println("test2" + from);
        return "test2" + from;
    }

    /**
     * 同步发送邮件，仅用于测试SMTP服务器是否能成功发送邮件，业务方需直接通过MQ调用
     */
    @Deprecated
    @ApiOperation(value = "发送邮件", notes = "发送邮件，仅用于测试业务方需通过")
    @PostMapping("/v1/email")
    public void addEmailV1(@ApiParam(value = "发送人", required = true) @RequestParam("from") String from,
                           @ApiParam(value = "接收人", required = true) @RequestParam("to") String to,
                           @ApiParam(value = "主题", required = true) @RequestParam("subject") String subject,
                           @ApiParam(value = "内容", required = true) @RequestParam("text") String text) {
        System.out.println("send an email " + from);
        this.emailService.addEmail(from, to, subject, text);
    }

    @Value("${message.mq.RocketMQ.emailTopic}")
    private String topic;

    /**
     * 通过MQ异步发送邮件，仅用于测试MQ是否可用，业务方需直接通过MQ调用
     * 调用MQ方法可参考本接口实现
     */
    @Deprecated
    @ApiOperation(value = "发送邮件", notes = "发送邮件，邮件内容由业务方自定义")
    @PostMapping("/v2/email")
    public void addEmailV2(@ApiParam(value = "发送人", required = true) @RequestParam("from") String from,
                           @ApiParam(value = "接收人", required = true) @RequestParam("to") String to,
                           @ApiParam(value = "主题", required = true) @RequestParam("subject") String subject,
                           @ApiParam(value = "内容", required = true) @RequestParam("text") String text) {
        EmailDtoV1 email = new EmailDtoV1(from, to, subject, text);
        try {
            this.messageSender.send(topic, UUID.randomUUID().toString(), JSON.toJSONString(email));
        } catch (MessageQueueException e) {
            e.printStackTrace();
        }
    }
}
