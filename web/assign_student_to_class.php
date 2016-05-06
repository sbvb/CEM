<?php
$username	= $_REQUEST['user'];
$signature	= 'assign_student_to_class.php';

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php 
	echo "<title>EMS - Assign student to class</title>";
?>
		
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

	echo "<h1>Assign student to class</h1>";
	echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
	echo '<form action="" method="get" name="frmCreate" id="frmCreate">';
	echo '<table width="353" align="center">';
	echo "<tr>\n";
		echo '<td><div align="right">Student: </div></td>';
		echo '<td colspan="2"><select name="lstStudent" id="lstStudent">';

			//Getting the courses list from webservices
			$url = "$host/axis2/services/ws_ems/get_student_list";
			//echo $url;
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_doua_ems.get_student_list");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");;
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
				
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;
			for($i = 1; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				$lst = explode(":",$xmlNode);
				echo "<option value=\"$lst[1]\">$lst[0]</option>\n";
			}
			
			echo "</select>\n";
		echo "</td>\n";
	echo "</tr>\n";
	echo "<tr>\n";
		echo '<td><div align="right">Class: </div></td>';
		echo '<td colspan="2"><select name="lstClass" id="lstClass">';
			
			//Getting the schedules list from webservices
			$url = "$host/axis2/services/ws_ems/get_classes_list";
			//echo $url;
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_doua_ems.get_classes_list");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");;
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
				
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;
			for($i = 1; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				$lst = explode(":",$xmlNode);
				echo "<option value=\"$lst[4]\">$lst[0]</option>\n";
			}
			
			//echo "<option selected=\"yes\">option 2</option>\n";
			echo "</select>\n";
		echo "</td>\n";
	echo "</tr>\n";	
	echo "<tr>\n";
		//echo '<td><input name="btnBack" type="button" id="btnBack" value="Back" onClick="back_page(\'man_class.php?user=' . $username . '&fc=0\')"></td>';
		//echo '<td><input name="btnClear" type="reset" id="btnClear" value="Clear"></td>';
		echo "<td></td>";
		echo "<td></td>";
		echo '<td align="right"><input name="btnCreate" type="button" id="btnCreate" value="Assign" onClick="assign_student_to_class()"></td>';
	echo "</tr>\n";
echo "</table>\n";
echo "</form>\n";

echo "</center>\n";	
	
?>

</body>
</html>
