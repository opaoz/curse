package ru.opa.pack.models;

import ru.opa.pack.api.Model;
import ru.opa.pack.references.References;

import java.util.HashMap;
import java.util.Map;

public class CitiesNew extends Model {
    public Map<String, String> requests = new HashMap<>();

    public CitiesNew() {
        super(References.NEW_PATH);

        requests.put("get", "PREFIX geonames: <http://www.geonames.org/ontology#> PREFIX pos: <http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#> PREFIX time: <http://www.w3.org/2006/time#> SELECT * WHERE { ?settlement a pos:Settlement.  OPTIONAL { ?settlement pos:entranceTo ?s. ?s pos:entranceTo ?settlementEntranceTo. } OPTIONAL { ?settlement pos:firstSettlersOf ?settlementFirstSettlersOf. } OPTIONAL { ?settlement pos:founders ?settlementFounders. } OPTIONAL { ?settlement pos:hasGeographicalArrangement ?settlementHasGeographicalArrangement. } OPTIONAL { ?settlement pos:typeSetlement ?settlementTypeSetlement. } OPTIONAL { ?settlement geonames:name ?settlementName. } OPTIONAL { ?settlement geonames:population ?settlementPopulation. } OPTIONAL { ?settlement time:hasBeginning ?settlementHasBeginning }  { select (group_concat(?culturalObjectName;separator=\", \") as ?culturalObjects) where { ?settlement pos:hasCulturalObjects ?hasCulturalObjects. ?hasCulturalObjects geonames:name ?culturalObjectName.  } group by ?settlement }  { select * where { ?state pos:sameAs ?settlement.  OPTIONAL { ?state geonames:name ?name. } OPTIONAL { ?state geonames:population ?population. } OPTIONAL { ?state time:hasBeginning ?hasBeginning } OPTIONAL { ?state time:hasEnd ?hasEnd } } } }");
        requests.put("year", "PREFIX pos: <http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#> PREFIX time: <http://www.w3.org/2006/time#> SELECT (MIN(?hasBeginning) as ?year) WHERE { ?settlement a pos:Settlement; time:hasBeginning ?hasBeginning. }");
    }

    public String getRequest(String request) {
        return requests.get(request);
    }
}
