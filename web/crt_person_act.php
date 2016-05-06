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
$fct		= $_REQUEST['fc'];
$signature	= 'man_person.php';

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
			
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$fct_edit	= 'edt';
$fct_create	= 'crt';
if ($fct == $fct_edit)
	$edt_usr = $_REQUEST['edusr'];
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
		
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<link href="menu/css3menu/style.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery.maskedinput-1.2.2.js"></script>
	
	<script type="text/javascript">
		jQuery(function($){
		   $("#mask_tel").mask("(99)9999-9999");
		});
	</script>

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
	
	echo "<p>&nbsp;</p>";	
	echo "<p><h2>$helloMsg $username</h2></p>\n";

	if ($fct == $fct_edit)
	{
		$url = "$host/axis2/services/ws_ems/get_user_settings?username=$username&cookie=$cookie_value&ip=$ip&nickname=$edt_usr";
		//echo $url;
		$myfile = fopen($url, "r")
				or die("died when doing OPEN ws_doua_ems.get_user_settings");
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
		$user_settings = $xmlNode; 
		
		$fullname	= "";
		$email		= "";
		$telephone	= "";
		$sc_name	= "";
		$scid		= 0;
		$pt_name	= "";
		$ptid		= 0;
		$obs		= "";
		$pid		= "";
		
		if ($user_settings != "") {
			$lst = explode(":", $user_settings);
			//echo "user_settings: $user_settings<br>";
			$fullname	= $lst[0];
			$email		= $lst[1];
			$telephone	= $lst[2];
			$sc_name	= $lst[3];
			$scid		= (int) $lst[4];
			$pt_name	= $lst[5];
			$ptid		= (int) $lst[6];
			$obs		= $lst[7];
			$pid		= $lst[8];
			
			//echo "fullname: $fullname<br>";
			//echo "email: $email<br>";
			//echo "telephone: $telephone<br>";
			//echo "sc_name: $sc_name<br>";
			//echo "scid: $scid<br>";
			//echo "pt_name: $pt_name<br>";
			//echo "ptid: $ptid<br>";
			//echo "obs: $obs<br>";
		}
		
		$url = "$host/axis2/services/ws_ems/get_additional_information_from_candidate?username=$username&cookie=$cookie_value&ip=$ip&nickname=$edt_usr&pid=$pid";
		//echo $url;
		$myfile = fopen($url, "r")
				or die("died when doing OPEN ws_doua_ems.get_additional_information_from_candidate");
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
			$userInformation = explode(":", $xmlNode);
	
		echo "<h1>$edtPersonActMsg</h1>\n";
		echo "<p>&nbsp; </p>\n";
		echo "<p>$curUserMsg $edt_usr</p>\n";
		echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
		echo '<form action="" method="get" name="frmEdit" id="frmEdit">';
			echo '<table width="391" align="center">';								
				echo "<tr>\n";
					echo "<td><div align=\"right\">$fullnameMsg</div></td>";
					echo "<td colspan=\"2\"><input name=\"txtFullname\" type=\"text\" id=\"txtFullname\" value=\"$fullname\"></td>\n";
				echo "</tr>\n";
				echo "<tr>\n";
					echo '<td><div align="right">email: </div></td>';
					echo "<td colspan=\"2\"><input name=\"txtEmail\" type=\"text\" id=\"txtEmail\" value=\"$email\"></td>\n";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$telMsg</div></td>";
					echo "<td colspan=\"2\"><input name=\"txtTelephone\" type=\"text\" id=\"txtTelephone\" value=\"$telephone\"></td>\n";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$actorMsg</div></td>";
					echo '<td colspan="2"><select name="lstPersonType">';
					
					$url = "$host/axis2/services/ws_doua_ems/get_user_groups?username=$username&cookie=$cookie_value&ip=$ip";
					//echo $url;
					$myfile = fopen($url, "r")
							or die("died when doing OPEN ws_doua_ems.get_user_groups");
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
						
						$line = explode(":", $xmlNode);
						if ($line[0] == $ptid)
							echo "<option value=\"$line[0]\" SELECTED>$line[1]</option>\n";
						else
							echo "<option value=\"$line[0]\">$line[1]</option>\n";
					}
															
					echo '</select></td>';
				echo "</tr>\n";
				
				$url = "$host/axis2/services/ws_ems/get_school_list?username=$username&cookie=$cookie_value&ip=$ip";
				//echo $url;
				$myfile = fopen($url, "r")
						or die("died when doing OPEN ws_doua_ems.get_user_groups");
				$allLines = "";
				while ($currline = fread($myfile,1024))
					$allLines .= $currline; // concat currline to allLines
				fclose($myfile) or die("died when doing CLOSE");;
				// parse XML
				$xmlDoc = new DOMDocument();
				$xmlDoc->loadXML($allLines);
					
				$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
				$cnt = $xmlElem->length;
				
				if ($cnt > 2) {
					echo "<tr>\n";
						echo "<td><div align=\"right\">$schoolMsg</div></td>";
						echo '<td colspan="2"><select name="lstSchool">';
					
					for($i = 0; $i < $cnt ; $i++) {
						$xmlNode = $xmlElem->item($i)->nodeValue;
						
						// Goes to doua_error.php because session expired.
						if ($xmlNode == "session_expired")
							echo "<script>back_page('doua_error.php')</script>";
						
						$line = explode(":", $xmlNode);
						
						if ($i > 0) {
							if ($line[1] == $scid)
								echo "<option value=\"$line[1]\" SELECTED>$line[0]</option>\n";
							else
								echo "<option value=\"$line[1]\">$line[0]</option>\n";
						}
					}
																
						echo '</select></td>';
					echo "</tr>\n";
				}
				else if ($cnt == 2) {
					$xmlNode = $xmlElem->item(0)->nodeValue;
									
					// Goes to doua_error.php because session expired.
					if ($xmlNode == "session_expired")
						echo "<script>back_page('doua_error.php')</script>";
					
					$xmlNode = $xmlElem->item(1)->nodeValue;
					
					$line = explode(':',$xmlNode);
					
					echo "<input type=\"hidden\" name=\"lstSchool\" value=\"$line[1]\">\n";
				}
				
				echo "<tr>\n";
					echo "<td><div align=\"right\">$addressMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtAddress\" id=\"txtAddress\" value=\"$userInformation[0]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$districtMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtDistrict\" id=\"txtDistrict\" value=\"$userInformation[1]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$cityMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtCity\" id=\"txtCity\" value=\"$userInformation[2]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$stateMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtState\" id=\"txtState\" value=\"$userInformation[3]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$zipCodeMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtZipCode\" id=\"txtZipCode\" value=\"$userInformation[4]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$civilStatusMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtCivilStatus\" id=\"txtCivilStatus\" value=\"$userInformation[5]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$careerMsg:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtCareer\" id=\"txtCareer\" value=\"$userInformation[6]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$identityMsg:</div></td>";
					echo "<td><input type=\"text\" name=\"txtIdentity\" id=\"txtIdentity\" value=\"$userInformation[7]\"></td>";
					echo "<td>$issuedByMsg:<input type=\"text\" name=\"txtIssuedBy\" id=\"txtIssuedBy\" size=\"5\" value=\"$userInformation[8]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">cpf:</div></td>";
					echo "<td colspan=\"2\"><input type=\"text\" name=\"txtCpf\" id=\"txtCpf\" value=\"$userInformation[9]\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$obsMsg</div></td>";
					echo "<td colspan=\"2\"><textarea name=\"txtObs\" cols=\"40\" rows=\"5\" maxlength=\"200\">$obs</textarea></td>\n";
				echo "</tr>\n";				
				echo "<tr>\n";
					echo "<td><input name=\"btnBack\" type=\"button\" value=\"$backBtnMsg\" onClick=\"back_page('man_person.php?user=$username&fc=0')\"></td>";
					echo "<td><input name=\"btnSave\" type=\"button\" value=\"$saveBtnMsg\" onClick=\"edit_person_user()\"></td>";
				echo "</tr>\n";
			echo "</table>\n";			
			echo '<input type="hidden" name="hdEdtUsr" value="' . $edt_usr . '">';
			echo '<input type="hidden" name="hdOwner" value="' . $username . '">';
		echo "</form>\n";
	}
	else
	{		
		echo "<h1>$crtPersonActMsg</h1>";
		echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
		echo '<form action="" method="get" name="frmCreate" id="frmCreate">';
			echo '<table width="391" align="center">';
				echo "<tr>\n";
					echo '<td><div align="right">nickname: </div></td>';
					echo '<td colspan="2"><input name="txtNick" type="text" id="txtUser"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$fullnameMsg</div></td>";
					echo '<td colspan="2"><input name="txtFullname" type="text" id="txtFullname"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo '<td><div align="right">email: </div></td>';
					echo '<td colspan="2"><input name="txtEmail" type="text" id="txtEmail"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$telMsg</div></td>";
					echo '<td colspan="2"><input name="txtTelephone" type="text" id="txtTelephone"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$pwdMsg</div></td>";
					echo '<td colspan="2"><input name="txtPass" type="password" id="txtPass"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$pwdConfMsg</div></td>";
					echo '<td colspan="2"><input name="txtPassConf" type="password" id="txtPassConf"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$actorMsg</div></td>";
					echo '<td colspan="2"><select name="lstPersonType">';
					
					$url = "$host/axis2/services/ws_doua_ems/get_user_groups?username=$username&cookie=$cookie_value&ip=$ip";
					//echo $url;
					$myfile = fopen($url, "r")
							or die("died when doing OPEN ws_doua_ems.get_user_groups");
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
						
						$line = explode(":", $xmlNode);
						echo "<option value=\"$line[0]\">$line[1]</option>\n";
					}
															
					echo '</select></td>';
				echo "</tr>\n";
									
				$url = "$host/axis2/services/ws_ems/get_school_list?username=$username&cookie=$cookie_value&ip=$ip";
				//echo $url;
				$myfile = fopen($url, "r")
						or die("died when doing OPEN ws_doua_ems.get_user_groups");
				$allLines = "";
				while ($currline = fread($myfile,1024))
					$allLines .= $currline; // concat currline to allLines
				fclose($myfile) or die("died when doing CLOSE");;
				// parse XML
				$xmlDoc = new DOMDocument();
				$xmlDoc->loadXML($allLines);
					
				$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
				$cnt = $xmlElem->length;
				
				if ($cnt > 2) {
					echo "<tr>\n";
						echo "<td><div align=\"right\">$schoolMsg</div></td>";
						echo '<td colspan="2"><select name="lstSchool">';
					
					for($i = 0; $i < $cnt ; $i++) {
						$xmlNode = $xmlElem->item($i)->nodeValue;
						
						// Goes to doua_error.php because session expired.
						if ($xmlNode == "session_expired")
							echo "<script>back_page('doua_error.php')</script>";
						
						$line = explode(":", $xmlNode);
						
						if ($i > 0)
							echo "<option value=\"$line[1]\">$line[0]</option>\n";
					}
																
						echo '</select></td>';
					echo "</tr>\n";
				}
				else if ($cnt == 2) {
					$xmlNode = $xmlElem->item(0)->nodeValue;
									
					// Goes to doua_error.php because session expired.
					if ($xmlNode == "session_expired")
						echo "<script>back_page('doua_error.php')</script>";
					
					$xmlNode = $xmlElem->item(1)->nodeValue;
					
					$line = explode(':',$xmlNode);
					
					echo "<input type=\"hidden\" name=\"lstSchool\" value=\"$line[1]\">\n";
				}
				
				echo "<tr>\n";
					echo "<td><div align=\"right\">$addressMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtAddress" id="txtAddress"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$districtMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtDistrict" id="txtDistrict"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$cityMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtCity" id="txtCity"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$stateMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtState" id="txtState"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$zipCodeMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtZipCode" id="txtZipCode"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$civilStatusMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtCivilStatus" id="txtCivilStatus"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$careerMsg:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtCareer" id="txtCareer"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$identityMsg:</div></td>";
					echo '<td><input type="text" name="txtIdentity" id="txtIdentity"></td>';
					echo "<td>$issuedByMsg:<input type=\"text\" name=\"txtIssuedBy\" id=\"txtIssuedBy\" size=\"5\"></td>";
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">cpf:</div></td>";
					echo '<td colspan="2"><input type="text" name="txtCpf" id="txtCpf"></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><div align=\"right\">$obsMsg</div></td>";
					echo '<td colspan="2"><textarea name="txtObs" cols="40" rows="5" maxlength="200"></textarea></td>';
				echo "</tr>\n";
				echo "<tr>\n";
					echo "<td><input name=\"btnBack\" type=\"button\" id=\"btnBack\" value=\"$backBtnMsg\" onClick=\"back_page('man_person.php?user=$username&fc=0')\"></td>";
					echo "<td><input name=\"btnClear\" type=\"reset\" id=\"btnClear\" value=\"$clearBtnMsg\"></td>";
					echo "<td><input name=\"btnCreate\" type=\"button\" id=\"btnCreate\" value=\"$createBtnMsg\" onClick=\"create_person_user()\"></td>";
				echo "</tr>\n";
			echo "</table>\n";		
			echo "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";	
		echo "</form>\n";
		
		echo "</center>\n";	
	}
?>

</body>
</html>
