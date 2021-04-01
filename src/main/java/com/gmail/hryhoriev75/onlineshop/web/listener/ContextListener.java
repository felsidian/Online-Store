package com.gmail.hryhoriev75.onlineshop.web.listener;

import com.gmail.hryhoriev75.onlineshop.db.DBHelper;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Servlet Context Listener
 */
@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // lazy initializing DBHelper
        DBHelper.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        DBHelper.getInstance().shutdown();
    }

}
