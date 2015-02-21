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
    <input id="submit" type="submit" value="Search"/>
</form>
<script src="<c:url value="/resources/js/startPage.js" />"></script>
</body>
</html>
