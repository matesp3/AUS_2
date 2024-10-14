package mpoljak.dataStructures.searchTrees.KdTree;

import mpoljak.data.GPS;
import mpoljak.data.Parcel;

import java.util.Random;

public class Tester {

    public void testRandomInsert(long insertionsCount) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        KDTree<Parcel, Integer> kdTree = new KDTree<Parcel, Integer>(2);
        for (int a = 0; a < 100; a++) {
            gpsGen.setSeed(seedGen.nextLong());
            for (int i = 0; i < insertionsCount; i++) {
                GPS g1 = generateGPS(gpsGen);
                GPS g2 = generateGPS(gpsGen);
                Parcel p = new Parcel(i, null, g1, g2);
                kdTree.insert(p);
            }
        }
    }

    private GPS generateGPS(Random generator) {
        char chLat;
        char chLon;
        double lat = generator.nextDouble() % 180.0 / 2.0;
        chLat = (lat < 90.0)  ? 'S' : 'N';
        double lon = generator.nextDouble()  % 360.0 / 2.0;
        chLon = (lon < 180.0)  ? 'E' : 'W';
        return new GPS(chLat, lat, chLon, lon);
    }
}
