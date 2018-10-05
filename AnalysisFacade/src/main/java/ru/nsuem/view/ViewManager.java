package ru.nsuem.view;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import ru.nsuem.ServiceManager;
import ru.nsuem.sql.SQLHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class ViewManager {
    private HashMap<String, View> viewMap;
    private HashMap<String, TableView> tableMap;


    public ViewManager(InputStream stream){
        viewMap=new HashMap<>();
        tableMap=new HashMap<>();
        JsonObject json= new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        for(JsonElement o:json.getAsJsonArray("tables")){
            TableView view=new TableView(o.getAsJsonObject());
            tableMap.put(view.getName(),view);
        }
        for(JsonElement o:json.getAsJsonArray("views")){
            View view=new View(o.getAsJsonObject());
            viewMap.put(view.getName(),view);
        }
    }

    public View getView(String key){
        return viewMap.get(key);
    }

    public TableView getTable(String key){
        return tableMap.get(key);
    }
    public static void main(String [] a) throws Exception{
        ViewManager man=ServiceManager.getINSTANCE().getViewManager();
        //System.out.println(man.getTable("contingent").asJson());
        System.out.println(man.getView("contingent").asJson());
    }
}
