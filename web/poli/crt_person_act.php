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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>UFRJ-POLI-sbVB-adm</title>
<link href="img/estilopoli.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style12 {color: #FF0000}
.style13 {color: #99CCFF}
.style14 {color: #666666}
.style18 {color: #808080}
.style19 {
	color: #717db0;
	font-weight: bold;
}
.style21 {color: #FFFFFF}
.style22 {color: #717db0}
.style9 {font-size: 11px}
.style24 {
	font-size: 10px;
	color: #FF0000;
}
-->
</style>
<script type="text/javascript" src="js/ajaxlib.js"></script>
<script type="text/javascript" src="js/scripts.js" charset="utf-8"></script>
</head>
<?php
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
?>
<body>
<!-- Início Tabela Centraliza -->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tbody><tr>
<td align="center" valign="top">
<!-- Início Tabela Conteúdos --> 
<table width="900" border="0" cellpadding="0" cellspacing="0">
<!-- Tabela Conteúdos - Linha 1 -->
<tbody><tr>
<td bgcolor="#FFFFFF"> 
<!-- Tabela Título/menutopo -->
<table width="900" border="0" cellpadding="0" cellspacing="0">
<tbody><tr>
<td width="170" height="108" rowspan="2" valign="top" bgcolor="#eeeeee"><a href="http://www.poli.ufrj.br/index.php">
<img src="img/menutopo1.jpg" width="170" height="108" border="0" alt="" /></a></td>
<td valign="top" width="730" height="85"><img src="img/menutopo2.jpg" width="730" height="85" alt="" /></td>
</tr>
<tr>
<td width="730"  bgcolor="#eeeeee" align="right" valign="top">
<!-- Início Menu Topo -->
<div class="pd_menu_01 "> 
<h2 align="center" class="txstyle3">Software para Cloud Computing e Dispositivos M&oacute;veis - Android B&aacute;sico </h2>
<?php
	echo "<p>Olá $actor $username</p>\n";
?>

<?php
	// BEGIN menu from xml
	if (file_exists("menu/$xmlFilename")) {
		$xml = simplexml_load_file("menu/$xmlFilename");
		
		$titleCnt = count($xml->title);			
		if ($titleCnt > 0) {			
			for($i = 0; $i < $titleCnt ; $i++) {
				$titleName = gettext($xml->title[$i][name]);
				$link = "#";
				
				if (isset($xml->title[$i]->link)) {
					$link = $xml->title[$i]->link . "?user=$username";
					echo "<ul>\n";					
						echo "<li><a href=\"$link\" title=\"$titleName\">$titleName</a>\n";
					echo "</ul>\n";
				} else {
					echo "<ul>\n";
						echo "<li><a href=\"$link\" title=\"$titleName\">$titleName&nbsp;<img src=\"./img/seta.gif\" border=\"0\" alt=\"\">&nbsp;</a>\n";
							echo "<ul>\n";
				} 
				
				if (isset($xml->title[$i]->item)) {	
					$itemCount = count($xml->title[$i]->item);
					for($j = 0; $j < $itemCount; $j++) {			
						$itemName = gettext($xml->title[$i]->item[$j][name]);
						$itemLink = $xml->title[$i]->item[$j]->link . "?user=$username";
										
						echo "<li><a href=\"$itemLink\" title=\"$itemName\">$itemName</a></li>\n";							
					} // END for
					echo "</ul></li></ul>";	
				} // if (isset($xml->title[$i]->item))
			}
		} // END if ($titleCnt > 0)			
	}	
	// END menu from xml
?>

</div>
<!-- Fim Menu topo -->
</td>
</tr>
</tbody></table>
<!-- Fim Tabela Título/menutopo -->
</td>
</tr>
<!-- Tabela Conteúdos - Linha 2 -->
<tr>
<td bgcolor="#FFFFFF" valign="top"> 
<!-- Início tabela menu lateral e conteúdo -->
<div align="center"></div>
<table width="700" border="0" cellpadding="0" cellspacing="0">
<tbody><tr bgcolor="#FFFFFF"> 
<td width="15" >&nbsp;</td>
<td class="txstyle1" valign="top"><!-- Início Conteúdo -->
<?php
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
		echo '<p><div align="center" id="edt_err_msg" style="color:#FF0000"></div></p>';
		echo '<form action="" method="get" name="frmEdit" id="frmEdit">';
			echo '<table width="400" align="center" style="background-color:yellow">';								
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
					echo "<td></td>";
					echo "<td align=\"right\"><input name=\"btnBack\" type=\"button\" value=\"$backBtnMsg\" onClick=\"back_page('man_person.php?user=$username&fc=0')\"></td>";
					echo "<td align=\"right\" colspan=\"2\"><input name=\"btnSave\" type=\"button\" value=\"$saveBtnMsg\" onClick=\"edit_person_user()\"></td>";
				echo "</tr>\n";
			echo "</table>\n";			
			echo '<input type="hidden" name="hdEdtUsr" value="' . $edt_usr . '">';
			echo '<input type="hidden" name="hdOwner" value="' . $username . '">';
		echo "</form>\n";
	}
	else
	{		
		echo "<h1 align=\"center\">$crtPersonActMsg</h1>";
		echo '<p><div align="center" id="edt_err_msg" style="color:#FF0000"></div></p>';
		echo '<form action="" method="get" name="frmCreate" id="frmCreate">';
			echo '<table width="400" align="center" style="background-color:yellow">';
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
	}
?>

    <!-- Fim Conteúdo -->
</h4>  
  </td>
</tr>
</tbody></table>
<!-- Fim tabela menu lateral e conteúdo -->
<div align="center"></div>
<p>&nbsp;</p></td>
</tr>



<!-- Tabela Conteúdos - Linha 3 -->

<!-- Tabela Conteúdos - Linha 4 -->
<tr>
<td bgcolor="#eeeeee" align="left" class="txstyle0">
<table><tr class="txstyle0">
<td><img src="img/logoufrj.jpg" width="53" height="63"></td>
<td>
&nbsp; &nbsp; &nbsp;Universidade Federal do Rio de Janeiro - Escola Politécnica<br>
&nbsp; &nbsp; &nbsp;Av. Athos da Silveira Ramos, 149, CT - Bloco A, 2º andar - Cidade Universitária - Rio de Janeiro - RJ - Brasil <br>
&nbsp; &nbsp; &nbsp;CEP: 21941-909 - Caixa Postal 68529 - Telefone: + 55 - 21 - 2562-7010 - Fax: + 55 - 21 - 2562-7718  
</td>

</tr></table>
</td>
</tr>

</tbody></table>
<!-- Fim Tabela Conteúdos --> 

</td>
</tr>
</tbody></table>
<!-- Fim Tabela Centraliza --> 

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script><script src="js/ga.js" type="text/javascript"></script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-6685945-1");
pageTracker._trackPageview();
} catch(err) {}</script>


</body></html>