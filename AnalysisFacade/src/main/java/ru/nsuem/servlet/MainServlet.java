package ru.nsuem.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import ru.nsuem.ServiceManager;
import ru.nsuem.view.ViewManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tables")
public class MainServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json");
        response.setCharacterEncoding("utf8");
        // Actual logic goes here.
        PrintWriter out = response.getWriter();

        try {
            JsonArray result=new JsonArray();
            result.add(ServiceManager.getINSTANCE().getViewManager().getTable("contingent").asJson());
            Gson gson = new Gson();
            gson.toJson(result,out);
        } catch(Exception e){
            out.println("{\"error\":\"Internal server error\"}");
        }

    }
    @Override
    public void init(){
    }

}
