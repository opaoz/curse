package ru.opa.pack.models;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import ru.opa.pack.Main;
import ru.opa.pack.api.*;
import ru.opa.pack.references.References;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vladimir_Levin on 04.02.2016.
 */
public class Cities extends ru.opa.pack.api.Model {
    public static final String CITIES_URI = "http://cities/";
    public static final String RELATIONSHIP_URI = "http://purl.org/vocab/relationship/";
    private Map<String, Resource> cities = new HashMap<>();
    private Map<String, Property> properties = new HashMap<>();

    public Cities() {
        super(References.MODEL_PATH);
        if (model == null) {
            model = reCreateModel();
            exportModel(model);
        }
    }

    public Model reCreateModel() {
        model = ModelFactory.createDefaultModel();
        Resource NAMESPACE = model.createResource(RELATIONSHIP_URI);
        model.setNsPrefix("rela", RELATIONSHIP_URI);

        fillProperties(new String[]{
                "latitude",
                "longitude",
                "year",
                "type",
                "name"
        });

        cities.put("Saratov", model.createResource(CITIES_URI + "saratov"));
        cities.get("Saratov")
                .addProperty(properties.get("name"), "Saratov")
                .addProperty(properties.get("latitude"), "51.32")
                .addProperty(properties.get("longitude"), "46")
                .addProperty(properties.get("year"), "1590")
                .addProperty(properties.get("type"), "City")
                .addProperty(properties.get("year"), "2016");

        return model;
    }

    public void fillProperties(String[] array) {
        for (String item : array) {
            properties.put(item, model.createProperty(RELATIONSHIP_URI, item));
        }
    }
}
