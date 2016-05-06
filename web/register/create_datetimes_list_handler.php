<?php
//create_datetime_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$schid		= $_REQUEST['schid'];
	$dtbegin	= $_REQUEST['dtbegin'];
	$dtend		= $_REQUEST['dtend'];
	$tline		= $_REQUEST['tline'];
		
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
		
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
					
	$url = "$host/axis2/services/ws_ems/create_datetimes_list?username=$username&cookie=$cookie_value&ip=$ip&schid=$schid&dtbegin=$dtbegin&dtend=$dtend&tline=$tline";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.create_datetimes_list");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$datetimesCreated = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}		
	$datetimesCreated = $xmlNode;
	
	echo "$datetimesCreated:$username";				
?>