<?php //download_document.php
$username	= $_REQUEST['user'];
$tid		= $_REQUEST['tid'];
$desc		= $_REQUEST['desc'];

header ("Content-Disposition: attachment; filename=$desc.tex");
header ("Content-Type: application/octet-stream");

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$url = "$host/axis2/services/ws_ems/download_agreement_template?username=$username&cookie=$cookie_value&ip=$ip&tid=$tid";
//echo "url = $url<br>";
$myfile = fopen($url, "r")
	or die("died when doing OPEN ws_ems.download_agreement_template");
$allLines = "";
while ($currline = fread($myfile,1024))
	$allLines .= $currline; // concat currline to allLines
fclose($myfile) or die("died when doing CLOSE");
//echo $allLines;
// parse XML
$xmlDoc = new DOMDocument();
$xmlDoc->loadXML($allLines);

$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
$cnt = $xmlElem->length;
for($i = 0; $i < $cnt ; $i++) {
	$xmlNode = $xmlElem->item($i)->nodeValue;	
	
	// Goes to doua_error.php because session expired.
	if ($xmlNode == "session_expired")
		echo "<script>back_page('doua_error.php')</script>";
}
if ($xmlNode !== "false")
	echo base64_decode($xmlNode);

?>