$("#submit").click(function (e) {
    var value = $("[name = 'q']").val();
    if (typeof value == "undefined" || value.length == 0) {
        e.preventDefault();
    }
});