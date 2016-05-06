<?php
$username	= $_REQUEST['user'];
//$fct		= $_REQUEST['fc'];
$signature	= 'answer_presence.php';

$class_id	= $_GET['class_id'];
$date_id	= $_GET['date_id'];

//$curdate	= date('Y/m/d');
$curdate	= "2011/05/13";

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>EMS - Answer presence</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
	
	<Script Language="JavaScript"> 
		function getCourse(what,user) { 
		   if (what.selectedIndex != '') { 
			  var course = what.value;
			  document.location=('answer_presence.php?user=' + user + '&course=' + course);
			  //alert(class); 
		   } 
		} 
		function getClass(what,user,course) { 
		   if (what.selectedIndex != '') { 
			  var class = what.value;
			  document.location=('answer_presence.php?user=' + user + '&course=' + course + '&class=' + class);
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
	echo "<h1>Answer presence</h1>\n";
			
	echo "<table align=\"center\">\n";
		echo "<form action=\"\" method=\"get\" name=\"frmAnsPresence\">";		
		echo "<input name=\"date_id\" type=\"hidden\" value=\"$date_id\">";
		echo "\t<tr>\n";
		echo "<td>Student</td>\n";
		echo "<td>Presence</td>\n";
		echo "</tr>\n";					
					
			$url = "$host/axis2/services/ws_ems/get_students_from_class?class_id=$class_id";
			//echo $url;
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_doua_ems.get_courses_from_tutor");
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
				if ($i > 0) {
					$xmlNode = $xmlElem->item($i)->nodeValue;
					$lst = explode(":", $xmlNode);
					echo "<tr>\n";
					echo "<td>$lst[0]</td>";
					//echo "<input name=\"student\" type=\"hidden\" value=\"$lst[1]\">";
					//echo "<td><input type=\"checkbox\" name=\"presence\" value=\"$i\"></td>";
					
					//in value the student id is passed.
					echo "<td><input type=\"checkbox\" name=\"presence_$i\" value=\"$lst[1]\"></td>";
					echo "</tr>\n";
					
				}				
			}		
		echo "<tr>\n";
			echo "<td></td>\n";
			echo "<td align=\"right\"><input name=\"btnOk\" type=\"button\" value=\"Ok\" onclick=\"make_presence(this.form)\"></td>\n";
		echo "</tr>\n";
		echo "</form>";
	echo "</table>\n";

	echo "</center>\n";
?>
</body>
</html>
