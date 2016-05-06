<?php
//create_course_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$cname		= $_REQUEST['cname'];
	$code		= $_REQUEST['code'];	
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$newName = str_replace(" ", "%20", $cname);
		
	$url = "$host/axis2/services/ws_ems/create_new_course?username=$username&cookie=$cookie_value&ip=$ip&cname=$newName&code=$code";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.create_new_course");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");;
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$courseCreated = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}		
	$courseCreated = $xmlNode;
	
	echo $courseCreated;
?>