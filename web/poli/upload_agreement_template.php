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

$username	= $_REQUEST['user'];
$signature	= 'man_agreement_template.php';

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$fileDir = "tmp/";
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>

<?php
	echo "<script type=\"text/javascript\">check_validity('$username','$signature')</script>";
?>
<body>
<form action="upload_agr_tmp_process.php" method="post" enctype="multipart/form-data" name="frmUpload" id="frmUpload">
<?php  
  echo "<p align=\"center\">$uploadAgreementTemplateTitle</p>";
?>
  <div align="center">
    <table width="304" border="1">
      <tr>
<?php      
        echo "<td width=\"70\"><div align=\"right\">$uploadAgreementTemplateCap: </div></td>";
?>		
        <td width="218"><div align="center">
          <input name="txtDescription" type="text" id="txtDescription">
        </div></td>
      </tr>
      <tr>
<?php
        echo "<td><div align=\"right\">template:</div></td>";
?>		
        <td><div align="center">
          <input name="txtFile" type="file" id="txtFile">
        </div></td>
      </tr>
      <!--
      <tr>
        <td><div align="right">permission:</div></td>
        <td><div align="center">
          <select name="lstPermission" id="lstPermission">
              <option value="0" selected>public</option>
              <option value="1">private</option>
          </select>
        </div></td>
      </tr>
      -->
      <tr>
        <td colspan="2"><div align="center">
          <input name="btnUpload" type="submit" id="btnUpload" value="Upload">
        </div></td>
      </tr>
      </table>
  </div>
<?php  
  echo "<input name=\"user\" type=\"hidden\" id=\"user\" value=\"$username\">";
?>
</form>
</body>
</html>
