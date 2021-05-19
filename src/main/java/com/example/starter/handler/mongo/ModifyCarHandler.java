package com.example.starter.handler.mongo;

import com.example.starter.db.MongoDB;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class ModifyCarHandler implements Handler<RoutingContext> {
  private final MongoDB client;

  public ModifyCarHandler(MongoDB client){
    this.client = client;
  }

  @Override
  public void handle(RoutingContext rc){

    JsonObject car = rc.get("car");
    JsonObject replace = new JsonObject();

    // Modify the car
    MultiMap attributes = rc.request().formAttributes();

    if (attributes.get("car_make") != null)
      replace
        .put("car_make", attributes.get("car_make"));
    if (attributes.get("car_model") != null)
      replace
        .put("car_model", attributes.get("car_model"));
    if (attributes.get("car_model_year") != null)
      replace
        .put("car_model_year", attributes.get("car_model_year"));

    client.modify(car, replace)
      .subscribe(
        value -> {
          if(!value.isEmpty())
            rc.json(value);
          else
            rc.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(HTTP_BAD_REQUEST)
              .end(Json.encodePrettily(new JsonObject().put("response", "Car not found")));
        },
        // On Error
        r -> rc.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end(Json.encodePrettily(new JsonObject().put("response", "Oops, something went wrong"))));
  }
}
