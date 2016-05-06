<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Select result</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="img/styles.css" type="text/css">
</head>

<body>
<?php
$query = $_REQUEST['txt_query'];
echo '<p align="center"><h1>' . $query . '</h1></p>';
// Connecting, selecting database
$link = mysql_connect('localhost', 'sbvbcomb_doua', 'doua')
    or die('Could not connect: ' . mysql_error());
echo 'Connected successfully';
mysql_select_db('sbvbcomb_doua') or die('Could not select database');

// Performing SQL query
$username = 'Camila';
$lnk      = 'man_rep_a.php';
$query = 'select `Session`.`sessionId` as `sessionId`, `Global`.`sessionExpTime` as `expTime` from Session inner join User on
		`Session`.`UsrId` = `User`.`UserId` inner join Global where name = "' . $username . '"';
$result = mysql_query($query) or die('Query failed: ' . mysql_error());

// Printing results in HTML
echo "<table border=1>\n";
while ($line = mysql_fetch_array($result, MYSQL_ASSOC)) {
    echo "\t<tr>\n";
    foreach ($line as $col_value) {
        echo "\t\t<td>$col_value</td>\n";
    }
    echo "\t</tr>\n";
}
echo "</table>\n";

// Free resultset
mysql_free_result($result);

// Closing connection
mysql_close($link);
?>


</body>
</html>
