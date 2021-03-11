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
import com.wwcai.crm.workbench.domain.ActivityRemark;
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
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(request, response);
        } else if ("/workbench/activity/update.do".equals(path)) {
            update(request, response);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(request, response);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        } else if ("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setId(id);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);

        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.updateRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注的操作");
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录人
        String createBy =
                ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setId(id);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.saveRemark(ar);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");
        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean falg = as.deleteRemark(id);

        PrintJson.printJsonFlag(response, falg);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动ID，取得备注信息");

        String activityId = request.getParameter("activityId");
        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response, arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入 跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a = as.detail(id);
        request.setAttribute("a", a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行市场活动修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改人：当前登录人
        String editBy =
                ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as =
                (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.update(a);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入查询用户信息列表和根据市场活动ID查询单条记录的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            uList
            a
            用的不多  用map集合

         */

        Map<String, Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response, map);
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

        System.out.println("执行市场活动的更改");

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
