package mem.kitek.server.sql.table;

import com.google.common.collect.Lists;
import mem.kitek.server.sql.Connector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by RINES on 20.10.17.
 */
public class TableConstructor {

    private final String tableName;
    private final List<TableColumn> columns;

    public TableConstructor(String tableName, TableColumn... columns) {
        this.tableName = tableName;
        this.columns = Lists.newArrayList(columns);
    }

    @Override
    public String toString() {
        String columns = this.columns.stream().map(Object::toString).collect(Collectors.joining(", "));
        String primary = this.columns.stream().filter(tc -> tc.isPrimary()).map(TableColumn::getName).collect(Collectors.joining(", "));
        if(primary != null && !primary.isEmpty())
            columns += ", PRIMARY KEY (" + primary + ")";
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci", this.tableName, columns);
    }

    public void create(Connector connector) {
        connector.query(toString());
    }

}
