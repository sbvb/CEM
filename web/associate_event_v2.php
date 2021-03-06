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
$signature	= 'man_event.php';
$class		= $_REQUEST['lstClass'];
$program	= $_REQUEST['lstProgram'];
$course		= $_REQUEST['lstCourse'];
$schedule	= $_REQUEST['lstSchedule'];

$classLst = explode(":",$class);
$className	= $classLst[0];
$classId	= $classLst[1];

$programLst 	= explode(":",$program);
$programName	= $programLst[0];
$programId		= $programLst[1];

$courseLst 	= explode(":",$course);
$courseName	= $courseLst[0];
$courseId	= $courseLst[1];

$scheduleLst 	= explode(":",$schedule);
$scheduleName	= $scheduleLst[0];
$scheduleId		= $scheduleLst[1];

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

//getting ambiance list
$url = "$host/axis2/services/ws_ems/get_ambiences_list?username=$username&cookie=$cookie_value&ip=$ip";
//echo "url = $url<br>";
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_ambiences_list");
$allLines = "";
while ($currline = fread($myfile,1024))
	$allLines .= $currline; // concat currline to allLines
fclose($myfile) or die("died when doing CLOSE");
// parse XML
$xmlDoc = new DOMDocument();
$xmlDoc->loadXML($allLines);

$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
$cnt = $xmlElem->length;
$ambLine = "";
for($i = 0; $i < $cnt ; $i++) {
	$xmlNode = $xmlElem->item($i)->nodeValue;
	
	// Goes to doua_error.php because session expired.
	if ($xmlNode == "session_expired")
		echo "<script>back_page('doua_error.php')</script>";
	
	$line = explode(':',$xmlNode);
			
	if ( $i > 0) {
		if ($ambLine === "") {
			//echo "empty<br>";
			$ambLine = "<option value=\"$line[1]\">$line[0]</option>";
		} 
		else {
			//echo "not empty<br>";
			$ambLine .= "\n<option value=\"$line[1]\">$line[0]</option>";
		}
	}
}

//getting ambiance and date list
$url = "$host/axis2/services/ws_ems/get_ambiance_and_date_list?username=$username&cookie=$cookie_value&ip=$ip&coid=$courseId&clid=$classId";
//echo "url = $url<br>";
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_ambiance_and_date_list");
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
	
	if ( $xmlNode !== "false") {
		$ambDateLst[] = $xmlNode;
	}
}

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
	
	<script type="text/javascript">
	<!--
		function SetAllAmbiances(me) {
			//alert(me.selectedIndex);
			var ambIndex = me.selectedIndex;
			var ambLstCnt = document.frmDates.lstAmbiance.length;			
			for(var i = 0; i < ambLstCnt; i++) {
				document.frmDates.lstAmbiance[i].selectedIndex = ambIndex;
			}
				
		}
		
		function SelectAmbiance(ambid,dtid) {
			var elCnt = document.frmDates.hdDateId.length;
			
			if (elCnt == undefined) { // only one element			
				if (document.frmDates.hdDateId.value == dtid) {
					for(var i = 0; i < document.frmDates.lstAmbiance.options.length; i++) {
						if (document.frmDates.lstAmbiance.options[i].value == ambid) {
							document.frmDates.lstAmbiance.selectedIndex = i;
							break;
						}
					}
				}
									
			}
			else { // more than one elements
			
				for(var i = 0; i < elCnt; i++) {				
					if (document.frmDates.hdDateId[i].value == dtid) {											
						for(var j = 0; j < document.frmDates.lstAmbiance[i].options.length; j++) {							
							if (document.frmDates.lstAmbiance[i].options[j].value == ambid) {
								document.frmDates.lstAmbiance[i].selectedIndex = j;								
								break;								
							}
						}
					}
				}
				
			}			
		}
	-->
	</script>
	
</head>
<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
	echo "<body>\n";
	echo "<center>\n";
	//echo "t1<br>";
	//echo $ambLine . "<br>";
	
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
	echo "<h1>$assocEventTitle \"$programName\",\"$courseName\",\"$className\",\"$scheduleName\"</h1>\n";
			
	echo "<form name=\"frmDates\" id=\"frmDates\">\n";
	  echo "<table width=\"400\" border=\"1\">\n";
			$url = "$host/axis2/services/ws_ems/get_schedule_settings?username=$username&cookie=$cookie_value&ip=$ip&schid=$scheduleId";
			//echo "url = $url<br>";
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_ems.get_schedule_settings");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
		
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;			
			
			if ($cnt > 1) {			
				echo "<tr border=\"0\">\n";
					echo "<td colspan=\"2\" align=\"right\">$setAllAmbMsg:</td>\n";
					echo "<td><select name=\"lstAllAmb\" onchange=\"SetAllAmbiances(this)\">\n";
						echo $ambLine;
					echo "</select></td>\n";
				echo "</tr>";
				echo "<tr><td colspan=\"3\"></td></tr>";				
			}
	  						
			echo "<tr>\n";
				echo "<th>$datetimeCap[0]</th>\n";
				echo "<th>$timeCap</th>\n";
				echo "<th>$ambMsg</th>\n";	  
			echo "</tr>\n";
			
			for($i = 0; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				
				// Goes to doua_error.php because session expired.
				if ($xmlNode == "session_expired")
					echo "<script>back_page('doua_error.php')</script>";
				
				$line = explode(';',$xmlNode);
				
				echo "<tr>\n";					
					echo "<td>\n";							
						//echo "<a href=\"fill_event.php?user=$username&clname=$className&clid=$classId&coname=$courseName&coid=$courseId&date=$line[2]&time=$line[3]-$line[4]&dateid=$line[5]\">$line[2]</a>";																			
						echo "$line[2]";
						echo "<input type=\"hidden\" name=\"hdDateId\" value=\"$line[5]\">";																		
					echo "</td>\n";
					echo "<td>\n";
						echo "$line[3]-$line[4]";																			
					echo "</td>\n";			
					echo "<td><select name=\"lstAmbiance\">\n";
						echo $ambLine;
					echo "</select></td>\n";		
				echo "</tr>\n";								
			}
		echo "<tr>\n";
			echo "<td colspan=\"3\" align=\"right\"><input type=\"button\" name=\"btnAssociate\" value=\"$associateBtnMsg\" onclick=\"associate_events()\"></td>";
		echo "</tr>\n";
		echo "</table>\n";
		echo  "<input type=\"hidden\" name=\"hdClassId\" value=\"$classId\">\n";
		echo  "<input type=\"hidden\" name=\"hdCourseId\" value=\"$courseId\">\n";
		echo  "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
	echo "</form>\n";

	echo "</center>\n";
	
	
	for($i = 0; $i < count($ambDateLst); $i++) {
		$line = explode(':',$ambDateLst[$i]);
		echo "<script>SelectAmbiance('$line[0]','$line[1]');</script>";
	}
?>
</body>
</html>
