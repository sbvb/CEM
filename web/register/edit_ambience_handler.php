<?php
//edit_ambience_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username		= $_REQUEST['name'];
	$max_students	= $_REQUEST['max_students'];
	$ambid			= $_REQUEST['ambid'];	
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
				
	$url = "$host/axis2/services/ws_ems/edit_ambience_settings?username=$username&cookie=$cookie_value&ip=$ip&max_students=$max_students&ambid=$ambid";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.edit_ambience_settings");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$ambienceEdited = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$ambienceEdited = $xmlNode;
	
	echo "$ambienceEdited:$username";
	
?>