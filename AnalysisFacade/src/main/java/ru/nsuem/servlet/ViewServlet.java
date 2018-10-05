package ru.nsuem.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import ru.nsuem.ServiceManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/views")
public class ViewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json");
        response.setCharacterEncoding("utf8");
        // Actual logic goes here.
        PrintWriter out = response.getWriter();

        try {
            JsonArray result=new JsonArray();
            result.add(ServiceManager.getINSTANCE().getViewManager().getView("contingent").asJson());
            Gson gson = new Gson();
            gson.toJson(result,out);
        } catch(Exception e){
            out.println("{\"error\":\"Internal server error\"}");
        }

    }
}
