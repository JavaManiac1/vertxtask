package io.vertx.starter;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

  List<Item> items = new ArrayList<>();


  @Override
  public void start(Promise<Void> promise) throws Exception{
    VertxOptions options = new VertxOptions();
    options.setWarningExceptionTime(2000);
    options.setBlockedThreadCheckInterval(50000);


    Future<Void>  step = startHttpServer();
    step.setHandler(ar->{
      if(ar.succeeded()){
        promise.complete();
      }else{
        promise.fail(ar.cause());
      }
    });
  }

  private Future<Void> startHttpServer() {
    Promise<Void> promise = Promise.promise();
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.get("/offers/olx/:keyword").handler(this::getItemsHandler);

    server
      .requestHandler(router)
      .listen(8087, ar -> {
      if(ar.succeeded()){
        System.out.println("Serwer works on 8087");
      }else{
        System.out.println("server failed");
      }
    });
    return promise.future();

  }


  private void getItemsHandler(RoutingContext context){

    String keyword = context.request().getParam("keyword");

    parseHtml(keyword);

   context.response().putHeader("content-type", "application/json; charset=utf-8");
   context.response().end(Json.encodePrettily(items));
  }

  private Future<Void> parseHtml(String keyword){
    Promise<Void> promise = Promise.promise();

    ItemService itemService = new ItemService();
    items = itemService.parseHTML(keyword);
    promise.complete();
    return  promise.future();

  }

}
