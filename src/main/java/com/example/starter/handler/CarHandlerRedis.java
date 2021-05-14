package com.example.starter.handler;

import com.example.starter.idValidator.idValidator;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisConnection;

import java.util.Optional;

public class CarHandlerRedis implements Handler<RoutingContext> {

  private RedisAPI redis;

  public CarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");

    // Return car info

    //TODO: null pointer exception cuando no está el coche en la base de datos
    if (idValidator.validate(carId))
      redis.get(carId).onComplete( value -> rc.json(new JsonObject().put( carId, value.result().toString() )));
  }
}
