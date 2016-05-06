<?php
//edit_class_handler.php

	//date_default_timezone_set('America/Sao_Paulo');

	$name			= $_REQUEST['name'];
	$course_id		= $_REQUEST['course_id'];
	$schedule_id	= $_REQUEST['schedule_id'];
	$tutor_id		= $_REQUEST['tutor_id'];
	$owner			= $_REQUEST['owner'];
	
	include("../conf/configuration.php");
	$conf = new Configuration();
	$host = $conf->get_host();
	
	$newName = str_replace(" ", "%20", $name);
		
	$url = "$host/axis2/services/ws_ems/edit_class_settings?name=$newName&course_id=$course_id&schedule_id=$schedule_id&tutor_id=$tutor_id";
	//echo $url;
	$myfile = fopen($url, "r")
       		or die("died when doing OPEN ws_ems.edit_class_settings");
	$allLines = "";
	while ($currline = fread($myfile,1024))
		$allLines .= $currline; // concat currline to allLines
	fclose($myfile) or die("died when doing CLOSE");
	// parse XML
	$xmlDoc = new DOMDocument();
	$xmlDoc->loadXML($allLines);

	$classEdited = "false";
	$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
	$cnt = $xmlElem->length;
	for($i = 0; $i < $cnt ; $i++) {
		$xmlNode = $xmlElem->item($i)->nodeValue;
	}
	$classEdited = $xmlNode;
	
	echo "$classEdited:$owner";
	
?>
