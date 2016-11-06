package com.kaushik.restapi;

import com.google.appengine.api.datastore.*;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.kaushik.restapi.dataobject.Project;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by kaushik on 5/11/16.
 */
public class ProjectDataStoreHelper {

    private ProjectDataStoreHelper() {

    }

    private static ProjectDataStoreHelper _projectHelper;
    private DatastoreService _projectDatastore = DatastoreServiceFactory.getDatastoreService();

    public static ProjectDataStoreHelper getInstance() {
        if (_projectHelper == null) {
            _projectHelper = new ProjectDataStoreHelper();
        }
        return _projectHelper;
    }

    String getProjectEntity(int id) {
        Key projectKey = KeyFactory.createKey("Project", id);
        try {
            Entity projectEntity = _projectDatastore.get(projectKey);
            return NetworkHelper.generateProjectJsonResponse(projectEntity).toString();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getAllProjects() {
        Query allProjectQuery = new Query("Project");
        List<Entity> projectist = _projectDatastore.prepare(allProjectQuery).asList(FetchOptions.Builder.withDefaults());
        if (projectist != null && projectist.size() > 0) {
            JSONArray array = new JSONArray();
            for (Entity entity : projectist) {
                array.put(NetworkHelper.generateProjectJsonResponse(entity));
            }
            return array.toString();
        }
        return null;
    }

    int addProjectEntity(String data, String contentType) throws JSONException, IOException, SAXException, ParserConfigurationException {
        Project project = null;
        if ("application/xml".equalsIgnoreCase(contentType)) {
            project = NetworkHelper.readProjectFromXML(data);
        } else {
            project = new Project(new JSONObject(data));
        }

        Key projectKey = KeyFactory.createKey("Project", project.getId());
        try {
            Entity existingProject = _projectDatastore.get(projectKey);
            if (existingProject != null) {
                return HttpServletResponse.SC_CONFLICT;
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        Entity entity = new Entity("Project", project.getId());
        entity.setProperty("name", project.getName());
        entity.setProperty("budget", project.getBudget());
        _projectDatastore.put(entity);
        return HttpServletResponse.SC_CREATED;
    }

    int updateProjectEntity(int id, String updateData) {
        Key projectKey = KeyFactory.createKey("Project", id);
        try {
            Entity existingProject = _projectDatastore.get(projectKey);
            JSONObject updateJson = new JSONObject(updateData);
            if (updateJson.has("name")) {
                existingProject.setProperty("name", updateJson.optString("name"));
            }
            if (updateJson.has("budget")) {
                existingProject.setProperty("lastName", updateJson.optDouble("budget"));
            }
            _projectDatastore.put(existingProject);
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
            _projectDatastore.get(projectKey);
            _projectDatastore.delete(projectKey);
            return HttpServletResponse.SC_OK;

        } catch (EntityNotFoundException e) {
            return HttpServletResponse.SC_NOT_FOUND;
        }

    }

}
