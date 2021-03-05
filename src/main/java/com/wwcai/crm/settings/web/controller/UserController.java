package com.wwcai.crm.settings.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.settings.service.impl.UserServiceImpl;
import com.wwcai.crm.utils.MD5Util;
import com.wwcai.crm.utils.PrintJson;
import com.wwcai.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException
            , IOException {

        System.out.println("进入用户控制器");

        String path = request.getServletPath();
        if("/settings/user/login.do".equals(path)) {
            login(request, response);
        } else if("/settings/user/xxx.do".equals(path)) {
            //xxx(request, response);
        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入验证登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        // 将密码的明文形式装换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接受浏览器端的IP地址
        String ip = request.getRemoteAddr();
        System.out.println("ip = " + ip);

        // 未来业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user = us.login(loginAct, loginPwd, ip);

            request.getSession().setAttribute("user", user);
            // 如果执行到此处，说明业务层 没有为controller抛出异常 登录成功
            // 为前端提供{"success" ： true}
            PrintJson.printJsonFlag(response, true);
        } catch (Exception e) {
            e.printStackTrace();

            // 一旦程序执行了catch块，表示业务层为controller抛出了异常 登录失败
            // 为前端提供{"success" ： false, "msg" : ?}
            String msg = e.getMessage();
            /*
                作为controller 向ajax请求提供多项信息
                    两种方式：
                        (1):将多项信息打包成map，将map解析为json串
                        (2):创建一个Vo
                            private boolean success;
                            private String msg;
                        如果对展现的信息将来还要大量使用，则创建一个Vo类，方便使用
                        如果对展现的信息只是这个需求中使用，则用map
             */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }

    }

}
