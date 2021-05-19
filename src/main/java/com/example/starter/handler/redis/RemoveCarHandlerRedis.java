package com.example.starter.handler.redis;

import com.example.starter.handler.idValidator.IdValidatorHandler;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.redis.client.RedisAPI;

import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class RemoveCarHandlerRedis  implements Handler<RoutingContext> {
  private RedisAPI redis;

  public RemoveCarHandlerRedis(RedisAPI redis){
    this.redis = redis;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");
    if(IdValidatorHandler.validateHelper(carId)){
      // delete car from db
      ArrayList<String> input = new ArrayList<>();
      input.add(rc.pathParam("carId"));
      redis.rxDel(input).subscribe( res -> {

        // Return that new car as a JSON file
        rc.json(Json.encodePrettily("<h1>Successfully deleted<h1>"));
        },
        //on error
        result -> rc.response()
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
