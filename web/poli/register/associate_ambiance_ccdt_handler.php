<?php
//associate_ambiance_ccdt_handler.php

	//date_default_timezone_set('America/Sao_Paulo');
	
	$username	= $_REQUEST['user'];	
	$ccdtid		= $_REQUEST['ccdtid'];
	$ambid		= $_REQUEST['ambid'];
		
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
		
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	//echo $cline;
		
	$url = "$host/axis2/services/ws_ems/associate_ambiance_ccdt?username=$username&cookie=$cookie_value&ip=$ip&ccdtid=$ccdtid&ambid=$ambid";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.associate_ambiance_ccdt");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$associationCreated = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}		
	$associationCreated = $xmlNode;
	
	echo "$associationCreated:$username";		
	
?>