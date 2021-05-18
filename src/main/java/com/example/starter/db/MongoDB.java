package com.example.starter.db;

import com.example.starter.handler.AddCarHandlerMongo;
import com.example.starter.handler.CarHandlerMongo;
import com.example.starter.handler.ModifyCarHandlerMongo;
import com.example.starter.handler.RemoveCarHandlerMongo;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;

public class MongoDB {
    //  Add JSON database to Mongo
  public static void populateDB(JsonArray db, MongoClient client){
    for (Object element:db) {
      JsonObject jsonElement = ((JsonObject) element).copy();
      jsonElement.put("_id", jsonElement.getValue("id"));
      jsonElement.remove("id");
      client.rxSave("cars", jsonElement);
    }
  }
}
