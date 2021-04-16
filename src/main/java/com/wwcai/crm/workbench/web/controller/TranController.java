package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.settings.service.impl.UserServiceImpl;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.PrintJson;
import com.wwcai.crm.utils.ServiceFactory;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.domain.Contacts;
import com.wwcai.crm.workbench.domain.Tran;
import com.wwcai.crm.workbench.domain.TranHistory;
import com.wwcai.crm.workbench.service.ActivityService;
import com.wwcai.crm.workbench.service.ContactsService;
import com.wwcai.crm.workbench.service.CustomerService;
import com.wwcai.crm.workbench.service.TranService;
import com.wwcai.crm.workbench.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException
            , IOException {

        System.out.println("进入交易控制器");

        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        } else if ("/workbench/transaction/getActivityByName.do".equals(path)) {
            getActivityByName(request, response);
        } else if ("/workbench/transaction/getContactsByName.do".equals(path)) {
            getContactsByName(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        } else if ("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/transaction/getHistoryByTranId.do".equals(path)) {
            getHistoryByTranId(request, response);
        } else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        } else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request, response);
        }


    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得交易阶段数量统计图标的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String, Object> map = ts.getChrats();

        PrintJson.printJsonObj(response, map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("改变交易阶段");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String expectedDate = request.getParameter("expectedDate");
        String money = request.getParameter("money");
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);

        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("t",t);

        PrintJson.printJsonObj(response, map);
    }

    private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("通过交易ID查询历史列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thlist = ts.getHistoryByTranId(tranId);

        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        for(TranHistory th : thlist) {

            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }

        PrintJson.printJsonObj(response, thlist);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        String stage = t.getStage();
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行添加交易操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        // 此处暂时只有名称，没有Id
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setContactsId(contactsId);
        t.setOwner(owner);
        t.setContactSummary(contactSummary);
        t.setDescription(description);
        t.setNextContactTime(nextContactTime);
        t.setSource(source);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setActivityId(activityId);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setName(name);
        t.setMoney(money);
        t.setType(type);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);

        if(flag) {

            response.sendRedirect(request.getContextPath() + "/workbench" +
                    "/transaction/index.jsp");

        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表（按照名称模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> slist = cs.getCustomerName(name);
        PrintJson.printJsonObj(response, slist);


    }

    private void getContactsByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("通过名称模糊查询联系人名称");

        String cname = request.getParameter("cname");

        ContactsService cs =
                (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> clist = cs.getContactsByName(cname);

        PrintJson.printJsonObj(response, clist);
    }

    private void getActivityByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("通过名称模糊查询市场活动");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> alist = as.getActivityByName(aname);

        PrintJson.printJsonObj(response, alist);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入跳转到交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> ulist = us.getUserList();

        request.setAttribute("ulist", ulist);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
