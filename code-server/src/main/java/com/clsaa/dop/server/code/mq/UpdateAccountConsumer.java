package com.clsaa.dop.server.code.mq;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.code.feign.UserFeign;
import com.clsaa.dop.server.code.model.dto.user.UserDto;
import com.clsaa.dop.server.code.service.UserService;
import com.clsaa.dop.server.code.util.crypt.CryptoResult;
import com.clsaa.dop.server.code.util.crypt.RSA;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * 修改密码消息消费类
 * @author wsy
 */
@Component
public class UpdateAccountConsumer implements MessageListenerConcurrently {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFeign userFeign;

    @Value("${message.mq.RocketMQ.namesrvAddr}")
    private String namesrvAddr;
    @Value("${message.mq.RocketMQ.updateAccountTopic}")
    private String topic;
    @Value("${message.mq.RocketMQ.updateAccountConsumerGroup}")
    private String consumerGroup;

    private DefaultMQPushConsumer consumer = null;

    /**
     * 初始化
     */
    @PostConstruct
    public void start() {
        try {
            consumer = new DefaultMQPushConsumer(this.consumerGroup);
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
                System.out.println(messageBody);
                UserDto userDto=JSON.parseObject(messageBody,UserDto.class);
                CryptoResult cryptoResult=RSA.decryptByPublicKey(userDto.getPassword(),userFeign.getAccountRSAPublicKey());
                userService.updateUserPassword(userDto.getName(),cryptoResult.getContent());

            }
        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
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
//            LOGGER.error("MQ：关闭EmailConsumer");
        }
    }


}
