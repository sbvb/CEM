<?php
//check_only_cookie_handler.php

	$username 	= $_REQUEST['user'];
	//$lnk		= $_REQUEST['link'];	
	
	//date_default_timezone_set('America/Sao_Paulo');

	//echo "$username _ $lnk<br>";
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();

	$cookie_name = 'sessionId';
	$timestamp   = time();
	$curtime = date("Y-m-d%20H:i:s", $timestamp);	
	$ip = $_SERVER['REMOTE_ADDR'];
	
	//Check if cookie is ok.
	$cookieIsOk = "false";
	if(isset($_COOKIE[$cookie_name]))
	{
		$cookie_val = $_COOKIE[$cookie_name];
		$url = "$host/axis2/services/ws_doua_ems/check_cookie?username=$username&cookie=$cookie_val&curtime=$curtime&ip=$ip";
		//echo "url = $url<br>";
		$myfile = fopen($url, "r")
        		or die("died when doing OPEN check_cookie");
		$allLines = "";
		while ($currline = fread($myfile,1024))
			$allLines .= $currline; // concat currline to allLines
		fclose($myfile) or die("died when doing CLOSE");
		// parse XML
		$xmlDoc = new DOMDocument();
		$xmlDoc->loadXML($allLines);
	
		$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
		$cnt = $xmlElem->length;
		for($i = 0; $i < $cnt ; $i++) {
			$xmlNode = $xmlElem->item($i)->nodeValue;
		}
		$cookieIsOk = $xmlNode;
	}
		
		//logout others sessions thats expired.
		$url = "$host/axis2/services/ws_doua_ems/logout_session_timeout?username=$username&curtime=$curtime&ip=$ip";
		//echo "url = $url<br>";
		$myfile = fopen($url, "r")
        		or die("died when doing OPEN check_cookie");
		$allLines = "";
		while ($currline = fread($myfile,1024))
			$allLines .= $currline; // concat currline to allLines
		fclose($myfile) or die("died when doing CLOSE");
		// parse XML
		$xmlDoc = new DOMDocument();
		$xmlDoc->loadXML($allLines);
	
		$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
		$cnt = $xmlElem->length;
		for($i = 0; $i < $cnt ; $i++) {
			$xmlNode = $xmlElem->item($i)->nodeValue;
		}
				
	if ($cookieIsOk == "false")
		echo "false";
	else
		echo "true";
			
	//echo trim($ret);
?>
