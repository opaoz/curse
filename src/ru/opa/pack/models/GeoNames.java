package ru.opa.pack.models;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import ru.opa.pack.references.References;

/**
 * @author Владимир
 *         11.10.2015
 */
public class GeoNames {
    private Model model;
    static GeoNames instance;

    static {
        instance = new GeoNames(References.PATH);
    }

    private GeoNames(String path) {
        model = FileManager.get().loadModel(path);
    }

    public static Model getModel() {
        return instance.model;
    }
}
