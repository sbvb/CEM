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
$signature	= 'man_datetime.php';

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

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
<link rel="stylesheet" media="screen" type="text/css" href="img/datepicker.css" />	
<link rel="stylesheet" media="all" type="text/css" href="img/jquery-ui-1.8.6.custom.css" />

<style type="text/css"> 
	#ui-datepicker-div{ font-size: 80%; }
	.ui-timepicker-div .ui-widget-header{ margin-bottom: 8px; }
	.ui-timepicker-div dl{ text-align: left; }
	.ui-timepicker-div dl dt{ height: 25px; }
	.ui-timepicker-div dl dd{ margin: -25px 0 10px 65px; }
	.ui-timepicker-div td { font-size: 90%; }
</style>
<script type="text/javascript" src="js/ajaxlib.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
	<!-- <script type="text/javascript" src="js/jquery.maskedinput-1.2.2.js"></script> -->
	<script type="text/javascript" src="js/datepicker.js"></script>
	<script type="text/javascript" src="js/eye.js"></script>
	<script type="text/javascript" src="js/utils.js"></script>
	<script type="text/javascript" src="js/layout.js?ver=1.0.2"></script>
	
	<script type="text/javascript" src="js/jquery-ui-1.8.6.custom.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
	
	<script type="text/javascript">
		$(function(){
			$('#tbeginSun').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendSun').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#tbeginMon').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendMon').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#tbeginTue').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendTue').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#tbeginWed').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendWed').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#tbeginThu').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendThu').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#tbeginFri').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendFri').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#tbeginSat').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});		
		$(function(){
			$('#tendSat').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		$(function(){
			$('#timebegin').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		$(function(){
			$('#timeend').timepicker({
				hourGrid: 4,
				minuteGrid: 10
			});
		});
		
		function toogleTimesEnable(me) {
			if (me.checked == true) {
				document.frmDatetime.txttbegin[me.value - 1].disabled=false;
				document.frmDatetime.txttend[me.value - 1].disabled=false;
				//alert('checked');
			}
			else {
				document.frmDatetime.txttbegin[me.value - 1].disabled=true;
				document.frmDatetime.txttend[me.value - 1].disabled=true;
			}
				
		}
		
		function disableAllElements() {
			var nElements = document.frmDatetime.txttbegin.length;
			
			for(var i = 0; i < nElements; i++) {
				document.frmDatetime.txttbegin[i].disabled = true;
				document.frmDatetime.txttend[i].disabled = true;
			}
		}
	</script>
	
	<STYLE TYPE="text/css">
	<!--
	body {
	background-color: #CCFF33;
	}
	
	.specialtable{
	border:none;
	border-collapse:collapse;
	background:#FFFFFF;
	text-align:center;
	}
	.specialtable td{
		padding: 10px;
		background:#FFFFFF;
	}
	.specialtable a{
		color:#003333;
		text-decoration:underline;
	}
	-->
	</STYLE>
</head>
<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
?>
<body onLoad="disableAllElements()">
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
	echo "<h1 align=\"center\">$crtDatetimesActMsg</h1>";
	echo '<p><div id="edt_err_msg" align="center" style="color:#FF0000"></div></p>';
?>

<form action="" method="post" name="frmDatetime" id="frmDatetime">
  <table width="520" align="center" border="1" CLASS="specialtable">
    <tr>
	<?php
      echo "<th><div align=\"center\">$frmDatetimeCap[0] </div></th>";
      echo "<th><div align=\"center\">$datetimeCap[1] </div></th>";
      echo "<th><div align=\"center\">$datetimeCap[2] </div></th>";
	?>
    </tr>
    <tr>
      <td width="200">
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="1">
		<?php
        echo "$frmDatetimeCap[1]</div></td>";
		?>
      <td width="160">        <div align="center">
        <input name="txttbegin" type="text" id="tbeginSun" value="08:00" readonly="readonly">      
      </div></td>
      <td width="160">        <div align="center">
        <input name="txttend" type="text" id="tendSun"  value="10:00" readonly="readonly">      
      </div></td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="2">  
      	<?php
        echo "$frmDatetimeCap[2]</div></td>";
		?>
      <td><div align="center">
        <input name="txttbegin" type="text" id="tbeginMon" value="08:00" readonly="readonly">
      </div></td>
      <td><div align="center">
        <input name="txttend" type="text" id="tendMon" value="10:00" readonly="readonly">
      </div></td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="3">
      	<?php
        echo "$frmDatetimeCap[3]</div></td>";
		?>
      <td><div align="center">
        <input name="txttbegin" type="text" id="tbeginTue" value="08:00" readonly="readonly">
      </div></td>
      <td><div align="center">
        <input name="txttend" type="text" id="tendTue" value="10:00" readonly="readonly">
      </div></td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="4">
      	<?php
        	echo "$frmDatetimeCap[4]</div></td>";
		?>
      <td><div align="center">
        <input name="txttbegin" type="text" id="tbeginWed" value="08:00" readonly="readonly">
      </div></td>
      <td><div align="center">
        <input name="txttend" type="text" id="tendWed" value="10:00" readonly="readonly">
      </div></td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="5">
      	<?php
        echo "$frmDatetimeCap[5]</div></td>";
		?>
      <td><div align="center">
        <input name="txttbegin" type="text" id="tbeginThu" value="08:00" readonly="readonly">
      </div></td>
      <td><div align="center">
        <input name="txttend" type="text" id="tendThu" value="10:00" readonly="readonly">
      </div></td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="6">
      	<?php
        echo "$frmDatetimeCap[6]</div></td>";
		?>
      <td><div align="center">
        <input name="txttbegin" type="text" id="tbeginFri" value="08:00" readonly="readonly">
      </div></td>
      <td><div align="center">
        <input name="txttend" type="text" id="tendFri" value="10:00" readonly="readonly">
      </div></td>
    </tr>
    <tr>
      <td>
        <div align="left">
          <input name="chkBxDay" type="checkbox" id="chkBxDay" onclick="toogleTimesEnable(this)" value="7">
      	<?php
        echo "$frmDatetimeCap[7]</div></td>";
		?>
      <td><div align="center">
        <input name="txttbegin" type="text" id="tbeginSat" value="08:00" readonly="readonly">
      </div></td>
      <td><div align="center">
        <input name="txttend" type="text" id="tendSat" value="10:00" readonly="readonly">
      </div></td>
    </tr>
  </table>
</form>
<br>
<form action="" method="post" name="frmCreate" id="frmCreate">
  <table width="271" align="center" CLASS="specialtable">
    <tr>
	  <?php
      echo "<td><div align=\"right\">$frmDatetimeCap[8]: </div></td>";
      echo "<td><div align=\"center\">";
        
	  	$date = date("Y/m/d");
		echo "<input name=\"txtDtBegin\" type=\"text\" class=\"dateBegin\" id=\"dateBegin\" value=\"$date\" readonly=\"readonly\">\n";
	  ?>
      </div></td>
    </tr>
    <tr>
	  <?php
      echo "<td><div align=\"right\">$frmDatetimeCap[9]: </div></td>";
      echo "<td><div align=\"center\">";

	  	$date = date("Y/m/d", strtotime($date . " +1 day"));
		echo "<input name=\"txtDtEnd\" type=\"text\" class=\"dateEnd\" id=\"dateEnd\" value=\"$date\" readonly=\"readonly\">\n";
	  ?>
      </div></td>
    </tr>
    <tr>
      <td width="100"><div align="right">schedule:</div></td>
      <td width="171"><div align="center">
        <select name="lstSchedule" id="lstSchedule">
        
		<?php
			//Getting the schedules list from webservices
			$url = "$host/axis2/services/ws_ems/get_schedules_list?username=$username&cookie=$cookie_value&ip=$ip";
			//echo $url;
			$myfile = fopen($url, "r")
					or die("died when doing OPEN ws_doua_ems.get_schedules_list");
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
					$lst = explode(":",$xmlNode);								
					echo "<option value=\"$lst[1]\">$lst[0]</option>\n";
				}
			}
		?>
		
        </select>
      </div></td>
    </tr>
    <tr>
      <td><div align="center">
	  <?php
	  	echo "<input name=\"btnBack\" type=\"button\" id=\"btnBack\" value=\"$backBtnMsg\" onClick=\"back_page('man_datetime.php?user=$username&fc=0')\">";
	  ?>
	  </div></td>
      <td><div align="center">
        <input name="btnOk" type="button" id="btnOk" value="OK" onClick="create_datetimes_list()">
      </div></td>
    </tr>
  </table>
  <?php
  	echo "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
  ?>
</form>
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