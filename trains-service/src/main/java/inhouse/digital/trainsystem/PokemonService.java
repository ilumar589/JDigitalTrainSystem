package inhouse.digital.trainsystem;

import io.helidon.common.context.Contexts;
import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbExecute;
import io.helidon.dbclient.DbTransaction;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;

import java.util.Map;

public final class PokemonService implements HttpService {

    private static final System.Logger LOGGER = System.getLogger(PokemonService.class.getName());
    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = Json.createBuilderFactory(Map.of());

    private final DbClient dbClient;
    private final boolean initSchema;
    private final boolean initData;

    PokemonService() {
        final Config config = Config.global().get("db");
        this.dbClient = Contexts.globalContext()
                .get(DbClient.class)
                .orElseThrow(() -> new IllegalStateException("No database client found in context"));
        this.initSchema = config.get("init-schema").asBoolean().orElse(true);
        this.initData = config.get("init-data").asBoolean().orElse(true);
    }

    @Override
    public void routing(HttpRules httpRules) {

    }


    private void init() {
    }

    private void initSchema() {
        final DbExecute exec = dbClient.execute();
        try {
            exec.namedDml("create-types");
            exec.namedDml("create-pokemons");
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, "Could not create tables", e);
            try {
                deleteData();
            } catch (Exception e2) {
                LOGGER.log(System.Logger.Level.ERROR, "Could not delete data", e2);
            }
        }
    }

    private void deleteData() {
        final DbTransaction transaction = dbClient.transaction();
        try {
            transaction.namedDml("delete-all-pokemons");
            transaction.namedDml("delete-all-types");
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
