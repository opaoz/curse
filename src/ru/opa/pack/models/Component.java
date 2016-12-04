package ru.opa.pack.models;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.Map;

/**
 * Created by Владимир on 08.03.2016.
 */
public class Component {
    public String name, latitude, longitude, desc, nameE;
    public int minY, maxY;
    public String[] type, nearest;

    public Component(String name, String nameE, String lat, String lon, String near, String type, String desc, int minY, int maxY) {
        this.name = name;
        this.nameE = nameE;
        this.latitude = lat;
        this.longitude = lon;
        this.nearest = near.split(",");
        this.type = type.split(",");
        this.desc = desc;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void register(Map<String, Resource> cities, Map<String, Property> properties, org.apache.jena.rdf.model.Model model, String CITIES_URI) {
        cities.put(this.nameE, model.createResource(CITIES_URI + this.nameE));

        cities.get(this.name)
                .addProperty(properties.get("name"), this.name)
                .addProperty(properties.get("latitude"), this.latitude)
                .addProperty(properties.get("longitude"), this.longitude)
                .addProperty(properties.get("desc"), this.desc);

        for (int i = this.minY; i <= this.maxY; i++) {
            cities.get(this.name).addProperty(properties.get("year"), "" + i);
        }

        /*for (String i : this.type) {
            cities.get(this.name).addProperty(properties.get("type"), i);
        }

        for (String i : this.nearest) {
            cities.get(this.name).addProperty(properties.get("nearest"), i);
        }*/
    }
}
