package com.example.starter.Router;

import com.example.starter.db.MongoDB;
import com.example.starter.handler.idValidator.IdValidatorHandler;
import com.example.starter.handler.mongo.*;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class MyRouter {

  private final Router router;

  public MyRouter(Vertx vertx){
    // Initialize variables
    this.router = Router.router(vertx);

    // Create BodyHandle to get the body in POSTs
    this.router.route().handler(BodyHandler.create());
  }

  public Router getRouter() {
    return router;
  }

  public void configureRouter(MongoDB client){
    this.router.get("/cars/:carId")
      .handler(new IdValidatorHandler(client))
      .handler(new CarHandler(client));
    this.router.post("/addCar")
//      .handler(new IdValidatorHandler(client))
      .handler(new AddCarHandler(client));
    this.router.post("/removeCar/:carId")
      .handler(new IdValidatorHandler(client))
      .handler(new RemoveCarHandler(client));
    this.router.post("/modifyCar/:carId")
      .handler(new IdValidatorHandler(client))
      .handler(new ModifyCarHandler(client));
  }
}
