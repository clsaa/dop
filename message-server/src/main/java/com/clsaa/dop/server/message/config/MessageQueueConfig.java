package com.clsaa.dop.server.message.config;

import com.clsaa.dop.server.message.mq.MessageSender;
import com.clsaa.dop.server.message.mq.RocketMQMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 消息队列配置类, 根据配置maat.mq.name决定初始化哪个MQ的{@link MessageSender}实现
 * @author joyren
 */
@SpringBootConfiguration
public class MessageQueueConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueueConfig.class);

    @Bean(name = "MessageSender")
    public MessageSender createMessageSender() {
        LOGGER.info("begin init message queue config RocketMQ");
        return new RocketMQMessageSender();
    }
}
