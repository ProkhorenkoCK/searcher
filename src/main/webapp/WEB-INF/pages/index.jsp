<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
</head>
<body>
<form action="/search" method="get">
    <input type="text" id="url" placeholder="Enter url for indexing"/>
    <input type="number" id="depth" placeholder="Enter depth of indexing"/>
    <input id="submit" type="submit" value="Index"/>
</form>
<script src="<c:url value="/resources/js/index.js" />"></script>
</body>
</html>