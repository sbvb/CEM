<?php
header("Content-type: text/html; charset=utf-8");

$lang = $_GET['lang'];

bindtextdomain("lang", "./locale");
textdomain("lang");

setlocale(LC_ALL, "pt_BR.utf-8");
setcookie("ems_lang","br");

	
include("constants.php");
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<?php
	echo "<title>$ems_title</title>\n";
?>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
<script type="text/javascript" src="js/tests.js"></script>

</head>

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
<td class="txstyle1" valign="top">  
<div>
 <div>
   <h4>[ <a href="index.php">home</a> ] [ <a href="help.php" target="_blank">ajuda</a> ]</h4>
   <p>Entre aqui com o email e a senha que voc&ecirc; recebeu para acessar o sistema.</p>
 </div>
</div>
<div style="color:#FF0000" id="loginErrMsg" align="center"></div>
  <form name="loginFrm" method="get" action="">
  <div align="center"></div>
  <div align="center">
  <table width="200" border="0" bordercolor="#FFFF00" bgcolor="#FFFF00">
    <tr bordercolor="#FFFF00" bgcolor="#FFFF00">
	  <td><div align="right">login:</div></td>
      <td><div align="center">
        <input name="txtLogin" type="text" id="txtLogin">
      </div></td>
    </tr>
    <tr bordercolor="#FFFF00" bgcolor="#FFFF00">
  	  <td><div align="right">senha:</div></td>
      <td><div align="center">
        <input name="txtPass" type="password" id="txtPass">
      </div></td>
    </tr>
    <tr bordercolor="#FFFF00" bgcolor="#FFFF00">
    <td colspan="2"><div align="right">
         <!-- <input name="btnLogin" type="button" value="Login" onClick="check_login()"> -->
  		  <input name="btnLogin" type="button" value="logar" onClick="check_login()">
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
    </div>
  </form>  <h2 align="center">&nbsp; </h2>
  <h2>Contato:</h2>
  Secretaria: 2562-8203 (2&ordf; a 6&ordf; 8h &agrave;s 16h), 2178-9222 (2&ordf; a 5&ordf; a partir das 18h)<br>
Coordenador: <a href="http://www.sbvb.com.br">Sergio Barbosa Villas-Boas</a>, Ph.D. <br>
Contato com coordenador: email: sbvb@poli.ufrj.br.<p>
  <!-- Início Conteúdo -->
    <!-- Fim Conteúdo -->
</p>  </td>
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


</body>
</html>
