package com.example.starter.acceptance.cucumber;

import com.example.starter.ServerVerticle;
import com.example.starter.handler.idValidator.IdValidatorHandler;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.reflect.TypeToken;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {
  private String id;
  private String response;
  private JsonObject carJson;
  private Vertx vertx = Vertx.vertx();
  private ServerVerticle sv;

//  @Before
//  public void before(){
//    sv = new ServerVerticle();
//    vertx.rxDeployVerticle(sv);
//    System.out.println("Verticle deployed");
//  }
//
//  @After
//  public void after(){
//    vertx.undeploy(sv.deploymentID());
//  }

  public MyStepdefs() {
  }

  @Given("{string} id")
  public void id(String state) {
    // Write code here that turns the phrase above into concrete actions
    switch (state) {
      case "valid":
        id = "1";
        break;
      case "invalid":
        id = "alkdjfakjsdhf";
        break;
    }
  }

  @Given("{string} id to modify")
  public void id_to_modify(String state) {
    // Write code here that turns the phrase above into concrete actions
    switch (state) {
      case "valid":
        id = "5";
        break;
      case "invalid":
        id = "aninvalidcar";
        break;
    }
  }

  @Given("{string} car")
  public void car(String car) {
    switch (car) {
      case "valid":
        carJson = new JsonObject()
          .put("car_make", "testCarMake")
          .put("car_model", "testCarModel")
          .put("car_model_year", "2021");
        break;
      case "invalid":
        carJson = new JsonObject();
        break;
    }
  }

  @When("I ask if this id is ok")
  public void i_ask_if_this_id_is_ok() {
    // Write code here that turns the phrase above into concrete actions
    response = IdValidatorHandler.validateHelper(id) ? "yes" : "no";
  }

  @Then("I should be told {string}")
  public void i_should_be_told(String string) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(string, response);
  }

  @When("I ask to add the car")
  public void iAskToAddTheCar() throws Exception {
    Map<Object, Object> data = new Gson().fromJson(carJson.toString(), new TypeToken<HashMap<Object,Object>>() {}.getType());

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://localhost:8888/addCar"))
      .setHeader("Content-Type", "multipart/form-data")
      .POST(ofFormData(data))
      .build();
    response = "" + httpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
  }

  @When("I ask to delete the car")
  public void iAskToDeleteTheCar() throws Exception{
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://localhost:8888/removeCar/"+id))
      .setHeader("Content-Type", "multipart/form-data")
      .POST(HttpRequest.BodyPublishers.ofString(""))
      .build();
    response = "" + httpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();

  }

  @When("I ask to modify the car")
  public void iAskToModifyTheCar() throws Exception{
    Map<Object, Object> data = new Gson().fromJson(carJson.toString(), new TypeToken<HashMap<Object,Object>>() {}.getType());

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://localhost:8888/modifyCar/"+id))
      .setHeader("Content-Type", "multipart/form-data")
      .POST(ofFormData(data))
      .build();
    response = "" + httpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();

  }

  @When("I ask to get the car")
  public void iAskToGetTheCar() throws Exception{
    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("http://localhost:8888/cars/"+id))
      .GET()
      .build();
    response = "" + httpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();

  }

  public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
    var builder = new StringBuilder();
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
      if (builder.length() > 0) {
        builder.append("&");
      }
      builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
      builder.append("=");
      builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
    }
    return HttpRequest.BodyPublishers.ofString(builder.toString());
  }

}
