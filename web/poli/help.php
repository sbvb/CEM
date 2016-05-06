<?php
$username	= $_GET['user'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

$actor = "";
$url = "$host/axis2/services/ws_ems/get_person_type_from_user?username=$username&cookie=$cookie_value&ip=$ip";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_person_type_from_user");
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

	$line = explode(":", $xmlNode);
	
	
	$actor = $line[0];
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
<h2 align="center" class="txstyle3">Ajuda</h2>
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

<?php if (($actor !== "false") && ($actor !== "session_expired")) { ?>
<h4>[ <a href="doua_menu.php?user=<?php echo $username; ?>">home</a> ] <!-- Início Conteúdo -->
</h4>
<?php } else { ?>
<h4>[ <a href="index_cem.php">home</a> ] <!-- Início Conteúdo -->
</h4>
<?php } ?>

<?php 
if ($actor !== "Candidate") {
?>
  <ul>
    <li>
      <h4>login como root do cloudeducationmanagement:</h4>
    <ul>
        <li>
          <h4>root cria escola.                  </h4>
        </li>
      <li>
          <h4>root cria administrador associado a escola.</h4>
      </li>
    </ul>
    </li>
  <li>
      <h4>login como administrador associado a escola:</h4>
  <ul>
        <li>
          <h4>admin cria curso(program).                                </h4>
        </li>
      <li>
          <h4>admin cria disciplinas(course).</h4>
      </li>
  <li>
        <h4> admin associa curso(program) com disciplinas(course).</h4>
  </li>
  <li>
    <h4>admin cria turma(class).                                </h4>
  </li>
  <li>
    <h4>admin cria tutores.                                </h4>
  </li>
  <li>
    <h4>admin cria ambientes.                                </h4>
  </li>
  <li>
    <h4>admin cria schedules(com uma lista de datetimes), associado a tutor e um ambiente para cada datetime.                                </h4>
  </li>
  <li>
    <h4>admin gerencia cada turma_disciplina(class_course).                                </h4>
  </li>
  <li>
    <h4>admin associa ambiente ao datetime.                                </h4>
  </li>
  <li>
    <h4>admin cria estudantes.                                </h4>
  </li>
  <li>
    <h4>admin matricula estudantes na turma.</h4>
  </li>
  </ul>
  </li>
  <li>
    <h4>login como administrador associado a escola:</h4>
  <ul>
      <li>
        <h4>tutor realiza chamada em cada datetime.                                </h4>
      </li>
    <li>
        <h4>tutor vê relatório de chamadas.                                </h4>
    </li>
  <li>
      <h4>tutor preenche notas.                                </h4>
  </li>
  <li>
    <h4>tutor vê relatórios de notas.
        <!-- Fim Conteúdo -->
                                                                                                                                </h4>
  </li>
  </ul>
  </li>
  </ul>
  <p>
    <?php 
} else {
?>
  </p>
  <p>  A sua situação foi mudada de "interessado" para "candidato"
    </p>
  <ul>
    <li>no Menu selecione Gerenciamento->gerenciar candidato
      clicar em Editar, e preencher os campos</li>
    <li>Clicar em "adicionar foto", clique em cima da foto e faça
      upload de uma foto sua (acrescente descrição se quiser).
      </li>
    <li>Se desejar, mude a senha.
      </li>
    <li>Clique em "download de contrato", assine-o e entregue-o para na
      secretaria.
    </li>
  </ul>  <?php
}
?>
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


