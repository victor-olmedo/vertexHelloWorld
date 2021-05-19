package com.example.starter.handler.mongo;

import com.example.starter.idValidator.idValidator;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class CarHandler implements Handler<RoutingContext> {
  private MongoClient client;

  public CarHandler(MongoClient client){
    this.client = client;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");

    JsonObject query = new JsonObject();
    // Return car info
    if (idValidator.validate(carId))
      query.put("_id", Integer.parseInt(carId));
    else
      query.put("_id", carId);
    client.rxFind("cars", query)
      .subscribe(
        value -> {
          if(!value.isEmpty())
            rc.json(value.get(0));
          else
            rc.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(HTTP_BAD_REQUEST)
              .end(Json.encodePrettily(new JsonObject().put("response", "Car not found")));
        },
        // On Error
        r -> rc.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end(Json.encodePrettily(new JsonObject().put("response", "Oops, something went wrong"))));
  }
}
