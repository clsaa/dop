package com.clsaa.dop.server.message.task;

import com.clsaa.dop.server.message.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author joyren
 */
@Component
public class EmailSendTask {
    @Autowired
    private EmailService emailService;

    /**
     * 上一次执行完毕时间点之后15秒再执行
     */
    @Scheduled(fixedDelay = 15000)
    public void reportCurrentTime() {
        this.emailService.sendAnEmail();
    }
}