<?php
//create_program_handler.php

	//date_default_timezone_set('America/Sao_Paulo');
	
	$username	= $_REQUEST['user'];
	$pname		= $_REQUEST['pname'];
	$value		= $_REQUEST['pvalue'];
	$desc		= $_REQUEST['desc'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
		
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$newName = str_replace(" ", "%20", $pname);
	$newDesc = str_replace(" ", "%20", $desc);
		
	$url = "$host/axis2/services/ws_ems/create_new_program?username=$username&cookie=$cookie_value&ip=$ip&name=$newName&value=$value&description=$newDesc";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.create_new_program");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");;
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$programCreated = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}		
	$programCreated = $xmlNode;
	
	echo $programCreated;		
?>