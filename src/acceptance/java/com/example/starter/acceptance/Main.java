package com.example.starter.acceptance;

import com.example.starter.PopulateDbVerticle;
import com.example.starter.ServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;

public class Main{
  private static final String MONGO_CONFIG = "mongodb://root:example@localhost:27017/cars?authSource=admin";


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    ServerVerticle sv = new ServerVerticle();

    vertx.rxDeployVerticle(sv)
      .subscribe(id -> System.out.println("Verticle " + id + " deployed"));

    vertx.rxDeployVerticle(new PopulateDbVerticle(), new DeploymentOptions().setConfig(new JsonObject().put("mongo", MONGO_CONFIG)))
      .subscribe(System.out::println);
    System.exit(passTests());
  }

  private static byte passTests() {

    byte exitStatus = io.cucumber.core.cli.Main.run(new String[]{
      "--glue",
      "com/example", // the package which contains the glue classes
      "--plugin",
      "pretty",
      "--plugin",
      "json:target/cucumber.json",
      "classpath:features/"}, Thread.currentThread().getContextClassLoader());
    System.out.println("******************************************************");
    System.out.println("********         exitStatus : " + exitStatus + "               ********");
    System.out.println("******************************************************");
    return exitStatus;
  }
}
