package ru.nsuem.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.nsuem.ServiceManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TableView implements IJsonView{
    private String name,description,source,identity,printName;
   // private ArrayList<String> columnNames;
    private ArrayList<String> rows;
   // private ArrayList<String> rowNames;
    private TreeSet<String> columns;
    private boolean proxy=false;
    private JsonArray columnArray;
    private HashMap<String,ArrayList<String>> titleList;
    private int numRows;
    private JsonArray rowTitleObject;

    public boolean isProxy(){
        return proxy;
    }
    public TableView(String name){
        this.name=name;
        proxy=true;
    }

    public TableView(JsonObject obj) {
        if(obj.get("name")!=null){
            name=obj.get("name").getAsString();
        }
        description=obj.get("description").getAsString();
        source=obj.get("source").getAsString();
        identity=obj.get("identity").getAsString();
        printName=obj.get("printName").getAsString();
        JsonArray array=obj.get("rows").getAsJsonArray();
        rows =new ArrayList<>();
        for(int i=0;i<array.size();i++){
            JsonObject e=array.get(i).getAsJsonObject();
                rows.add(e.get("source").getAsString());
        }
        columnArray = obj.get("columns").getAsJsonArray();
        columns = new TreeSet<>();
        for(JsonElement e:columnArray.get(0).getAsJsonArray()){
            JsonObject e1=e.getAsJsonObject();
            columns.add(e1.get("source").getAsString());
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public JsonObject asJson()throws SQLException {
        //create temp table
        String sql=formSql();//prepare select sql
        return prepareJson(sql);
    }

    private JsonObject prepareJson(String sql) throws SQLException {


        JsonObject result=new JsonObject();
        result.addProperty("table",name);
        JsonObject header=new JsonObject();
        header.addProperty("description",description);
        header.addProperty("printName",printName);
        {
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm dd.MM.yyyy");
            header.addProperty("update",formatForDateNow.format(dateNow));
        }
        header.addProperty("width",38);
        //header.addProperty("height",0);
        header.addProperty("headerLevels",4);
        JsonObject bodyHeader=new JsonObject();
        JsonObject body=new JsonObject();
        body.add("header",bodyHeader);
        body.addProperty("styles", "[{\"style\":1,\"align\":\"left\",\"color\":\"#000000\",\"background-color\":\"#FFFFFF\"}]");
        body.add("columns",ServiceManager.getINSTANCE().getSqlHelper().execQuery(sql,rs->{
            int titleRowNum=1;
            int rowNum=1;
            JsonObject json=new JsonObject();
            int size=this.rows.size();
            JsonArray nums=new JsonArray();

            JsonArray[] title=new JsonArray[size];
            String[] titleName=new String[size-1];
            for(int i=0;i<size;i++){
                title[i]=new JsonArray();
            }
            while(rs.next()){
                for(int j=size-2;j>=0;j-=1){
                    String name=rs.getString(j+1);
                    if(titleName[j]==null){
                        titleName[j]=name;
                    }else if(!titleName[j].equals(name)){
                            JsonObject o = new JsonObject();
                            o.addProperty("title", titleName[j]);
                            o.add("groups", title[j + 1]);
                            o.addProperty("style", 1);
                            title[j + 1] = new JsonArray();
                            titleName[j] = name;
                            title[j].add(o);

                    }
                }
                JsonObject o = new JsonObject();
                o.addProperty("title", rs.getString(size));
                o.addProperty("style", 1);
                o.addProperty("rowNum", titleRowNum++);
                title[size-1].add(o);
                JsonObject row=new JsonObject();
                row.addProperty("rowNum",rowNum++);
                JsonArray numRow=new JsonArray();
                for(int i=0;i<this.numRows;i++){
                    JsonObject cell=new JsonObject();
                    cell.addProperty("columnNum",i+size);
                    cell.addProperty("style",1);
                    cell.addProperty("value",rs.getInt(size+1+i));
                    numRow.add(cell);
                }
                row.add("cells",numRow);
                nums.add(row);
            }
            JsonObject o = new JsonObject();
            o.addProperty("name",titleName[0]);
            o.add("array",title[1]);
            title[0].add(o);
            bodyHeader.add("columnGroupTitles",title[0]);
            return  nums;
        }));
        result.add("header",header);
        result.add("body",body);
 //////////////////////////
            JsonArray columnsTitles=new JsonArray();
            bodyHeader.add("columnTitles",columnsTitles);
            JsonObject tempO=new JsonObject();
            tempO.addProperty("title","Наименование специальности");
            tempO.addProperty("columnNum",1);
            tempO.addProperty("style",1);
            columnsTitles.add(tempO);
            tempO=new JsonObject();
            tempO.addProperty("title","Уровень образования");
            tempO.addProperty("columnNum",2);
            tempO.addProperty("style",1);
            columnsTitles.add(tempO);
            /////////////////////////
        return result;
    }


    private HashMap<String,ArrayList<String>> prepare() throws SQLException {
        ArrayList<String> temp=new ArrayList<>();
        for(String s: columns){
            StringBuilder sql=new StringBuilder();
            sql.append("SELECT DISTINCT '");
            sql.append(s);
            sql.append("' as COLUMN1, cast(");
            sql.append(s);
            sql.append(" as varchar(50)) as COLUMN2 FROM ");
            sql.append(source);
            sql.append(" WHERE ");
            sql.append(s);
            sql.append(" IS NOT NULL ");
            temp.add(sql.toString());
        }
        String resultSql=String.join(" UNION ALL ",temp);
        resultSql=resultSql+" ORDER BY COLUMN1,COLUMN2;";
        return ServiceManager.getINSTANCE().getSqlHelper().execQuery(resultSql,(e)->{
            HashMap<String,ArrayList<String>> result=new HashMap<>();
            ArrayList<String> list=new ArrayList<>();
            String group=null;
            while(e.next()){
                String name=e.getString(1);
                if(group==null){
                    group= name;
                }else if(!name.equals(group)){
                    result.put(group,list);
                    list=new ArrayList<>();
                    group= name;
                }
                list.add(e.getString(2));
            }
            result.put(group,list);
            return result;
        });
    }

    private String formSql() throws SQLException {
        titleList=prepare();
        StringBuilder sql=new StringBuilder();
        sql.append("SELECT DISTINCT ");
        String column=String.join(", ", rows);
        sql.append(column);
        int sqlCol=0;
        ArrayList<ArrayList<String>> tempList;
        this.rowTitleObject=new JsonArray();
        for(JsonElement e1: columnArray){
            JsonArray rowTitleObject=new JsonArray();
            this.rowTitleObject.add(rowTitleObject);
            JsonArray ar=e1.getAsJsonArray();
            tempList=new ArrayList<>();
            int num=1;
            for(JsonElement e:ar){
                JsonObject o=e.getAsJsonObject();
                ArrayList<String> s=titleList.get(o.get("source").getAsString());
                tempList.add(s);
                num=num*s.size();
                numRows=num;
            }
            String[] strArr=new String[num];

            int[] keyArr=new int[tempList.size()];
            Arrays.fill(keyArr,0);
            JsonObject[] jArray=new JsonObject[tempList.size()];
            String[] nameAr=new String[tempList.size()-1];
            Arrays.fill(nameAr,"");
            JsonArray[] jArray1=new JsonArray[tempList.size()];
            for(int i=0;i<jArray.length;i++){
                jArray[i]=new JsonObject();
                jArray1[i]=new JsonArray();
            }
            int i=0;
            while (i<num){
                StringBuilder caseBuilder=new StringBuilder();
                caseBuilder.append("COUNT(CASE WHEN ");
                for(int j=0;j<tempList.size();j++) {
                    ArrayList<String> aTempList=tempList.get(j);
                    String name=aTempList.get(keyArr[j]);
                    caseBuilder.append(ar.get(j).getAsJsonObject().get("source").getAsString());
                    caseBuilder.append("='");
                    caseBuilder.append(name);
                    if(j==0){
                        jArray1[j].add(name);
                    }else{
                        if(nameAr[j-1].equals("")){
                            nameAr[j-1]=name;
                            jArray[j-1].addProperty("name",name);
                        }else if(!nameAr[j-1].equals(name)){
                            jArray[j-1]=new JsonObject();
                        }
                    }
                        if (j != tempList.size() - 1) {
                            caseBuilder.append("' AND ");

                        } else {
                            caseBuilder.append("'THEN 1 ELSE null END)");
                        }
                    }

                    nextCircle(keyArr,tempList,0,jArray);


                i++;
                    sql.append(",");
                    sql.append(caseBuilder);
                sql.append(" as '");
                sql.append(sqlCol++);
                sql.append("'");
            }
            System.out.println(String.join(" ",strArr));
        }
        //sql.append(", COUNT(");
        //sql.append(identity);
        sql.append(" FROM ");
        sql.append(source);
        sql.append(" GROUP BY ");
        sql.append(column);
        sql.append(" ORDER BY ");
        sql.append(column);
        return sql.toString();
    }


    private void nextCircle(int[] arr,ArrayList<ArrayList<String>> list,int pos,JsonObject[] json){
        if(arr.length<=pos){return;}
            arr[pos]++;
            if(arr[pos]>=list.get(pos).size()){
                if(json.length>pos+1){
                    json[pos+1].add("array",json[pos]);
                    json[pos]=new JsonObject();
                }
                arr[pos]=0;
                nextCircle(arr,list,pos+1,json);
            }

    }
}
