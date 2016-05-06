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
$signature	= 'man_interested.php';

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$url = "$host/axis2/services/ws_ems/get_candidate_settings?username=$username&cookie=$cookie_value&ip=$ip";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_candidate_settings");
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
$regSettings = explode(":", $xmlNode);

$url = "$host/axis2/services/ws_ems/get_candidate_program?username=$username&cookie=$cookie_value&ip=$ip";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_candidate_program");
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
$interestedProgram = explode(":", $xmlNode);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="img/styles.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="menu/css3menu/style.css" type="text/css">
<script src="js/ajaxlib.js" type="text/javascript"></script>
<script src="js/scripts.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.maskedinput-1.2.2.js"></script>
<style type="text/css">
<!--
.style1 {color: #FF0000}
-->
</style>
</head>

<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
	echo "<body>\n";
	echo "<div align=\"center\">\n";
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
	
	//echo "<p>&nbsp;</p>";
	echo "<p><h2>$helloMsg $username</h2></p>\n";
	echo "<h1>$editInterestedTitle</h1>\n";
?>  
  <!-- <div align="center">b</div> -->
  <div align="center" class="style1" id="edt_err_msg"></div>
  <form action="" method="post" name="frmEdit" id="frmEdit">
    <table width="200">
      <tr>
	  	<td width="59" colspan="2">&nbsp;</td>
      </tr>
              
<?php
/*
	  echo "<tr>";
		echo "<td><div align=\"right\">$usernameRepMsg:</div></td>";
        echo "<td colspan=\"2\"><input name=\"txtName\" type=\"text\" id=\"txtName\" value=\"$regSettings[0]\"></td>";
	  echo "</tr>";
*/
?>
 	  <tr>        
<?php
		echo "<td><div align=\"right\">$fullnameMsg</div></td>";
        echo "<td colspan=\"2\"><input name=\"txtFullname\" type=\"text\" id=\"txtFullname\" value=\"$regSettings[1]\"></td>";
?>
      </tr>
      <tr>
        <td><div align="right">email:</div></td>
<?php
        echo "<td colspan=\"2\"><input name=\"txtEmail\" type=\"email\" id=\"txtEmail\" value=\"$regSettings[3]\"></td>";
?>
      </tr>
	  <tr>        
<?php
		echo "<td><div align=\"right\">$telMsg</div></td>";
		
		if ($regSettings[4] === "-")
        	echo "<td colspan=\"2\"><input name=\"txtTelephone\" type=\"text\" id=\"mask_tel\"></td>";
		else
			echo "<td colspan=\"2\"><input name=\"txtTelephone\" type=\"text\" id=\"mask_tel\" value=\"$regSettings[4]\"></td>";
?>
      </tr>
	  <tr>	  	
<?php
	echo "<td><div align=\"right\">$selProgramMsg:</div></td>";
	echo "<td colspan=\"2\"><select name=\"lstProgram\" id=\"lstProgram\">";
		
	$url = "$host/axis2/services/ws_ems/get_programs_list?username=$username&cookie=$cookie_value&ip=$ip";
	//echo $url;
	$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.get_programs_list");
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
		
		if ($i > 0) {
			$line = explode(":", $xmlNode);
			
			if ($interestedProgram[1] === $line[1])
				echo "<option value=\"$line[1]\" selected>$line[0]</option>";
			else
				echo "<option value=\"$line[1]\">$line[0]</option>";
		}
	}
		
	echo "</select></td>";
?>	  
	  </tr>
	  <tr>        
<?php
		echo "<td><div align=\"right\">$obsMsg</div></td>";
		
		if ($regSettings[5] === "null")
			echo "<td colspan=\"2\"><textarea name=\"txtObs\" cols=\"40\" rows=\"5\" maxlength=\"200\"></textarea></td>\n";
		else
			echo "<td colspan=\"2\"><textarea name=\"txtObs\" cols=\"40\" rows=\"5\" maxlength=\"200\">$regSettings[5]</textarea></td>\n";
?>
      </tr>
      <tr>
	  	<td><div align="center">
<?php
        	echo "<input name=\"btnBack\" type=\"button\" id=\"btnBack\" value=\"$backBtnMsg\" onClick=\"back_page('man_interested.php?user=$username')\">";
?>
      	</div></td>
        <td><div align="center">
<?php
          	echo "<input type=\"reset\" name=\"Reset\" value=\"$clearBtnMsg\">";
?>			
        </div></td>
      	<td><div align="center">
<?php		
        	echo "<input name=\"btnSave\" type=\"button\" id=\"btnSave\" value=\"$saveBtnMsg\" onClick=\"edit_interested_act()\">";
?>			
      	</div></td>
      </tr>
    </table>
<?php
	echo "<input name=\"hdOwner\" type=\"hidden\" id=\"hdOwner\" value=\"$username\">";
?>
  </form>
  <p>&nbsp;  </p>
</div>
</body>
</html>
