package com.clsaa.dop.server.message.mq;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.message.model.dto.EmailDtoV1;
import com.clsaa.dop.server.message.service.EmailService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * RocketMQ消费者
 */
@Component
public class EmailConsumer implements MessageListenerConcurrently {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConsumer.class);

    @Autowired
    private EmailService emailService;

    @Value("${message.mq.RocketMQ.namesrvAddr}")
    private String namesrvAddr;
    @Value("${message.mq.RocketMQ.emailTopic}")
    private String topic;
    @Value("${message.mq.RocketMQ.emailConsumerGroup}")
    private String emailConsumerGroup;


    private DefaultMQPushConsumer consumer = null;

    /**
     * 初始化
     */
    @PostConstruct
    public void start() {
        try {
            LOGGER.info("MQ：启动EmailConsumer, namesrvAddr: [{}],  topic: [{}], emailConsumerGroup: [{}]",
                    namesrvAddr, topic, emailConsumerGroup);
            consumer = new DefaultMQPushConsumer(this.emailConsumerGroup);
            consumer.setNamesrvAddr(namesrvAddr);
            // 从消息队列尾部消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            // 集群消费模式
            consumer.setMessageModel(MessageModel.CLUSTERING);
            // 订阅主题
            consumer.subscribe(topic, "*");
            // 注册消息监听器
            consumer.registerMessageListener(this);
            // 启动消费端
            consumer.start();
        } catch (MQClientException e) {
            LOGGER.error("MQ：启动EmailConsumer失败：{}-{}", e.getResponseCode(), e.getErrorMessage());
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * 消费消息
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        int index = 0;
        try {
            for (; index < msgs.size(); index++) {
                MessageExt msg = msgs.get(index);
                String messageBody = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                LOGGER.info("MQ：EmailConsumer接收新信息: {} {} {} {} {}", msg.getMsgId(), msg.getTopic(), msg.getTags(), msg.getKeys(), messageBody);
                EmailDtoV1 email = JSON.parseObject(messageBody, EmailDtoV1.class);
                this.emailService.addEmail(email.getFrom(), email.getTo(), email.getSubject(), email.getText());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (index < msgs.size()) {
                context.setAckIndex(index + 1);
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @PreDestroy
    public void stop() {
        if (consumer != null) {
            consumer.shutdown();
            LOGGER.error("MQ：关闭EmailConsumer");
        }
    }
}