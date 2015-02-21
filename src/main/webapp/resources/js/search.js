var currentPage = getParameterByName("page");
if (currentPage.length == 0) {
    currentPage = 1;
} else {
    currentPage = parseInt(currentPage);
}
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function getQueryString(page) {
    return "search?q=" + getParameterByName("q") + "&page=" + page;
}
$("#submit").click(function (e) {
    var value = $("[name = 'q']").val();
    if (typeof value == "undefined" || value.length == 0) {
        e.preventDefault();
    }
});
$(".link").click(function () {
    var link = $(this).attr("link");
    window.open(link);
});
$.each($(".page"), function () {
    var currentPage = $(this);
    var page = parseInt(currentPage.attr("page"));
    currentPage.attr("href", getQueryString(page));
});
$("#previousPage").attr("href", getQueryString(currentPage - 1));
$("#nextPage").attr("href", getQueryString(currentPage + 1));