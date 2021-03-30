package com.gmail.hryhoriev75.onlineshop.web.controller.admin;

import com.gmail.hryhoriev75.onlineshop.model.OrderDAO;
import com.gmail.hryhoriev75.onlineshop.model.UserDAO;
import com.gmail.hryhoriev75.onlineshop.model.entity.Order;
import com.gmail.hryhoriev75.onlineshop.model.entity.User;
import com.gmail.hryhoriev75.onlineshop.web.Path;
import com.gmail.hryhoriev75.onlineshop.web.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UpdateServlet", value = Path.UPDATE_PATH)
public class UpdateServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect(Path.CATALOG_PATH);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String what = RequestUtils.getStringParameter(request, "what");
        if ("status".equals(what)) {
            int statusId = RequestUtils.getIntParameter(request, "statusId");
            long orderId = RequestUtils.getLongParameter(request, "orderId");
            if (statusId > 0 && statusId <= 4 && orderId > 0) {
                OrderDAO.updateStatus(orderId, statusId);
                response.sendRedirect(Path.ORDER_PATH + "?id=" + orderId);
                return;
            }
        }
//        else if ("block".equals(what)) {
//            int blocked = RequestUtils.getIntParameter(request, "blocked");
//            long userId = RequestUtils.getLongParameter(request, "userId");
//            if (userId > 0) {
//                UserDAO.updateBlock(userId, blocked != 0);
//                response.sendRedirect(Path.ORDER_PATH + "?id=" + orderId);
//            }
//        }
        response.sendRedirect(Path.CATALOG_PATH);
    }

}