<?php
$destinatario = "heineck@gmail.com";
$assunto = "Esta mensagem é um teste";
$corpo = '
<html>
<head>
   <title>Teste de correio</title>
</head>
<body>
<h1>Olá amigos!</h1>
<p>
<b>Bem-vindos ao meu correio electrónico de teste</b>. Estou contente de ter tantos leitores.
</p>
</body>
</html>
'; 

//para o envio em formato HTML
$headers = "MIME-Version: 1.0";
$headers .= "Content-type: text/html;charset=iso-8859-1";

//endereço do remitente
$headers .= "From: Xuxé <xuxe@criarweb.com>";

//endereço de resposta, se queremos que seja diferente a do remitente
$headers .= "Reply-To: mariano@desarrolloweb.com";

//endereços que receberão uma copia $headers .= "Cc: manel@desarrolloweb.com"; 
//endereços que receberão uma copia oculta
$headers .= "Bcc: heineck@gmail.com,heineck@gmail.com";
mail($destinatario,$assunto,$corpo,$headers);
?>