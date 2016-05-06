<?php
//edit_program_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$pname		= $_REQUEST['pname'];
	$value		= $_REQUEST['value'];	
	$desc		= $_REQUEST['desc'];
	$pid		= $_REQUEST['pid'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$newDesc = str_replace(" ", "%20", $desc);
	$newName = str_replace(" ", "%20", $pname);
				
	$url = "$host/axis2/services/ws_ems/edit_program_settings?username=$username&cookie=$cookie_value&ip=$ip&pname=$newName&value=$value&description=$newDesc&pid=$pid";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.edit_program_settings");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$programEdited = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$programEdited = $xmlNode;
	
	echo "$programEdited:$username";
	
?>