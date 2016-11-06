package com.kaushik.restapi;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by kaushik on 29/10/16.
 */
public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int id = NetworkHelper.readUrlValues(req.getRequestURL().toString(), "employee");

        if (id != NetworkHelper.URL_FIND_ALL) {
            String response = EmployeeDataStoreHelper.getInstance().retrieveEmployeeEntity(id,req.getContentType());
            if (response != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(response);

            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            String employeelist = EmployeeDataStoreHelper.getInstance().getAllEmployees(req.getContentType());
            if (employeelist != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(employeelist);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("No employees");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int responseCode;
        try {
            String request = NetworkHelper.readRequest(req);
            responseCode = EmployeeDataStoreHelper.getInstance().addEmployeeEntity(request, req.getContentType());
        } catch (JSONException | ParserConfigurationException | SAXException e) {
            responseCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        resp.setStatus(responseCode);
        resp.setHeader("Location", req.getServletPath());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = NetworkHelper.readUrlValues(req.getRequestURL().toString(), "employee");
        String request = NetworkHelper.readRequest(req);
        int status = EmployeeDataStoreHelper.getInstance().updateEmployeeEntity(id, request);
        resp.setStatus(status);
        resp.setHeader("Location", req.getServletPath());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = NetworkHelper.readUrlValues(req.getRequestURL().toString(), "employee");
        int status = EmployeeDataStoreHelper.getInstance().deleteEmployeeEntity(id);
        resp.setStatus(status);
        resp.setHeader("Location", req.getServletPath());
    }

}
