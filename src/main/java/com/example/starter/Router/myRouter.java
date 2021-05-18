package com.example.starter.Router;

import com.example.starter.handler.AddCarHandlerMongo;
import com.example.starter.handler.CarHandlerMongo;
import com.example.starter.handler.ModifyCarHandlerMongo;
import com.example.starter.handler.RemoveCarHandlerMongo;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class myRouter{

  private final Router router;
  private final Vertx vertx;

  public myRouter(Vertx vertx){
    // Initialize variables
    this.vertx = vertx;
    this.router = Router.router(vertx);

    // Create BodyHandle to get the body in POSTs
    this.router.route().handler(BodyHandler.create());
  }

  public Router getRouter() {
    return router;
  }

  public void configureRouter(String mongoConfig){
    MongoClient client = configureMongo(mongoConfig);
    configureRouter(client);
  }

  private MongoClient configureMongo(String mongoConfig){
    JsonObject mongoJson = new JsonObject().put("connection_string", mongoConfig);
    return MongoClient.createShared(vertx, mongoJson);
  }

  private void configureRouter(MongoClient client){
    this.router.get("/carsMongo/:carId").handler(new CarHandlerMongo(client));
    this.router.post("/addCarMongo").handler(new AddCarHandlerMongo(client));
    this.router.post("/removeCarMongo/:carId").handler(new RemoveCarHandlerMongo(client));
    this.router.post("/modifyCarMongo/:carId").handler(new ModifyCarHandlerMongo(client));
  }
}
