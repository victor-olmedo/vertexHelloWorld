package com.example.starter.handler.redis;

import com.example.starter.handler.idValidator.IdValidatorHandler;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.redis.client.RedisAPI;

import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class ModifyCarHandlerRedis  implements Handler<RoutingContext> {
  private RedisAPI redis;

  public ModifyCarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");

    if(IdValidatorHandler.validateHelper(carId) && rc.request().formAttributes().get("car_make") != null){
      ArrayList<String> input = new ArrayList<>();
      input.add(carId);
      redis.rxExists(input).subscribe(
        res -> {
          if(res.toBoolean()) {
            input.add(rc.request().formAttributes().get("car_make"));
            redis.rxSet(input).subscribe(result ->
              rc.json(new JsonObject().put("response", "Successfully modified"))
            );
            return;
          }
          rc.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(HTTP_BAD_REQUEST)
            .end(Json.encodePrettily(new JsonObject().put("response", "Not a valid id and/or car_make missing")));
        });
//      redis.exists(input, res -> {
//        if(res.result().toBoolean()) {
//          input.add(rc.request().formAttributes().get("car_make"));
//          redis.rxSet(input).subscribe(result ->
//            rc.json(new JsonObject().put("response", "Successfully modified"))
//          );
//          return;
//        }
//        rc.response()
//          .putHeader("content-type", "application/json")
//          .setStatusCode(HTTP_BAD_REQUEST)
//          .end(Json.encodePrettily(new JsonObject().put("response", "Not a valid id and/or car_make missing")));
//      });
    }
  }
}
