<?php
$username	= $_REQUEST['user'];
//$fct		= $_REQUEST['fc'];
$signature	= 'associate_ambiance_ccdt.php';
$className	= $_REQUEST['clname'];
$classId	= $_REQUEST['clid'];
$courseName	= $_REQUEST['coname'];
$courseId	= $_REQUEST['coid'];
$date		= $_REQUEST['date'];
$time		= $_REQUEST['time'];
$ccdtId		= $_REQUEST['ccdtid'];

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
	
	echo "<h1>Associate ambiance to datetime</h1><br>\n";
	echo "<h3>(Course \"$courseName\", Class \"$className\")</h3>\n";
	echo "<h3>(Date \"$date\", Time \"$time\")</h3>\n";
		
	echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
		echo '<form action="" method="get" name="frmAssociate" id="frmCreate">';
			echo '<table width="291" align="center">';				
				echo "<tr>\n";
					echo '<td><div align="right">ambiance: </div></td>';
					echo '<td><select name="lstAmbiance" type="lstAmbiance">';

						//Getting the ambiances list from webservices
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
						for($i = 0; $i < $cnt ; $i++) {
							$xmlNode = $xmlElem->item($i)->nodeValue;
							
							// Goes to doua_error.php because session expired.
							if ($xmlNode == "session_expired")
								echo "<script>back_page('doua_error.php')</script>";
							
							$line = explode(':',$xmlNode);
							if ($i > 0) {
								echo "<option value=\"$line[1]\">$line[0]</option>";
							}						
						}
						
					echo "</select></dt>";
				echo "</tr>\n";					
				echo "<tr>\n";
					echo '<td><input name="btnBack" type="button" value="Back" onClick="back_page(\'associate_ambiance_ccdt.php?user=' . $username . '\')"></td>';
					echo '<td><input name="btnOk" type="button" value="OK" onClick="associate_ambiance_ccdt()"></td>';				
				echo "</tr>\n";
			echo "</table>\n";			
			echo "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
			//echo "<input type=\"hidden\" name=\"hdClassId\" value=\"$classId\">\n";
			//echo "<input type=\"hidden\" name=\"hdCourseId\" value=\"$courseId\">\n";
			echo "<input type=\"hidden\" name=\"hdCcdtId\" value=\"$ccdtId\">\n";
		echo "</form>\n";	
	echo "</center>\n";
?>
</body>
</html>
