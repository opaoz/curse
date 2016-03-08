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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                "name",
                "desc",
                "nearest",
                "culture",
                "minYear",
                "maxYear"
        });

        cities.put("Saratov", model.createResource(CITIES_URI + "saratov"));
        cities.get("Saratov")
                .addProperty(properties.get("name"), "Саратов")
                .addProperty(properties.get("latitude"), "51.32")
                .addProperty(properties.get("longitude"), "46")
                .addProperty(properties.get("minYear"), "1590")
                .addProperty(properties.get("maxYear"), "2016")
                .addProperty(properties.get("desc"), "Основан как сторожевая крепость для охраны южных рубежей Российского государства в 1590 году, в царствование Фёдора Иоанновича. Во второй половине XVIII века — крупный перевалочный пункт и центр торговли рыбой и солью, а с XIX века — один из центров торговли зерном. Губернский город с 1780 года, в начале XX века — крупнейший по численности жителей город на Волге.")
                .addProperty(properties.get("type"), "Город")
                .addProperty(properties.get("culture"), "Церковь,Музей")
                .addProperty(properties.get("nearest"), "Река");

        cities.put("Ozinki", model.createResource(CITIES_URI + "ozinki"));
        cities.get("Ozinki")
                .addProperty(properties.get("name"), "Озинки")
                .addProperty(properties.get("latitude"), "51.12")
                .addProperty(properties.get("minYear"), "1940")
                .addProperty(properties.get("maxYear"), "2016")
                .addProperty(properties.get("longitude"), "49.42")
                .addProperty(properties.get("desc"), "Посёлок городского типа, административный центр, крупнейший населённый пункт Озинского района Саратовской области и одноимённая железнодорожная станция Приволжской железной дороги на стыке с КТЖ.\n" +
                        "\n" +
                        "Посёлок расположен на берегах реки Большая Чалыкла ")
                .addProperty(properties.get("type"), "ПГТ")
                .addProperty(properties.get("culture"), "Церковь,Музей")
                .addProperty(properties.get("nearest"), "Река,Пруд");

        cities.put("OzinkiPos", model.createResource(CITIES_URI + "ozinki_pos"));
        cities.get("OzinkiPos")
                .addProperty(properties.get("name"), "Посёлок Озинский")
                .addProperty(properties.get("latitude"), "51.12")
                .addProperty(properties.get("longitude"), "49.42")
                .addProperty(properties.get("minYear"), "1873")
                .addProperty(properties.get("maxYear"), "1940")
                .addProperty(properties.get("desc"), "Хутор Озинский основан в 1873 году.\n" +
                        "\n" +
                        "В 1928 году село Озинки становится центром Озинского района в составе Пугачёвского округа Нижне-Волжского края (с 1936 года — в Саратовской области).\n" +
                        "\n" +
                        "Статус посёлка городского типа — с 1940 года.")
                .addProperty(properties.get("type"), "Посёлок,Хутор")
                .addProperty(properties.get("culture"), "Церковь")
                .addProperty(properties.get("nearest"), "Пруд,Река");

        return model;
    }

    public void fillProperties(String[] array) {
        for (String item : array) {
            properties.put(item, model.createProperty(RELATIONSHIP_URI, item));
        }
    }
}
