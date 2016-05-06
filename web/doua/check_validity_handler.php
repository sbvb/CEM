<?php
//check_validity_handler.php

	$username 	= $_REQUEST['user'];
	$lnk		= $_REQUEST['link'];	

	//date_default_timezone_set('America/Sao_Paulo');
	
	//echo "$username _ $lnk<br>";
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();

	$cookie_name = 'session_id';
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
		
	//Check resource
	$resourceIsOk = "false";
	$url = "$host/axis2/services/ws_doua_ems/check_resource?username=$username&cookie=$cookie_value&ip=$ip&signature=$lnk";
	//echo "url2 = $url";
	$myfile = fopen($url, "r")
        	or die("died when doing OPEN check_resource");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");;
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$resourceIsOk = $xmlNode;
		
	echo $resourceIsOk;
?>
