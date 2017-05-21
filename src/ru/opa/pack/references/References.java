package ru.opa.pack.references;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author ��������
 *         11.10.2015
 */
public final class References {
    /*Models*/
    public static final String FAMILY_PATH = "D:/Family/family.rdf";
    public static final String MODEL_PATH = "D:/Curse/curse.rdf";
    public static final String NEW_PATH = "D:/Curse/curve2.owl";

    /*BackEnd*/
    public static final int DEFAULT_SERVER_PORT = 8080;
    public static final int SERVER_PORT = 8080;

    /*FrontEnd*/
    public static final String FRONT_END_FOLDER = "app";

    /*Log*/
    public static final Font LOG_FONT = new Font("Verdana", Font.BOLD, 12);
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
}
