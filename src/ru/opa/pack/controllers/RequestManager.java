package ru.opa.pack.controllers;

import org.apache.jena.rdf.model.Model;
import ru.opa.pack.models.FamilyTree;
import ru.opa.pack.util.JSONHelper;
import ru.opa.pack.util.QueryExec;


/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class RequestManager {
    private String jsonString;

    private static FamilyTree familyTree = new FamilyTree();
    private static Model model = familyTree.getModel();

    public RequestManager(String request) {
        jsonString = JSONHelper.generateJSONResponse(QueryExec.exec(request, model));
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
