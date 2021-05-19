package com.example.starter.handler.idValidator;

import com.example.starter.db.MongoDB;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class IdValidatorHandler implements Handler<RoutingContext> {
  String carId;

  private MongoDB client;

  public IdValidatorHandler(MongoDB client){
    this.client = client;
  }

  @Override
  public void handle(RoutingContext rc){

    carId = rc.pathParam("carId");
    JsonObject car = new JsonObject();

    //TODO: Puedo intentar poner el validate para que devuelva un Optional
    if (validate(carId))
      car.put("_id", Integer.parseInt(carId));

    else
      car.put("_id", carId);

    client.find(car)
      .subscribe(a -> rc.put("car", a).next(),
        // On Error
        error ->
          response(rc, "Oops, something went wrong"),
        () ->
          response(rc, "Car not found")
      );
  }

  private void response(RoutingContext rc, String s) {
    rc.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(HTTP_BAD_REQUEST)
      .end(Json.encodePrettily(new JsonObject().put("response", s)));
  }

  private boolean validate(String carIdString){
    try {
      Integer.parseInt(carIdString);
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static boolean validateHelper(String carIdString){
    try {
      Integer.parseInt(carIdString);
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

}
