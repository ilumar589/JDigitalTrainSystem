package inhouse.digital.trainsystem;

import io.helidon.common.context.Contexts;
import io.helidon.config.Config;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.health.DbClientHealthCheck;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.observe.ObserveFeature;
import io.helidon.webserver.observe.health.HealthObserver;

public class Main {

    private Main() {}

    public static void main(String[] args) {
        // load logging configuration
        LogConfig.configureRuntime();

        // initialize config from default configuration
        final Config config = Config.global();

        final DbClient dbClient = DbClient.create(config.get("db"));
        Contexts.globalContext().register(dbClient);

        final ObserveFeature observe = ObserveFeature.builder()
                .addObserver(HealthObserver.builder()
                        .addCheck(DbClientHealthCheck.create(dbClient, config.get("db.health-check")))
                        .build())
                .build();

        //TODO add coherence later and test it out

        final WebServer server = WebServer.builder()
                .config(config.get("server"))
                .addFeature(observe)

    }

    static void routing(HttpRouting.Builder routing) {

    }
}
