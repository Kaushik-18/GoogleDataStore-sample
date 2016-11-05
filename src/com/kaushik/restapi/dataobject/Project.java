package com.kaushik.restapi.dataobject;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Created by kaushik on 29/10/16.
 */
public class Project {
    private int id ;
    private String name;
    private double budget ;

    public Project(int id, String name, float budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    public Project(JSONObject obj) throws JSONException {
        this.id = obj.getInt("id");
        this.name = obj.getString("name");
        this.budget = obj.getDouble("budget");
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }
}
