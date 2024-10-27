package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.ISimilar;

import java.util.Comparator;

//public abstract class GeoData implements Comparator<GeoData> {
//public abstract class GeoData implements Comparator<IGpsLocalizable> {
public abstract class GeoData implements ISimilar<IGpsLocalizable> {
}
