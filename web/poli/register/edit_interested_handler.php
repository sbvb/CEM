<?php
//edit_interested_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$fullname	= $_REQUEST['fullname'];
	$email		= $_REQUEST['email'];
	$tel		= $_REQUEST['tel'];
	$obs		= $_GET['obs'];
	$pid		= $_GET['pid'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$newFullname = str_replace(" ", "%20", $fullname);
	$newObs		 = str_replace(" ", "%20", $obs);
		
	$url = "$host/axis2/services/ws_ems/edit_interested_settings?username=$username&cookie=$cookie_value&ip=$ip&fullname=$newFullname&email=$email&telephone=$tel&obs=$newObs&pid=$pid";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.edit_interested_settings");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$userEdited = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$userEdited = $xmlNode;
		
	echo "$userEdited:$username";
	
?>