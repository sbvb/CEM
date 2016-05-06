<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Register test</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>
<div align="center">
  <h1>Error while uploading file. </h1>
  <p>&nbsp;</p>
<?php
$error = (int) $_REQUEST['error'];

function file_upload_error_message($error_code) {
    switch ($error_code) { 
        case UPLOAD_ERR_INI_SIZE: 
            return 'The uploaded file exceeds the upload_max_filesize directive in php.ini'; 
        case UPLOAD_ERR_FORM_SIZE: 
            return 'The uploaded file exceeds the MAX_FILE_SIZE directive that was specified in the HTML form'; 
        case UPLOAD_ERR_PARTIAL: 
            return 'The uploaded file was only partially uploaded'; 
        case UPLOAD_ERR_NO_FILE: 
            return 'No file was uploaded'; 
        case UPLOAD_ERR_NO_TMP_DIR: 
            return 'Missing a temporary folder'; 
        case UPLOAD_ERR_CANT_WRITE: 
            return 'Failed to write file to disk'; 
        case UPLOAD_ERR_EXTENSION: 
            return 'File upload stopped by extension'; 
		case 30: // Not image file
			return 'File is not image'; 
		case 31: // Can't add image to database
			return 'Can\'t add file to database'; 
		case 32: // Can't add image to registry
			return 'Can\'t add file to registry'; 
		case 33: // Can't add image to registry
			return 'Can\'t upload file to webservices'; 
		case 34: // file not .tex
			return 'File must be a LATEX template(.tex)'; 
		case 35: // Can't add template to database
			return 'Can\'t add template to database, try to choose another name'; 
		case 36: // file exists
			return 'File already exists';
		case 37: // file not allowed
			return 'File type not allowed';
        default: 
            return 'Unknown upload error'; 
    } 
} 

$error_message = file_upload_error_message($error);
  echo "<p>$error_message</p>";
?>
 
 <script>
 setTimeout("window.close();",1000*3);
 </script>
 
</div>

</body>
</html>
