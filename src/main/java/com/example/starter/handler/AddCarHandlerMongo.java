package com.example.starter.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class AddCarHandlerMongo implements Handler<RoutingContext> {
  private MongoClient client;

  public AddCarHandlerMongo(MongoClient client){
    this.client = client;
  }

  @Override
  public void handle(RoutingContext rc){
    // Get form attributes
    MultiMap attributes = rc.request().formAttributes();

    // Return error message if not all fields are specified
    if (attributes.get("car_make") == null || attributes.get("car_model") == null || attributes.get("car_model_year") == null){
      rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response","All car fields (car_make, car_model and car_model_year) must be specified")));
      return;
    }

    //Create a new JsonObject from input data and add it to the database
    JsonObject newCar = new JsonObject()
      .put("car_make", attributes.get("car_make"))
      .put("car_model", attributes.get("car_model"))
      .put("car_model_year", attributes.get("car_model_year"));


    // Add new car to the database
    client.rxInsert("cars", newCar)
      .subscribe(id -> {
        System.out.println(id);
        rc.json(newCar.put("_id", id));
      }, error -> {
        System.out.println(error);
        rc.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end(Json.encodePrettily(new JsonObject().put("response","Error")));

      });
  }
}
