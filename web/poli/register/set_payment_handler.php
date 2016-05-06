<?php
//set_payment_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$pid		= $_REQUEST['pid'];
	$value		= $_REQUEST['value'];
	$quota		= $_REQUEST['quota'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
				
	$newValue = str_replace(" ", "%20", $value);
	$newQuota = str_replace(" ", "%20", $quota);
				
	$url = "$host/axis2/services/ws_ems/set_user_payment?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid&desc_value=$newValue&desc_quota=$newQuota";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.set_user_payment");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$paymentSet = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$paymentSet = $xmlNode;
	
	echo "$paymentSet:$username";
	
?>