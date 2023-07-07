package com.example.btl_web.controller.admin.edit;
import com.example.btl_web.constant.Constant.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = Admin.CATEGORIES_PAGE + Admin.EDIT + "/*")
public class EditCategoryController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfor = req.getPathInfo();

        req.setAttribute("path_infor", pathInfor);
        RequestDispatcher rd = req.getRequestDispatcher(Admin.CATEGORY_EDIT_JSP);
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
