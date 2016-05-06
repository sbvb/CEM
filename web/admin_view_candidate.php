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
$pid		= $_REQUEST['pid'];
$signature	= 'admin_man_candidate.php';

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$url = "$host/axis2/services/ws_ems/get_information_from_candidate?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_information_from_candidate");
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
if ($xmlNode !== "false")
	$regSettings = explode(":", $xmlNode);
	
$url = "$host/axis2/services/ws_ems/get_additional_information_from_candidate?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_additional_information_from_candidate");
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
if ($xmlNode !== "false")
	$regInformation = explode(":", $xmlNode);
	
$url = "$host/axis2/services/ws_ems/get_program_from_person?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_program_from_person");
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
if ($xmlNode !== "false")
	$personProgram = explode(":", $xmlNode);

//getting the profile photo
$photoProperties = "false";
$url = "$host/axis2/services/ws_ems/get_photo_from_candidate?username=$username&cookie=$cookie_value&ip=$ip&pid=$pid";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_photo_from_candidate");
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
$photoProperties = explode(":", $xmlNode);
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

<script language="javascript">
function open_upload(username) {
	window.open('upload_photo.php?user=' + username,'uploadWnd',"status=0,toolbar=0,location=0,menubar=0,directories=0,resizable=0,scrollbars=0,height=200,width=400");		
}
</script>

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
	echo "<h1>$viewCandidateTitle</h1>\n";
?>  
  <!-- <div align="center">b</div> -->
  <form action="" method="post" name="frmCreate" id="frmCreate">
    <table width="200" border="0">
      <tr>
<?php
	if ($photoProperties[0] === "false")
	  	echo "<td width=\"125\"><img src=\"img/no_image_male.gif\" width=\"125\" height=\"125\"></td>";
	else { // load the image from user
		echo "<td width=\"125\"><img src=\"admin_photo.php?user=$username&pid=$pid&fid=$photoProperties[1]\" width=\"125\" height=\"125\"></td>";		
	}
	
?>
		<td width="59">&nbsp;</td>
      </tr>
      <tr>        
<?php
		echo "<td><div align=\"right\">$usernameRepMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regSettings[0]</div></td>";
?>
      </tr>
      <tr>        
<?php
		echo "<td><div align=\"right\">$fullnameMsg</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regSettings[1]</div></td>";
?>
      </tr>
      <tr>
        <td><div align="right">email:</div></td>
<?php
        echo "<td colspan=\"2\"><div align=\"center\">$regSettings[2]</div></td>";
?>
      </tr>
      <tr>        
<?php
		echo "<td><div align=\"right\">$telMsg</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regSettings[3]</div></td>";
?>
      </tr> 
	  <tr>        
<?php
		echo "<td><div align=\"right\">$addressMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[0]</div></td>";
?>
      </tr> 
	  <tr>        
<?php
		echo "<td><div align=\"right\">$districtMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[1]</div></td>";
?>
      </tr>      
	  <tr>        
<?php
		echo "<td><div align=\"right\">$cityMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[2]</div></td>";
?>
      </tr> 
	  <tr>        
<?php
		echo "<td><div align=\"right\">$stateMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[3]</div></td>";
?>
      </tr>
	  <tr>        
<?php
		echo "<td><div align=\"right\">$zipCodeMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[4]</div></td>";
?>
      </tr> 
	  <tr>        
<?php
		echo "<td><div align=\"right\">$civilStatusMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[5]</div></td>";
?>
      </tr> 
	  <tr>        
<?php
		echo "<td><div align=\"right\">$careerMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[6]</div></td>";
?>
      </tr> 
	  <tr>        
<?php
		echo "<td><div align=\"right\">$identityMsg:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[7] $regInformation[8]</div></td>";
?>
      </tr>
	  <tr>        
<?php
		echo "<td><div align=\"right\">cpf:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regInformation[9]</div></td>";
?>
      </tr>
	  <tr>        
<?php
		echo "<td><div align=\"right\">$programCap[0]:</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$personProgram[0]</div></td>";
?>
      </tr>
      <tr>        
<?php
		echo "<td><div align=\"right\">Obs:</div></td>";
		
		if ($regSettings[5] === "null")
			echo "<td colspan=\"2\"><textarea name=\"txtObs\" cols=\"40\" rows=\"5\" maxlength=\"200\" readonly></textarea></td>\n";
		else
			echo "<td colspan=\"2\"><textarea name=\"txtObs\" cols=\"40\" rows=\"5\" maxlength=\"200\" readonly>$regSettings[8]</textarea></td>\n";
?>
      </tr>
      <tr>
        <td><div align="center">
          <!-- <input type="reset" name="Reset" value="Reset"> -->
        </div></td>
        <td><div align="center">
          <!-- <input type="reset" name="Reset" value="Reset"> -->
        </div></td>        
      <td><div align="center">
<?php       
        echo "<input name=\"btnBack\" type=\"button\" id=\"btnBack\" value=\"$backBtnMsg\" onClick=\"back_page('admin_man_candidate.php?user=$username')\">";
?>		
      </div></td>
      </tr>
    </table>    
  </form>
  <p>&nbsp;  </p>
</div>
</body>
</html>
