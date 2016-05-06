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
}

include("constants.php");

$username	= $_REQUEST['user'];
$fct		= $_REQUEST['fc'];
$signature	= 'man_datetime.php';

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
	
	<!-- <link href="img/styles.css" rel="stylesheet" type="text/css"> -->
	<link rel="stylesheet" href="menu/css3menu/style.css" type="text/css">
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
	echo "<body onLoad=\"disableAllElements()\">\n";
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
	
	echo "<p><h2>$helloMsg $username</h2></p>\n";
	
	echo "<h1>$crtDatetimesActMsg</h1>";
	echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';
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
	
</center>		

</body>
</html>
