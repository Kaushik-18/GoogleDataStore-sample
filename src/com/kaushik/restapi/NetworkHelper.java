package com.kaushik.restapi;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.kaushik.restapi.dataobject.Employee;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by kaushik on 3/11/16.
 */
public class NetworkHelper {

    public static final int URL_FIND_ALL = -1;

    static int readUrlValues(String url, String keyword) {
        String params[] = url.split("/");
        String reqId = params[params.length - 1];
        if (reqId.equalsIgnoreCase(keyword)) {
            return URL_FIND_ALL;
        }
        int id = 0;
        try {
            id = Integer.parseInt(reqId);
        } catch (NumberFormatException ne) {
            ne.printStackTrace();
        }
        return id;
    }

    static String readRequest(HttpServletRequest req) {
        StringBuilder requestData = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                requestData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestData.toString();
    }

    static JSONObject generateEmployeeJsonResponse(Entity employee) {
        JSONObject respJson = new JSONObject();
        try {
            respJson.put("id", employee.getKey());
            respJson.put("firstName", employee.getProperty("firstName"));
            respJson.put("lastName", employee.getProperty("lastName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respJson;
    }

    static JSONObject generateProjectJsonResponse(Entity project) {
        JSONObject respJson = new JSONObject();
        try {
            respJson.put("id", project.getKey());
            respJson.put("name", project.getProperty("name"));
            respJson.put("budget", project.getProperty("budget"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respJson;
    }

}
