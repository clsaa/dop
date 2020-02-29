package com.clsaa.dop.server.message.service;

import com.clsaa.dop.server.message.dao.EmailRepository;
import com.clsaa.dop.server.message.model.po.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sample;

/**
 * 邮件业务实现类
 *
 * @author joyren
 */
@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${spring.mail.username}")
    private String mailUsername;

    /**
     * 添加邮件
     *
     * @param from    发送人
     * @param to      接收人
     * @param subject 主题
     * @param text    内容
     */
    public void addEmail(String from, String to, String subject, String text) {
        //未来支持多租户
        if (!from.equals(this.mailUsername)) {
            from = this.mailUsername;
        }
        Email email = Email.builder().from(from)
                .to(to)
                .subject(subject)
                .text(text)
                .retry(0)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .status(Email.Status.SENDING)
                .build();
        email = this.emailRepository.save(email);
        this.sendEmail(email);
    }

    public void sendAnEmail() {
        LOGGER.info("GET AN EMAIL TO SEND!");
        Aggregation aggregation = Aggregation.newAggregation(sample(1), match(Criteria.where("status").is(Email.Status.SENDING)));
        AggregationResults<Email> email = this.mongoTemplate.aggregate(aggregation, "email", Email.class);
        if (email.getUniqueMappedResult() != null) {
            sendEmail(email.getUniqueMappedResult());
        } else {
            LOGGER.info("NO EMAIL TO SEND!");
        }
    }

    /**
     * 发送邮件
     *
     * @param email 电子邮件
     */
    private void sendEmail(Email email) {
        try {
            LOGGER.info("SEND EMAIL {}", email.toString());
            this.sendSimpleEmail(email.getFrom(), email.getTo(), email.getSubject(), email.getText());
            email.setStatus(Email.Status.SENT);
            email.setMtime(LocalDateTime.now());
            this.emailRepository.save(email);
        } catch (MailException e) {
            e.printStackTrace();
            email.setRetry(email.getRetry() + 1);
            if (email.getRetry() >= Email.MAX_RETRY) {
                email.setStatus(Email.Status.CANCELED);
                email.setMtime(LocalDateTime.now());
            }
            this.emailRepository.save(email);
        }
    }

    /**
     * 发送简单邮件
     *
     * @param from    发送人
     * @param to      接收人
     * @param subject 主题
     * @param text    内容
     */
    private void sendSimpleEmail(String from, String to, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        mailSender.send(email);
    }
}
