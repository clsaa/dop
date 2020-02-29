package com.clsaa.dop.server.user.mq;

/**
 * <p>
 * 消息发送者接口, 对不同消息队列发送消息进行统一
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2019-02-15
 */
public interface MessageSender {
    /**
     * <p>
     * 发送消息接口, 不同消息队列发送消息的抽象
     * </p>
     *
     * @param queue     MQ中的queue或topic
     * @param messageId 消息id, 用于唯一标识某条消息
     * @param data      消息数据(消息内容/消息body), 一般为JSON格式的字符串
     * @summary 发送消息
     * @author 任贵杰 812022339@qq.com
     * @since 2019-02-15
     */
    void send(String queue, String messageId, String data) throws MessageQueueException;
}
