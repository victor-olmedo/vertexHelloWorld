package com.example.starter;

import com.sun.tools.javac.Main;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  @DisplayName("Deploy a verticle")
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.rxDeployVerticle(new MainVerticle()).doOnSuccess(r -> testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  @DisplayName("Test HTTP server init")
  void http_server_check_response(Vertx vertx, VertxTestContext testContext) {
    HttpClient client = vertx.createHttpClient();
    client.rxRequest(HttpMethod.GET, 8888, "localhost", "/hello")
      .compose(s -> s.doOnSuccess(req -> req.rxSend()))
      .doOnSuccess(r -> testContext.succeeding(buffer -> testContext.verify(() -> {
        assertThat(buffer.toString(), is(equalTo("Hello")));
        testContext.completeNow();
      })));
  }

  // Tests JSON
//  @Test
//  @DisplayName("Test JSON db init")
//  void json_init(Vertx vertx, VertxTestContext testContext){
//    vertx.fileSystem().rxReadFile(dbPath)
//      .subscribe(readJson -> {
//          JsonArray db = new JsonArray(readJson.toString());
//        });
//    HttpClient client = vertx.createHttpClient();
//    client.request(HttpMethod.GET, 8888, "localhost", "/hello")
//      .compose(req -> req.send().compose(HttpClientResponse::body))
//      .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
//        assertThat(buffer.toString(), is(equalTo("Hello")));
//        testContext.completeNow();
//      })));
//  }
}
