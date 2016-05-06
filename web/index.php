<?php
header("Content-type: text/html; charset=utf-8");

$lang = $_GET['lang'];

bindtextdomain("lang", "./locale");
textdomain("lang");

if (isset($lang)) {
	if ($lang == "en") {
		setlocale(LC_ALL, "en_US.utf-8");
		setcookie("ems_lang","en");
	}
	else if ($lang == "br") {
		setlocale(LC_ALL, "pt_BR.utf-8");
		setcookie("ems_lang","br");
	}
} 
else {
	if (isset($_COOKIE['ems_lang'])) {
		if ($_COOKIE['ems_lang'] == "br")
			setlocale(LC_ALL, "pt_BR.utf-8");
		else if ($_COOKIE['ems_lang'] == "en")
			setlocale(LC_ALL, "en_US.utf-8");
	}
}
	
include("constants.php");
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="img/styles.css" type="text/css">
<script type="text/javascript" src="js/ajaxlib.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>
<script type="text/javascript" src="js/tests.js"></script>

</head>

<body>
<div align="center"><a href="index.php?lang=en"><img src="img/eua.jpg" width="44" height="30" border="0"></a>
    <a href="index.php?lang=br"><img src="img/br.jpg" width="44" height="30" border="0"></a>
 <?php
 	echo "<div align=\"center\">[ <a href=\"new_candidate.php\">$newCandidateMsg</a> ] [ <a href=\"help.php\" target=\"_blank\">$helpMsg</a> ]</div>";
	echo "<h1 align=\"center\">$ems_title</h1>\n";	
?>
</div>
<div style="color:#FF0000" id="loginErrMsg" align="center"></div>
  <form name="loginFrm" method="get" action="">
  <div align="center"></div>
  <div align="center">
  <table width="200">
    <tr>
  <?php
    echo "<td><div align=\"right\">$loginMsg</div></td>\n";
?>
      <td><input name="txtLogin" type="text" id="txtLogin"></td>
    </tr>
    <tr>
  <?php
    echo "<td><div align=\"right\">$pwdMsg</div></td>\n";
?>
      <td><input name="txtPass" type="password" id="txtPass"></td>
    </tr>
    <tr>
    <td></td>
    <td><div align="center">
       <!-- <input name="btnLogin" type="button" value="Login" onClick="check_login()"> -->
  <?php
	 echo "<input name=\"btnLogin\" type=\"button\" value=\"$loginBtnMsg\" onClick=\"check_login()\">\n";
?>
      </div></td>
    </tr>
  </table>
  <!--
  </form>  
  <form name="form1" method="get" action="">
    <input type="button" name="button" value="test" onclick="test_button()">
  </form>
  <form name="form2" method="get" action="http://www.google.com" target="_blank">
    <input type="submit" name="submit" value="test2">
  </form>
  <p>&nbsp;</p>
-->
  </div>
  </div> 
</body>
</html>
