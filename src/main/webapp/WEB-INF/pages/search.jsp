<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Searcher</title>
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
</head>
<body>

<form action="/search" method="get">
    <input type="text" name="q" placeholder="Enter your search query"/>
    <input id="submit" type="submit"/>
</form>
<c:forEach var="page" items="${searchData}">
    <div>
        <span link="${page.url}" class="title link textSetting">${page.title}</span>
        <span link="${page.url}" class="url link textSetting">${page.url}</span>
        <span link="${page.url}" class="text link">${page.text}</span>
    </div>
</c:forEach>

<div class="pagination">
    <c:if test="${currentPage != null && currentPage != 1}">
        <span><a id="previousPage">Previous</a></span>
    </c:if>

    <c:forEach begin="1" end="${noOfPages}" var="i">
        <c:choose>
            <c:when test="${currentPage eq i}">
                <span class="currentPage">${i}</span>
            </c:when>
            <c:otherwise>
                <span><a class="page" page="${i}">${i}</a></span>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <c:if test="${currentPage lt noOfPages}">
        <span><a id="nextPage">Next</a></span>
    </c:if>
</div>
<script src="<c:url value="/resources/js/search.js" />"></script>
</body>
</html>
