<?php
//send_new_pass_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$login = $_REQUEST['login'];
	$name	= $_REQUEST['name'];
	$email	= $_REQUEST['email'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();		
			
	$newName = str_replace(" ", "%20", $name);
			
	$url = "$host/axis2/services/ws_doua_ems/generate_new_password?login=$login&name=$newName&email=$email";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_doua_ems.generate_new_password");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$ret = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$ret = $xmlNode;
		
	echo $ret;
	
?>