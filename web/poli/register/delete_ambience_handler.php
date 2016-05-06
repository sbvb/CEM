<?php
//delete_ambience_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['owner'];
	$ambid		= $_REQUEST['ambid'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
				
	$url = "$host/axis2/services/ws_ems/delete_ambience?username=$username&cookie=$cookie_value&ip=$ip&ambid=$ambid";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.delete_ambience");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$ambienceDeleted = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$ambienceDeleted = $xmlNode;
		
	echo $ambienceDeleted;
	
?>