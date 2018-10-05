<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Категории</title>
</head>
<body>
<h2>Категории:</h2>
<c:if test="${!empty listCategories}">
    <c:forEach items="${listCategories}" var="item">
        <div>
            <a href="<c:url value='/category/${item.id}' />"> ${item.name}</a>
        </div>
    </c:forEach>
</c:if>
</body>
</html>
