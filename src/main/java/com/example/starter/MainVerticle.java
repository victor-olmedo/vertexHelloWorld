package com.example.starter;

import com.example.starter.Router.myRouter;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;



public class MainVerticle extends AbstractVerticle {

  private static final Integer port = 8888;
  private static final String mongoConfig = "mongodb://root:example@localhost:27017/cars?authSource=admin";
//  private static final String dbPath = "src/main/java/com/example/starter/db/dummy_database.json";

  @Override
  public Completable rxStart() {

    // Configure Router
    myRouter router = new myRouter(vertx);
    router.configureRouter(mongoConfig);

    // Create the HTTP server
    // Handle every request using the router
    return vertx.createHttpServer()
      .requestHandler(router.getRouter())
      .rxListen(port)
      .doOnSuccess(response -> System.out.println("HTTP server started on port " + response.actualPort()))
      .ignoreElement();
  }
}



//    // Redis
//    Redis.createClient(vertx, "redis://localhost:6379/0")
//      .rxConnect()
//      .map(conn -> routerRedis(router, RedisAPI.api(conn)));
//
//    // Read JSON file
//    vertx.fileSystem().rxReadFile(dbPath)
//      .map(readJson -> new JsonArray(readJson.toString()))
//      .map(db -> router(router, db))
//      .map(db -> populateDBs(db, client, redis));
//
//        router.get("/hello").handler(rc -> rc.end("Hello"));
//      });
