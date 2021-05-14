package com.example.starter.handler;

import com.example.starter.idValidator.idValidator;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class RemoveCarHandler  implements Handler<RoutingContext> {
  private JsonArray db;

  public RemoveCarHandler(JsonArray db){
    this.db = db;
  }

  @Override
  public void handle(RoutingContext rc){

    JsonObject responseJson = idValidator.validate(rc, db).get();

    // Set all attributes to null
    responseJson
      .putNull("id")
      .putNull("car_make")
      .putNull("car_model")
      .putNull("car_model_year");

    // Return that new car as a JSON file
    rc.json(Json.encodePrettily("<h1>Successfully deleted<h1>"));
  }
}
