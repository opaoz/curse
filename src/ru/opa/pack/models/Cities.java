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
    private static final String CITIES_URI = "http://cities/";
    private static final String RELATIONSHIP_URI = "http://purl.org/vocab/relationship/";
    private Map<String, Resource> cities = new HashMap<>();
    private Map<String, Property> properties = new HashMap<>();

    public Map<String, String> requests = new HashMap<>();

    public Cities() {
        super(References.MODEL_PATH);
        if (model == null) {
            model = reCreateModel();
            exportModel(model);
        }

        requests.put("get", "PREFIX r: <http://purl.org/vocab/relationship/>  SELECT * WHERE {  ?city r:latitude ?lat;   r:longitude ?long;  r:desc ?desc;  r:culture ?objects;  r:group ?group;  r:type ?type;  r:nearest ?nearest;  r:minYear ?minYear;  r:maxYear ?maxYear;  r:entranceTo ?entranceTo; r:name ?name.  }");
        requests.put("year", "PREFIX r: <http://purl.org/vocab/relationship/> SELECT (MIN(?year) as ?minYear) WHERE{ ?city r:minYear ?year }");
    }

    public String getRequest(String request) {
        return requests.get(request);
    }

    private Model reCreateModel() {
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
                "maxYear",
                "group",
                "entranceTo"
        });

        cities.put("Saratov", model.createResource(CITIES_URI + "saratov"));
        cities.get("Saratov")
                .addProperty(properties.get("name"), "Саратов")
                .addProperty(properties.get("latitude"), "51.32")
                .addProperty(properties.get("longitude"), "46")
                .addProperty(properties.get("minYear"), "1590")
                .addProperty(properties.get("maxYear"), "2016")
                .addProperty(properties.get("group"), "saratov")
                //.addProperty(properties.get("image"), "https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Coat_of_Arms_of_Saratov.svg/800px-Coat_of_Arms_of_Saratov.svg.png")
                .addProperty(properties.get("desc"), "Основан как сторожевая крепость для охраны южных рубежей Российского государства в 1590 году, в царствование Фёдора Иоанновича. Во второй половине XVIII века — крупный перевалочный пункт и центр торговли рыбой и солью, а с XIX века — один из центров торговли зерном. Губернский город с 1780 года, в начале XX века — крупнейший по численности жителей город на Волге.")
                .addProperty(properties.get("type"), "Город")
                .addProperty(properties.get("culture"), "Церковь,Музей")
                .addProperty(properties.get("nearest"), "Река")
                .addProperty(properties.get("entranceTo"), "Саратовская область");

        cities.put("Ozinki", model.createResource(CITIES_URI + "ozinki"));
        cities.get("Ozinki")
                .addProperty(properties.get("name"), "Озинки")
                .addProperty(properties.get("latitude"), "51.12")
                .addProperty(properties.get("minYear"), "1940")
                .addProperty(properties.get("maxYear"), "2016")
                .addProperty(properties.get("group"), "ozinki")
                //.addProperty(properties.get("image"), "http://www.hrono.ru/heraldicum/russia/subjects/towns/images/ozinki.gif")
                .addProperty(properties.get("longitude"), "49.42")
                .addProperty(properties.get("desc"), "Посёлок городского типа, административный центр, крупнейший населённый пункт Озинского района Саратовской области и одноимённая железнодорожная станция Приволжской железной дороги на стыке с КТЖ.\n" +
                        "\n" +
                        "Посёлок расположен на берегах реки Большая Чалыкла ")
                .addProperty(properties.get("type"), "ПГТ")
                .addProperty(properties.get("culture"), "Церковь,Музей")
                .addProperty(properties.get("nearest"), "Река,Пруд")
                .addProperty(properties.get("entranceTo"), "Саратовская область");

        cities.put("Ershov", model.createResource(CITIES_URI + "ershov"));
        cities.get("Ershov")
                .addProperty(properties.get("name"), "Ершов")
                .addProperty(properties.get("latitude"), "51.12")
                .addProperty(properties.get("minYear"), "1940")
                .addProperty(properties.get("maxYear"), "2016")
                .addProperty(properties.get("group"), "erhov")
                .addProperty(properties.get("longitude"), "49.42")
                .addProperty(properties.get("desc"), "Посёлок городского типа, административный центр, крупнейший населённый пункт Озинского района Саратовской области и одноимённая железнодорожная станция Приволжской железной дороги на стыке с КТЖ.\n" +
                        "\n")
                .addProperty(properties.get("type"), "Город")
                .addProperty(properties.get("culture"), "Церковь,Музей")
                .addProperty(properties.get("nearest"), "Река")
                .addProperty(properties.get("entranceTo"), "Ершовский район");

        cities.put("OzinkiPos", model.createResource(CITIES_URI + "ozinki_pos"));
        cities.get("OzinkiPos")
                .addProperty(properties.get("name"), "Посёлок Озинский")
                .addProperty(properties.get("latitude"), "51.12")
                .addProperty(properties.get("longitude"), "49.42")
                .addProperty(properties.get("group"), "ozinki")
                .addProperty(properties.get("minYear"), "1873")
                .addProperty(properties.get("maxYear"), "1940")
                .addProperty(properties.get("desc"), "Хутор Озинский основан в 1873 году.\n" +
                        "\n" +
                        "В 1928 году село Озинки становится центром Озинского района в составе Пугачёвского округа Нижне-Волжского края (с 1936 года — в Саратовской области).\n" +
                        "\n" +
                        "Статус посёлка городского типа — с 1940 года.")
                .addProperty(properties.get("type"), "Посёлок,Хутор")
                .addProperty(properties.get("culture"), "Церковь")
                .addProperty(properties.get("nearest"), "Пруд,Река")
                .addProperty(properties.get("entranceTo"), "Саратовская область");

        cities.put("Balashov", model.createResource(CITIES_URI + "balashov"));
        cities.get("Balashov")
                .addProperty(properties.get("name"), "Балашов")
                .addProperty(properties.get("latitude"), "51.34")
                .addProperty(properties.get("longitude"), "43.10")
                .addProperty(properties.get("minYear"), "1780")
                .addProperty(properties.get("maxYear"), "2016")
                .addProperty(properties.get("group"), "balashow")
                .addProperty(properties.get("desc"), "Город расположен на восточной окраине Окско-Донской равнины, на реке Хопёр (приток Дона), на пересечении железнодорожных линий Тамбов — Камышин и Поворино — Пенза, в 210 км к западу от Саратова.\n" +
                        "Через город протекает одна из красивейших рек средней полосы России — Хопёр, которая делит Балашов на две неравные части — частный сектор и центральный, с постройками городского типа.")
                .addProperty(properties.get("type"), "Город,Уездный город")
                .addProperty(properties.get("culture"), "Музей,Памятник")
                .addProperty(properties.get("nearest"), "Равнина,Река")
                .addProperty(properties.get("entranceTo"), "Балашовский район");

        cities.put("BalashovH", model.createResource(CITIES_URI + "balashov_h"));
        cities.get("BalashovH")
                .addProperty(properties.get("name"), "Село Балашово")
                .addProperty(properties.get("latitude"), "51.33")
                .addProperty(properties.get("longitude"), "43.10")
                .addProperty(properties.get("minYear"), "1768")
                .addProperty(properties.get("maxYear"), "1780")
                .addProperty(properties.get("group"), "balashow")
                .addProperty(properties.get("desc"), "Город расположен на восточной окраине Окско-Донской равнины, на реке Хопёр (приток Дона), на пересечении железнодорожных линий Тамбов — Камышин и Поворино — Пенза, в 210 км к западу от Саратова.\n" +
                        "Через город протекает одна из красивейших рек средней полосы России — Хопёр, которая делит Балашов на две неравные части — частный сектор и центральный, с постройками городского типа.")
                .addProperty(properties.get("type"), "Хутор")
                .addProperty(properties.get("culture"), "")
                .addProperty(properties.get("nearest"), "Равнина,Река")
                .addProperty(properties.get("entranceTo"), "Балашовский район");
        return model;
    }

    public void fillProperties(String[] array) {
        for (String item : array) {
            properties.put(item, model.createProperty(RELATIONSHIP_URI, item));
        }
    }
}
