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
//$fct		= $_REQUEST['fc'];
$signature	= 'man_event.php';
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

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

//getting ambiance list
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
$ambLine = "";
for($i = 0; $i < $cnt ; $i++) {
	$xmlNode = $xmlElem->item($i)->nodeValue;
	
	// Goes to doua_error.php because session expired.
	if ($xmlNode == "session_expired")
		echo "<script>back_page('doua_error.php')</script>";
	
	$line = explode(':',$xmlNode);
			
	if ( $i > 0) {
		if ($ambLine === "") {
			//echo "empty<br>";
			$ambLine = "<option value=\"$line[1]\">$line[0]</option>";
		} 
		else {
			//echo "not empty<br>";
			$ambLine .= "\n<option value=\"$line[1]\">$line[0]</option>";
		}
	}
}

//getting ambiance and date list
$url = "$host/axis2/services/ws_ems/get_ambiance_and_date_list?username=$username&cookie=$cookie_value&ip=$ip&coid=$courseId&clid=$classId";
//echo "url = $url<br>";
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_ambiance_and_date_list");
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
	
	if ( $xmlNode !== "false") {
		$ambDateLst[] = $xmlNode;
	}
}
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
<script type="text/javascript">
<!--
	function SetAllAmbiances(me) {
		//alert(me.selectedIndex);
		var ambIndex = me.selectedIndex;
		var ambLstCnt = document.frmDates.lstAmbiance.length;			
		for(var i = 0; i < ambLstCnt; i++) {
			document.frmDates.lstAmbiance[i].selectedIndex = ambIndex;
		}
			
	}
	
	function SelectAmbiance(ambid,dtid) {
		var elCnt = document.frmDates.hdDateId.length;
		
		if (elCnt == undefined) { // only one element			
			if (document.frmDates.hdDateId.value == dtid) {
				for(var i = 0; i < document.frmDates.lstAmbiance.options.length; i++) {
					if (document.frmDates.lstAmbiance.options[i].value == ambid) {
						document.frmDates.lstAmbiance.selectedIndex = i;
						break;
					}
				}
			}
								
		}
		else { // more than one elements
		
			for(var i = 0; i < elCnt; i++) {				
				if (document.frmDates.hdDateId[i].value == dtid) {											
					for(var j = 0; j < document.frmDates.lstAmbiance[i].options.length; j++) {							
						if (document.frmDates.lstAmbiance[i].options[j].value == ambid) {
							document.frmDates.lstAmbiance[i].selectedIndex = j;								
							break;								
						}
					}
				}
			}
			
		}			
	}
-->
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
<?php
	echo "<h1 align=\"center\">$assocEventTitle \"$programName\",\"$courseName\",\"$className\",\"$scheduleName\"</h1>\n";
			
	echo "<form name=\"frmDates\" id=\"frmDates\">\n";
	  echo "<table width=\"400\" border=\"1\" align=\"center\">\n";
			$url = "$host/axis2/services/ws_ems/get_schedule_settings?username=$username&cookie=$cookie_value&ip=$ip&schid=$scheduleId";
			//echo "url = $url<br>";
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_ems.get_schedule_settings");
			$allLines = "";
			while ($currline = fread($myfile,1024))
				$allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");
			// parse XML
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
		
			$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
			$cnt = $xmlElem->length;			
			
			if ($cnt > 1) {			
				echo "<tr border=\"0\">\n";
					echo "<td colspan=\"2\" align=\"right\">$setAllAmbMsg:</td>\n";
					echo "<td><select name=\"lstAllAmb\" onchange=\"SetAllAmbiances(this)\">\n";
						echo $ambLine;
					echo "</select></td>\n";
				echo "</tr>";
				echo "<tr><td colspan=\"3\"></td></tr>";				
			}
	  						
			echo "<tr>\n";
				echo "<th>$datetimeCap[0]</th>\n";
				echo "<th>$timeCap</th>\n";
				echo "<th>$ambMsg</th>\n";	  
			echo "</tr>\n";
			
			for($i = 0; $i < $cnt ; $i++) {
				$xmlNode = $xmlElem->item($i)->nodeValue;
				
				// Goes to doua_error.php because session expired.
				if ($xmlNode == "session_expired")
					echo "<script>back_page('doua_error.php')</script>";
				
				$line = explode(';',$xmlNode);
				
				echo "<tr>\n";					
					echo "<td>\n";							
						//echo "<a href=\"fill_event.php?user=$username&clname=$className&clid=$classId&coname=$courseName&coid=$courseId&date=$line[2]&time=$line[3]-$line[4]&dateid=$line[5]\">$line[2]</a>";																			
						echo "$line[2]";
						echo "<input type=\"hidden\" name=\"hdDateId\" value=\"$line[5]\">";																		
					echo "</td>\n";
					echo "<td>\n";
						echo "$line[3]-$line[4]";																			
					echo "</td>\n";			
					echo "<td><select name=\"lstAmbiance\">\n";
						echo $ambLine;
					echo "</select></td>\n";		
				echo "</tr>\n";								
			}
		echo "<tr>\n";
			echo "<td colspan=\"3\" align=\"right\"><input type=\"button\" name=\"btnAssociate\" value=\"$associateBtnMsg\" onclick=\"associate_events()\"></td>";
		echo "</tr>\n";
		echo "</table>\n";
		echo  "<input type=\"hidden\" name=\"hdClassId\" value=\"$classId\">\n";
		echo  "<input type=\"hidden\" name=\"hdCourseId\" value=\"$courseId\">\n";
		echo  "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
	echo "</form>\n";

	echo "</center>\n";
	
	
	for($i = 0; $i < count($ambDateLst); $i++) {
		$line = explode(':',$ambDateLst[$i]);
		echo "<script>SelectAmbiance('$line[0]','$line[1]');</script>";
	}
?>
</h4>
  <h4>      <!-- Fim Conteúdo -->
  </h4></td>
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