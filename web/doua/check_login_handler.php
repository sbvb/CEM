<?php 
header("Content-type: text/html; charset=utf-8");

	$username = $_REQUEST['user']; 
	$passwd   = $_REQUEST['pass'];

	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	//$host = "http://146.164.42.193:8081";

	$cookie_name	= "session_id";
	$ip 			= $_SERVER['REMOTE_ADDR'];
		
	//date_default_timezone_set('America/Sao_Paulo');
	
	$pass_hash = md5($passwd);
	
	$myfile = fopen("$host/axis2/services/ws_doua_ems/check_login?username=$username&pwd_hash=$pass_hash&ip=$ip" , "r")
        		or die("died when doing OPEN check_login");
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
	$cookie_value = $xmlNode;
	//echo '$loginIsOk = ' . $loginIsOk . "<br>"; 

	$ret = "false";
	if ($cookie_value != "false")
	{
			//echo "$cookie_value <br>";		
			setcookie($cookie_name, $cookie_value);
			setcookie($cookie_name, $cookie_value,0,'/ems');
			//setcookie('nomeEMS', 'manoel', 0, '/ems');
			$ret = $username;
	}
	
		
	echo $ret;		
?>