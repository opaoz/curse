package ru.opa.pack.models;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.*;


/**
 * @author Владимир
 *         11.10.2015
 */
public class FamilyTree {
    public static final String FAMILY_URI = "http://family/";
    public static final String RELATIONSHIP_URI = "http://purl.org/vocab/relationship/";
    private Map<String, Resource> family = new HashMap<String, Resource>();
    private Model model = ModelFactory.createDefaultModel();

    public FamilyTree() {
        Resource NAMESPACE = model.createResource(RELATIONSHIP_URI);
        model.setNsPrefix("rela", RELATIONSHIP_URI);

        Property childOf = model.createProperty(RELATIONSHIP_URI, "childOf");
        Property siblingOf = model.createProperty(RELATIONSHIP_URI, "siblingOf");
        Property spouseOf = model.createProperty(RELATIONSHIP_URI, "spouseOf");

        family.put("Adam", model.createResource(FAMILY_URI + "adam"));
        family.put("Dotty", model.createResource(FAMILY_URI + "dotty"));
        family.put("Beth", model.createResource(FAMILY_URI + "beth"));
        family.put("Chuck", model.createResource(FAMILY_URI + "chuck"));
        family.put("Edward", model.createResource(FAMILY_URI + "edward"));
        family.put("Fan", model.createResource(FAMILY_URI + "fan"));
        family.put("Greg", model.createResource(FAMILY_URI + "greg"));
        family.put("Harriet", model.createResource(FAMILY_URI + "harriet"));

        push("Adam", spouseOf, "Dotty");
        push("Beth", spouseOf, "Chuck");
        push("Fan", spouseOf, "Greg");
        push("Edward", childOf, "Adam");
        push("Edward", childOf, "Dotty");
        push("Fan", childOf, "Adam");
        push("Fan", childOf, "Dotty");
        push("Harriet", childOf, "Fan");
        push("Harriet", childOf, "Greg");
    }

    public void push(String who, Property what, String whom) {
        model.add(model.createStatement(family.get(who), what, family.get(whom)));
    }

    public Model getModel() {
        return model;
    }


    public void exportModel(){

    }
}
