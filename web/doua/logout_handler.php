<?php
//logout_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username = $_REQUEST['user'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();

	$cookie_name = 'session_id';

	if (isset($_COOKIE[$cookie_name]))
	{
		$cookie_val = $_COOKIE[$cookie_name];
		$ip 		= $_SERVER['REMOTE_ADDR'];

		
		//deleting cookie
		setcookie($cookie_name, $cookie_val, time() - 3600);	
		setcookie($cookie_name, $cookie_val, time() - 3600, '/ems');	

		
		$myfile = fopen("$host/axis2/services/ws_doua_ems/logout_session?username=$username&cookie=$cookie_val&ip=$ip", "r")
        		or die("died when doing OPEN logout_session");
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
		$ret = $xmlNode;
	}
	echo $ret;
?>
