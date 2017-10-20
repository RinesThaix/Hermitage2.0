package mem.kitek.server.sql.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by RINES on 20.10.17.
 */
@RequiredArgsConstructor
public class TableColumn {

    @Getter
    private final String name;

    @Getter
    private final ColumnType type;

    @Getter
    private boolean nullable;

    @Getter
    private Object defaultValue;

    @Getter
    private boolean primary;

    public TableColumn nullable(boolean value) {
        this.nullable = value;
        return this;
    }

    public TableColumn defaultValue(Object value) {
        this.defaultValue = value;
        return this;
    }

    public TableColumn primary(boolean value) {
        this.primary = value;
        return this;
    }

    private String getDefaultValueString() {
        if(this.defaultValue == null)
            return "";
        if(this.defaultValue instanceof String)
            return "'" + this.defaultValue.toString() + "'";
        return this.defaultValue.toString();
    }

    @Override
    public String toString() {
        return this.name + " " + this.type.getRepresentation() + (this.nullable ? "" : " NOT NULL") + (this.defaultValue == null ? "" : " DEFAULT " + getDefaultValueString());
    }

}
