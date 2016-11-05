package com.kaushik.restapi;


import com.google.appengine.api.datastore.*;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.kaushik.restapi.dataobject.Employee;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaushik on 29/10/16.
 */


public class EmployeeDataStoreHelper {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    private EmployeeDataStoreHelper() {

    }

    private static EmployeeDataStoreHelper _helper;

    public static EmployeeDataStoreHelper getInstance() {
        if (_helper == null) {
            _helper = new EmployeeDataStoreHelper();
        }
        return _helper;
    }

    public void addEmployeeEntity(String data) throws JSONException {
        Employee employee  = new Employee(new JSONObject(data));
        Entity entity = new Entity("Employee", employee.getId());
        entity.setProperty("firstName", employee.getFirstName());
        entity.setProperty("lastName", employee.getLastName());
        datastore.put(entity);
    }

    String retrieveEmployeeEntity(int id) {
        Key employeeKey = KeyFactory.createKey("Employee", id);
        try {
            Entity employeeEntity = datastore.get(employeeKey);
            return NetworkHelper.generateEmployeeJsonResponse(employeeEntity).toString();
        } catch (EntityNotFoundException e) {
            return null;
        }
    }


    int updateEmployeeEntity(int id, String updateData) {
        Key employeeKey = KeyFactory.createKey("Employee", id);
        try {
            Entity existingEmployee = datastore.get(employeeKey);
            JSONObject updateJson = new JSONObject(updateData);
            if (updateJson.has("firstName")) {
                existingEmployee.setProperty("firstName", updateJson.opt("firstName"));
            }
            if (updateJson.has("lastName")) {
                existingEmployee.setProperty("lastName", updateJson.opt("lastName"));
            }
            datastore.put(existingEmployee);
            return HttpServletResponse.SC_OK;

        } catch (EntityNotFoundException e) {
            return HttpServletResponse.SC_NOT_FOUND;
        } catch (JSONException e) {
            e.printStackTrace();
            return HttpServletResponse.SC_BAD_REQUEST;
        }
    }

    public int deleteEmployeeEntity(int id) {
        Key employeeKey = KeyFactory.createKey("Employee", id);
        try {
            datastore.get(employeeKey);
            datastore.delete(employeeKey);
            return HttpServletResponse.SC_OK;

        } catch (EntityNotFoundException e) {
            return HttpServletResponse.SC_NOT_FOUND;
        }
    }


    String getAllEmployees() {
        Query allEmployeeQuery = new Query("Employee");
        List<Entity> employeeList = datastore.prepare(allEmployeeQuery).asList(FetchOptions.Builder.withDefaults());
        if (employeeList != null && employeeList.size() > 0) {
            JSONArray array = new JSONArray();
            for (Entity entity : employeeList) {
                array.put(NetworkHelper.generateEmployeeJsonResponse(entity));
            }
            return array.toString();
        }
        return null;
    }
}
