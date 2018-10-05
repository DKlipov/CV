<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    	<%@ page import="java.io.File" %>
    	<%@ page import="java.io.FilenameFilter" %>
    	<%@ page import="java.nio.file.Paths" %>
    	<%@ page import="ru.beans.UserBeanManager" %>
    	
    	

<%

if (request.getSession().getAttribute("user") == null
|| !UserBeanManager.getUser(request.getSession().getAttribute("user").toString()).canRead("robot1"))
{
out.println("Tou not have permissons");
return;
}

String path=request.getServletContext().getRealPath("")+"/video/";


File folder = new File(path);

String[] files = folder.list(new FilenameFilter() {
	 
    @Override 
    public boolean accept(File folder, String name) {
        return name.endsWith(".ogg");
    }
    
});

for ( String fileName : files ) {
    out.println("<video controls width='400' height='300'><source src='video/"+fileName+"' type='video/ogg'></video>");
    		  
}



%>

<!-- video controls width="400" height="300">
  <source src="video/video.ogg" type="video/ogg">
</video!-->
