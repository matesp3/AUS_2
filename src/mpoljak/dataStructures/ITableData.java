package mpoljak.dataStructures;

import mpoljak.dataStructures.KdTree.ISame;

public interface ITableData extends ISame<ITableData> {
    public void update(IParams modified);
}
