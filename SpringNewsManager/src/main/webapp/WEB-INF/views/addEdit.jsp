<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <c:if test="${editableNew.id==0}"><c:set var="titleVar" value="Добавление новости"/><c:set var="buttonVar"
                                                                                               value="Добавить"/></c:if>
    <c:if test="${editableNew.id!=0}"><c:set var="titleVar" value="Изменение новости"/><c:set var="buttonVar"
                                                                                              value="Изменить"/></c:if>
    <title><c:out value="${titleVar}"/></title>
</head>
<body>
<h2><c:out value="${titleVar}"/></h2>
<form:form action="/add" commandName="editableNew">
    <form:input path="id" hidden="true"/>
    <form:input path="name" required="required" placeholder="Название"/>
    <div><form:textarea path="text" placeholder="Содержание"/></div>
    <input list="datalist" name="data" required placeholder="Категория" value="${editableNew.category.name}"/>
    <datalist id="datalist">
        <c:forEach items="${categoriesList}" var="cat">
            <option value="${cat.name}"/>
        </c:forEach>
    </datalist>
    <div>
        <input type="submit" value="<c:out value="${buttonVar}"/>">
    </div>

</form:form>
</body>
</html>
