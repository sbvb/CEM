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
$signature	= 'man_class_course.php';
$className	= $_REQUEST['clname'];
$classId	= $_REQUEST['clid'];
$courseName	= $_REQUEST['coname'];
$courseId	= $_REQUEST['coid'];
$manOk		= $_REQUEST['manok'];

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

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
	
	echo "<h1>$fillClCoTitle</h1><br>\n";
	echo "<h1>($assocClassCourseCap[0] \"$courseName\", $assocClassCourseCap[2] \"$className\")</h1>\n";
	
	if ($manOk == 0) { // create a new class_course management
		echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
			echo '<form action="" method="get" name="frmAssociate" id="frmCreate">';
				echo '<table width="291" align="center">';				
					echo "<tr>\n";
						echo "<td><div align=\"right\">$datetimeCap[3]:</div></td>";
						echo '<td><select name="lstSchedule" id="lstSchedule">';
							
							//Getting the schedules list from webservices
							$url = "$host/axis2/services/ws_ems/get_schedules_list?username=$username&cookie=$cookie_value&ip=$ip";
							//echo $url;
							$myfile = fopen($url, "r")
									or die("died when doing OPEN ws_doua_ems.get_schedules_list");
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
								
								// Goes to doua_error.php because session expired.
								if ($xmlNode == "session_expired")
									echo "<script>back_page('doua_error.php')</script>";
								
								$lst = explode(":",$xmlNode);
								if ($i > 0)							
									echo "<option value=\"$lst[1]\">$lst[0]</option>\n";
								
							}
							
						echo "</select></td>\n";
						echo "<td><input type=\"button\" name=\"btnViewSchedule\" id=\"btnViewSchedule\" value=\"$viewMsg\" onclick=\"view_schedule()\"><td>\n";
					echo "</tr>\n";	
					echo "<tr>\n";
						echo "<td><div align=\"right\">$tutorCap:</div></td>";
						echo '<td><select name="lstTutor" id="lstTutor">';
							
							//Getting the tutor list from webservices
							$url = "$host/axis2/services/ws_ems/get_person_list?username=$username&cookie=$cookie_value&ip=$ip&ptype=Tutor";
							//echo $url;
							$myfile = fopen($url, "r")
									or die("died when doing OPEN ws_doua_ems.get_person_list");
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
								
								// Goes to doua_error.php because session expired.
								if ($xmlNode == "session_expired")
									echo "<script>back_page('doua_error.php')</script>";
								
								$lst = explode(":",$xmlNode);
								if ($i > 0) 
									echo "<option value=\"$lst[1]\">$lst[0]</option>\n";
								
							}
							
						echo "</select></td>\n";
						echo "<td><input type=\"button\" name=\"btnViewTutor\" id=\"btnViewTutor\" value=\"$viewMsg\" onclick=\"view_tutor()\"><td>\n";
					echo "</tr>\n";							
					echo "<tr>\n";
						echo "<td><input name=\"btnBack\" type=\"button\" value=\"$backBtnMsg\" onClick=\"back_page('man_class_course.php?user=$username')\"></td>";
						echo "<td><input name=\"btnAssociate\" type=\"button\" value=\"$associateBtnMsg\" onClick=\"associate_class_to_course()\"></td>";				
					echo "</tr>\n";
				echo "</table>\n";			
				echo "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
				echo "<input type=\"hidden\" name=\"hdClassId\" value=\"$classId\">\n";
				echo "<input type=\"hidden\" name=\"hdCourseId\" value=\"$courseId\">\n";
			echo "</form>\n";
	}
	else { // edit class_course
	
		//Getting the class_course settings from webservices
		$url = "$host/axis2/services/ws_ems/get_class_course_settings?username=$username&cookie=$cookie_value&ip=$ip&clid=$classId&coid=$courseId";
		//echo $url;
		$myfile = fopen($url, "r")
				or die("died when doing OPEN ws_doua_ems.get_class_course_settings");
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
			
			// Goes to doua_error.php because session expired.
			if ($xmlNode == "session_expired")
				echo "<script>back_page('doua_error.php')</script>";			
		}
	
		$class_course_settings = $xmlNode; 
		
		$personName		= "";
		$personId		= 0;
		$scheduleName	= "";
		$scheduleId		= 0;
		if ($class_course_settings != "") {
			$lst = explode(":", $class_course_settings);
			$personName		= $lst[0];
			$personId		= (int) $lst[1];
			$scheduleName	= $lst[2];
			$scheduleId		= (int) $lst[3];			
		}
	
		echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
			echo '<form action="" method="get" name="frmAssociate" id="frmCreate">';
				echo '<table width="291" align="center">';				
					echo "<tr>\n";
						echo '<td><div align="right">schedule: </div></td>';
						echo '<td><select name="lstSchedule" id="lstSchedule">';
							
							//Getting the schedules list from webservices
							$url = "$host/axis2/services/ws_ems/get_schedules_list?username=$username&cookie=$cookie_value&ip=$ip";
							//echo $url;
							$myfile = fopen($url, "r")
									or die("died when doing OPEN ws_doua_ems.get_schedules_list");
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
								
								// Goes to doua_error.php because session expired.
								if ($xmlNode == "session_expired")
									echo "<script>back_page('doua_error.php')</script>";
								
								$lst = explode(":",$xmlNode);
								if ($i > 0) {
									if ($scheduleId == (int)$lst[1])
										echo "<option value=\"$lst[1]\" SELECTED>$lst[0]</option>\n";
									else
										echo "<option value=\"$lst[1]\">$lst[0]</option>\n";
								}
								
							}
							
						echo "</select></td>\n";
						echo "<td><input type=\"button\" name=\"btnViewSchedule\" id=\"btnViewSchedule\" value=\"$viewMsg\" onclick=\"view_schedule()\"><td>\n";
					echo "</tr>\n";	
					echo "<tr>\n";
						echo '<td><div align="right">tutor: </div></td>';
						echo '<td><select name="lstTutor" id="lstTutor">';
							
							//Getting the tutor list from webservices
							$url = "$host/axis2/services/ws_ems/get_person_list?username=$username&cookie=$cookie_value&ip=$ip&ptype=Tutor";
							//echo $url;
							$myfile = fopen($url, "r")
									or die("died when doing OPEN ws_doua_ems.get_person_list");
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
								
								// Goes to doua_error.php because session expired.
								if ($xmlNode == "session_expired")
									echo "<script>back_page('doua_error.php')</script>";
								
								$lst = explode(":",$xmlNode);
								if ($i > 0) {
									if ($personId == (int)$lst[1])
										echo "<option value=\"$lst[1]\" SELECTED>$lst[0]</option>\n";
									else
										echo "<option value=\"$lst[1]\">$lst[0]</option>\n";
								}
								
							}
							
						echo "</select></td>\n";
						echo "<td><input type=\"button\" name=\"btnViewTutor\" id=\"btnViewTutor\" value=\"$viewMsg\" onclick=\"view_tutor()\"><td>\n";
					echo "</tr>\n";							
					echo "<tr>\n";
						echo "<td><input name=\"btnBack\" type=\"button\" value=\"$backBtnMsg\" onClick=\"back_page('man_class_course.php?user=$username')\"></td>";
						echo "<td><input name=\"btnAssociate\" type=\"button\" value=\"$saveBtnMsg\" onClick=\"edit_class_to_course()\"></td>";				
					echo "</tr>\n";
				echo "</table>\n";			
				echo "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
				echo "<input type=\"hidden\" name=\"hdClassId\" value=\"$classId\">\n";
				echo "<input type=\"hidden\" name=\"hdCourseId\" value=\"$courseId\">\n";
			echo "</form>\n";
	}

	echo "</center>\n";
?>
</body>
</html>
