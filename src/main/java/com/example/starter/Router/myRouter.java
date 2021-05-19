package com.example.starter.Router;

import com.example.starter.handler.mongo.AddCarHandler;
import com.example.starter.handler.mongo.CarHandler;
import com.example.starter.handler.mongo.ModifyCarHandler;
import com.example.starter.handler.mongo.RemoveCarHandler;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class myRouter{

  private final Router router;

  public myRouter(Vertx vertx){
    // Initialize variables
    this.router = Router.router(vertx);

    // Create BodyHandle to get the body in POSTs
    this.router.route().handler(BodyHandler.create());
  }

  public Router getRouter() {
    return router;
  }

  public void configureRouter(MongoClient client){
    this.router.get("/cars/:carId").handler(new CarHandler(client));
    this.router.post("/addCar").handler(new AddCarHandler(client));
    this.router.post("/removeCar/:carId").handler(new RemoveCarHandler(client));
    this.router.post("/modifyCar/:carId").handler(new ModifyCarHandler(client));
  }
}
