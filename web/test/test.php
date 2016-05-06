<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Test page</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" media="screen" type="text/css" href="../img/datepicker.css" />
<link rel="stylesheet" media="all" type="text/css" href="../img/jquery-ui-1.8.6.custom.css" />

<style type="text/css"> 
	#ui-datepicker-div{ font-size: 80%; }
	.ui-timepicker-div .ui-widget-header{ margin-bottom: 8px; }
	.ui-timepicker-div dl{ text-align: left; }
	.ui-timepicker-div dl dt{ height: 25px; }
	.ui-timepicker-div dl dd{ margin: -25px 0 10px 65px; }
	.ui-timepicker-div td { font-size: 90%; }
</style>

<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/datepicker.js"></script>
<script type="text/javascript" src="../js/eye.js"></script>
<script type="text/javascript" src="../js/utils.js"></script>
<script type="text/javascript" src="../js/layout.js?ver=1.0.2"></script>

<script type="text/javascript" src="../js/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui-timepicker-addon.js"></script>

<script type="text/javascript">
	$(function(){
		$('#timepicker').timepicker({
			hourGrid: 4,
			minuteGrid: 10
		});
	});
</script>

</head>

<body>
<?php
// tests.php

echo "<h1 align=\"center\">PHP Tests by Vincius Heineck</h1>\n";
echo "<h1>IP test</h1>\n";
$ip=$_SERVER['REMOTE_ADDR'];
echo "Show my IP: " . $ip;
echo "<p><p>\n";

echo "<h1>MD5 test</h1>\n";
$test1= "123";
echo $test1 . "_" . md5($test1);
//echo "<br>\n";
//echo $test2 . "_" . md5($test2);
echo "<p><p>\n";

echo "<h1>hello_doua parsed with PHP</h1>\n";
$myfile = fopen("http://187.60.57.99:8081/axis2/services/Version/getVersion", "r")
        or die("died when doing OPEN");
while ($currline = fread($myfile,1024))
       $allLines .= $currline; // concat currline to allLines
fclose($myfile) or die("died when doing CLOSE");;
// parse XML
$xmlDoc = new DOMDocument();
$xmlDoc->loadXML($allLines);
$hello_doua_returns = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
$hello_doua_return = $hello_doua_returns->item(0)->nodeValue;
echo "Response of hello_doua: " . $hello_doua_return;

echo "<h1>date test</h1>\n";
echo "now: " . date("d/m/Y h:i:s a") . "<br>\n";
echo "after 20min: " . date("d/m/Y h:i:s a", time() + 60 * 20) . "<br>\n";

echo "now: " . date("d/m/Y H:i:s") . "<br>\n";
echo "after 20min: " . date("d/m/Y H:i:s", time() + 60 * 20);

echo "<h1>parse xml test</h1>\n";
include("../conf/configuration.php");

$conf = new Configuration();
 
echo "host: " . $conf->get_host() . "<br />";
echo "Author: " . $conf->get_author() . "<br />";

echo "<h1>datepicker with jquery test</h1>";
date_default_timezone_set('UTC');
$date = date("Y/m/d");
echo "date: $date<br>";
echo "<input class=\"inputDate\" id=\"inputDate\" value=\"$date\" readonly=\"readonly\" />";

echo "<h1>timepicker with jquery test</h1>";
$curtime = date("H:i");
echo "time: $curtime<br>";
echo "<input type=\"text\" name=\"timepicker\" id=\"timepicker\" value=\"$curtime\" readonly=\"readonly\" />";

?>
<p align="center">developed by Vinícius Heineck</p>
</body>
</html>
