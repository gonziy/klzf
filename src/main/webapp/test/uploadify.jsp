<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>uploadify</title>
<link rel="stylesheet" type="text/css" href="../static/uploadify/uploadify.css">
<script type="text/javascript" src="../static/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="../static/uploadify/jquery.uploadify.min.js"></script>
<script>
$(function() {
	$("#file_upload_1").uploadify({
		height        : 30,
		swf           : '../static/uploadify/uploadify.swf',
		uploader      : '/uploadify/uploadify.php',
		width         : 120
	});
});
</script>
</head>
<body>
<div id="file_upload_1" class="uploadify"></div>
</body>
</html>