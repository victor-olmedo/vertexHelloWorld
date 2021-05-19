package com.example.starter.handler.mongo;

import com.example.starter.db.MongoDB;
import com.example.starter.handler.idValidator.IdValidatorHandler;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class RemoveCarHandler implements Handler<RoutingContext> {
  private MongoDB client;

  public RemoveCarHandler(MongoDB client){
    this.client = client;
  }

  @Override
  public void handle(RoutingContext rc){
    String carId = rc.pathParam("carId");

    JsonObject query = new JsonObject();
    // Return car info
    if (IdValidatorHandler.validateHelper(carId))
      query.put("_id", Integer.parseInt(carId));
    else
      query.put("_id", carId);
    client.remove(query)
      .subscribe(
        value ->
          rc.json(new JsonObject().put("response", "Deleted Successfully")),
        // On Error
        r ->
          rc.response()
          .putHeader("content-type", "application/json")
          .setStatusCode(HTTP_BAD_REQUEST)
          .end(Json.encodePrettily(new JsonObject().put("response", "Oops, something went wrong "+r.toString()))));
  }
}
