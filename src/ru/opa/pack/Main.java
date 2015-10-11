package ru.opa.pack;

import org.apache.jena.rdf.model.Model;
import ru.opa.pack.models.FamilyTree;
import ru.opa.pack.utils.QueryExec;

/**
 * @author Владимир
 *         11.10.2015
 */
public class Main {
    public static void main(String[] args) {
        Model model = new FamilyTree().getModel();
        String queryString = "PREFIX rela: <http://purl.org/vocab/relationship/> " +
                "SELECT ?child ?parent " +
                "WHERE {" +
                " ?child rela:childOf ?parent . " +
                " }";

        QueryExec.exec(queryString, model);
    }
}
