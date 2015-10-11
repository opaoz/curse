package ru.opa.pack.controllers;

import org.apache.jena.rdf.model.Model;
import ru.opa.pack.models.FamilyTree;
import ru.opa.pack.utils.JSONHelper;
import ru.opa.pack.utils.QueryExec;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class RequestManager {
    private String jsonString;

    public RequestManager(String request) {
        FamilyTree familyTree = new FamilyTree();
        Model model = familyTree.getModel();

        //jsonString = JSONHelper.generateJSONResponse(QueryExec.exec(request, model)).toJSONString();
        jsonString = "Azazaaz";
    }

    @Override
    public String toString() {
        return jsonString;
    }
}
