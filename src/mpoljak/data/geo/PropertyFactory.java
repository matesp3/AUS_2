package mpoljak.data.geo;

import mpoljak.dataStructures.ITableData;
import mpoljak.dataStructures.IDataFactory;
import mpoljak.dataStructures.IParams;
import mpoljak.utilities.IntIdGenerator;

public class PropertyFactory implements IDataFactory{

    @Override
    public ITableData createData(IParams params) {
        PropertyParams pa = (PropertyParams) params;
        return new Property(pa.getPropertyNr(), pa.getDescription(), pa.getGps1(), pa.getGps2(),
                pa.getUniqueId() == -1 ? IntIdGenerator.nextId() : pa.getUniqueId(), null);
    }

    @Override
    public IParams createParams() {
        return new PropertyParams(-1, null, null, null, -1, null);
    }
}
