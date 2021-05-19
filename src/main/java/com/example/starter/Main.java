package com.example.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;

public class Main {

  private static final String mongoConfig = "mongodb://root:example@localhost:27017/cars?authSource=admin";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    ServerVerticle sv = new ServerVerticle();

    vertx.rxDeployVerticle(sv)
    .subscribe(id -> System.out.println("Verticle " + id + " deployed"));

    vertx.rxDeployVerticle(new PopulateDbVerticle(), new DeploymentOptions().setConfig(new JsonObject().put("mongo", mongoConfig)))
      .subscribe(System.out::println);
  }
}
