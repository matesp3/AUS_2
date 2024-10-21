package mpoljak.dataStructures.searchTrees.KdTree;

import mpoljak.data.GPS;
import mpoljak.data.Parcel;

import java.util.Random;

public class Tester {

    public void testRandomInsert(long insertionsCount, int seedCount) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();
//        KDTree<Parcel, Integer> kdTree = new KDTree<Parcel, Integer>(2);
        KDTree<GPS, Double> kdTree = new KDTree<GPS, Double>(2);
        for (int a = 0; a < seedCount; a++) {
            gpsGen.setSeed(seedGen.nextLong());
            dirGen.setSeed(seedGen.nextLong());
            for (int i = 0; i < insertionsCount; i++) {
                GPS g1 = generateGPS(gpsGen, dirGen);
//                GPS g2 = generateGPS(gpsGen);
//                Parcel p = new Parcel(i, null, g1, g2);
                kdTree.insert(g1);
            }
        }
//        kdTree.printTree();
    }

    private static GPS generateGPS(Random generator, Random dirGenerator) {
        char chLat;
        char chLon;

        double lat = generator.nextDouble() * 90;
        chLat = (dirGenerator.nextDouble() > 0.5)  ? 'S' : 'N';

        double lon = generator.nextDouble() * 180;
        chLon = (dirGenerator.nextDouble() > 0.5)  ? 'E' : 'W';

        return new GPS(chLat, lat, chLon, lon);
    }
}
