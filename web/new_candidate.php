<?php
bindtextdomain("lang", "./locale");
textdomain("lang");

if (isset($_COOKIE['ems_lang'])) {
	if ($_COOKIE['ems_lang'] == "br")
		setlocale(LC_ALL, "pt_BR.utf-8");
	else if ($_COOKIE['ems_lang'] == "en")
		setlocale(LC_ALL, "en_US.utf-8");
}

include("constants.php");

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$schoolName = "sbvb_ufrj";
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php 
	echo "<title>$ems_title</title>";
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="img/styles.css" type="text/css">
<script type="text/javascript" src="js/ajaxlib.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>
</head>

<body>
<div align="center"> [ <a href="index.php">home</a> ]
<?php
  echo "<h1>$newCandidateTitle</h1>";
?>
  <p>&nbsp;</p>
  <div id="edt_err_msg" style="color:#FF0000"></div>
  <form action="" method="get" name="frmCreate" id="frmCreate">
    <table width="350" border="0">
      <tr>
<?php	  
        echo "<td><div align=\"right\">email:</div></td>";
?>		
        <td colspan="2"><div align="center">
          <input name="txtEmail" type="text" id="txtEmail">
        </div></td>
      </tr>
      <tr>
<?php	  
        echo "<td><div align=\"right\">$telMsg:</div></td>";
?>		
        <td colspan="2"><div align="center">
          <input name="txtTelephone" type="text" id="txtTelephone">
        </div></td>
      </tr>      
      <tr>
        <td colspan="2"><div align="center">
<?php
          echo "<input name=\"btnClear\" type=\"reset\" id=\"btnClear\" value=\"$clearBtnMsg\">";
		  echo "<input name=\"hdSchool\" type=\"hidden\" value=\"$schoolName\">";
?>		  
        </div></td>
      <td width="61"><div align="center">
        <input name="btnOk" type="button" id="btnOk" value="Ok" onClick="create_candidate()">
      </div></td>
      </tr>
    </table>	
  </form>
  <p>&nbsp;  </p>
</div>
</body>
</html>
