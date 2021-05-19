package com.example.starter.db;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;

import java.util.List;

public class MongoDB {
  private final MongoClient client;

  public MongoDB(String mongoConfig){
    this.client = configureMongo(mongoConfig);
  }

  public MongoClient getClient() {
    return client;
  }

  //  Add JSON database to Mongo
  public void populateDB(JsonArray db){

    for (Object element:db) {
      JsonObject jsonElement = ((JsonObject) element).copy();
      jsonElement.put("_id", jsonElement.getValue("id"));
      jsonElement.remove("id");
      client.rxSave("cars", jsonElement).subscribe();
    }
  }

  public Maybe<String> insert(JsonObject json){
    return client.rxInsert("cars", json);
  }

  public Maybe<JsonObject> find(JsonObject json){
    return client.rxFindOne("cars", json, new JsonObject());
  }

  public Maybe<MongoClientDeleteResult> remove(JsonObject json){
    return client.rxRemoveDocument("cars", json);
  }

  public Maybe<JsonObject> modify(JsonObject original, JsonObject actual){
    return client.rxFindOneAndReplace("cars", original, actual);
  }



  private MongoClient configureMongo(String mongoConfig){
    JsonObject mongoJson = new JsonObject().put("connection_string", mongoConfig);
    return MongoClient.createShared(Vertx.currentContext().owner(), mongoJson);
  }
}
