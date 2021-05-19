package com.example.starter.db;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;

public class MongoDB {
  private final Vertx vertx;
  private final MongoClient client;

  public MongoDB(Vertx vertx, String mongoConfig){
    this.vertx = vertx;
    this.client = configureMongo(mongoConfig);
  }

  public MongoClient getClient() {
    return client;
  }

  //  Add JSON database to Mongo
  public void populateDB(JsonArray db){
    System.out.println("populateDB Entered");
    for (Object element:db) {
      JsonObject jsonElement = ((JsonObject) element).copy();
      jsonElement.put("_id", jsonElement.getValue("id"));
      jsonElement.remove("id");
      client.rxSave("cars", jsonElement).subscribe();
    }
  }

  private MongoClient configureMongo(String mongoConfig){
    JsonObject mongoJson = new JsonObject().put("connection_string", mongoConfig);
    return MongoClient.createShared(vertx, mongoJson);
  }
}
