package mem.kitek.server.sql.table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by RINES on 20.10.17.
 */
@RequiredArgsConstructor
public enum ColumnType {
    INT("int"),
    TINY_INT("tinyint"),
    VARCHAR_16("varchar(16)"),
    VARCHAR_32("varchar(32)"),
    VARCHAR_48("varchar(48)"),
    VARCHAR_64("varchar(64)"),
    VARCHAR_128("varchar(128)"),
    TEXT("text(0)"),
    BIG_INT("bigint(18)"),
    BOOLEAN("boolean");

    @Getter(AccessLevel.PACKAGE)
    private final String representation;
}
