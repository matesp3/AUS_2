package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.GPS;
import mpoljak.data.IGpsLocalizable;
import mpoljak.data.Parcel;
import mpoljak.data.Property;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;

import java.util.List;
import java.util.Random;

public class Tester {

    public void testSearchOfGPS(long insertionsCount, int seedCount, int times) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();
        String className = Property.class.getSimpleName();

        System.out.println("        INSERTING '" + className + "' based on GPS positions\n         " +
                "---------------------------------------------------------");
        int id = 1;


        for (int a = 0; a < seedCount; a++) {
            int seedSeed = seedGen.nextInt();
            int gpsSeed = seedGen.nextInt();
            int dirSeed = seedGen.nextInt();
            seedGen.setSeed(seedSeed);
            gpsGen.setSeed(gpsSeed);
            dirGen.setSeed(dirSeed);
            System.out.println(String.format("\n    ----------------------------------------------------\n" +
                    "Iteration %d: seed=%s; gpsSeed=%s; directionSeed=%s", a+ 1, "" + seedSeed, "" + gpsSeed, "" + dirSeed));
//            -----------------------------------------
            KDTree<Property, GPS, Double> kdTree = new KDTree<Property, GPS, Double>(2);
            GPS myGps1 = generateGPS(gpsGen, dirGen);
            GPS myGps2 = generateGPS(gpsGen, dirGen);
            Property myP = new Property(1_000_000, "myProp_v1", myGps1, myGps2);
            int insertedTimes = 0;
            for (int i = 0; i < times; i++) {
                insertElements(insertionsCount, kdTree, gpsGen, dirGen, 1);
                if (dirGen.nextDouble() <= 0.5) { // after inserting 'insertionsCount' elements, insert searched Property
                    kdTree.insert(myGps1, myP);
                    kdTree.insert(myGps2, myP);
                    insertedTimes++;
                }
            }

            System.out.println("    SEARCHING FOR " + myGps1 + " ...\n");
            System.out.println("    ** was inserted " + insertedTimes + "x");
            printFoundElements(kdTree.find(myGps1));
//            -----------------------------------------
        }

//        kdTree.printTree();
    }


    public void debugSearchOfGPS(long insertionsCount, int seedCount, int times, int seedSeed, int gpsSeed, int dirSeed) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();
        String className = Property.class.getSimpleName();

        System.out.println("        INSERTING '" + className + "' based on GPS positions\n         " +
                "---------------------------------------------------------");
        int a = 3;
        seedGen.setSeed(seedSeed);
        gpsGen.setSeed(gpsSeed);
        dirGen.setSeed(dirSeed);
        System.out.println("Iteration " + (a+ 1) + ". seed=" +  seedSeed + "; gpsSeed=" + gpsSeed + "; directionSeed=" + dirSeed);
//            -----------------------------------------
        KDTree<Property, GPS, Double> kdTree = new KDTree<Property, GPS, Double>(2);
        GPS myGps1 = generateGPS(gpsGen, dirGen);
        GPS myGps2 = generateGPS(gpsGen, dirGen);
        Property myP = new Property(1_000_000, "myProp_v1", myGps1, myGps2);
        int insertedTimes = 0;
        for (int i = 0; i < times; i++) {
            insertElements(insertionsCount, kdTree, gpsGen, dirGen, 1);
            if (dirGen.nextDouble() <= 0.5) { // after inserting 'insertionsCount' elements, insert searched Property
                kdTree.insert(myGps1, myP);
                kdTree.insert(myGps2, myP);
                insertedTimes++;
            }
        }

        System.out.println("    SEARCHING FOR " + myGps1 + " ...\n");
        System.out.println("    ** was inserted " + insertedTimes + "x");
        printFoundElements(kdTree.find(myGps1));
//            -----------------------------------------


//        kdTree.printTree();
    }

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

        printFoundElements(kdTree.find(g5));
    }

//    |
//   |_>-----------------------> PRIVATE >---------------------v
//                                                             |
//                                                            v
    private static <E> void printFoundElements(List<E> lResult) {
        if (lResult == null)
            System.out.println("NOTHING FOUND..");
        else {
            int c = 1;
            for (E e : lResult) {
                System.out.println(c++ + ". " + e);
            }
        }
    }

    private static void testInsertingBothParcelsAndProperties() {
        KDTree<IGpsLocalizable, GPS, Double> kdTreeBoth = new KDTree<>(2);
        GPS g1 = new GPS('N', 27.87, 'W', 25.4);
        GPS g2 = new GPS('S', 79.87, 'E', 52.4);
        Parcel par = new Parcel(1, "", g1, g2);
        Property prop = new Property(1, "", g2, g1);
        kdTreeBoth.insert(g1, par);
        kdTreeBoth.insert(g2, prop);
        // OK..
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

    private static void insertElements(long insertionsCount, KDTree<Property, GPS, Double> kdTree, Random dirGenerator, Random
            valGenerator, int nextId) {
        for (int i = 0; i < insertionsCount; i++) {
            GPS g1 = generateGPS(valGenerator, dirGenerator);
            GPS g2 = generateGPS(valGenerator, dirGenerator);

            Property p = new Property(nextId++, null, g1, g2);
//                Parcel p = new Parcel(id++, null, g1, g2);
            kdTree.insert(p.getPositions()[0], p); // inserted by first GPS
            kdTree.insert(p.getPositions()[1], p); // inserted by second GPS

        }
    }

}
