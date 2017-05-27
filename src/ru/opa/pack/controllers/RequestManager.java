package ru.opa.pack.controllers;

import org.apache.jena.rdf.model.Model;
import ru.opa.pack.models.Cities;
import ru.opa.pack.models.CitiesNew;
import ru.opa.pack.util.JSONHelper;
import ru.opa.pack.util.QueryExec;


/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class RequestManager {
    private String jsonString;

    private static ru.opa.pack.api.Model md = new CitiesNew();
    private static Model model = md.getModel();

    public RequestManager(String request) {
        String result = md.getRequest(request);

        if (result.contains("Done")) {
            jsonString = result;
        } else {
            jsonString = JSONHelper.generateJSONResponse(QueryExec.exec(result, model));
        }

    }

    public static void sayHello() {
        System.out.print("Hello");
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
