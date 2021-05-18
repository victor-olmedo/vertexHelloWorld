package com.example.starter.db;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.redis.client.RedisAPI;


import java.util.ArrayList;

public class RedisDB {
    //  Add JSON database to Redis
  public static void populateDB(JsonArray db, RedisAPI redis){
    ArrayList<String> input;
    for (Object element:db) {
      input = new ArrayList<>();
      input.add(((JsonObject) element).getValue("id").toString());
      input.add(((JsonObject) element).getValue("car_model").toString());
      redis.set(input);
    }
  }
}
