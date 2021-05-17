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

public class RemoveCarHandlerRedis  implements Handler<RoutingContext> {
  private RedisAPI redis;

  public RemoveCarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");
    if(idValidator.validate(carId)){
      // delete car from db
      ArrayList<String> input = new ArrayList<>();
      input.add(rc.pathParam("carId"));
      redis.del(input).onSuccess( res -> {

        // Return that new car as a JSON file
        rc.json(Json.encodePrettily("<h1>Successfully deleted<h1>"));
        }
      ).onFailure(result -> rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response", "Id does not match our records"))));
      return;
    }
    rc.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(HTTP_BAD_REQUEST)
      .end(Json.encodePrettily(new JsonObject().put("response", "Not a valid id")));
  }
}
