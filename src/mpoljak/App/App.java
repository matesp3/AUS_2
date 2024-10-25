package mpoljak.App;

import mpoljak.data.GPS;
import mpoljak.data.forTesting.MyInt;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.dataStructures.searchTrees.KdTree.Tester;
import mpoljak.utilities.DoubleComparator;

public class App {

    public static void main(String[] args) {
        DoubleComparator.getInstance().setEpsilon(0.0001);
        Tester tester = new Tester();
        tester.testManualInsertion();
//        tester.testRandomInsert(2,2);
// ---------------------------------------------------------------------------------------------------------------------

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

//--------------------------------------------------------------
//        GPS g1 = new GPS('N', 15, 'W', 4);
//        GPS g2 = new GPS('N', 30, 'W', 5);
//        Property p = new Property(1, "Dom", g1, g2);
//        GPS[] pos = p.getPositions();

//        int k = 2;
//        System.out.println((18 % k + 1));

//        GeoData gd = new GeoData(1,2);
//        GeoData gd2 = new GeoData(2,1);
//        KdNode<GeoData> node1 = new KdNode<GeoData>(null, null, null, gd);
//        KdNode<GeoData> node2 = new KdNode<GeoData>(null, null, null, gd2);
//        System.out.println("node1.compareTo(node2, 1): " + node1.compareTo(node2, 1));
//        System.out.println("node1.compareTo(node2, 2): " + node1.compareTo(node2, 2));
    }
}
