<?php
//create_candidate_handler.php

	//date_default_timezone_set('America/Sao_Paulo');
	//create_candidate_handler.php?login=name&name=newFullname&email=email&scid=scid	
	$fullname	= $_REQUEST['fullname'];
	$email		= $_REQUEST['email'];
	$tel		= $_REQUEST['tel'];
	$promo_code	= $_REQUEST['promo_code'];
	$scname		= $_REQUEST['scname'];
		
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$newFullname = str_replace(" ", "%20", $fullname);
	$newName = str_replace(" ", "%20", $scname);				
				
	$url = "$host/axis2/services/ws_ems/register_new_candidate_to_school_web?fullname=$newFullname&email=$email&telephone=$tel&scname=$newName&uid=$promo_code&reg_type=web";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_doua_ems.register_new_candidate");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");;
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$userCreated = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;		
	}
	$userCreated = $xmlNode; 
	
	echo $userCreated;
?>
