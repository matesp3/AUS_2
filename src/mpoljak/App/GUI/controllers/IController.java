package mpoljak.App.GUI.controllers;

import mpoljak.dataStructures.*;

import java.util.List;

public interface IController {
    public boolean addToDb(IParams params);
    public boolean updateInDb(IParams oldParams, IParams newParams);
    public ITableData searchInDb(ITableKey key, IParams params);
    public List<ITableData> searchInDb(ITableKey key);
    public List<ITableData> searchInDb(IPredicate<ITableData> predicate);
    public boolean deleteFromDb(ITableKey keyOfDeleted, ITableData dataToDelete);
}
