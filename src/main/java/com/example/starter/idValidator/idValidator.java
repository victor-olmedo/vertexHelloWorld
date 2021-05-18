package com.example.starter.idValidator;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class idValidator {
  JsonObject responseJson;
  String carId;
  public Optional<JsonObject> validate(RoutingContext rc, JsonArray db){
    carId = rc.pathParam("carId");
    Optional<JsonObject> response = Optional.empty();
    // Check if input id is an int
    if(!validate(carId)) {
      rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response", "Input id is not an integer")));
      return response;
    }

    int intCarId = Integer.parseInt(carId);

    if(!exists(db, intCarId)){
      rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response", "Id does not match our records")));
      return response;
    }

    return response.of(this.responseJson);
  }

  public static boolean validate(String carIdString){
    try {
      Integer.parseInt(carIdString);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }

  // Check if id is in the database
  public boolean exists(JsonArray db, int carId){

    try {
      this.responseJson = db.getJsonObject(carId-1);
      // As we require all 3 values, when we delete a value we set its fields to null,
      // and here we check if either of them is null (to see if it was deleted)
      if (this.responseJson.getValue("car_make") == null || this.responseJson.getValue("car_model") == null || this.responseJson.getValue("car_model_year") == null){
        return false;
      }
    }
    catch (IndexOutOfBoundsException e){
      return false;
    }
    return true;
  }
}
