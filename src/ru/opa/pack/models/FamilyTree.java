package ru.opa.pack.models;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import ru.opa.pack.references.References;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * @author Владимир
 *         11.10.2015
 */
public class FamilyTree {
    public static final String FAMILY_URI = "http://family/";
    public static final String RELATIONSHIP_URI = "http://purl.org/vocab/relationship/";
    private Map<String, Resource> family = new HashMap<String, Resource>();
    private Model model;

    public FamilyTree() {
        model = FileManager.get().loadModel(References.FAMILY_PATH);

        if (model == null) {
            model = reCreateModel();
            System.out.println("Model was recreated...");
        } else {
            System.out.println("Model was loaded...");
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

        push("Adam", spouseOf, "Dotty");
        push("Beth", spouseOf, "Chuck");
        push("Fan", spouseOf, "Greg");

        push("Adam", siblingOf, "Beth");
        push("Edward", siblingOf, "Fan");

        push("Edward", childOf, "Adam");
        push("Edward", childOf, "Dotty");
        push("Fan", childOf, "Adam");
        push("Fan", childOf, "Dotty");
        push("Harriet", childOf, "Fan");
        push("Harriet", childOf, "Greg");

        push("Adam", parentOf, "Edward");
        push("Dotty", parentOf, "Edward");
        push("Adam", parentOf, "Fan");
        push("Dotty", parentOf, "Fan");
        push("Fan", parentOf, "Harriet");
        push("Greg", parentOf, "Harriet");

        return model;
    }

    public void push(String who, Property what, String whom) {
        model.add(model.createStatement(family.get(who), what, family.get(whom)));
    }

    public Model getModel() {
        return model;
    }


    public static Boolean exportModel(Model model) {
        File file = new File(References.FAMILY_PATH);

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
