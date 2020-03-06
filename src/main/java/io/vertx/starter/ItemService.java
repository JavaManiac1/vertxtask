package io.vertx.starter;

import java.util.ArrayList;
import java.util.List;

public class ItemService {
   private  List<Item> items = new ArrayList<>();


   public List<Item> parseHTML(String keyword){

       JsoupParser jsoupParser = new JsoupParser();
       items = jsoupParser.getAllItems(keyword);
       return items;
   }


}
