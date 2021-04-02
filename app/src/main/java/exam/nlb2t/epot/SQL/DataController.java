package exam.nlb2t.epot.SQL;

import exam.nlb2t.epot.SQL.DataModel;
import java.sql.Connection;

public class DataController {

    DataModel dataModel = new DataModel();

    public Connection ConnnectionData() {
        return dataModel.getConnectionOf();
    }
}
