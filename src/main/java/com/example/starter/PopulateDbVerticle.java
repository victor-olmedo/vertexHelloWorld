package com.example.starter;

import com.example.starter.db.MongoDB;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.AbstractVerticle;



public class PopulateDbVerticle extends AbstractVerticle {

  private static final String dbPath = "src/main/java/com/example/starter/db/dummy_database.json";

  @Override
  public Completable rxStart() {

    // Get mongo data
    MongoDB mongo = new MongoDB(vertx, config().getValue("mongo").toString());

    // Read JSON file & populate DB with it
    return readDB(dbPath)
      .doOnSuccess(mongo::populateDB)
      .ignoreElement();
  }

  // Read JSON file
  private Single<JsonArray> readDB(String dbPath){
    return vertx.fileSystem().rxReadFile(dbPath)
      .map(readJson -> new JsonArray(readJson.toString()));
  }
}



//    // Redis
//    Redis.createClient(vertx, "redis://localhost:6379/0")
//      .rxConnect()
//      .map(conn -> routerRedis(router, RedisAPI.api(conn)));
//
