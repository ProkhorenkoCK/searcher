<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
</head>
<body>
<input type="text" id="query" placeholder="Enter your search query"/>
<input id="submit" type="button" value="Index"/>
</body>
</html>
<script>
	$("#submit").click(function() {
		var url = $("#query").val();
		$.post('/index', { q: url, depth: 0 });
	});

</script>