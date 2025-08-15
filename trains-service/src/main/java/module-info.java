module trains.service {
    requires io.helidon.webserver;
    requires io.helidon.http;
    requires io.helidon.config;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires io.helidon.http.media.multipart;
    requires io.helidon.webserver.staticcontent;
    requires jakarta.json;
    requires io.helidon.webserver.observe.metrics;
    requires io.helidon.metrics.api;
    requires io.helidon.webserver.observe.health;
    requires io.helidon.health.checks;
    requires io.helidon.webserver.http2;
    requires io.helidon.webserver.observe.tracing;
    requires io.helidon.tracing;
    requires io.helidon.webserver.observe;
    requires io.helidon.dbclient;
    requires io.helidon.dbclient.metrics;
    requires io.helidon.dbclient.tracing;
    requires io.helidon.security.integration.common;
    requires io.helidon.webserver.security;
    requires com.oracle.coherence;
    requires io.helidon.logging.common;
    requires io.helidon.dbclient.health;

    exports inhouse.digital.trainsystem;

    opens WEB;
}