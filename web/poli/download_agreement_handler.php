<?php //download_agreement_handler.php
$username	= $_REQUEST['user'];
$agr_id		= $_REQUEST['agr_id'];
$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

//header ("Content-Disposition: attachment; filename=agreement.pdf");
//header ("Content-Type: application/octet-stream");

$url = "$host/axis2/services/ws_ems/generate_agreement?username=$username&cookie=$cookie_value&ip=$ip&agr_id=$agr_id";
//echo "url = $url<br>";
$myfile = fopen($url, "r")
	or die("died when doing OPEN ws_ems.generate_agreement");
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
}

echo "$xmlNode:$username";
?>