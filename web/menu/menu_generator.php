<?php
$username = $_REQUEST['user'];

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("../conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$myfile = fopen("$host/axis2/services/ws_doua_ems/get_resources?username=$username&cookie=$cookie_value&ip=$ip", "r")
        		or die("died when doing OPEN get_resources");
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
	
	// Goes to doua_error.php because session expired.
	//if ($xmlNode == "session_expired")
		/*echo "<script>back_page('doua_error.php')</script>";*/
	
	$line = explode(":", $xmlNode);
	
	//add actor to coookie
	if ($i == 0) {		
		setcookie("ems_actor",$line[0]);
		setcookie("ems_actor",$line[0], 0, "/ems");
		
		if ($line[0] === "Administrator")
			$xmlFilename = "menu_admin.xml";
		else if ($line[0] === "School Manager")
			$xmlFilename = "menu_scman.xml";
		else if ($line[0] === "Tutor")
			$xmlFilename = "menu_tutor.xml";
		else if ($line[0] === "Student")
			$xmlFilename = "menu_student.xml";
		else if ($line[0] === "Secretary")
			$xmlFilename = "menu_secretary.xml";
		else if ($line[0] === "Client")
			$xmlFilename = "menu_client.xml";
		else if ($line[0] === "Student Represent")
			$xmlFilename = "menu_stud_rep.xml";
		else if ($line[0] === "Coordinator")
			$xmlFilename = "menu_coordinator.xml";
		else if ($line[0] === "Candidate")
			$xmlFilename = "menu_candidate.xml";
		else if ($line[0] === "Interested")
			$xmlFilename = "menu_interested.xml";
		
	}
	
	$menuLst[] = "$line[1]:$line[2]";
}

$menuLstCount = count($menuLst);
//echo "$menuLstCount<br>";

for($i = 0; $i < $menuLstCount; $i++) {
	$line = explode(":", $menuLst[$i]);
	//echo "$line[0]<br>";
	
	if (strpos($line[0], "manage ") !== false)
		$manageLst[] = $menuLst[$i];
	else if (strpos($line[0], " report") !== FALSE)
		$reportLst[] = $menuLst[$i];
	else if (strpos($line[0], " payment") !== FALSE)
		$paymentLst[] = $menuLst[$i];
	else if (strpos($line[0], "presence ") !== FALSE)
		$presenceLst[] = $menuLst[$i];
	else if (strpos($line[0], " grade") !== FALSE)
		$gradeLst[] = $menuLst[$i];
	else
		$serviceLst[] = $menuLst[$i];
} // END for($i = 0; $i < $menuLstCount; $i++)

$doc = new DOMDocument("1.0", "UTF-8");
$doc->formatOutput = true;

$r = $doc->createElement( "menu" );
$doc->appendChild( $r );

//creating the home node
$b = $doc->createElement( "title");
$b->SetAttribute("name", "home");
$name = $doc->createElement( "link" );
$name->appendChild($doc->createTextNode( 'doua_menu.php' ));
$b->appendChild( $name );
$r->appendChild( $b );

//creating the manage node if esxists
if (isset($manageLst)) {
	$b = $doc->createElement( "title");
	$b->SetAttribute("name", "manage");
	
	foreach($manageLst as $manageItem) {
		$line = explode(":", $manageItem);
		$item = $doc->createElement( "item" );
		$item->SetAttribute("name", $line[0]);
		$link = $doc->createElement( "link" );
		$link->appendChild($doc->createTextNode( $line[1] ));
		$item->appendChild( $link );
		$b->appendChild( $item );
		$r->appendChild( $b );
	}
}

//creating the report node if esxists
if (isset($reportLst)) {
	$b = $doc->createElement( "title");
	$b->SetAttribute("name", "report");
	
	foreach($reportLst as $reportItem) {
		$line = explode(":", $reportItem);
		$item = $doc->createElement( "item" );
		$item->SetAttribute("name", $line[0]);
		$link = $doc->createElement( "link" );
		$link->appendChild($doc->createTextNode( $line[1] ));
		$item->appendChild( $link );
		$b->appendChild( $item );
		$r->appendChild( $b );
	}
}

//creating the payment node if esxists
if (isset($paymentLst)) {
	$b = $doc->createElement( "title");
	$b->SetAttribute("name", "payment");
	
	foreach($paymentLst as $paymentItem) {
		$line = explode(":", $paymentItem);
		$item = $doc->createElement( "item" );
		$item->SetAttribute("name", $line[0]);
		$link = $doc->createElement( "link" );
		$link->appendChild($doc->createTextNode( $line[1] ));
		$item->appendChild( $link );
		$b->appendChild( $item );
		$r->appendChild( $b );
	}
}

//creating the presence node if esxists
if (isset($presenceLst)) {
	$b = $doc->createElement( "title");
	$b->SetAttribute("name", "presence");
	
	foreach($presenceLst as $presenceItem) {
		$line = explode(":", $presenceItem);
		$item = $doc->createElement( "item" );
		$item->SetAttribute("name", $line[0]);
		$link = $doc->createElement( "link" );
		$link->appendChild($doc->createTextNode( $line[1] ));
		$item->appendChild( $link );
		$b->appendChild( $item );
		$r->appendChild( $b );
	}
}

//creating the grade node if esxists
if (isset($gradeLst)) {
	$b = $doc->createElement( "title");
	$b->SetAttribute("name", "grade");
	
	foreach($gradeLst as $gradeItem) {
		$line = explode(":", $gradeItem);
		$item = $doc->createElement( "item" );
		$item->SetAttribute("name", $line[0]);
		$link = $doc->createElement( "link" );
		$link->appendChild($doc->createTextNode( $line[1] ));
		$item->appendChild( $link );
		$b->appendChild( $item );
		$r->appendChild( $b );
	}
}

//creating the services node if esxists
if (isset($serviceLst)) {
	$b = $doc->createElement( "title");
	$b->SetAttribute("name", "services");
	
	foreach($serviceLst as $serviceItem) {
		$line = explode(":", $serviceItem);
		$item = $doc->createElement( "item" );
		$item->SetAttribute("name", $line[0]);
		$link = $doc->createElement( "link" );
		$link->appendChild($doc->createTextNode( $line[1] ));
		$item->appendChild( $link );
		$b->appendChild( $item );
		$r->appendChild( $b );
	}
}

//creating the logout node
$b = $doc->createElement( "title");
$b->SetAttribute("name", "logout");
$name = $doc->createElement( "link" );
$name->appendChild($doc->createTextNode( 'doua_logout.php' ));
$b->appendChild( $name );
$r->appendChild( $b );

//creating the help node
$b = $doc->createElement( "title");
$b->SetAttribute("name", "help");
$name = $doc->createElement( "link" );
$name->appendChild($doc->createTextNode( 'help.php' ));
$b->appendChild( $name );
$r->appendChild( $b );

$doc->saveXML();

$doc->save($xmlFilename);

echo $username;
?>