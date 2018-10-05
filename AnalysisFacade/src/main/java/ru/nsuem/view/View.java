package ru.nsuem.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.nsuem.ServiceManager;
import ru.nsuem.sql.SQLHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class View implements IJsonView{
    private String name,description,printName,printHeader,printFooter;
    private ArrayList<TableView> tables;

    public View (JsonObject obj){
        name=obj.get("name").getAsString();
        description=obj.get("description").getAsString();
        printName=obj.get("printName").getAsString();
        printHeader=obj.get("printHeader").getAsString();
        printFooter=obj.get("printFooter").getAsString();
        JsonArray array=obj.get("tables").getAsJsonArray();
        tables=new ArrayList<>();
        for(JsonElement e:array){
            if(e.isJsonPrimitive()){
                tables.add(new TableView(e.getAsString()));
            }else{
                TableView view=new TableView(e.getAsJsonObject());
                tables.add(view);
            }
        }

    }


    @Override
    public JsonObject asJson() throws SQLException {
        JsonObject result=new JsonObject();
        result.addProperty("view",name);
        JsonObject header=new JsonObject();
        result.add("header",header);

            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm dd.MM.yyyy");
            SimpleDateFormat shortFormat = new SimpleDateFormat("dd.MM.yyyy");
            header.addProperty("update",formatForDateNow.format(dateNow));

        header.addProperty("printTitle",printName);
        header.addProperty("numTables",tables.size());
        header.addProperty("description",description);
        JsonObject body=new JsonObject();
        result.add("body",body);
        body.addProperty("printHeader", String.format(printHeader,shortFormat.format(dateNow)));
        body.addProperty("printFooter",String.format(printFooter,shortFormat.format(dateNow),"summ1","summ2"));
        JsonArray array=new JsonArray();
        for(int i=0;i<tables.size();i++){
            TableView view=tables.get(i);
            if(view.isProxy()){
              tables.set(i,ServiceManager.getINSTANCE().getViewManager().getTable(view.getName()));
              view=tables.get(i);
            }
            array.add(view.asJson());
        }
        body.add("tables",array);
        return result;
    }

    public String getName(){
        return name;
    }
}
