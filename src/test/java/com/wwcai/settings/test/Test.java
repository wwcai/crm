package com.wwcai.settings.test;

import com.wwcai.crm.utils.DateTimeUtil;


public class Test {

    @org.junit.Test
    public void test1(){

        // 验证失效时间

        // 失效时间
        String experiTime = "2025-03-04 16:11:11";
        // 当前系统时间
        String currentTime = DateTimeUtil.getSysTime();

        int i = experiTime.compareTo(currentTime);
        System.out.println(i);

    }

    @org.junit.Test
    public void test2() {
        String lockState = "0";
        if("0".equals(lockState)) {
            System.out.println("账号以锁定");
        }
    }

    @org.junit.Test
    public void test3() {
        String ip = "192.168.1.1";
        String allowIp = "192.168.1.1, 192.168.2.1";
        if(allowIp.contains(ip)) {
            System.out.println("有效IP，允许访问");
        } else {
            System.out.println("无效IP，拒绝访问");
        }
    }
}
