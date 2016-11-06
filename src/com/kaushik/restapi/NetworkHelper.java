package com.kaushik.restapi;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.kaushik.restapi.dataobject.Employee;
import com.kaushik.restapi.dataobject.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

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


    static Employee readEmployeeFromXML(String data) throws ParserConfigurationException, IOException, SAXException,ArrayIndexOutOfBoundsException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String firstName, lastName, id;
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(data));
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("employee");
        Element mNode = (Element) list.item(0);
        firstName = mNode.getElementsByTagName("firstName").item(0).getTextContent();
        lastName = mNode.getElementsByTagName("lastName").item(0).getTextContent();
        id = mNode.getElementsByTagName("id").item(0).getTextContent();

        return new Employee(Integer.parseInt(id), firstName, lastName);
    }


    static Project readProjectFromXML(String data) throws ParserConfigurationException, IOException, SAXException ,ArrayIndexOutOfBoundsException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(data));
        Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();

        NodeList list = doc.getElementsByTagName("project");
        Element mNode = (Element) list.item(0);
        String name = mNode.getElementsByTagName("name").item(0).getTextContent();
        String budget = mNode.getElementsByTagName("budget").item(0).getTextContent();
        String id = mNode.getElementsByTagName("id").item(0).getTextContent();

        return new Project(Integer.parseInt(id), name,Float.parseFloat(budget));
    }

}
