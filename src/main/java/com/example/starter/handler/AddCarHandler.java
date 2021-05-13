package com.example.starter.handler;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class AddCarHandler  implements Handler<RoutingContext> {
  private JsonArray db;

  public AddCarHandler(JsonArray db){
    this.db = db;
  }

  @Override
  public void handle(RoutingContext rc){
    // Get form attributes
    MultiMap attributes = rc.request().formAttributes();

    // Return error message if not all fields are specified
    if (attributes.get("car_make") == null || attributes.get("car_model") == null || attributes.get("car_year") == null){
      rc.response()
        .putHeader("content-type", "text/html")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end("<h1>All car fields (car_make, car_model and car_year) must be specified</h1>");
      return;
    }

    //Create a new JsonObject from input data and add it to the database
    JsonObject newCar = new JsonObject()
    .put("id", db.size()+1)
    .put("car_make", attributes.get("car_make"))
    .put("car_model", attributes.get("car_model"))
    .put("car_year", attributes.get("car_year"));

    db.add(newCar);
    rc.json(newCar);
  }
}


