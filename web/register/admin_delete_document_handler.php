<?php
//admin_delete_document_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username = $_REQUEST['user'];
	$fid	= $_REQUEST['fid'];
	$ext	= $_REQUEST['ext'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();		
			
	$url = "$host/axis2/services/ws_ems/delete_file_from_candidate?username=$username&cookie=$cookie_value&ip=$ip&fid=$fid&ext=$ext";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.delete_file_from_candidate");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$fileDeleted = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$fileDeleted = $xmlNode;
		
	echo $fileDeleted;
	
?>