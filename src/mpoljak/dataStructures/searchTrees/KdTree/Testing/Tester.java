package mpoljak.dataStructures.searchTrees.KdTree.Testing;

import mpoljak.data.*;
import mpoljak.data.forTesting.MyCoupleInt;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.dataStructures.searchTrees.KdTree.KdNode;

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
            KDTree<Property, IGpsLocalizable, GPS> kdTree = new KDTree<Property, IGpsLocalizable, GPS>(2);
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
            printFoundElements(kdTree.findAll(myGps1));
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
        KDTree<Property, IGpsLocalizable, GPS> kdTree = new KDTree<Property, IGpsLocalizable, GPS>(2);
//        KDTree<Parcel, GPS> kdTree = new KDTree<>(2);
//        KDTree<GeoData, GPS> kdTree = new KDTree<>(2);
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
        printFoundElements(kdTree.findAll(myGps1));
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
        KDTree<Property, IGpsLocalizable,  GPS> kdTree = new KDTree<Property, IGpsLocalizable, GPS>(2);
//        KDTree<Parcel, Double> kdTree = new KDTree<Parcel, Double>(2, 1);
//        KDTree<GPS, Double> kdTree = new KDTree<GPS, Double>(2);
        for (int a = 0; a < seedCount; a++) {
            gpsGen.setSeed(seedGen.nextLong());
            dirGen.setSeed(seedGen.nextLong());
            for (int i = 0; i < insertionsCount; i++) {
                GPS g1 = generateGPS(gpsGen, dirGen);
                GPS g2 = generateGPS(gpsGen, dirGen);

                Property p = new Property(id++, null, g1, g2);
                Parcel par = new Parcel(id++, null, g1, g2);

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

        KDTree<GPS, GPS, GPS> kdTree = new KDTree<>(2);
        kdTree.insert(g1, g1);
        kdTree.insert(g2,g2);
        kdTree.insert(g3,g3);
        kdTree.insert(g4,g4);
        kdTree.insert(g5,g5);
        kdTree.insert(g6,g6);

        kdTree.printTree();

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g1.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        printFoundElements(kdTree.findAll(g1));

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g2.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        printFoundElements(kdTree.findAll(g2));

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g3.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        printFoundElements(kdTree.findAll(g3));

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g4.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        printFoundElements(kdTree.findAll(g4));

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g5.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        printFoundElements(kdTree.findAll(g5));

        System.out.println("\n ---------------------------------------------------------------------------- ");
        System.out.println(" SEARCHING FOR NODE WITH DATA: " + g6.toString());
        System.out.println(" ---------------------------------------------------------------------------- ");

        printFoundElements(kdTree.findAll(g6));
    }

    public void testFindingExtremes(int insertionsCount, int seedSeed, int gpsSeed, int dirSeed) {
        Random seedGen = new Random();
        Random gpsGen = new Random();
        Random dirGen = new Random();
        int seedCount = 1;
        int id = 1;
        int a = 0;
        KDTree<Property, IGpsLocalizable, GPS> kdTree = new KDTree<Property, IGpsLocalizable, GPS>(2);
        GPS max = new GPS('S', 90.0, 'W', 180); // starting from minimum
        GPS min = new GPS('N', 90.0, 'E', 180); // starting from maximum

//        for (int a = 0; a < seedCount; a++) {
        if (seedSeed < 0)
            seedSeed = seedGen.nextInt();
        if (gpsSeed < 0)
            gpsSeed = seedGen.nextInt();
        if (dirSeed < 0)
            dirSeed = seedGen.nextInt();
        seedGen.setSeed(seedSeed);
        gpsGen.setSeed(gpsSeed);
        dirGen.setSeed(dirSeed);
        System.out.println(String.format("\n    ----------------------------------------------------\n" +
                "Iteration %d: seed=%s; gpsSeed=%s; directionSeed=%s", a+ 1, "" + seedSeed, "" + gpsSeed, "" + dirSeed));
//            -----------------------------------------

        for (int i = 0; i < insertionsCount; i++) {
            GPS g1 = generateGPS(gpsGen, dirGen);
            GPS g2 = generateGPS(gpsGen, dirGen);
            Property p = new Property(id++, null, g1, g2);
            kdTree.insert(g1, p); // inserted by first GPS
            if (g1.compareTo(min, 1) == -1)
                min = g1;
            if (g1.compareTo(max, 1) == 1)
                max = g1;
        }
//        }
//        kdTree.printTree();
        System.out.println("MIN=" + min + "; MAX=" + max);
        System.out.println("  >>>  FOUND:");
        System.out.println(" ===== M I N I M U M ===== ");
        kdTree.printRootMin();
        System.out.println(" ===== M A X I M U M ===== ");
        kdTree.printRootMax();
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
        KDTree<IGpsLocalizable, IGpsLocalizable, GPS> kdTreeBoth = new KDTree<>(2);
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

    private static void insertElements(long insertionsCount, KDTree<Property, IGpsLocalizable, GPS> kdTree, Random dirGenerator, Random
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

    public void printMaxForRoot() {
        MyCoupleInt i1 = new MyCoupleInt(110,4, "");
        MyCoupleInt i2 = new MyCoupleInt(215,2, "");
        MyCoupleInt i3 = new MyCoupleInt(178,9, "");
        MyCoupleInt i4 = new MyCoupleInt(105,2, "");
        MyCoupleInt i5 = new MyCoupleInt(305,3, "");
        MyCoupleInt i6 = new MyCoupleInt(110,3, "");
        MyCoupleInt i7 = new MyCoupleInt(255,3, "");
        MyCoupleInt i8 = new MyCoupleInt(110,4, "");
        MyCoupleInt i9 = new MyCoupleInt(305,3, "");
        MyCoupleInt i10 = new MyCoupleInt(174,7, "");
        MyCoupleInt i11 = new MyCoupleInt(180,6, "");
        MyCoupleInt i12 = new MyCoupleInt(115,5, "");
        MyCoupleInt i13 = new MyCoupleInt(175,8, "");
        MyCoupleInt i14 = new MyCoupleInt(179,5, "");
        MyCoupleInt i15 = new MyCoupleInt(120,5, "");
        MyCoupleInt i16 = new MyCoupleInt(180,5, "");

        KDTree<MyCoupleInt, MyCoupleInt, MyCoupleInt> kdTree = new KDTree<MyCoupleInt, MyCoupleInt, MyCoupleInt>(2);
        kdTree.insert(i1, i1);
        kdTree.insert(i2, i2);
        kdTree.insert(i3, i3);
        kdTree.insert(i4, i4);
        kdTree.insert(i5, i5);
        kdTree.insert(i6, i6);
        kdTree.insert(i7, i7);
        kdTree.insert(i8, i8);
        kdTree.insert(i9, i9);
        kdTree.insert(i10, i10);
        kdTree.insert(i11, i11);
        kdTree.insert(i12, i12);
        kdTree.insert(i13, i13);
        kdTree.insert(i14, i14);
        kdTree.insert(i15, i15);
        kdTree.insert(i16, i16);

        kdTree.printTree();
        System.out.println("--MAX--");
//        kdTree.findMax(); // public no more
        System.out.println("\n--MIN--");
//        kdTree.findMin(); // public no more

    }


    public void testVillages() {
        MyCoupleInt i1 = new MyCoupleInt(23,35, "Nitra");
        MyCoupleInt i2 = new MyCoupleInt(22,39, "Senica");
        MyCoupleInt i3 = new MyCoupleInt(22,31, "Senica - skola");
        MyCoupleInt i4 = new MyCoupleInt(22,42, "Senica - stanica");
        MyCoupleInt i5 = new MyCoupleInt(22,32, "Senica - urad");
        MyCoupleInt i6 = new MyCoupleInt(12,41, "Hodonin");
        MyCoupleInt i7 = new MyCoupleInt(24,36, "Tlmace - urad");
        MyCoupleInt i8 = new MyCoupleInt(24,34, "Tlmace");
        MyCoupleInt i9 = new MyCoupleInt(24,40, "Tlmace - parkovisko");
        MyCoupleInt i10 = new MyCoupleInt(24,35, "Tlmace - nem.");
        MyCoupleInt i11 = new MyCoupleInt(30,33, "Levice");
        MyCoupleInt i12 = new MyCoupleInt(29,46, "Bojnice");
        MyCoupleInt i13 = new MyCoupleInt(27,43, "Novaky");

        KDTree<MyCoupleInt, MyCoupleInt, MyCoupleInt> kdTree = new KDTree<MyCoupleInt, MyCoupleInt, MyCoupleInt>(2);
        kdTree.insert(i1, i1);
        kdTree.insert(i2, i2);
        kdTree.insert(i3, i3);
        kdTree.insert(i4, i4);
        kdTree.insert(i5, i5);
        kdTree.insert(i6, i6);
        kdTree.insert(i7, i7);
        kdTree.insert(i8, i8);
        kdTree.insert(i9, i9);
        kdTree.insert(i10, i10);
        kdTree.insert(i11, i11);
        kdTree.insert(i12, i12);
        kdTree.insert(i13, i13);

//      ----------- DELETING LEAVES: OK..
//        kdTree.printTree();
//        System.out.println("    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -");
//        kdTree.delete(i13, i13);
//        System.out.println(" > DELETED NODE [KEY=" + i13 + "; DATA=" + i13 + "]");
//        kdTree.printTree();
//        System.out.println("    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -");
//        kdTree.delete(i12, i12);
//        System.out.println(" > DELETED NODE [KEY=" + i12 + "; DATA=" + i12 + "]");
//        kdTree.printTree();
//        System.out.println("    -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -");
//        kdTree.delete(i5, i5);
//        System.out.println(" > DELETED NODE [KEY=" + i5 + "; DATA=" + i5 + "]");
//        kdTree.printTree();

//      ----------- DELETING AND REPLACING FROM LEFT SUBTREE: OK...
//        kdTree.printTree(); // tlmace -urad <= Tlmace - nem.
//        kdTree.delete(i7, i7);
//        System.out.println(" > DELETED NODE [KEY=" + i7 + "; DATA=" + i7 + "]");
//        kdTree.printTree();

//        kdTree.printTree();     // Nitra <= Senica
//        kdTree.delete(i1, i1);
//        System.out.println(" > DELETED NODE [KEY=" + i1 + "; DATA=" + i1 + "]");
//        kdTree.printTree();

//        kdTree.printTree();     // Senica <= Senica - urad
//        kdTree.delete(i2, i2);
//        System.out.println(" > DELETED NODE [KEY=" + i2 + "; DATA=" + i2 + "]");
//        kdTree.printTree();

//        kdTree.printTree();     // Senica - stanica <= Hodonin
//        kdTree.delete(i4, i4);
//        System.out.println(" > DELETED NODE [KEY=" + i4 + "; DATA=" + i4 + "]");
//        kdTree.printTree();
//        kdTree.delete(i9, i9);
    }

//        MyInt i1 = new MyInt(5);
//        MyInt i2 = new MyInt(1);
//        MyInt i3 = new MyInt(8);
//        MyInt i4 = new MyInt(3);
//        MyInt i5 = new MyInt(4);
//        MyInt i6 = new MyInt(21);
//        MyInt i7 = new MyInt(13);
//        MyInt i8 = new MyInt(6);
//        MyInt i9 = new MyInt(6);
//        MyInt i10 = new MyInt(18);
//        MyInt i11 = new MyInt(15);
//        MyInt i12 = new MyInt(2);
//        MyInt i13 = new MyInt(2);
//
//        KDTree<MyInt, MyInt, Integer> kdTree = new KDTree<MyInt, MyInt, Integer>(1);
//        kdTree.insert(i1, i1);
//        kdTree.insert(i2, i2);
//        kdTree.insert(i3, i3);
//        kdTree.insert(i4, i4);
//        kdTree.insert(i5, i5);
//        kdTree.insert(i6, i6);
//        kdTree.insert(i7, i7);
//        kdTree.insert(i8, i8);
//        kdTree.insert(i9, i9);
//        kdTree.insert(i10, i10);
//        kdTree.insert(i11, i11);
//        kdTree.insert(i12, i12);
//        kdTree.insert(i13, i13);
//
//        kdTree.printTree();

//        Integer i_1 = new Integer(5);
//        Integer i_2 = new Integer(4);
//        System.out.println("Vysledok >> " + i_1.compareTo(i_2));

}

