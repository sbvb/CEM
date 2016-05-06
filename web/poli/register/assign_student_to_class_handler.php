<?php
//assign_student_to_class_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$student_id	= $_REQUEST['student_id'];
	$class_id	= $_REQUEST['class_id'];
	
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$url = "$host/axis2/services/ws_ems/assign_student_to_class?student_id=$student_id&class_id=$class_id";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.assign_student_to_class");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");;
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$studentAssigned = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}		
	$studentAssigned = $xmlNode;
	
	echo $studentAssigned;
?>