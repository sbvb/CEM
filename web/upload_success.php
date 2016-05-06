<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Register test</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<script>
function reload_page(uid) {
	window.close();
	//window.opener.location.href = "add_photo.php?uid=" + uid;
	window.opener.location.reload();
}
</script>

</head>

<body>
<div align="center">
  <h1>File uploaded successfull </h1>
  <p>&nbsp;</p>
<?php
	$filename_loc = $_REQUEST['filename'];
	$uid = $_REQUEST['uid'];
	
	echo "<p>local file: $filename_loc</p>";
	
	echo "<script>";
 	echo "setTimeout(\"reload_page('$uid');\",1000*3);";
	echo "</script>";
?>
</div>
</body>
</html>
