package com.example.starter.idValidator;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class idValidator {
  public static JsonObject validate(RoutingContext rc, JsonArray db){
    int carId;
    JsonObject responseJson;
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
    try {
      responseJson = db.getJsonObject(carId-1);
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
}
