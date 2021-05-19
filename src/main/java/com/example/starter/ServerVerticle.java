package com.example.starter;

import com.example.starter.Router.myRouter;
import com.example.starter.db.MongoDB;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;



public class ServerVerticle extends AbstractVerticle {

  private static final Integer port = 8888;
  private static final String mongoConfig = "mongodb://root:example@localhost:27017/cars?authSource=admin";

  @Override
  public Completable rxStart() {

    // Configure Router
    myRouter router = new myRouter(vertx);
    MongoDB mongo = new MongoDB(vertx, mongoConfig);
    router.configureRouter(mongo.getClient());

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
