package com.it.proxy.mybatis;

/**
 * Created by piguanghua on 2017/3/8.
 */
public class MethodMain {
    public static void main(String args[]) {
        MethodInterface method = MethodProxyFactory.newInstance(MethodInterface.class);
        method.helloWorld();
    }
}
