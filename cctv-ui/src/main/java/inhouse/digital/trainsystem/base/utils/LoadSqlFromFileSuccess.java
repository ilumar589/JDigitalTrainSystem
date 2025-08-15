package inhouse.digital.trainsystem.base.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LoadSqlFromFileSuccess(String loadedSql) implements LoadSqlResult {
}
