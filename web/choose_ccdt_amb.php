<?php
$username	= $_REQUEST['user'];
//$fct		= $_REQUEST['fc'];
$signature	= 'associate_ambiance_ccdt.php';
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

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>EMS - Associate ambiance to datetime</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
</head>
<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
	echo "<body>\n";
	echo "<center>\n";
	echo '<p>[ <a href="doua_menu.php?user=' . $username . '">home</a> ]</p>';
	echo '<p><h2>Hello, ' . $username . "</h2></p>\n";
	echo "<h1>Associate ambiance to datetime of \"$programName\",\"$courseName\",\"$className\",\"$scheduleName\"</h1>\n";
	
	echo "<table width=\"400\" border=\"1\">\n";
		echo "<tr>\n";
		  echo "<th>date</th>\n";
		  echo "<th>time</th>\n";		  
		echo "</tr>\n";
		
			$url = "$host/axis2/services/ws_ems/get_ccdt_from_schedule_class_course?username=$username&cookie=$cookie_value&ip=$ip&coid=$courseId&clid=$classId&schid=$scheduleId&show_past_dates=1";
			//echo "url = $url<br>";
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_ems.get_ccdt_from_schedule_class_course");
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
				
				$line = explode(';',$xmlNode);
				if ($i > 0) {
					echo "<tr>\n";	 						
						echo "<td>\n";							
							echo "<a href=\"fill_ccdta.php?user=$username&clname=$className&clid=$classId&coname=$courseName&coid=$courseId&date=$line[1]&time=$line[2]-$line[3]&ccdtid=$line[0]\">$line[1]</a>";																			
						echo "</td>\n";
						echo "<td>\n";
							echo "$line[2]-$line[3]";																			
						echo "</td>\n";
						
						
					echo "</tr>\n";
				}
			}
		
		
	  echo "</table>\n";
	  echo  "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
	echo "</form>\n";

	echo "</center>\n";
?>
</body>
</html>
