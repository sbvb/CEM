<?php
// tests.php

echo "<h1 align=\"center\">PHP Tests by Vinícius Heineck</h1>\n";
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
$myfile = fopen("http://localhost:8080/axis2/services/ws_doua_ems/hello_doua", "r")
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

echo "<h1>test_strArr parsed with PHP</h1>\n";
$myfile1 = fopen("http://localhost:8080/axis2/services/ws_doua_ems/test_strArr", "r")
        or die("died when doing OPEN");
$allLines = "";
while ($currline = fread($myfile1,1024))
	$allLines .= $currline; // concat currline to allLines
fclose($myfile1) or die("died when doing CLOSE");;
// parse XML
$xmlDoc = new DOMDocument();
$xmlDoc->loadXML($allLines);
//echo $allLines . "<br>\n";

$tst_strArr_returns = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
$cnt = $tst_strArr_returns->length;
for($i = 0; $i < $cnt ; $i++) {
	$tst_strArr_return = $tst_strArr_returns->item($i)->nodeValue;
	echo $tst_strArr_return . "<br>";
}

echo "<h1>Users from table User parsed with PHP</h1>\n";
$myfile1 = fopen("http://localhost:8080/axis2/services/ws_doua_ems/getUsers", "r")
        or die("died when doing OPEN");
$allLines = "";
while ($currline = fread($myfile1,1024))
	$allLines .= $currline; // concat currline to allLines
fclose($myfile1) or die("died when doing CLOSE");;
// parse XML
$xmlDoc = new DOMDocument();
$xmlDoc->loadXML($allLines);
//echo $allLines . "<br>\n";

$tst_strArr_returns = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
$cnt = $tst_strArr_returns->length;
for($i = 0; $i < $cnt ; $i++) {
	$tst_strArr_return = $tst_strArr_returns->item($i)->nodeValue;
	echo $tst_strArr_return . "<br>";
}

echo "<h1>date test</h1>\n";
echo "now: " . date("d/m/Y h:i:s a") . "<br>\n";
echo "after 20min: " . date("d/m/Y h:i:s a", time() + 60 * 20) . "<br>\n";

echo "now: " . date("d/m/Y H:i:s") . "<br>\n";
echo "after 20min: " . date("d/m/Y H:i:s", time() + 60 * 20);

?>

