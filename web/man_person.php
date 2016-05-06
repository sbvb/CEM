<?php
bindtextdomain("lang", "./locale");
textdomain("lang");

if (isset($_COOKIE['ems_lang'])) {
	if ($_COOKIE['ems_lang'] == "br")
		setlocale(LC_ALL, "pt_BR.utf-8");
	else if ($_COOKIE['ems_lang'] == "en")
		setlocale(LC_ALL, "en_US.utf-8");
}

if (isset($_COOKIE['ems_actor'])) {
	$actor = $_COOKIE['ems_actor'];
	
	if ($actor === "Administrator")
		$xmlFilename = "menu_admin.xml";
	else if ($actor === "School Manager")
		$xmlFilename = "menu_scman.xml";
	else if ($actor === "Tutor")
		$xmlFilename = "menu_tutor.xml";
	else if ($actor === "Student")
		$xmlFilename = "menu_student.xml";
	else if ($actor === "Secretary")
		$xmlFilename = "menu_secretary.xml";
	else if ($actor === "Client")
		$xmlFilename = "menu_client.xml";
	else if ($actor === "Student Represent")
		$xmlFilename = "menu_stud_rep.xml";
	else if ($actor === "Coordinator")
		$xmlFilename = "menu_coordinator.xml";
	else if ($actor === "Candidate")
		$xmlFilename = "menu_candidate.xml";
	else if ($actor === "Interested")
		$xmlFilename = "menu_interested.xml";
}

include("constants.php");

$username	= $_REQUEST['user'];
//$fct		= $_REQUEST['fc'];
$signature	= 'man_person.php';

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

$gline = $_GET['gid'];
$gLst = explode(":",$gline);
$gname = $gLst[1];
$gid = $gLst[0];

$ptype = "School Manager";

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<link href="menu/css3menu/style.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
	
	<Script Language="JavaScript"> 
		function getGroupId(what,user) { 
		   if (what.selectedIndex != '') { 
			  var gid = what.value;
			  document.location=('man_person.php?user=' + user + '&gid=' + gid);
			  //alert(class); 
		   } 
		}		
	</Script>
	
