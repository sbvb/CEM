<?php
bindtextdomain("lang", "./locale");
textdomain("lang");

if (isset($_COOKIE['ems_lang'])) {
	if ($_COOKIE['ems_lang'] == "br")
		setlocale(LC_ALL, "pt_BR.utf-8");
	else if ($_COOKIE['ems_lang'] == "en")
		setlocale(LC_ALL, "en_US.utf-8");
}

include("constants.php");
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="img/styles.css" type="text/css">
<script type="text/javascript" src="js/ajaxlib.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>
</head>
<?php
	$username = $_REQUEST['user'];
	echo '<body onLoad="logout(\'' . $username . '\')">';
	echo "<p align=\"center\"><h1>$username ";
	echo "$logoutInfMsg</h1></p>";
?>

</body>
</html>
