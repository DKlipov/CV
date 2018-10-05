<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Person Page</title>
    <style type="text/css">
        .tg {
            border-collapse: collapse;
            border-spacing: 0;
            border-color: #ccc;
        }

        .tg td {
            font-family: Arial, sans-serif;
            font-size: 14px;
            padding: 10px 5px;
            border-style: solid;
            border-width: 1px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            color: #333;
            background-color: #fff;
        }

        .tg th {
            font-family: Arial, sans-serif;
            font-size: 14px;
            font-weight: normal;
            padding: 10px 5px;
            border-style: solid;
            border-width: 1px;
            overflow: hidden;
            word-break: normal;
            border-color: #ccc;
            color: #333;
            background-color: #f0f0f0;
        }

        .tg .tg-4eph {
            background-color: #f9f9f9
        }
    </style>
</head>
<div>
    <a href="<c:url value='/' />">На главную</a>
    <a href="<c:url value='/categories' />">Категории</a> <a href="<c:url value='/add' />">Добавить новость</a>
</div>
<div style="position: absolute;
  top: 10px;
  right: 5px;">
    <c:if test="${!search.fullSearch}">
        <form:form action="/search" commandName="search">
            <form:input path="quickSearch" placeholder="Поиск"/>
            <input type="submit"
                   value="<spring:message text="Поиск"/>">
        </form:form>
        <a href="<c:url value='/fullSearch' />">Расширенный поиск</a>
    </c:if>

    <c:if test="${search.fullSearch}">
        <form:form action="/search" commandName="search">
            <form:input path="title" placeholder="В названии"/>
            <form:input path="text" placeholder="В содержании"/>
            <form:input path="category" placeholder="В категории"/>
            <form:input path="fullSearch" hidden="true"/>
            <div style="text-align: right;"><input type="submit"
                                                   value="<spring:message text="Поиск"/>">
            </div>
        </form:form>
    </c:if>
</div>
<c:if test="${pageTitle!=null}">
    <h2>${pageTitle}</h2>
</c:if>
<c:if test="${pageTitle==null}">
    <h2>Список новостей:</h2>
</c:if>

<c:if test="${!empty listNews}">
    <c:forEach items="${listNews}" var="news">
        <div style=" border: 3px solid #000000; margin-left:10px;margin-right:10px;">
            <a href="<c:url value='/edit/${news.id}' />"><h3>${news.name}</h3></a>
            <div><p>Дата: ${news.date}</p>
                <p><a href="<c:url value='/category/${news.category.id}' />"> ${news.category.name}</a></p></div>
            <a href="<c:url value='/edit/${news.id}' />">Изменить</a>
            <a href="<c:url value='/remove/${news.id}' />">Удалить</a>
        </div>
        <br>
    </c:forEach>
</c:if>

</body>
</html>