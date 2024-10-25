package mpoljak.dataStructures.searchTrees.KdTree;

import mpoljak.data.GPS;
import mpoljak.data.Property;

import java.util.List;
import java.util.Random;



public class Tester {

    public void testRandomInsert(long insertionsCount, int seedCount) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();
        String className = Property.class.getSimpleName();

        System.out.println("        INSERTING '" + className + "' based on GPS positions, tree built by GPS_1\n         " +
                "---------------------------------------------------------");
        int id = 1;
        KDTree<Property, GPS, Double> kdTree = new KDTree<Property, GPS, Double>(2);
//        KDTree<Parcel, Double> kdTree = new KDTree<Parcel, Double>(2, 1);
//        KDTree<GPS, Double> kdTree = new KDTree<GPS, Double>(2);
        for (int a = 0; a < seedCount; a++) {
            gpsGen.setSeed(seedGen.nextLong());
            dirGen.setSeed(seedGen.nextLong());
            for (int i = 0; i < insertionsCount; i++) {
                GPS g1 = generateGPS(gpsGen, dirGen);
                GPS g2 = generateGPS(gpsGen, dirGen);

                Property p = new Property(id++, null, g1, g2);
//                Parcel p = new Parcel(id++, null, g1, g2);

                kdTree.insert(p.getPositions()[0], p); // inserted by first GPS
                System.out.println("\n ---------------------------------------------------------------------------- ");
                System.out.println(" (i) Inserting " + p + " FIRST time... (by FIRST GPS)");
                System.out.println(" ---------------------------------------------------------------------------- ");
                kdTree.printTree();

                kdTree.insert(p.getPositions()[1], p); // inserted by second GPS
                System.out.println("\n ---------------------------------------------------------------------------- ");
                System.out.println(" (i) Inserting " + p + " SECOND time... (by SECOND GPS)");
                System.out.println(" ---------------------------------------------------------------------------- ");
                kdTree.printTree();

            }
        }
    }

    public void testManualInsertion() {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();

        gpsGen.setSeed(seedGen.nextLong());
        dirGen.setSeed(seedGen.nextLong());

        GPS g1 = generateGPS(gpsGen, dirGen);
        GPS g2 = generateGPS(gpsGen, dirGen);
        GPS g3 = generateGPS(gpsGen, dirGen);
        GPS g4 = generateGPS(gpsGen, dirGen);
        GPS g5 = generateGPS(gpsGen, dirGen);
        GPS g6 = generateGPS(gpsGen, dirGen);

        KDTree<GPS, GPS, Double> kdTree = new KDTree<>(2);
        kdTree.insert(g1, g1);
        kdTree.insert(g2,g2);
        kdTree.insert(g3,g3);
        kdTree.insert(g4,g4);
        kdTree.insert(g5,g5);
        kdTree.insert(g6,g6);

        kdTree.printTree();

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g5.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        List<GPS> lResult = kdTree.find(g5);
        if (lResult == null)
            System.out.println("NOTHING FOUND..");
        else {
            for (GPS g : lResult) {
                System.out.print(g + ", ");
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
