package inhouse.digital.trainsystem;

import io.helidon.common.context.Contexts;
import io.helidon.common.media.type.MediaTypes;
import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbExecute;
import io.helidon.dbclient.DbTransaction;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.*;

import java.util.Map;
import java.util.stream.Collectors;

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
        init();
    }

    @Override
    public void routing(HttpRules httpRules) {
        httpRules
                .get("/", this::index)
                .get("/type", this::listTypes);
    }

    /**
     * Return the index page.
     *
     * @param request  the server request
     * @param response the server response
     */
    private void index(ServerRequest request, ServerResponse response) {
        response.headers().contentType(MediaTypes.TEXT_PLAIN);
        response.send("""
                Pokemon JDBC Example:
                     GET /type                - List all pokemon types
                     GET /pokemon             - List all pokemons
                     GET /pokemon/{id}        - Get pokemon by id
                     GET /pokemon/name/{name} - Get pokemon by name
                     POST /pokemon            - Insert new pokemon:
                                                {"id":<id>,"name":<name>,"type":<type>}
                     PUT /pokemon             - Update pokemon
                                                {"id":<id>,"name":<name>,"type":<type>}
                     DELETE /pokemon/{id}     - Delete pokemon with specified id
                """);
    }

    private void listTypes(ServerRequest request, ServerResponse response) {

        JsonArray jsonArray = dbClient.execute()
                .namedQuery("select-all-types")
                .map(row -> row.as(JsonObject.class))
                .collect(JSON_BUILDER_FACTORY::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::addAll)
                .build();

        response.send(jsonArray);
    }

    private void init() {
        resetDb();
        if (initSchema) {
            initSchema();
        }
        if (initData) {
            initData();
        }
    }

    private void resetDb() {
        final DbExecute exec = dbClient.execute();
        exec.namedDml("drop-schema-and-recreate");
    }

    private void initData() {
        final DbTransaction tx = dbClient.transaction();
        try {
            initTypes(tx);
            initPokemons(tx);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    private static void initPokemons(DbExecute tx) {
        try (final var reader = Json.createReader(PokemonService.class.getResourceAsStream("/pokemons.json"))) {
            final JsonArray pokemons = reader.readArray();
            for (JsonValue pokemonValue : pokemons) {
                final JsonObject pokemon = pokemonValue.asJsonObject();
                try {
                    tx.namedInsert(
                            "insert-pokemon",
                            pokemon.getString("name"),
                            pokemon.getInt("idType"));
                } catch (Exception e) {
                    LOGGER.log(System.Logger.Level.ERROR, "Could not insert pokemon " + pokemon.getString("name"), e);
                }
            }
        }
    }

    private static void initTypes(DbExecute tx) {
        try (final var reader = Json.createReader(PokemonService.class.getResourceAsStream("/pokemon-types.json"))) {
            final JsonArray types = reader.readArray();
            for (JsonValue typeValue : types) {
                final JsonObject type = typeValue.asJsonObject();
                try {
                    tx.namedInsert(
                            "insert-type",
                            type.getString("name"));
                } catch (Exception e) {
                    LOGGER.log(System.Logger.Level.ERROR, "Could not insert type " + type.getString("name"), e);
                }
            }
        }
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
