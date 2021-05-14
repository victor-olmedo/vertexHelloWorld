package com.example.starter.idValidator;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.client.RedisConnection;

import java.util.Optional;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class idValidator {
  public static Optional<JsonObject> validate(RoutingContext rc, JsonArray db){
    int carId;
    JsonObject responseJson;
    Optional<JsonObject> response = Optional.empty();
    // Check if input id is an int
    try {
      carId = Integer.parseInt(rc.pathParam("carId"));
    }
    catch (NumberFormatException e)
    {
      rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response", "Input id is not an integer")));
      return response;
    }
    // Check if id is in the database
    try {
      responseJson = db.getJsonObject(carId-1);
      // As we require all 3 values, when we delete a value we set its fields to null,
      // and here we check if either of them is null (to see if it was deleted)
      if (responseJson.getValue("car_make") == null || responseJson.getValue("car_model") == null || responseJson.getValue("car_model_year") == null){
        rc.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end(Json.encodePrettily(new JsonObject().put("response", "Id does not match our records")));
        return response;
      }
    }
    catch (IndexOutOfBoundsException e){
      rc.response()
        .putHeader("content-type", "application/json")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end(Json.encodePrettily(new JsonObject().put("response", "Id does not match our records")));
      return response;
    }
    return response.of(responseJson);
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
}
