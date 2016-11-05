package com.kaushik.restapi.dataobject;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Created by kaushik on 29/10/16.
 */
public class Employee {
    private int id;
    private String firstName;
    private String lastName;

    public Employee(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Employee(JSONObject data) throws JSONException {
        this.id = data.getInt("id");
        this.firstName = data.getString("firstName");
        this.lastName = data.getString("lastName");
    }


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
