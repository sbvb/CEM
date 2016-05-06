<?php
//complete_registration_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username 	= $_REQUEST['user'];
	$nickname	= $_REQUEST['nickname'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();		
			
	$url = "$host/axis2/services/ws_doua_ems/complete_registration?username=$username&cookie=$cookie_value&ip=$ip&nickname=$nickname";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_doua_ems.complete_registration");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$registrationCompleted = $xmlNode;
		
	if ($registrationCompleted === "true") {			
		$url = "$host/axis2/services/ws_ems/complete_registration?username=$username&cookie=$cookie_value&ip=$ip&nickname=$nickname";
		//echo $url;
		$myfile = fopen($url, "r")
				or die("died when doing OPEN ws_ems.complete_registration");
		$allLines = "";
		while ($currline = fread($myfile,1024))
			$allLines .= $currline; // concat currline to allLines
		fclose($myfile) or die("died when doing CLOSE");
		// parse XML
		$xmlDoc = new DOMDocument();
		$xmlDoc->loadXML($allLines);
			
		$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
		$cnt = $xmlElem->length;
		for($i = 0; $i < $cnt ; $i++) {
			$xmlNode = $xmlElem->item($i)->nodeValue;
		}		
	}
		
	echo $registrationCompleted;
	
?>