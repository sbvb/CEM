<?php
$username = $_REQUEST['user'];
$description	= $_POST['txtDescription'];
$permission		= $_POST['lstPermission'];
$fieldname		= 'txtFile';
$uploadDirectory = "tmp/";

$newDescription = str_replace(" ", "%20", $description);

$cookie_name	= "session_id";
$cookie_value 	= "";
if (isset($_COOKIE[$cookie_name]))
	$cookie_value = $_COOKIE[$cookie_name];
		
$ip 			= $_SERVER['REMOTE_ADDR'];

include("conf/configuration.php");
$conf = new Configuration();
$host = $conf->get_host();

($_FILES[$fieldname]['error'] == 0)
	or header("location: upload_error.php?error=" . $_FILES[$fieldname]['error']);

@is_uploaded_file($_FILES[$fieldname]['tmp_name']) 
	or header("location: upload_error.php?error=" . $_FILES[$fieldname]['error']);

@getimagesize($_FILES[$fieldname]['tmp_name']) 
	or header("location: upload_error.php?error=" . $_FILES[$fieldname]['error']);

$tmp_name = $_FILES[$fieldname]['tmp_name'];
$filename = $_FILES[$fieldname]['name'];

$file_format = explode("/", $_FILES[$fieldname]['type']);
$filenameLst = explode(".", $filename);
$found = false;
$url = "$host/axis2/services/ws_ems/get_file_format_list?username=$username&cookie=$cookie_value&ip=$ip";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.get_file_format_list");
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
	$formatLst = explode(":", $xmlNode);
			
	if ($formatLst[0] === $file_format[1]) {//found extension
		$found = true;
		$format_id = $formatLst[1];
		break;
	}
}

($found === true) 
	or header("location: upload_error.php?error=30");		
	
/*if ($permission === "0")
	$permStr = "public";
else if ($permission === "1")
	$permStr = "private";
*/
	
$url = "$host/axis2/services/ws_ems/create_new_file?username=$username&cookie=$cookie_value&ip=$ip&description=$newDescription&permission=$permission&id_formats=$format_id";
//echo $url;
$myfile = fopen($url, "r")
		or die("died when doing OPEN ws_ems.create_new_file");
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
}

$createdLst = explode(":", $xmlNode);

($createdLst[0] === "true")
	or header("location: upload_error.php?error=31");		

$uploadFilename = $uploadDirectory . "" . $permission . "/" . $createdLst[1] . "." . $formatLst[2]; 
@move_uploaded_file($_FILES[$fieldname]['tmp_name'], $uploadFilename)
	or header("location: upload_error.php?error=" . $_FILES[$fieldname]['error']);
	
//uploading file to webservices
$webservFilename = $permission . "/" . $createdLst[1] . "." . $formatLst[2];
$fp = fopen($uploadFilename, "r");
if (!$fp) {
    header("location: upload_error.php?error=31");
} else {
	while (!feof($fp)) {  
        $fileData = fread($fp, 4096);
	
		
		$newFileData = base64_encode($fileData);
		//$data_md5 = md5($newFileData);
		
        $url = "$host/axis2/services/ws_ems/upload_file?filename=$webservFilename&fdata=$newFileData";
		//echo "url = $url<br>";
		$myfile = fopen($url, "r")
			or die("died when doing OPEN ws_ems.upload_file");
		$allLines = "";
		while ($currline = fread($myfile,1024))
			$allLines .= $currline; // concat currline to allLines
		fclose($myfile) or die("died when doing CLOSE");
		// parse XML
		$xmlDoc = new DOMDocument();
		$xmlDoc->loadXML($allLines);

		$xmlElem = $xmlDoc->getElementsByTagName("return"); // not "ns:return"
		$cnt = $xmlElem->length;
		//echo "cnt = $cnt<br>";
		//echo "<table border=\"1\">\n";
		for($i = 0; $i < $cnt ; $i++) {
			$xmlNode = $xmlElem->item($i)->nodeValue;
		}
		
		//if ($xmlNode === "false") {
		//	fclose($fp);
		//	header("location: upload_error.php?error=33");
		//}
    }
    fclose($fp);
    //echo $content;
}
//END uploading file to webservices

//delete file from tmp
unlink($uploadFilename);

header("location: upload_success.php?filename=" . $_FILES[$fieldname]['name'] . "&uid=$uid");

?>