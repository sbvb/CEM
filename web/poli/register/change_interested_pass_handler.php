<?php
//change_interested_pass_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$oldPass	= $_REQUEST['old_pass'];
	$pass		= $_REQUEST['pass'];
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$old_pwd_hash	= md5($oldPass);
	$pwd_hash		= md5($pass);
	
	/*echo "$username<br>\n";
	echo "$email<br>\n";
	echo "$tel<br>\n";
	echo "$pass<br>\n";
	echo "$g_name<br>\n";*/
	
	$url = "$host/axis2/services/ws_doua_ems/change_candidate_password?username=$username&cookie=$cookie_value&ip=$ip&nickname=$username&old_pass=$old_pwd_hash&pass=$pwd_hash";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_doua_ems.change_candidate_password");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");;
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$passChanged = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$passChanged = $xmlNode; 
		
	echo "$passChanged:$username";
?>