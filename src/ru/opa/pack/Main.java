package ru.opa.pack;

import org.apache.jena.rdf.model.Model;
import org.json.simple.JSONArray;
import ru.opa.pack.models.FamilyTree;
import ru.opa.pack.server.Server;
import ru.opa.pack.utils.JSONHelper;
import ru.opa.pack.utils.QueryExec;

import java.io.IOException;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class Main {
    public static void main(String[] args) {
        /*String queryString = "PREFIX rela: <http://purl.org/vocab/relationship/> " +
                "SELECT ?child ?parent " +
                "WHERE {" +
                " ?child rela:childOf ?parent . " +
                " }";*/
        try {
            new Server(8080).accept();
        } catch (IOException e) {
            System.out.println("Server crashed" + e);
        }
    }
}
