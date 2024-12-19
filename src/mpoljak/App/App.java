package mpoljak.App;

import mpoljak.App.GUI.GeoAppFrame;
import mpoljak.App.Logic.DataDb;
import mpoljak.App.Logic.GeoDbClient;
import mpoljak.data.geo.GPS;
import mpoljak.data.geo.ParcelFactory;
import mpoljak.dataStructures.ITable;
import mpoljak.dataStructures.ITableData;
import mpoljak.dataStructures.KdTree.KDTree;
import mpoljak.utilities.DoubleComparator;

public class App {

    public static void main(String[] args) {
        DoubleComparator.getInstance().setEpsilon(0.01);

        GeoDbClient client = new GeoDbClient();

        ParcelFactory factory = new ParcelFactory();
        ITable<ITableData, ITableData, GPS> table = new KDTree<ITableData, ITableData, GPS>(2);
        DataDb<GPS> generalClient = new DataDb<>(factory, table);

        GeoAppFrame appFrame = new GeoAppFrame(client, generalClient);
    }


}
