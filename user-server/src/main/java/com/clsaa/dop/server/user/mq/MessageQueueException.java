package com.clsaa.dop.server.user.mq;

/**
 * <p>
 * MQ自定义异常
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018/9/1
 */
public class MessageQueueException extends Exception {
    public MessageQueueException() {
        super();
    }

    public MessageQueueException(String message) {
        super(message);
    }

    public MessageQueueException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageQueueException(Throwable cause) {
        super(cause);
    }

    protected MessageQueueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}