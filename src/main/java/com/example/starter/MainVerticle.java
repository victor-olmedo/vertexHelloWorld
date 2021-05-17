package com.example.starter;

import com.example.starter.handler.*;
import io.reactivex.Completable;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import io.vertx.reactivex.redis.client.Redis;
import io.vertx.reactivex.redis.client.RedisAPI;


public class MainVerticle extends AbstractVerticle {

  private static Integer port = 8888;
//  private static Integer jedisPort = 6379;
  private static String dbPath = "src/main/java/com/example/starter/db/dummy_database.json";
  private HttpServer server;

  //
//  JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt );
//  JsonParser jsonParser = JsonParser.newParser();
//  private static JsonObject jsonObject = jsonParser.parse(is);

  @Override
  public Completable rxStart() {
    // Create a Router
    Router router = Router.router(vertx);

    // Create BodyHandle to get the body in POSTs
    router.route().handler(BodyHandler.create());

    vertx.fileSystem().rxReadFile(dbPath)
      .subscribe(readJson ->{
        JsonArray db = new JsonArray(readJson.toString());
        router.get("/cars/:carId").handler(new CarHandler(db));
        router.post("/addCar").handler(new AddCarHandler(db));
        router.post("/removeCar/:carId").handler(new RemoveCarHandler(db));
        router.post("/modifyCar/:carId").handler(new ModifyCarHandler(db));
        // Redis
        Redis.createClient(
          vertx, "redis://localhost:6379/0")
          .rxConnect()
          .subscribe(conn -> {

            RedisAPI redis = RedisAPI.api(conn);

            redis.rxDbsize()
              .subscribe(result -> System.out.println(result.toString()));

            //  First Initialization
//            ArrayList<String> input;
//            for (Object element:db) {
//              input = new ArrayList<>();
//              input.add(((JsonObject) element).getValue("id").toString());
//              input.add(((JsonObject) element).getValue("car_model").toString());
//              redis.set(input);
//            }

            router.get("/carsRedis/:carId").handler(new CarHandlerRedis(redis));
            router.post("/addCarRedis").handler(new AddCarHandlerRedis(redis));
            router.post("/removeCarRedis/:carId").handler(new RemoveCarHandlerRedis(redis));
            router.post("/modifyCarRedis/:carId").handler(new ModifyCarHandlerRedis(redis));

            // Mongo
        String mongoConfig = "mongodb://root:example@localhost:27017/cars?ssl=true";
        JsonObject mongoJson = new JsonObject().put("connection_string", mongoConfig);
        MongoClient client = MongoClient.createShared(vertx, mongoJson);
        for (Object element:db) {
          JsonObject jsonElement = ((JsonObject) element).copy();
          jsonElement.put("_id", jsonElement.getValue("id"));
          jsonElement.remove("id");
          client.rxSave("cars", jsonElement).subscribe(res -> System.out.println(res));
        }
//              input = new ArrayList<>();
//              input.add(((JsonObject) element).getValue("id").toString());
//              input.add(((JsonObject) element).getValue("car_model").toString());
//              redis.set(input);
//            }
          });
      });

    // Create the HTTP server
    server = vertx.createHttpServer()
      // Handle every request using the router
      .requestHandler(router);
    // Start listening

    return server.rxListen(port).ignoreElement();
//      .subscribe(
//        res -> System.out.println(
//          "HTTP server started on port " + res.actualPort() + " with rx enabled"
//        ),
//        error -> System.out.println(
//          error.getCause().toString()
//        ));

    // return server.rxClose();
  }

//  @Override
//  public void start(Promise<Void> startPromise) throws Exception {
//    //db
////    Path fileName = Path.of(dbPath);
//
////    String readJson = Files.readString(fileName);
////    JsonArray db = new JsonArray(readJson);
//
//    // Create a Router
//    Router router = Router.router(vertx);
//
//    // Create BodyHandle to get the body in POSTs
//    router.route().handler(BodyHandler.create());
//
//    vertx.fileSystem().rxReadFile(dbPath)
//      .subscribe(readJson ->{
//
//        //JSON database
//
//        JsonArray db = new JsonArray(readJson.toString());
//        router.get("/cars/:carId").handler(new CarHandler(db));
//        router.post("/addCar").handler(new AddCarHandler(db));
//        router.post("/removeCar/:carId").handler(new RemoveCarHandler(db));
//        router.post("/modifyCar/:carId").handler(new ModifyCarHandler(db));
//
//        // Redis
//
//        Redis.createClient(
//          vertx, "redis://localhost:6379/0")
//          .rxConnect()
//          .subscribe(conn -> {
//
//            RedisAPI redis = RedisAPI.api(conn);
//
//            redis.rxDbsize()
//              .subscribe(result -> System.out.println(result.toString()));
//
//            //  First Initialization
////            ArrayList<String> input;
////            for (Object element:db) {
////              input = new ArrayList<>();
////              input.add(((JsonObject) element).getValue("id").toString());
////              input.add(((JsonObject) element).getValue("car_model").toString());
////              redis.set(input);
////            }
//
//            router.get("/carsRedis/:carId").handler(new CarHandlerRedis(redis));
//            router.post("/addCarRedis").handler(new AddCarHandlerRedis(redis));
//            router.post("/removeCarRedis/:carId").handler(new RemoveCarHandlerRedis(redis));
//            router.post("/modifyCarRedis/:carId").handler(new ModifyCarHandlerRedis(redis));
//          });
//
//        // Mongo
//        String mongoConfig = "mongodb://root:example@localhost:27017/cars?ssl=true";
//        JsonObject mongoJson = new JsonObject().put("connection_string", mongoConfig);
//        MongoClient client = MongoClient.createShared(vertx, mongoJson);
//        for (Object element:db) {
//          JsonObject jsonElement = ((JsonObject) element).copy();
//          jsonElement.put("_id", jsonElement.getValue("id"));
//          jsonElement.remove("id");
//          client.rxSave("cars", jsonElement).subscribe(res -> System.out.println(res));
//        }
////              input = new ArrayList<>();
////              input.add(((JsonObject) element).getValue("id").toString());
////              input.add(((JsonObject) element).getValue("car_model").toString());
////              redis.set(input);
////            }
//      });
//
//    // Route paths to corresponding handlers
////    router.get("/hello").respond(response -> Maybe.just(new JsonObject().put("hello", "world")));
//
//
//
//
////    try (Jedis jedis = new Jedis("localhost", jedisPort)) {
////      jedis.connect();
////      jedis.select(0);
////      for (Object element:db) {
////        jedis.set(((JsonObject) element).getValue("id").toString(), ((JsonObject) element).getValue("car_model").toString());
////      }
////
////    }
//
//    // Welcome user in root path:
//
////    // Mount the handler for all incoming requests at every path and HTTP method
////    router.route().handler(context -> {
////      // Get the address of the request
////      String address = context.request().connection().remoteAddress().toString();
////      // Get the query parameter "name"
////      MultiMap queryParams = context.queryParams();
////      String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
////      // Write a json response
////      context.json(
////        new JsonObject()
////          .put("name", name)
////          .put("address", address)
////          .put("message", "Hello " + name + " connected from " + address)
////      );
////    });
//
//    // Create the HTTP server
//    vertx.createHttpServer()
//      // Handle every request using the router
//      .requestHandler(router)
//      // Start listening
//      .listen(port, res -> {
//        if (res.succeeded()) {
//          startPromise.complete();
//          System.out.println(
//            "HTTP server started on port " + res.result().actualPort()
//          );
//        } else {
//          System.out.println(
//            res.cause().toString()
//          );
//          startPromise.fail(res.cause());
//        }
//      });
//  }
}
