package com.it.proxy.mybatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by piguanghua on 2017/3/8.
 */
public class MethodProxy<T> implements InvocationHandler {
    private Class<T> methodInterface;

    public MethodProxy(Class<T> methodInterface) {
        this.methodInterface = methodInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("=========================");
        System.out.println("方法名:" + method.getName());
        //针对不同的方法进行不同的操作
        return null;
    }
}