</head>
<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
	echo "<body>\n";
	echo "<center>\n";
	
	// BEGIN menu from xml
	echo "<table align=\"center\" class=\"menutable\"><tr><td>\n";
	
	if (file_exists("menu/$xmlFilename")) {
		$xml = simplexml_load_file("menu/$xmlFilename");
		
		$titleCnt = count($xml->title);			
		if ($titleCnt > 0) {
			echo "<ul id=\"css3menu1\" class=\"topmenu\">\n"; 
			for($i = 0; $i < $titleCnt ; $i++) {
				$titleName = gettext($xml->title[$i][name]);
				$link = "#";
				
				if (isset($xml->title[$i]->link))
					$link = $xml->title[$i]->link . "?user=$username";
					
				if ($i == 0) 
					echo "<li class=\"topfirst\"><a href=\"$link\" title=\"$titleName\" style=\"height:18px;line-height:18px;\">$titleName</a>";
				else if ($i == $titleCnt - 1)
					echo "<li class=\"toplast\"><a href=\"$link\" title=\"$titleName\" style=\"height:18px;line-height:18px;\">$titleName</a>";
				else
					echo "<li class=\"topmenu\"><a href=\"$link\" title=\"$titleName\" style=\"height:18px;line-height:18px;\"><span>$titleName</span></a>";
				
				if (!isset($xml->title[$i]->link))
					echo "<ul>";
				
				if (isset($xml->title[$i]->submenu)) {				
					$submenuCnt = count($xml->title[$i]->submenu);			
													
					for($j = 0; $j < $submenuCnt; $j++) {					
						$subTitleName = gettext($xml->title[$i]->submenu[$j][name]);
						echo "<li><a href=\"#\" title=\"$subTitleName\"><span>$subTitleName</span></a>";
						
						if (isset($xml->title[$i]->submenu[$j]->item)) {						
							$submenuItemCnt = count($xml->title[$i]->submenu[$j]->item);
							
							echo "<ul>";
							for($k = 0; $k < $submenuItemCnt; $k++) {
								$submenuItemName = gettext($xml->title[$i]->submenu[$j]->item[$k][name]);
								$submenuItemLink = $xml->title[$i]->submenu[$j]->item[$k]->link . "?user=$username";
							
								echo "<li><a href=\"$submenuItemLink\" title=\"$submenuItemName\">$submenuItemName</a></li>";						
							}
							echo "</ul>";
						}
					}				
				}
				
				if (isset($xml->title[$i]->item)) {	
					$itemCount = count($xml->title[$i]->item);
					for($j = 0; $j < $itemCount; $j++) {			
						$itemName = gettext($xml->title[$i]->item[$j][name]);
						$itemLink = $xml->title[$i]->item[$j]->link . "?user=$username";
										
						echo "<li><a href=\"$itemLink\" title=\"$itemName\">$itemName</a></li>";							
					} // END for
					
				} // if (isset($xml->title[$i]->item))
				
				if (!isset($xml->title[$i]->link))
					echo "</ul>";	
				
			}
		} // END if ($titleCnt > 0)	
		echo "</ul>";	
	}
	echo "</td></tr></table>\n";
	// END menu from xml
		
	echo "<p><h2>$helloMsg $username</h2></p>\n";
	echo "<h1>$manPersonTitle</h1>\n";
	echo "<p><a href=\"crt_person_act.php?user=$username&fc=crt\">$crtNewPersonMsg</a></p>";
	
	echo "$actorMsg <select name=\"lstUserGroup\" onchange=\"getGroupId(this, '$username')\">\n";
		echo "<option>$lstUserGrpMsg</option>";
		$url = "$host/axis2/services/ws_doua_ems/get_user_groups?username=$username&cookie=$cookie_value&ip=$ip";
		//echo "url = $url<br>";		
		$myfile = fopen($url, "r")
        		or die("died when doing OPEN get_user_groups");
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
			if ($xmlNode == "session_expired")
				echo "<script>back_page('doua_error.php')</script>";
			
			$line = explode(':',$xmlNode);
			if ($line[0] == $gid)
				echo "<option value=\"$line[0]:$line[1]\" SELECTED>$line[1]</option>\n";
			else
				echo "<option value=\"$line[0]:$line[1]\">$line[1]</option>\n";					
		}	
	echo "</select>\n";

	if (isset($gline)) { // gets all persons with this group id
	
		$isUserSchoolMan = false;
		$url = "$host/axis2/services/ws_ems/get_person_type_from_user?username=$username&cookie=$cookie_value&ip=$ip";
		//echo "url = $url<br>";
		$myfile = fopen($url, "r")
				or die("died when doing OPEN get_person_type_from_user");
		$allLines = "";
		while ($currline = fread($myfile,1024))
			$allLines .= $currline; // concat currline to allLines
		fclose($myfile) or die("died when doing CLOSE");
		// parse XML
		$xmlDoc = new DOMDocument();
		$xmlDoc->loadXML($allLines);
	
		$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
		$cnt = $xmlElem->length;
		echo "<table border =\"1\" align=\"center\">\n";
		for($i = 0; $i < $cnt ; $i++) {
			$xmlNode = $xmlElem->item($i)->nodeValue;
			
			// Goes to doua_error.php because session expired.
			if ($xmlNode == "session_expired")
				echo "<script>back_page('doua_error.php')</script>";
			
			$line = explode(':',$xmlNode);
			if ($ptype == $line[0]) {
				$isUserSchoolMan = true;
				//echo "OK<br>";
			}
		}
	
		if ($isUserSchoolMan == true) { // username is school manager
			$url = "$host/axis2/services/ws_doua_ems/get_user_accounts?username=$username&cookie=$cookie_value&ip=$ip&gid=$gid";
			//echo "url = $url<br>";
			$myfile = fopen($url, "r")
					or die("died when doing OPEN get_user_accounts");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
		
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;
			echo "<table border =\"1\" align=\"center\">\n";
			for($i = 0; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				
				// Goes to doua_error.php because session expired.
				if ($xmlNode == "session_expired")
					echo "<script>back_page('doua_error.php')</script>";
				
				$line = explode(':',$xmlNode);
				if ($i == 0) {
					echo "<tr>\n<th>" . gettext($line[0]) . "</th>\n<th>" . gettext($line[2]) . "</th>\n<th colspan=\"3\">&nbsp;</th></tr>\n";
				}
				else {
					echo "\t<tr>\n";
					echo '<td>' . $line[0] . '</td>';
					echo '<td>' . $line[2] . '</td>';
					echo "<td><a href=\"reset_person_pass.php?user=$username&edusr=$line[0]\">$chgPwdMsg</a></td>";
						echo "<td><a href=\"crt_person_act.php?user=$username&fc=edt&edusr=$line[0]\">$editMsg</a></td>";
						echo "<td><a href=\"#\" onClick=\"delete_person_user('$username','$line[0]')\">$delMsg</a></td>\n";
					echo "\t</tr>\n";
				}
			}
			echo "</table>\n";
		}	
		else { // username is not school manager
			$url = "$host/axis2/services/ws_ems/get_person_list?username=$username&cookie=$cookie_value&ip=$ip&ptype=$gname";
			//echo "url = $url<br>";
			$myfile = fopen($url, "r")
					or die("died when doing OPEN get_person_list");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
		
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;
			$arrPerson[] = "";
			for($i = 0; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				
				// Goes to doua_error.php because session expired.
				if ($xmlNode == "session_expired")
					echo "<script>back_page('doua_error.php')</script>";
				//echo "$xmlNode<br>";
				$line = explode(':',$xmlNode);
				if ($i > 0) {
					$arrPerson[] = $line[2];
				}			
			}
			
			//print_r($arrPerson);
	
			$url = "$host/axis2/services/ws_doua_ems/get_user_accounts?username=$username&cookie=$cookie_value&ip=$ip&gid=$gid";
			//echo "url = $url<br>";
			$myfile = fopen($url, "r")
					or die("died when doing OPEN get_user_accounts");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
		
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;
			echo "<table border =\"1\" align=\"center\">\n";
			for($i = 0; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				
				// Goes to doua_error.php because session expired.
				if ($xmlNode == "session_expired")
					echo "<script>back_page('doua_error.php')</script>";
				
				$line = explode(':',$xmlNode);
				if ($i == 0) {
					echo "<tr>\n<th>" . gettext($line[0]) . "</th>\n<th>" . gettext($line[2]) . "</th>\n<th colspan=\"3\">&nbsp;</th></tr>\n";
				}
				else {
					if (array_search($line[0], $arrPerson) !== FALSE) {
						echo "\t<tr>\n";
						echo '<td>' . $line[0] . '</td>';
						echo '<td>' . $line[2] . '</td>';
						echo "<td><a href=\"reset_person_pass.php?user=$username&edusr=$line[0]\">$chgPwdMsg</a></td>";
						echo "<td><a href=\"crt_person_act.php?user=$username&fc=edt&edusr=$line[0]\">$editMsg</a></td>";
						echo "<td><a href=\"#\" onClick=\"delete_person_user('$username','$line[0]')\">$delMsg</a></td>\n";
						echo "\t</tr>\n";
					} // END if (array_search($line[0], $arrPerson) !== FALSE)
				} // END if ($i == 0)
			} // END for($i = 0; $i < $cnt ; $i++)
		} // END else { // username is not school manager
	}

	echo "</center>\n";
?>
</body>
</html>

