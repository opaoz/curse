package ru.opa.pack.utils;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Владимир
 *         11.10.2015
 */
public final class QueryExec {
    public static final Map<String[], List<QuerySolution>> exec(String queryString, Model model) {
        Query query = QueryFactory.create(queryString);

        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qe.execSelect();
            List<QuerySolution> results = new ArrayList<>();
            Map<String[], List<QuerySolution>> result = new HashMap<>();
            List<String> resultVars = resultSet.getResultVars();

            while (resultSet.hasNext()) {
                results.add(resultSet.next());
            }

            result.put(resultVars.toArray(new String[resultVars.size()]), results);
            //ResultSetFormatter.out(System.out, results);
            return result;
        }
    }
}
