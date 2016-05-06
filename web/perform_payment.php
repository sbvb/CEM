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
}

include("constants.php");

$username	= $_REQUEST['user'];
//$fct		= $_REQUEST['fc'];
$signature	= 'perform_payment.php';
$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$pline = $_GET['pline'];
$clline = $_GET['clline'];

$plst = explode(":",$pline);
$cllst = explode(":",$clline);

$pid = $plst[1];
$clid = $cllst[1];


?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="menu/css3menu/style.css" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
	
	<Script Language="JavaScript"> 
		function getProgram(what,user) { 
		   if (what.selectedIndex != '') { 
			  var pline = what.value;
			  document.location=('perform_payment.php?user=' + user + '&pline=' + pline);
			  //alert(class); 
		   } 
		}		
		function getClass(what,user,pline) { 
		   if (what.selectedIndex != '') { 
			  var clline = what.value;
			  document.location=('perform_payment.php?user=' + user + '&pline=' + pline + '&clline=' + clline);
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
	echo "<h1>$performPaymentTitle</h1>\n";
	
	echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
	echo '<form action="" method="get" name="frmCreate" id="frmCreate">';
			echo '<table width="353" align="center">';
				echo "<tr>\n";
					echo "<td><div align=\"right\">$programCap[0]:</div></td>";
					echo "<td colspan=\"2\"><select name=\"lstProgram\" id=\"lstProgram\" onchange=\"getProgram(this,'$username')\">";
					echo "<option value=\"\">$selProgramMsg</option>";
										
					$url = "$host/axis2/services/ws_ems/get_programs_list?username=$username&cookie=$cookie_value&ip=$ip";
					//echo "url = $url<br>";
					$myfile = fopen($url, "r")
							or die("died when doing OPEN ws_ems.get_programs_list");
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
						if ($i > 0) {
							if ($pid == $line[1])
								echo "<option value=\"$line[0]:$line[1]\" SELECTED>$line[0]</option>";
							else
								echo "<option value=\"$line[0]:$line[1]\">$line[0]</option>";
						}						
					}
					
					echo '</select></td>';									
				echo "</tr>\n";
				
				if (isset($pline)) {
					echo "<tr>\n";
					echo "<td><div align=\"right\">$classCap:</div></td>";
					echo "<td colspan=\"2\"><select name=\"lstClass\" id=\"lstClass\" onchange=\"getClass(this,'$username', '$pline')\">";
					echo "<option value=\"\">$selClassMsg</option>";
										
					$url = "$host/axis2/services/ws_ems/get_classes_from_program?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid";
					//echo "url = $url<br>";
					$myfile = fopen($url, "r")
							or die("died when doing OPEN ws_ems.get_classes_from_program");
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
						if ($i > 0) {
							if ($clid == $line[1])
								echo "<option value=\"$line[0]:$line[1]\" SELECTED>$line[0]</option>";
							else
								echo "<option value=\"$line[0]:$line[1]\">$line[0]</option>";
						}						
					}
					
					if (isset($clline)) {
						echo "<tr>\n";
						echo "<td><div align=\"right\">$studentCap:</div></td>";
						echo "<td colspan=\"2\"><select name=\"lstStudent\" id=\"lstStudent\">";
						echo "<option value=\"\">$selStudentMsg</option>";
											
						$url = "$host/axis2/services/ws_ems/get_students_associated_enrollment?username=$username&cookie=$cookie_value&ip=$ip&clid=$clid";
						//echo "url = $url<br>";
						$myfile = fopen($url, "r")
								or die("died when doing OPEN ws_ems.get_students_associated_enrollment");
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
							if ($i > 0) {
								if ($clid == $line[1])
									echo "<option value=\"$line[0]:$line[1]\" SELECTED>$line[0]</option>";
								else
									echo "<option value=\"$line[0]:$line[1]\">$line[0]</option>";
							}						
						}
						
						echo '</select></td>';					
						echo "</tr>\n";
						echo "<tr>\n";
							echo "<td colspan=\"2\"><input type=\"button\" name=\"btnOk\" value=\"Ok\" onclick=\"perform_payment()\"></td>\n";
						echo "</tr>\n";
				}
			}
			echo "</table>\n";
			echo "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
		echo "</form>\n";

	echo "</center>\n";
?>
</body>
</html>
