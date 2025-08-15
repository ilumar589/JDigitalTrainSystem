package inhouse.digital.trainsystem.base.utils;

import org.jspecify.annotations.NullMarked;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@NullMarked
public final class SqlUtils {

    private SqlUtils() {}

    public LoadSqlResult loadSqlFromFile(final String classPathLocation) {
        try {
            final var resource = new ClassPathResource(classPathLocation);
            final var sqlString = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
            return new LoadSqlFromFileSuccess(sqlString);
        }
        catch (IOException ioException) {
            return new LoadSqlFromFileError(ioException);
        }
    }
}
