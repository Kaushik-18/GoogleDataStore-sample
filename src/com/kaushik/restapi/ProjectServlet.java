package com.kaushik.restapi;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.kaushik.restapi.dataobject.Project;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by kaushik on 29/10/16.
 */
public class ProjectServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ProjectServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = NetworkHelper.readUrlValues(req.getRequestURL().toString(), "project");
        if (id > NetworkHelper.URL_FIND_ALL) {
            log.info("retiving project from datastore");
            String project = ProjectDataStoreHelper.getInstance().getProjectEntity(id,req.getContentType());
            if (project != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(project);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } else {
            log.info("retiving all projects from datastore");
            String projectlist = ProjectDataStoreHelper.getInstance().getAllProjects(req.getContentType());
            if (projectlist != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(projectlist);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().println("No projects");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int responseCode ;
        try {
            log.info("adding project to datastore");
            String request = NetworkHelper.readRequest(req);
            responseCode = ProjectDataStoreHelper.getInstance().addProjectEntity(request,req.getContentType());
        } catch (JSONException | SAXException | ParserConfigurationException e) {
            responseCode = HttpServletResponse.SC_CONFLICT;
        }
        resp.setStatus(responseCode);
        resp.setHeader("Location", req.getServletPath());
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("updating project to datastore");
        int id = NetworkHelper.readUrlValues(req.getRequestURL().toString(), "project");
        String request = NetworkHelper.readRequest(req);
        int status = ProjectDataStoreHelper.getInstance().updateProjectEntity(id, request);
        resp.setStatus(status);
        resp.setHeader("Location", req.getServletPath());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("deleting project to datastore");
        int id = NetworkHelper.readUrlValues(req.getRequestURL().toString(), "project");
        int status = ProjectDataStoreHelper.getInstance().deleteProjectEntity(id);
        resp.setStatus(status);
        resp.setHeader("Location", req.getServletPath());
    }
}
