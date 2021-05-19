package com.example.starter;

import com.example.starter.db.MongoDB;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;

public class Main {

  private static final String MONGO_CONFIG = "mongodb://root:example@localhost:27017/cars?authSource=admin";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    ServerVerticle sv = new ServerVerticle();

    vertx.rxDeployVerticle(sv)
      .subscribe(id -> System.out.println("Verticle " + id + " deployed"));

    vertx.rxDeployVerticle(new PopulateDbVerticle(), new DeploymentOptions().setConfig(new JsonObject().put("mongo", MONGO_CONFIG)))
      .subscribe(System.out::println);
  }
}
