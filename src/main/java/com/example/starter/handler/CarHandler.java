package com.example.starter.handler;

import com.example.starter.idValidator.idValidator;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class CarHandler implements Handler<RoutingContext> {

  private JsonArray db;

  public CarHandler(JsonArray db){
    this.db = db;
  }

  @Override
  public void handle(RoutingContext rc){

    // Check if given carID is a valid one
    JsonObject responseJson = idValidator.validate(rc, db);

    // Return car info
    if (responseJson != null) rc.json(responseJson);
  }
}
