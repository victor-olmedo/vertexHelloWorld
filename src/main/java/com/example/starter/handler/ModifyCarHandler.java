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

public class ModifyCarHandler  implements Handler<RoutingContext> {
  private JsonArray db;

  public ModifyCarHandler(JsonArray db){
    this.db = db;
  }

  @Override
  public void handle(RoutingContext rc){

    JsonObject responseJson = idValidator.validate(rc, db).get();

    MultiMap attributes = rc.request().formAttributes();


    // Modify the car
    if (attributes.get("car_make") != null)
      responseJson
        .put("car_make", attributes.get("car_make"));
    if (attributes.get("car_model") != null)
      responseJson
        .put("car_model", attributes.get("car_model"));
    if (attributes.get("car_model_year") != null)
      responseJson
        .put("car_model_year", attributes.get("car_model_year"));

    // Return that new car as a JSON file
    rc.json(Json.encodePrettily("Successfully modified"));
  }
}
