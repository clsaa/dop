package com.clsaa.dop.server.message.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * RocketMQ发送消息实现类
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2019-02-15
 */
public class RocketMQMessageSender implements MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQMessageSender.class);

    private DefaultMQProducer producer;

    @Value("${message.mq.RocketMQ.namesrvAddr}")
    private String namesrvAddr;
    @Value("${message.mq.RocketMQ.groupName}")
    private String groupName;
    @Value("${message.mq.RocketMQ.instanceName}")
    private String instanceName;
    @Value("${message.mq.RocketMQ.maxMessageSize}")
    private Integer maxMessageSize;
    @Value("${message.mq.RocketMQ.sendMessageTimeout}")
    private Integer sendMessageTimeout;


    /**
     * <p>
     * 发送消息,RocketMQ中所需tags由send函数实现自行决定
     * </p>
     *
     * @param queue     RocketMQ中的topic
     * @param messageId RocketMQ中的key
     * @param data      RocketMQ中的body
     * @see MessageSender#send(String, String, String)
     */
    @Override
    public void send(String queue, String messageId, String data) throws MessageQueueException {
        try {
            byte[] body = data.getBytes(RemotingHelper.DEFAULT_CHARSET);
            this.send(queue, queue, messageId, body);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(String.format("RocketMQ producer send fail! topic:[%s],key:[%s], error:[%s]"
                    , queue, messageId, e.getMessage()));
            throw new MessageQueueException(e);
        }

    }

    /**
     * <p>
     * 发送消息
     * </p>
     *
     * @param topic 主题,相当于queue
     * @param tags  标记,二级分类
     * @param keys  key业务唯一标记
     * @param body  消息内容
     */
    private void send(String topic, String tags, String keys, byte[] body) throws MessageQueueException {
        Message message = new Message(topic, tags, keys, body);
        try {
            this.producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOGGER.info("RocketMQ producer send success! {}", sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    LOGGER.error(String.format("RocketMQ producer send fail! topic:[%s],key:[%s], error:[%s]"
                            , topic, keys, throwable.getMessage()));
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("RocketMQ producer send fail! topic:[%s],key:[%s], error:[%s]"
                    , topic, keys, e.getMessage()));
            throw new MessageQueueException(e);
        }
    }

    /**
     * 初始化RocketMQ生产者
     *
     * @author 任贵杰 812022339@qq.com
     * @since 2018/9/1
     */
    @PostConstruct
    public void start() throws MessageQueueException {
        if (StringUtils.isEmpty(this.groupName)) {
            throw new MessageQueueException("groupName is empty");
        }
        if (StringUtils.isEmpty(this.namesrvAddr)) {
            throw new MessageQueueException("nameServerAddr is empty");
        }
        if (StringUtils.isEmpty(this.instanceName)) {
            throw new MessageQueueException("instanceName is empty");
        }

        this.producer = new DefaultMQProducer(this.groupName);
        this.producer.setNamesrvAddr(this.namesrvAddr);
        this.producer.setInstanceName(instanceName);
        this.producer.setMaxMessageSize(this.maxMessageSize);
        this.producer.setSendMsgTimeout(this.sendMessageTimeout);

        try {
            this.producer.start();
            LOGGER.info(String.format("RocketMQ producer is running ! groupName:[%s],namesrvAddr:[%s]"
                    , this.groupName, this.namesrvAddr));
        } catch (MQClientException e) {
            LOGGER.error(String.format("RocketMQ producer start fail! groupName:[%s],namesrvAddr:[%s],error:[%s]"
                    , this.groupName, this.namesrvAddr, e.getMessage()));
            throw new MessageQueueException(e);
        }
    }


    /**
     * 关闭MQ生产者
     */
    @PreDestroy
    public void stop() {
        if (this.producer != null) {
            this.producer.shutdown();
            LOGGER.info(String.format("RocketMQ producer is shutdown ! groupName:[%s],namesrvAddr:[%s]"
                    , this.groupName, this.namesrvAddr));
        }
    }
}
