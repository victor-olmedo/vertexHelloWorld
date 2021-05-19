package com.example.starter.handler.mongo;

import com.example.starter.db.MongoDB;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class CarHandler implements Handler<RoutingContext> {
  private MongoDB client;

  public CarHandler(MongoDB client){
    this.client = client;
  }

  @Override
  public void handle(RoutingContext rc){
    JsonObject car = rc.get("car");

    rc.json(car);

//    car = new JsonObject().put("_id", rc.pathParam("carId"));
//    client.find(car)
//      .subscribe(
//        rc::json,
//        // On Error
//        error ->
//          response(rc, "Oops, something went wrong"),
//        () -> response(rc, "Car not found"));

  }

  private void response(RoutingContext rc, String s) {
    rc.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(HTTP_BAD_REQUEST)
      .end(Json.encodePrettily(new JsonObject().put("response", s)));
  }
}
