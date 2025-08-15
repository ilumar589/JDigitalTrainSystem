package inhouse.digital.trainsystem.base.utils;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public record LoadSqlFromFileError(IOException cause) implements LoadSqlResult {
}
