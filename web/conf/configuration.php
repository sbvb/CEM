<?php
//configuration.php
	class Configuration {
		private $m_host 	= "";
		private $m_author 	= "";
		
		//Constructor
		function __construct() {
		  	
			$myfile = fopen("http://selectos.net/ems/conf/g_conf.xml", "r")
					or die("died when doing OPEN");
			while ($currline = fread($myfile,1024))
				   $allLines .= $currline; // concat currline to allLines
			fclose($myfile) or die("died when doing CLOSE");
			
			$xmlDoc = new DOMDocument();
			$xmlDoc->loadXML($allLines);
			
			$host	= $xmlDoc->getElementsByTagName("host");
			$author	= $xmlDoc->getElementsByTagName("author");
			
			//echo "debug: " . $host->item(0)->nodeValue . "<br>"; 
			//echo "debug: " . $author->item(0)->nodeValue . "<br>"; 
			
			$this->m_host	= $host->item(0)->nodeValue;
			$this->m_author	= $author->item(0)->nodeValue;
	  
			
   		}
		
		function get_host() {
			return $this->m_host;
		}
		
		function get_author() {
			return $this->m_author;
		}
	}
?>