package com.clsaa.dop.server.pipline.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 用于查找已注册的Spring Bean，主要用于消除Spring基于代理实现的AOP this.method()调用不生效的缺陷；
 *
 * @author 任贵杰 812022339@qq.com
 */
public class Services {

    private static ApplicationContext delegateContext;

    static void setContext(ApplicationContext applicationContext) {
        delegateContext = applicationContext;
    }

    /**
     * 此方法只能查询到单个类型为{@code cls}的Bean，如果某个class有多个实现类被Spring托管，则会报错；
     *
     * @param cls Bean的类型
     */
    public static <T> T of(Class<T> cls) {
        return delegateContext.getBean(cls);
    }

    /**
     * 查找特定类型、特定名字的Spring Bean
     *
     * @param beanName Bean的名字
     * @param cls      期望返回的类型
     * @author 任贵杰
     */
    public static <T> T withName(String beanName, Class<T> cls) {
        return delegateContext.getBean(beanName, cls);
    }

    /**
     * 利用Spring提供的生命周期接口配置{@link Services#setContext(ApplicationContext)}
     *
     * @author 任贵杰
     */
    @Configuration
    static class ServicesConfig implements ApplicationContextAware {
        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            Services.setContext(applicationContext);
        }
    }
}
