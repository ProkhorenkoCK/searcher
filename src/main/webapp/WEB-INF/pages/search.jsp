<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Searcher</title>
</head>
<body>
    <form action="/search" method="post">
        <input type="text" placeholder="Enter your search query"/>
        <input type="submit"/>
    </form>
${SESSION_SEARCH_DATA}
</body>
</html>
