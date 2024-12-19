package mpoljak.App.Logic;

import mpoljak.data.geo.GPS;
import mpoljak.dataStructures.*;
import mpoljak.data.geo.ParcelFactory;
import mpoljak.data.geo.GeoResource;
import mpoljak.dataStructures.KdTree.KDTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataDb<K extends ITableKey> {
    private ITable<ITableData, ITableData, K> table;
    private IDataFactory factory;

    public DataDb(IDataFactory factory, ITable<ITableData, ITableData, K> dataCollector) {
        this.factory = factory;
        this.table = dataCollector.cloneInstance();
        IPredicate<ITableData> predicate1 = new Predicate<ITableData>() {
            @Override
            public boolean evaluate(ITableData o) {
                if (!(o instanceof GeoResource))
                    return false;
                GeoResource p = (GeoResource) o;
                return p.getGps1().getLongitude() == 'E';
            }
        };

        IPredicate<ITableData> predicate2 = new Predicate<ITableData>() {
            @Override
            public boolean evaluate(ITableData o) {
                if (!(o instanceof GeoResource))
                    return false;
                GeoResource p = (GeoResource) o;
                return p.getGps1().getLongitude() == 'E';
            }
        };
        IPredicate<ITableData> compoundPredicate = new CompositePredicateAnd<>();
        compoundPredicate.addSubPredicate(predicate1);
        compoundPredicate.addSubPredicate(predicate2);

        Iterator<ITableData> it = table.iterator(compoundPredicate);
        while (it.hasNext()) {
            ITableData geoData = it.next();
        }
    }

    public boolean addData(IParams dataParams, K key) {
        if (dataParams == null || key == null)
            return false;
        ITableData newData = this.factory.createData(dataParams);
        if (key.isUnique()) {
            if (this.table.find(key, newData) != null)
                return false;
        }
        else {
            List<ITableData> matches = this.table.findAll(key);
            if (matches != null) {
                for (ITableData record : matches) {
                    if (record.isSame(newData))
                        return false;
                }
            }
        }
        this.table.insert(key, newData);
        return true;
    }

    public boolean editData(IParams modifiedParams, K oldKey, K newKey) {
        if (modifiedParams == null || oldKey == null || newKey == null)
            return false;
        ITableData modifiedData = this.factory.createData(modifiedParams);
        ITableData retrieved = this.table.find(oldKey, modifiedData); // unique id couldn't be changed
        if (retrieved == null)
            return false;
        if (oldKey.equalsTo(newKey)) {
            retrieved.update(modifiedParams); // reference is updated
        }
        else {
            if (this.table.delete(oldKey, retrieved) == null)
                return false;
            this.table.insert(newKey, modifiedData);
        }
        return true;
    }

    /**
     * @param predicate data filter
     * @return all data that meets criteria of <code>predicate</code>.
     */
    public List<ITableData> findMatches(IPredicate<ITableData> predicate) {
        List<ITableData> lMatches = new ArrayList<>();
        Iterator<ITableData> it = this.table.iterator(predicate);
        while (it.hasNext()) {
            lMatches.add(it.next());
        }
        return lMatches;
    }

    /**
     *
     * @param key key of data
     * @param data <code>null</code> if <code>key</code> is unique, else key has to be specified more by providing data
     *             with unique identifier.
     * @return found data regarding provided parameters or <code>null</code> if data not found.
     */
    public ITableData findValue(K key, ITableData data) {
        if (key == null)
            return null;
        if (key.isUnique()) {
            List<ITableData> lRes = this.table.findAll(key);
            return lRes == null || lRes.size() != 1 ? null : lRes.get(0);
        }
        if (data == null)
            return null;
        return this.table.find(key, data);
    }

    public List<ITableData> findAllValues(K key) {
        if (key == null)
            return null;
        return this.table.findAll(key);
    }

//    public void addNewCollector(IDataFactory factory, ITable<IData, IData, someKey> collectorOfData, String classification) {
//        // nemohlo by toto byt ako pridanie novej strategie?
//    }

    public static void main(String[] args) {
        ParcelFactory factory = new ParcelFactory();
        ITable<ITableData, ITableData, GPS> table = new KDTree<ITableData, ITableData, GPS>(2);
        DataDb<GPS> geoDb = new DataDb<>(factory, table);
    }
}
