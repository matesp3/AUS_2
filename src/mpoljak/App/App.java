package mpoljak.App;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.Logic.GeoDbClient;
import mpoljak.utilities.DoubleComparator;

public class App {

    public static void main(String[] args) {
        DoubleComparator.getInstance().setEpsilon(0.01);
        GeoDbClient client = new GeoDbClient();
        GeoAppFrame appFrame = new GeoAppFrame(client);
    }


}
