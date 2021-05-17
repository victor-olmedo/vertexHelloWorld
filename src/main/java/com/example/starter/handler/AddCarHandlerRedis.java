package com.example.starter.handler;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisConnection;

import java.util.ArrayList;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class AddCarHandlerRedis  implements Handler<RoutingContext> {
  private RedisAPI redis;
  public AddCarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    // Get form attributes
    MultiMap attributes = rc.request().formAttributes();
    // Return error message if not all fields are specified
    if (attributes.get("car_make") == null){
      rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response","car_make field must be specified")));
      return;
    }


    // Add new car to the database
    redis.dbsize()
      .onComplete(result -> {
        String carId = Integer.toString((result.result().toInteger())+1);
//        System.out.println(carId);
        ArrayList<String> input = new ArrayList<>();
        input.add(carId);
        input.add(rc.request().formAttributes().get("car_make"));
        redis.set(input, handler -> rc.json(new JsonObject().put(carId, attributes.get("car_make"))));
      });
//    rc.json(new JsonObject().put(Integer.toString(db.dbSize().intValue()+1), attributes.get("car_make")));
  }

}


