package com.wwcai.crm.settings.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException
            , IOException {

        String path = request.getContextPath();
        if("/settings/user/xxx.do".equals(path)) {
            xxx(request, response);
        } else if("/settings/user/xxx.do".equals(path)) {
            xxx(request, response);
        }

    }

    private void xxx(HttpServletRequest request, HttpServletResponse response) {
    }
}
