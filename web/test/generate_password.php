<?php
$passLength = $_REQUEST['cnt'];

function generatePassword($length=6) {
	$vowels = "aeiouAEIOU";
	$consonants = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
	$special_chars = "!@#$¨*()";
	$numbers = "0123456789";
	$chars = $vowels . $consonants . $special_chars . $numbers;
	 
	return substr(str_shuffle($chars), 0, $length);
}

echo generatePassword($passLength) . "<br>";
echo generatePassword($passLength) . "<br>";
echo generatePassword($passLength) . "<br>";
echo generatePassword($passLength) . "<br>";
echo generatePassword($passLength) . "<br>";
?>
