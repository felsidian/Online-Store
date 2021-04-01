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

/**
 * Service controller for updating entities
 * Available only for admin
 */
@WebServlet(name = "UpdateServlet", value = Path.UPDATE_PATH)
public class UpdateServlet extends HttpServlet {

    public static final String WHAT_PARAMETER = "what";
    public static final String BLOCK_ACTION = "block";
    public static final String UNBLOCK_ACTION = "unblock";
    public static final String USER_ID_PARAMETER = "userId";
    public static final String STATUS_ID_PARAMETER = "statusId";
    public static final String ORDER_ID_PARAMETER = "orderId";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String what = RequestUtils.getStringParameter(request, WHAT_PARAMETER);
        if (BLOCK_ACTION.equals(what) || UNBLOCK_ACTION.equals(what)) {
            long userId = RequestUtils.getLongParameter(request, USER_ID_PARAMETER);
            UserDAO.blockUser(userId, BLOCK_ACTION.equals(what));
            response.sendRedirect(Path.USERS_PATH);
            return;
        }
        response.sendRedirect(Path.CATALOG_PATH);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String what = RequestUtils.getStringParameter(request, WHAT_PARAMETER);
        if ("status".equals(what)) {
            // if status parameter were sent updating order's status
            int statusId = RequestUtils.getIntParameter(request, STATUS_ID_PARAMETER);
            long orderId = RequestUtils.getLongParameter(request, ORDER_ID_PARAMETER);
            if (statusId > 0 && statusId <= 4 && orderId > 0) {
                OrderDAO.updateStatus(orderId, statusId);
                response.sendRedirect(Path.ORDER_PATH + "?id=" + orderId);
                return;
            }
        }
        response.sendRedirect(Path.CATALOG_PATH);
    }

}