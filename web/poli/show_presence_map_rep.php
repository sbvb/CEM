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
$signature	= 'presence_map_report.php';
$class		= $_REQUEST['lstClass'];
$course		= $_REQUEST['lstCourse'];
$schedule	= $_REQUEST['lstSchedule'];

$classLst = explode(":",$class);
$className	= $classLst[0];
$classId	= $classLst[1];

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
	echo "<h1 align=\"center\">$presenceReportTitle \"$courseName\",\"$className\",\"$scheduleName\"</h1>\n";
	
	//getting ccdts from schedule
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
		$ccdtList[] = "$line[1]:$line[0]";
		
		//echo "$line[1]:$line[0]";
		
	}  

	//getting presences per ccdtid
	$cntCcdtList = count($ccdtList);
	for($k = 0; $k < $cntCcdtList; $k++) {
	  	//Getting the presence report from webservices
		$line = explode(':',$ccdtList[$k]);
		$url = "$host/axis2/services/ws_ems/show_presence_report?username=$username&cookie=$cookie_value&ip=$ip&coid=$courseId&clid=$classId&ccdtid=$line[1]";
		//echo "url = $url<br>";
		$myfile = fopen($url, "r")
				or die("died when doing OPEN ws_ems.show_presence_report");
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
			
			$map[$k][] = "$line[0]:$line[1]";
			
			//echo "$line[0]:$line[1] <br>";
		}
	}
	
	//table goes here
	echo "<table width=\"400\" border=\"1\" align=\"center\">\n";
		echo "<tr>\n";
		  echo "<th style=\"background-color:cyan\"></th>\n";
		  $cntCcdtLst = count($ccdtList);
		  for($k = 0; $k < $cntCcdtLst; $k++) {
		  	$line = explode(":", $ccdtList[$k]);
			  echo "<th style=\"background-color:cyan\">$line[0]</th>\n";	
		  }		  
		echo "</tr>\n";
		
		//filling students and respective presence
		$cntStudents = count($map[0]);
		for($k = 1; $k < $cntStudents; $k++) {
			if ($k % 2 === 0)
				echo "<tr style=\"background-color:yellow;height:80\">";
			else
				echo "<tr style=\"height:80\">";
			for($l = 0; $l < $cntCcdtLst; $l++) {
				$line = explode(":", $map[$l][$k]);
				if ($l === 0) {
					echo "<td>$line[0]</td>";
				}
				
				if ($line[1] === "1") //present
					echo "<td align=\"center\"><div style=\"color:green\">P</div></td>";
				else
					echo "<td align=\"center\"><div style=\"color:red\">F</div></td>";
			}
			echo "</tr>";
		} 
			
	echo "</table>"; 
	//END map table

?>
</div>
    <!-- Fim Conteúdo -->
</h4>  
  </td>
</tr>
</tbody></table>
<!-- Fim tabela menu lateral e conteúdo -->
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