package ru.opa.pack.models;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Resource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.opa.pack.api.Model;
import ru.opa.pack.references.References;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CitiesNew extends Model {
    public Map<String, String> requests = new HashMap<>();
    private Property dbDesc;
    private Property dbUpdate;

    public CitiesNew() {
        super(References.NEW_PATH);

        dbDesc = model.createProperty("http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#", "dbpediaDesc");
        dbUpdate = model.createProperty("http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#", "dbpediaUpdate");
        requests.put("get", "PREFIX geonames: <http://www.geonames.org/ontology#> PREFIX pos: <http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#> PREFIX time: <http://www.w3.org/2006/time#> SELECT * WHERE { ?settlement a pos:Settlement.  OPTIONAL { ?settlement pos:entranceTo ?s. ?s pos:entranceTo ?settlementEntranceTo. } OPTIONAL { ?settlement pos:firstSettlersOf ?settlementFirstSettlersOf. } OPTIONAL { ?settlement pos:dbpediaDesc ?dbpediaDesc. } OPTIONAL { ?settlement pos:dbpediaUpdate ?dbpediaUpdate. } OPTIONAL { ?settlement pos:founders ?settlementFounders. } OPTIONAL { ?settlement pos:metionedInSources ?settlementSource. ?settlementSource pos:url ?settlementSourceUrl. } OPTIONAL { ?settlement pos:geographicalDescription ?settlementHasGeographicalArrangement. } OPTIONAL { ?settlement pos:typeSetlement ?settlementTypeSetlement. } OPTIONAL { ?settlement geonames:name ?settlementName. } OPTIONAL { ?settlement geonames:population ?settlementPopulation. } OPTIONAL { ?settlement time:hasBeginning ?settlementHasBeginning }  OPTIONAL { select (group_concat(?culturalObjectName;separator=\", \") as ?culturalObjects) (group_concat(?culturalObjectYear;separator=\",\") as ?culturalObjectsYears) where { ?settlement pos:hasCulturalObjects ?hasCulturalObjects. ?hasCulturalObjects geonames:name ?culturalObjectName; time:hasBeginning ?culturalObjectYear.  } group by ?settlement }  { select * where { ?state pos:sameAs ?settlement.  OPTIONAL { ?state geonames:name ?name. } OPTIONAL { ?state geonames:population ?population. } OPTIONAL { ?state time:hasBeginning ?hasBeginning } OPTIONAL { ?state time:hasEnd ?hasEnd } OPTIONAL { ?state pos:metionedInSources ?source. ?source pos:url ?sourceUrl.  optional { ?source pos:description ?sourceDesc. } } } } }");
        requests.put("year", "PREFIX pos: <http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#> PREFIX time: <http://www.w3.org/2006/time#> SELECT (MIN(?hasBeginning) as ?year) WHERE { ?settlement a pos:Settlement; time:hasBeginning ?hasBeginning. }");
        requests.put("settlements", "PREFIX pos: <http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#> SELECT * WHERE { ?settlement a pos:Settlement.  OPTIONAL { ?settlement pos:dbpediaDesc ?dbpediaDesc. } OPTIONAL { ?settlement pos:dbpediaUpdate ?dbpediaUpdate. } }");
    }

    public String getRequest(String request) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        request = request.replace("\u0000", "");

        try {
            jsonObject = (JSONObject) parser.parse(request);
        } catch (ParseException ex) {
            System.out.println(ex.getStackTrace().toString());
        }

        if (jsonObject != null) {
            if (jsonObject.containsKey("get")) {
                return requests.get("get");
            } else if (jsonObject.containsKey("year")) {
                return requests.get("year");
            } else if (jsonObject.containsKey("settlements")) {
                return requests.get("settlements");
            }

            final JSONObject jsonObjectConst = jsonObject;

            jsonObject.keySet().forEach((v) -> {
                apply((String) v, (String) (jsonObjectConst.get(v)));
            });

            exportModel(model);

            return "{\"result\":\"Done\"}";
        }

        return requests.get("get");
    }

    public void apply(String key, String value) {
        System.out.printf("%s %s", key, value);
        Resource res = model.getResource(key);

        String[] values = value.split("\\|");
        res.removeAll(dbUpdate);
        res.removeAll(dbDesc);
        res.addProperty(dbUpdate, values[0]);
        res.addProperty(dbDesc, values[1]);

        exportModel(model);
    }

    protected Boolean exportModel(org.apache.jena.rdf.model.Model model) {
        File file = new File("D:/Curse/curve2.rdf");

        try {
            file.createNewFile();
        } catch (IOException e) {
            return false;
        }
        try (FileOutputStream fout = new FileOutputStream(file)) {
            RDFWriter writer = model.getWriter("RDF/XML-ABBREV");
            writer.write(model, fout, null);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
