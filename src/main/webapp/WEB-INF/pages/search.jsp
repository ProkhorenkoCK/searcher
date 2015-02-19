<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Searcher</title>
</head>
<body>
    <form action="/search" method="get">
        <input type="text" name="q" placeholder="Enter your search query"/>
        <input type="submit"/>
    </form>

    <c:forEach var="page" items="${searchData}" varStatus="loop">
        <h3>${page.title}</h3>
        <a href="${page.url}">${page.url}</a>
        </br>
    </c:forEach>
</body>
</html>
