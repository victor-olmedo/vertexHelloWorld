package com.example.starter.handler;

import com.example.starter.idValidator.idValidator;
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

public class ModifyCarHandlerRedis  implements Handler<RoutingContext> {
  private RedisAPI redis;

  public ModifyCarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");

    if(idValidator.validate(carId) && rc.request().formAttributes().get("car_make") != null){
      ArrayList<String> input = new ArrayList<>();
      input.add(carId);
      redis.exists(input, res -> {
        if(res.result().toBoolean()) {
          input.add(rc.request().formAttributes().get("car_make"));
          redis.set(input).onComplete(result ->
            rc.json(new JsonObject().put("response", "Successfully modified"))
          );
          return;
        }
        rc.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end(Json.encodePrettily(new JsonObject().put("response", "Not a valid id and/or car_make missing")));
      });
    }
  }
}
