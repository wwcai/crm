package com.wwcai.crm.web.listener;

import com.wwcai.crm.settings.domain.DicValue;
import com.wwcai.crm.settings.service.DicService;
import com.wwcai.crm.settings.service.impl.DicServiceImpl;
import com.wwcai.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    /**
     * 该方法用来监听上下文域对象的方法，服务器启动，上下文域对象创建
     * 对象创建完毕，马上执行该方法
     * @param event 该参数能够取得坚挺的对象
     *              监听的是什么对象，就可以通过该参数取得什么对象
     */
    public void contextInitialized(ServletContextEvent event){

        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        /**
         * 向业务层要7个list
         *
         * 打包为一个map
         *
         * */

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();
        // 将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key, map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");

    }
}
