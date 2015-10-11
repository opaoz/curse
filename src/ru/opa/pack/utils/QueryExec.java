package ru.opa.pack.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;


/**
 * @author Владимир
 *         11.10.2015
 */
public final class QueryExec {
    public static final ResultSet exec(String queryString, Model model) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();

            ResultSetFormatter.out(System.out, results);
            return results;
        }
    }
}
