package com.example.starter.handler.dummy;

import com.example.starter.idValidator.idValidator;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.Optional;

public class CarHandler implements Handler<RoutingContext> {

  private JsonArray db;

  public CarHandler(JsonArray db){
    this.db = db;
  }

  @Override
  public void handle(RoutingContext rc){

    // Check if given carID is a valid one
    Optional<JsonObject> responseJson = new idValidator().validate(rc, db);

    // Return car info
    responseJson.ifPresent(rc::json);
  }
}
