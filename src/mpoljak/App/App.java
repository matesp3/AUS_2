package mpoljak.App;

import mpoljak.data.GPS;
import mpoljak.data.GeoData;
import mpoljak.data.Parcel;
import mpoljak.data.Property;
import mpoljak.data.forTesting.MyInt;
import mpoljak.data.forTesting.MyIntervalInt;
import mpoljak.dataStructures.searchTrees.KdTree.KDTree;
import mpoljak.dataStructures.searchTrees.KdTree.KdNode;

public class App {
    public static void main(String[] args) {
//        MyIntervalInt ii1 = new MyIntervalInt(5, 12);
//        MyIntervalInt ii2 = new MyIntervalInt(1, 5);
//        MyIntervalInt ii3 = new MyIntervalInt(8, 17);
//        MyIntervalInt ii4 = new MyIntervalInt(3, 4);
//        MyIntervalInt ii5 = new MyIntervalInt(4, 75);
//        MyIntervalInt ii6 = new MyIntervalInt(21, 27);
//        MyIntervalInt ii7 = new MyIntervalInt(13, 30);
//        MyIntervalInt ii8 = new MyIntervalInt(2, 8);
//        MyIntervalInt ii9 = new MyIntervalInt(6, 12);
//        MyIntervalInt ii10 = new MyIntervalInt(18, 41);
        MyInt i1 = new MyInt(5);
        MyInt i2 = new MyInt(1);
        MyInt i3 = new MyInt(8);
        MyInt i4 = new MyInt(3);
        MyInt i5 = new MyInt(4);
        MyInt i6 = new MyInt(21);
        MyInt i7 = new MyInt(13);
        MyInt i8 = new MyInt(2);
        MyInt i9 = new MyInt(6);
        MyInt i10 = new MyInt(18);

        KDTree<MyInt> kdTree = new KDTree<MyInt>(1);
        kdTree.insert(i1);
        kdTree.insert(i2);
        kdTree.insert(i3);
        kdTree.insert(i4);
        kdTree.insert(i5);
        kdTree.insert(i6);
        kdTree.insert(i7);
        kdTree.insert(i8);
        kdTree.insert(i9);
        kdTree.insert(i10);

        kdTree.printTree();
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
