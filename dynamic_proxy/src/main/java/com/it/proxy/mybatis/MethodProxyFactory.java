package com.it.proxy.mybatis;

import java.lang.reflect.Proxy;

/**
 * Created by piguanghua on 2017/3/8.
 */
public class MethodProxyFactory {
    public static <T> T newInstance(Class<T> methodInterface) {
        final MethodProxy<T> methodProxy = new MethodProxy<T>(methodInterface);
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{methodInterface},
                methodProxy);
    }
}
