<?php
$username	= $_REQUEST['user'];
//$fct		= $_REQUEST['fc'];
$signature	= 'associate_ambiance_ccdt.php';
$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$pline = $_GET['pline'];
$coline = $_GET['coline'];
$clline = $_GET['clline'];

$plst = explode(":",$pline);
$colst = explode(":",$coline);
$cllst = explode(":",$clline);

$pid = $plst[1];
$coid = $colst[1];
$clid = $cllst[1];


?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>EMS - Associate ambiance to datetime</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
	
	<Script Language="JavaScript"> 
		function getProgram(what,user) { 
		   if (what.selectedIndex != '') { 
			  var pline = what.value;
			  document.location=('associate_ambiance_ccdt.php?user=' + user + '&pline=' + pline);
			  //alert(class); 
		   } 
		}
		function getCourse(what,user,pline) { 
		   if (what.selectedIndex != '') { 
			  var coline = what.value;
			  document.location=('associate_ambiance_ccdt.php?user=' + user + '&pline=' + pline + '&coline=' + coline);
			  //alert(class); 
		   } 
		} 
		function getClass(what,user,pline,coline) { 
		   if (what.selectedIndex != '') { 
			  var clline = what.value;
			  document.location=('associate_ambiance_ccdt.php?user=' + user + '&pline=' + pline + '&coline=' + coline + '&clline=' + clline);
			  //alert(class); 
		   } 
		}
	</Script>
	
</head>
<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
	echo "<body>\n";
	echo "<center>\n";
	echo '<p>[ <a href="doua_menu.php?user=' . $username . '">home</a> ]</p>';
	echo '<p><h2>Hello, ' . $username . "</h2></p>\n";
	echo "<h1>Associate ambiance to datetime</h1>\n";
		
	echo '<form action="choose_ccdt_amb.php" method="get" name="frmCreate" id="frmCreate">';
			echo '<table width="353" align="center">';
				echo "<tr>\n";
					echo '<td><div align="right">program: </div></td>';
					echo "<td colspan=\"2\"><select name=\"lstProgram\" id=\"lstProgram\" onchange=\"getProgram(this,'$username')\">";
					echo "<option value=\"\">select program</option>";
										
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
					echo '<td><div align="right">course: </div></td>';
					echo "<td colspan=\"2\"><select name=\"lstCourse\" id=\"lstCourse\" onchange=\"getCourse(this,'$username','$pline')\">";
					echo "<option value=\"\">select course</option>";
										
					$url = "$host/axis2/services/ws_ems/get_courses_associated_program?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid";
					//echo "url = $url<br>";
					$myfile = fopen($url, "r")
							or die("died when doing OPEN ws_ems.get_courses_associated_program");
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
							if ($coid == $line[1])
								echo "<option value=\"$line[0]:$line[1]\" SELECTED>$line[0]</option>";
							else
								echo "<option value=\"$line[0]:$line[1]\">$line[0]</option>";
						}						
					}
					
					echo '</select></td>';					
					echo "</tr>\n";
				
					if (isset($coline)) {
						echo "<tr>\n";
						echo '<td><div align="right">class: </div></td>';
						echo "<td colspan=\"2\"><select name=\"lstClass\" id=\"lstClass\" onchange=\"getClass(this,'$username','$pline','$coline')\">";
						//echo "<td colspan=\"2\"><select name=\"lstClass\" id=\"lstClass\">";
						echo "<option value=\"\">select class</option>";
											
						$url = "$host/axis2/services/ws_ems/get_classes_associated_course?username=$username&cookie=$cookie_value&ip=$ip&coid=$coid";
						//echo "url = $url<br>";
						$myfile = fopen($url, "r")
								or die("died when doing OPEN ws_ems.get_courses_associated_program");
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
							echo '<td><div align="right">schedule: </div></td>';
							//echo "<td colspan=\"2\"><select name=\"lstSchedule\" id=\"lstSchedule\" onchange=\"getSchedule(this,'$username','$pline','$coline')\">";
							echo "<td colspan=\"2\"><select name=\"lstSchedule\" id=\"lstSchedule\">";
							echo "<option value=\"\">select schedule</option>";
												
							$url = "$host/axis2/services/ws_ems/get_schedule_from_class_course?username=$username&cookie=$cookie_value&ip=$ip&coid=$coid&clid=$clid";
							//echo "url = $url<br>";
							$myfile = fopen($url, "r")
									or die("died when doing OPEN ws_ems.get_schedule_from_class_course");
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
								if ($schid == $line[1])
									echo "<option value=\"$line[0]:$line[1]\" SELECTED>$line[0]</option>";
								else
									echo "<option value=\"$line[0]:$line[1]\">$line[0]</option>";
								}						
							}
							
							echo '</select></td>';					
						echo "</tr>\n";
						
						echo "<tr>\n";
							echo "<td colspan=\"2\"><input type=\"submit\" name=\"btnOk\" value=\"Ok\"></td>\n";
						echo "</tr>\n";
						
					}
					
				}
			}
			echo "</table>\n";
			echo "<input type=\"hidden\" name=\"user\" value=\"$username\">\n";
		echo "</form>\n";

	echo "</center>\n";
?>
</body>
</html>
