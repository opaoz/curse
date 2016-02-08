package ru.opa.pack.models;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import ru.opa.pack.Main;
import ru.opa.pack.api.*;
import ru.opa.pack.references.References;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * @author ��������
 *         11.10.2015
 */
public class FamilyTree extends ru.opa.pack.api.Model {
    public static final String FAMILY_URI = "http://family/";
    public static final String RELATIONSHIP_URI = "http://purl.org/vocab/relationship/";
    private Map<String, Resource> family = new HashMap<>();

    public FamilyTree() {
        super(References.FAMILY_PATH);
        if (model == null) {
            model = reCreateModel();
            exportModel(model);
        }
    }

    public Model reCreateModel() {
        model = ModelFactory.createDefaultModel();
        Resource NAMESPACE = model.createResource(RELATIONSHIP_URI);
        model.setNsPrefix("rela", RELATIONSHIP_URI);

        Property childOf = model.createProperty(RELATIONSHIP_URI, "childOf");
        Property siblingOf = model.createProperty(RELATIONSHIP_URI, "siblingOf");
        Property spouseOf = model.createProperty(RELATIONSHIP_URI, "spouseOf");
        Property parentOf = model.createProperty(RELATIONSHIP_URI, "parentOf");

        family.put("Adam", model.createResource(FAMILY_URI + "adam"));
        family.put("Dotty", model.createResource(FAMILY_URI + "dotty"));
        family.put("Beth", model.createResource(FAMILY_URI + "beth"));
        family.put("Chuck", model.createResource(FAMILY_URI + "chuck"));
        family.put("Edward", model.createResource(FAMILY_URI + "edward"));
        family.put("Fan", model.createResource(FAMILY_URI + "fan"));
        family.put("Greg", model.createResource(FAMILY_URI + "greg"));
        family.put("Harriet", model.createResource(FAMILY_URI + "harriet"));

        push("Adam", spouseOf, "Dotty", family);
        push("Beth", spouseOf, "Chuck", family);
        push("Fan", spouseOf, "Greg", family);

        push("Adam", siblingOf, "Beth", family);
        push("Edward", siblingOf, "Fan", family);

        push("Edward", childOf, "Adam", family);
        push("Edward", childOf, "Dotty", family);
        push("Fan", childOf, "Adam", family);
        push("Fan", childOf, "Dotty", family);
        push("Harriet", childOf, "Fan", family);
        push("Harriet", childOf, "Greg", family);

        push("Adam", parentOf, "Edward", family);
        push("Dotty", parentOf, "Edward", family);
        push("Adam", parentOf, "Fan", family);
        push("Dotty", parentOf, "Fan", family);
        push("Fan", parentOf, "Harriet", family);
        push("Greg", parentOf, "Harriet", family);

        return model;
    }
}
