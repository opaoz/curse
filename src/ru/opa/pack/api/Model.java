package ru.opa.pack.api;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import ru.opa.pack.Main;
import ru.opa.pack.references.References;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Vladimir_Levin on 04.02.2016.
 */
public abstract class Model {
    protected org.apache.jena.rdf.model.Model model = null;
    protected String path;

    public org.apache.jena.rdf.model.Model getModel() {
        return model;
    }

    public Model(String path) {
        this.path = path;
        try {
            model = FileManager.get().loadModel(path);
            Main.ui.println("Model was loaded...");
        } catch (Exception e) {
            Main.ui.println("Model was recreated...");
        }
    }


    public void push(String who, Property what, String whom, Map<String, Resource> map) {
        model.add(model.createStatement(map.get(who), what, map.get(whom)));
    }

    public Boolean exportModel(org.apache.jena.rdf.model.Model model) {
        File file = new File(path);

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
