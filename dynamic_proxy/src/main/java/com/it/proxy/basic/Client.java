package com.it.proxy.basic;

/**
 * Created by piguanghua on 2017/1/31.
 */
public class Client {
    public static void main(String args[]) {
        UserService userService =  new MyInvocationHandler(new UserServiceImpl()).getProxy();
        userService.add();
    }
}
