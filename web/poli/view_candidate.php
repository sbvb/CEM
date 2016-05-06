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
$uid		= $_REQUEST['uid'];
$signature	= 'man_candidate.php';

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

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
if ($xmlNode !== "false")
	$regSettings = explode(":", $xmlNode);

$url = "$host/axis2/services/ws_ems/get_candidate_information?username=$username&cookie=$cookie_value&ip=$ip";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_candidate_information");
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
	
$url = "$host/axis2/services/ws_ems/get_program_from_person?username=$username&cookie=$cookie_value&ip=$ip&pid=$uid";
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
$url = "$host/axis2/services/ws_ems/get_photo_from_user?username=$username&cookie=$cookie_value&ip=$ip";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_photo_from_user");
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
<script type="text/javascript" src="js/scripts.js"></script>
<script language="javascript">
function open_upload(username) {
	window.open('upload_photo.php?user=' + username,'uploadWnd',"status=0,toolbar=0,location=0,menubar=0,directories=0,resizable=0,scrollbars=0,height=200,width=400");		
}
</script>
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
<table width="700" border="0" cellpadding="0" cellspacing="0">
<tbody><tr bgcolor="#FFFFFF"> 
<td width="15" >&nbsp;</td>
<td class="txstyle1" valign="top"><h4><!-- Início Conteúdo -->
<div align="center">
<?php
	echo "<h1>$viewCandidateTitle</h1>\n";
?>  
  <!-- <div align="center">b</div> -->
  <form action="" method="post" name="frmCreate" id="frmCreate">
    <table width="200" border="0" style="background-color:yellow">
      <tr>
<?php
	if ($photoProperties[0] === "false")
	  	echo "<td width=\"125\"><img src=\"img/no_image_male.gif\" width=\"125\" height=\"125\"></td>";
	else { // load the image from user
		echo "<td width=\"125\"><img src=\"photo.php?user=$username&fid=$photoProperties[1]\" width=\"125\" height=\"125\"></td>";		
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
        echo "<td colspan=\"2\"><div align=\"center\">$regSettings[3]</div></td>";
?>
      </tr>
      <tr>        
<?php
		echo "<td><div align=\"right\">$telMsg</div></td>";
        echo "<td colspan=\"2\"><div align=\"center\">$regSettings[4]</div></td>";
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
			echo "<td colspan=\"2\"><textarea name=\"txtObs\" cols=\"40\" rows=\"5\" maxlength=\"200\" readonly>$regSettings[5]</textarea></td>\n";
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
        echo "<input name=\"btnBack\" type=\"button\" id=\"btnBack\" value=\"$backBtnMsg\" onClick=\"back_page('man_candidate.php?user=$username')\">";
?>		
      </div></td>

      </tr>
    </table>    
  </form>
  <p>&nbsp;  </p>
    <!-- Fim Conteúdo -->
</h4>  
  </td>
</tr>
</tbody></table>
<!-- Fim tabela menu lateral e conteúdo -->
</td>
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