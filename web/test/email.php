<?php
$destinatario = "heineck@gmail.com";
$assunto = "Esta mensagem � um teste";
$corpo = '
<html>
<head>
   <title>Teste de correio</title>
</head>
<body>
<h1>Ol� amigos!</h1>
<p>
<b>Bem-vindos ao meu correio electr�nico de teste</b>. Estou contente de ter tantos leitores.
</p>
</body>
</html>
'; 

//para o envio em formato HTML
$headers = "MIME-Version: 1.0";
$headers .= "Content-type: text/html;charset=iso-8859-1";

//endere�o do remitente
$headers .= "From: Xux� <xuxe@criarweb.com>";

//endere�o de resposta, se queremos que seja diferente a do remitente
$headers .= "Reply-To: mariano@desarrolloweb.com";

//endere�os que receber�o uma copia $headers .= "Cc: manel@desarrolloweb.com"; 
//endere�os que receber�o uma copia oculta
$headers .= "Bcc: heineck@gmail.com,heineck@gmail.com";
mail($destinatario,$assunto,$corpo,$headers);
?>