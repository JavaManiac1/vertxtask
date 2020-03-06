package io.vertx.starter;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsoupParser {
  ExecutorService myExecutor = Executors.newCachedThreadPool();

  private List<Item> items;


  public List<Item> getAllItems(String keyword) {
    getall(keyword);

       return items;

  }


  public List<Item> getall(String keyword) {

    items = new ArrayList<>();
    synchronized (this) {
      try {
        Document doc = Jsoup
          .connect("https://www.olx.pl/oferty/q-" + keyword + "/")
          .followRedirects(true)
          .get();

        Elements el = doc.select("div.offer-wrapper");

        for (Element element : el) {

          Element div = element.select("table[data-id]").first();
          String attr = div.attr("data-id");
          String id = attr;
          String name = div.getElementsByTag("strong").text();
          String price = div.getElementsByClass("price").first().text();
          items.add(new Item(id, name, price));

        }
      } catch (IOException io) {
        System.out.println("Jsoup parser failded");
        io.printStackTrace();
      }
      //to delete
      for (Item item : items) {
        System.out.println(item.toString());
      }
      return items;
    }
  }
}

