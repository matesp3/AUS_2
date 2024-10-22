package mpoljak.dataStructures.searchTrees.KdTree;

import mpoljak.data.GPS;
import mpoljak.data.Parcel;

import java.util.Random;

public class Tester {

    public void testRandomInsert(long insertionsCount, int seedCount) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();
        KDTree<Parcel, Integer> kdTree = new KDTree<Parcel, Integer>(2, 1);
        System.out.println("        INSERTING Parcels based on GPS positions, tree built by GPS_1\n         " +
                "---------------------------------------------------------");
        int id = 1;
//        KDTree<GPS, Double> kdTree = new KDTree<GPS, Double>(2);
        for (int a = 0; a < seedCount; a++) {
            gpsGen.setSeed(seedGen.nextLong());
            dirGen.setSeed(seedGen.nextLong());
            for (int i = 0; i < insertionsCount; i++) {
                GPS g1 = generateGPS(gpsGen, dirGen);
                GPS g2 = generateGPS(gpsGen, dirGen);
                Parcel p = new Parcel(id++, null, g1, g2);
                kdTree.insert(p); // inserted by first GPS
                System.out.println("\n ---------------------------------------------------------------------------- ");
                System.out.println(" (i) Inserting Parcel " + p + " FIRST time... (by FIRST GPS)");
                System.out.println(" ---------------------------------------------------------------------------- ");
                kdTree.printTree();

                p.toggleComparedKey();

                kdTree.insert(p); // inserted by second GPS
                System.out.println("\n ---------------------------------------------------------------------------- ");
                System.out.println(" (i) Inserting Parcel " + p + " SECOND time... (by SECOND GPS)");
                System.out.println(" ---------------------------------------------------------------------------- ");
                kdTree.printTree();

            }
        }

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
