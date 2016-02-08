package ru.opa.pack.controllers;

import org.apache.jena.rdf.model.Model;
import ru.opa.pack.models.Cities;
import ru.opa.pack.models.FamilyTree;
import ru.opa.pack.util.JSONHelper;
import ru.opa.pack.util.QueryExec;


/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class RequestManager {
    private String jsonString;

    //new FamilyTree();
    private static ru.opa.pack.api.Model md = new Cities();
    private static Model model = md.getModel();

    public RequestManager(String request) {
        jsonString = JSONHelper.generateJSONResponse(QueryExec.exec(request, model));
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
