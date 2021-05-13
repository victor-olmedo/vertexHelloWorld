package com.example.starter.handler;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class AddCarHandler  implements Handler<RoutingContext> {
  private JsonArray db;

  public AddCarHandler(JsonArray db){
    this.db = db;
  }

  @Override
  public void handle(RoutingContext rc){
    // Get form attributes
    MultiMap attributes = rc.request().formAttributes();

    // Iterate through all entries getting Keys and Values
    for (Map.Entry<String,String> attribute: attributes.entries()) {
      System.out.println(attribute.getKey());
      System.out.println(attribute.getValue());
    }

    //TODO: Modify database

    rc.end();
  }
}


