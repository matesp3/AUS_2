package mpoljak.data.geo;

import mpoljak.dataStructures.ITableData;
import mpoljak.dataStructures.IDataFactory;
import mpoljak.dataStructures.IParams;
import mpoljak.utilities.IntIdGenerator;

public class ParcelFactory implements IDataFactory {

    @Override
    public ITableData createData(IParams params) {
        ParcelParams pa = (ParcelParams) params;
        return new Parcel(pa.getParcelId(), pa.getDescription(), pa.getGps1(), pa.getGps2(),
                pa.getUniqueId() == -1 ? IntIdGenerator.nextId() : pa.getUniqueId());
    }

    @Override
    public IParams createParams() {
        return new ParcelParams(-1, null, null, null, -1);
    }
}
