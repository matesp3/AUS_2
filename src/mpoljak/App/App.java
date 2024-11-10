package mpoljak.App;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.Logic.GeoDbClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) {
        GeoDbClient client = new GeoDbClient();
        GeoAppFrame appFrame = new GeoAppFrame(client);

    }


}
