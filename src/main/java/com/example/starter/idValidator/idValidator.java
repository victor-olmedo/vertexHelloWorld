package com.example.starter.idValidator;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class idValidator {
  public static JsonObject validate(RoutingContext rc, JsonArray db){
    int carId;
    JsonObject responseJson;

    // Check if input id is an int
    try {
      carId = Integer.parseInt(rc.pathParam("carId"));
    }
    catch (NumberFormatException e)
    {
      rc.response()
        .putHeader("content-type", "text/html")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end("<h1>Input id is not an integer</h1>");
      return null;
    }
    // Check if id is in the database
    try {
      responseJson = db.getJsonObject(carId-1);
      // As we require all 3 values, when we delete a value we set its fields to null,
      // and here we check if either of them is null (to see if it was deleted)
      if (responseJson.getValue("car_make") == null || responseJson.getValue("car_model") == null || responseJson.getValue("car_model_year") == null){
        rc.response()
          .putHeader("content-type", "text/html")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end("<h1>Id does not match our records</h1>");
        return null;
      }
    }
    catch (IndexOutOfBoundsException e){
      rc.response()
        .putHeader("content-type", "text/html")
        .setStatusCode(HTTP_BAD_REQUEST)
        .end("<h1>Id does not match our records</h1>");
      return null;
    }
    return responseJson;
  }

  public static boolean validate(int carId, JsonArray db){
    try {
      db.getJsonObject(carId-1);
    }
    catch (IndexOutOfBoundsException e){
      return false;
    }
    return true;
  }
}
