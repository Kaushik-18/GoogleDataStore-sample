package com.kaushik.restapi;

import com.google.appengine.api.datastore.*;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.kaushik.restapi.dataobject.Project;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by kaushik on 5/11/16.
 */
public class ProjectDataStoreHelper {

    private ProjectDataStoreHelper() {

    }

    private static ProjectDataStoreHelper _projectHelper;
    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public static ProjectDataStoreHelper getInstance() {
        if (_projectHelper == null) {
            _projectHelper = new ProjectDataStoreHelper();
        }
        return _projectHelper;
    }

    String getProjectEntity(int id) {
        Key projectKey = KeyFactory.createKey("Project", id);
        try {
            Entity projectEntity = datastore.get(projectKey);
            return NetworkHelper.generateProjectJsonResponse(projectEntity).toString();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getAllProjects() {
        Query allProjectQuery = new Query("Project");
        List<Entity> projectist = datastore.prepare(allProjectQuery).asList(FetchOptions.Builder.withDefaults());
        if (projectist != null && projectist.size() > 0) {
            JSONArray array = new JSONArray();
            for (Entity entity : projectist) {
                array.put(NetworkHelper.generateProjectJsonResponse(entity));
            }
            return array.toString();
        }
        return null;
    }

    void addProjectEntity(String data) throws JSONException {
        Project project = new Project(new JSONObject(data));
        Entity entity = new Entity("Project", project.getId());
        entity.setProperty("name", project.getName());
        entity.setProperty("budget", project.getBudget());
        datastore.put(entity);
    }

    int updateProjectEntity(int id, String updateData) {
        Key projectKey = KeyFactory.createKey("Project", id);
        try {
            Entity existingProject = datastore.get(projectKey);
            JSONObject updateJson = new JSONObject(updateData);
            if (updateJson.has("name")) {
                existingProject.setProperty("name", updateJson.optString("name"));
            }
            if (updateJson.has("budget")) {
                existingProject.setProperty("lastName", updateJson.optDouble("budget"));
            }
            datastore.put(existingProject);
            return HttpServletResponse.SC_OK;

        } catch (EntityNotFoundException e) {
            return HttpServletResponse.SC_NOT_FOUND;
        } catch (JSONException e) {
            e.printStackTrace();
            return HttpServletResponse.SC_BAD_REQUEST;
        }
    }

    int deleteProjectEntity(int id) {
        Key projectKey = KeyFactory.createKey("Project", id);
        try {
            datastore.get(projectKey);
            datastore.delete(projectKey);
            return HttpServletResponse.SC_OK;

        } catch (EntityNotFoundException e) {
            return HttpServletResponse.SC_NOT_FOUND;
        }

    }

}
