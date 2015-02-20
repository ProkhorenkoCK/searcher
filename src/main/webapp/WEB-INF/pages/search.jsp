<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Searcher</title>
    <script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
</head>
<body>
    <form action="/search" method="get">
        <input type="text" name="q" placeholder="Enter your search query"/>
        <input type="submit"/>
    </form>

    <c:forEach var="page" items="${searchData}">
        <div>
            <a href="${page.url}">${page.title}</a>
            <a href="${page.url}">${page.url}</a>
        </div>
    </c:forEach>

    <c:if test="${currentPage != 1}">
        <td><a id="previousPage">Previous</a></td>
    </c:if>

    <table border="1" cellpadding="5" cellspacing="5">
        <tr>
            <c:forEach begin="1" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a class="page" page="${i}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </table>

    <c:if test="${currentPage lt noOfPages}">
        <td><a id="nextPage">Next</a></td>
    </c:if>
</body>
<script>
    var currentPage = parseInt("${currentPage}");
    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    function getQueryString(page) {
        return "search?q=" + getParameterByName("q") + "&page=" + page;
    }

    $.each($(".page"), function() {
        var currentPage = $(this);
        var page = parseInt(currentPage.attr("page"));
        currentPage.attr("href", getQueryString(page));
    });
    $("#previousPage").attr("href", getQueryString(currentPage - 1));
    $("#nextPage").attr("href", getQueryString(currentPage + 1));
</script>
</html>
