package inhouse.digital.trainsystem.base.utils;

import java.io.IOException;

public record LoadSqlFromFileError(IOException cause) implements LoadSqlResult {
}
