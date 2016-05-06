<?php
//create_person_user_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$username	= $_REQUEST['user'];
	$nickname	= $_REQUEST['nickname'];
	$fullname	= $_REQUEST['fullname'];
	$email		= $_REQUEST['email'];
	$tel		= $_REQUEST['tel'];
	$pass		= $_REQUEST['pass'];
	$scid		= $_REQUEST['scid'];
	$ptid		= $_REQUEST['ptid'];
	$obs		= $_GET['obs'];
	$address	= $_GET['address'];
	$district	= $_GET['district'];
	$city		= $_GET['city'];
	$state		= $_GET['state'];
	$zip_code	= $_GET['zip_code'];
	$civil_status	= $_GET['civil_status'];
	$career		= $_GET['career'];
	$identity	= $_GET['identity'];
	$issued_by	= $_GET['issued_by'];
	$cpf		= $_GET['cpf'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$cookie_name	= "session_id";
	$cookie_value 	= "";
	if (isset($_COOKIE[$cookie_name]))
		$cookie_value = $_COOKIE[$cookie_name];
			
	$ip 			= $_SERVER['REMOTE_ADDR'];
	
	$pwd_hash	 = md5($pass);
	$newFullname = str_replace(" ", "%20", $fullname);
	$newObs		 = str_replace(" ", "%20", $obs);
	$newAddress	 = str_replace(" ", "%20", $address);
	$newDistrict = str_replace(" ", "%20", $district);
	$newCity	 = str_replace(" ", "%20", $city);
	$newState	 = str_replace(" ", "%20", $state);
	$newCareer	 = str_replace(" ", "%20", $career);
			
	$url = "$host/axis2/services/ws_doua_ems/create_new_user?username=$username&cookie=$cookie_value&ip=$ip&name=$nickname&pass=$pwd_hash&gid=$ptid";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_doua_ems.create_new_user");
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
		
	if ($userCreated == "true") {
		$url = "$host/axis2/services/ws_ems/create_new_user?username=$username&cookie=$cookie_value&ip=$ip";
		$url .= "&nickname=$nickname&fullname=$newFullname&email=$email&telephone=$tel&ptid=$ptid&scid=$scid&obs=$newObs";
		$url .= "&address=$newAddress&district=$newDistrict&city=$newCity&state=$newState&zip_code=$zip_code";
		$url .= "&civil_status=$civil_status&career=$newCareer&identity=$identity&issued_by=$issued_by&cpf=$cpf";
		//echo $url;
		$myfile = fopen($url, "r")
				or die("died when doing OPEN ws_ems.create_new_user");
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
	}
	
	echo $userCreated;
?>
