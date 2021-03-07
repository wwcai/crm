package com.wwcai.crm.workbench.web.controller;

import com.wwcai.crm.settings.domain.User;
import com.wwcai.crm.settings.service.UserService;
import com.wwcai.crm.settings.service.impl.UserServiceImpl;
import com.wwcai.crm.utils.DateTimeUtil;
import com.wwcai.crm.utils.PrintJson;
import com.wwcai.crm.utils.ServiceFactory;
import com.wwcai.crm.utils.UUIDUtil;
import com.wwcai.crm.vo.PaginationVo;
import com.wwcai.crm.workbench.domain.Activity;
import com.wwcai.crm.workbench.service.ActivityService;
import com.wwcai.crm.workbench.service.impl.ActivityServiceImpl;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException
            , IOException {

        System.out.println("进入市场活动控制器");

        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(request, response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(request, response);
        }

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的删除操作");

        String ids[] = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean falg = as.delete(ids);
        PrintJson.printJsonFlag(response, falg);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询市场活动信息列表的操作（条件查询 + 分页查询）");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        // 计算出略过的记录数
        int skipCount = (pageNo - 1) * pageSize;
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            前端需要：市场活动信息列表和查询总条数

                业务层拿到以上信息，做返回
                map
                    map.put("datalist":datalist)
                    map.put("total":total)
                    PrintJSON map -->json
                    {"total":10, "datalist":[{市场活动1}.{2},{3}...]}

                将来分页查询 每个模块都有，使用通用的VO 方便
                vo
                    PaginationVO<T>
                        private int total;
                        private List<T> datalist;
                PaginationVO<Activity> vo = new PaginationVO<>;
                vo.setTotal(total);
                vo.setDatalist(datalist);
                PrintJSON vo -->json
                {"total":10, "datalist":[{市场活动1}.{2},{3}...]}

         */

        PaginationVo<Activity> vo = as.pageList(map);
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动的添加");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录人
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.save(a);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us =
                (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response, uList);
    }

}
