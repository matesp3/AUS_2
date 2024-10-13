package mpoljak.data;

import mpoljak.dataStructures.searchTrees.KdTree.IKdComparable;

public class GeoData implements IKdComparable<GeoData> {
//    { duplicate keys --> could be whatever type, but has to be comparable
    private final int k1 = 1;
    private final int kn = 10;
//    }

//    { data
    private final int x;
    private final int y;
//    }
    public GeoData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(GeoData other, int dim) {
        if (dim == 1) // numbering from 1
            return Integer.compare(this.x, other.x);
        else
            return Integer.compare(this.y, other.y);
    }
}
