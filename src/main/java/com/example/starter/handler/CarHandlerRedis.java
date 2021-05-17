package com.example.starter.handler;

import com.example.starter.idValidator.idValidator;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.redis.client.RedisAPI;
import io.vertx.redis.client.RedisConnection;

import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class CarHandlerRedis implements Handler<RoutingContext> {

  private RedisAPI redis;

  public CarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");

    // Return car info

    if (idValidator.validate(carId))
      redis.rxGet(carId)
        .subscribe(
          value -> {
            rc.json(new JsonObject().put( carId, value.toString() ));
          },
          // On Error
          r -> {
              rc.response()
                .putHeader("content-type", "application/json")
                .setStatusCode(HTTP_BAD_REQUEST)
                .end(Json.encodePrettily(new JsonObject().put("response", "Oops, something went wrong")));
          },
          // On empty response
          () -> {
            rc.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(HTTP_BAD_REQUEST)
              .end(Json.encodePrettily(new JsonObject().put("response", "Id does not match our records")));
          });
  }
}
