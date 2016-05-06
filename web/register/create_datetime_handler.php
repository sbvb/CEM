<?php
//create_datetime_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$date		= $_REQUEST['date'];
	$tbegin		= $_REQUEST['time_begin'];
	$tend		= $_REQUEST['time_end'];
	$schid		= $_REQUEST['schid'];	
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
		
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
		
	$url = "$host/axis2/services/ws_ems/create_new_datetime?username=$username&cookie=$cookie_value&ip=$ip&date=$date&time_begin=$tbegin&time_end=$tend&schid=$schid";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.create_new_datetime");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$datetimeCreated = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}		
	$datetimeCreated = $xmlNode;
	
	echo $datetimeCreated;			
?>