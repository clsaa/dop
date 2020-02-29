package com.clsaa.dop.server.image.mq;


import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.image.feign.UserFeign;
import com.clsaa.dop.server.image.feign.harborfeign.HarborUserFeign;
import com.clsaa.dop.server.image.model.dto.UserDto1;
import com.clsaa.dop.server.image.model.enumtype.UserCredentialType;
import com.clsaa.dop.server.image.model.po.User;
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
import java.util.UUID;

/**
 * <p>
 * 注册消息消费类
 * </p>
 *
 * @author xzt
 * @since 2019-3-28
 */

@Component
public class RegisterConsumer implements MessageListenerConcurrently {


    @Value("${message.mq.RocketMQ.namesrvAddr}")
    private String namesrvAddr;
    @Value("${message.mq.RocketMQ.registerAccountTopic}")
    private String topic;
    @Value("${message.mq.RocketMQ.registerConsumerGroup}")
    private String registerConsumerGroup;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private HarborUserFeign harborUserFeign;


    private DefaultMQPushConsumer consumer = null;

    /**
     * 初始化
     */
    @PostConstruct
    public void start() {
        try {
            consumer = new DefaultMQPushConsumer(this.registerConsumerGroup);
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
                UserDto1 userDto1 = JSON.parseObject(messageBody, UserDto1.class);
                Long id = userDto1.getId();
                String username = userDto1.getName();
                String email = userDto1.getEmail();

                String password = id.toString().length() > 8 ? id.toString().substring(0, 8) : id
                        + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase()
                        + UUID.randomUUID().toString().replace("-", "").substring(0, 6).toLowerCase();
                System.out.println(password);
                userFeign.addUserCredential(userDto1.getId(), username,
                        password, UserCredentialType.DOP_INNER_HARBOR_LOGIN_EMAIL);
                //在harbor中注册账户
                User user = new User(id.intValue(), username, email, password, username, "", false, "", 0, false, "", "", "2018-12-31T17:39:18Z", "2018-12-31T17:39:18Z");
                harborUserFeign.usersPost(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
    }
}
