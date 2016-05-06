<?php
$username	= $_REQUEST['user'];
$program	= $_REQUEST['lstProgram'];
$course		= $_REQUEST['lstCourse'];
$class		= $_REQUEST['lstClass'];
$signature	= 'man_grade.php';

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$programLst 	= explode(":",$program);
$programName	= $programLst[0];
$programId		= $programLst[1];

$courseLst 	= explode(":",$course);
$courseName	= $courseLst[0];
$courseId	= $courseLst[1];

$classLst 	= explode(":",$class);
$className	= $classLst[0];
$classId	= $classLst[1];


?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>EMS - Associate student to grade</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="img/styles.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="js/ajaxlib.js"></script>
	<script type="text/javascript" src="js/scripts.js"></script>

<script language="JavaScript" type="text/JavaScript">
<!--	
function addToSel1(){
	var sel1 = document.getElementById('lstSel1');
	var sel2 = document.getElementById('lstSel2');
	
	var seltext = sel2.options[sel2.selectedIndex].text;
	var selvalue = sel2.options[sel2.selectedIndex].value;
	
	addOption(sel1,selvalue,seltext);
	removeOption(sel2,sel2.selectedIndex);
	sortList(sel1);
}
function addToSel2(){  
	var sel1 = document.getElementById('lstSel1');
	var sel2 = document.getElementById('lstSel2');
	
	var seltext = sel1.options[sel1.selectedIndex].text;
	var selvalue = sel1.options[sel1.selectedIndex].value;
	
	addOption(sel2,selvalue,seltext);
	removeOption(sel1,sel1.selectedIndex);
	sortList(sel2);
    
}

function addOption(selec, val, tex) {
	selec = typeof(selec) == "string" ? document.getElementById(selec) : selec;
	var opt = document.createElement('option');
	opt.value = val;
	opt.text = tex;
	
	try {
		selec.add(opt, null); 
	} // NS/FF
	catch(e) { 
		selec.add(opt); 
	} // IE
}

function removeOption(selec, ind) {
	ind = /^\d+$/.test(ind) ? Number(ind) : false;
		if(typeof ind != 'number') return;
	var opts = [];
	for(var i = 0; i < selec.options.length; i++) {
		if(i != ind) opts.push({"val": selec.options[i].value, "tex": selec.options[i].innerHTML});
	}
	selec.innerHTML = "";
	for(var i = 0; i < opts.length; i++)
		addOption(selec, opts[i].val, opts[i].tex);
}

function sortList(selec) {
	//var lb = document.getElementById('mylist');
	var lb = selec;
	arrTexts = new Array();

	for(i=0; i<lb.length; i++)  {
		arrTexts[i] = lb.options[i].text + ":" + lb.options[i].value;
	}

	arrTexts.sort();

	for(i=0; i<lb.length; i++)  {
		var line = arrTexts[i].split(":");		
		lb.options[i].text = line[0];
		lb.options[i].value = line[1];		
	}
}
//-->
</script>
	
</head>
<?php 
	echo '<script type="text/javascript">check_validity(\'' . $username . '\',\'' . $signature . '\')</script>';
	echo "<body>\n";
	echo "<center>\n";
	echo '<p>[ <a href="doua_menu.php?user=' . $username . '">home</a> ]</p>';
	echo '<p><h2>Hello, ' . $username . "</h2></p>\n";
	
	echo "<h1>Associate student to grade</h1>\n";
	echo "<h2>(\"$programName\",\"$courseName\",\"$className\")</h2>\n";
	
	echo '<p><div id="edt_err_msg" style="color:#FF0000"></div></p>';	
	echo "<form name=\"frmAssociate\" id=\"frmAssociate\">\n";
	  echo "<table width=\"400\" border=\"1\">\n";
		echo "<tr>\n";
		  echo "<th width=\"200\">is associated</td>\n";
		  echo "<th width=\"50\"></td>\n";
		  echo "<th width=\"200\">is not associated</td>\n";
		echo "</tr>\n";
		echo "<tr>\n";
			echo "<td><select name=\"lstSel1\" id=\"lstSel1\" size=\"10\">\n";
				
				//Getting the students list from webservices
				$url = "$host/axis2/services/ws_ems/get_students_associated_grade?username=$username&cookie=$cookie_value&ip=$ip&coid=$courseId&clid=$classId";
				//echo $url;
				$myfile = fopen($url, "r")
						or die("died when doing OPEN ws_doua_ems.get_students_associated_grade");
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
				
			echo "</select></td>\n";
			echo "<td><p>\n";
			echo "<input name=\"btnToSel2\" type=\"button\" id=\"btnToSel2\" value=\"&gt;&gt;\" onClick=\"addToSel2()\">\n";
			echo "</p>\n";
			echo "<p>\n";
			echo "<input name=\"btnToSel1\" type=\"button\" id=\"btnToSel1\" value=\"&lt;&lt;\" onClick=\"addToSel1()\">\n";
			echo "</p></td>\n";
			echo "<td><select name=\"lstSel2\" id=\"lstSel2\" size=\"10\">\n";
			
				//Getting the students list from webservices
				$url = "$host/axis2/services/ws_ems/get_students_not_associated_grade?username=$username&cookie=$cookie_value&ip=$ip&coid=$courseId&clid=$classId";
				//echo $url;
				$myfile = fopen($url, "r")
						or die("died when doing OPEN ws_doua_ems.get_students_not_associated_grade");
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
			
			echo "</select></td>\n";
			echo "</tr>\n";
		echo "<tr>\n";
		echo "<td colspan=\"2\">\n";
		  echo "<input name=\"btnRefresh\" type=\"button\" id=\"btnRefresh\" value=\"Refresh\" onclick=\"refresh_page()\">\n";
		  echo "</td>\n";
		  echo "<td colspan=\"1\">\n";
		  echo "<input name=\"btnAssociate\" type=\"button\" id=\"btnAssociate\" value=\"Associate\" onclick=\"associate_student_to_grade()\">\n";
		  echo "</td>\n";
		echo "</tr>\n";
	  echo "</table>\n";
	  echo  "<input type=\"hidden\" name=\"hdOwner\" value=\"$username\">\n";
	  echo  "<input type=\"hidden\" name=\"hdCoid\" value=\"$courseId\">\n";
	  echo  "<input type=\"hidden\" name=\"hdClid\" value=\"$classId\">\n";
	echo "</form>\n";

	echo "</center>\n";
?>
</body>
</html>
