package com.cloudeducationmanagement.CEMPresenceCall;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WebservicesConnection {
	private static String mHost = "http://www.cloudeducationmanagement.com:8081";
	
	/** Creates the md5 hash from str
    *
    * @param str. String to be hashed.
    * @return Resturns the md5 hash.
    */
   private static String md5(String str){
       String enc_str = "";
       MessageDigest md = null;
       try {
           md = MessageDigest.getInstance("MD5");
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       BigInteger hash = new BigInteger(1, md.digest(str.getBytes()));
       enc_str = hash.toString(16);
       return enc_str;
   }
	
	private static InputStream OpenHttpConnection(String urlString)
		    throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");
        }
        return in;
    }
	
	static public String Login(String login, String pass, String ip) {
		String pwd_hash = md5(pass);
		
		String url = mHost
                + "/axis2/services/ws_doua_ems/check_login?username=" + login
                + "&pwd_hash=" + pwd_hash
                + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "no_conection";
        
        try {
            // URL url = new URL(urlStr);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();        
            InputStream in = OpenHttpConnection(urlStr);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            // Assert.assertTrue(nodeList.getLength() == 1);
            // outStr = "length=" + new Integer(nodeList.getLength()).toString();
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return outStr;		
	}
	
	static public boolean isTutor(String login, String sessionId, String ip) {
		boolean ret = false;		
		
		String url = mHost
                + "/axis2/services/ws_doua_ems/get_resources?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            
            if (i == 0) {
            	String []lst = outStr.split(":");
            	if (lst[0].equals("Tutor"))
            		ret = true;
            }
            	
        }
        
        return ret;		
	}
	
	public static boolean Logout(String login, String sessionId, String ip) {
		boolean ret = false;		
		
		String url = mHost
                + "/axis2/services/ws_doua_ems/logout_session?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                        	
        }
        
        if (outStr.equals("true"))
        	ret = true;
        
        return ret;		
	}
	
	public static String [] GetCoursesFromTutor(String login, String sessionId, String ip) {
		String [] ret = null;		
		
		String url = mHost
                + "/axis2/services/ws_ems/get_courses_from_tutor?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        String line = "false";
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            
            if (outStr.equals("session_expired")) {
            	line = "session_expired";
            }
            else {
            	if (i > 0) {
	            	if (line.equals("false"))
	            		line = outStr;
	            	else
	            		line += "&" + outStr;
            	}
            }
        }
        
        ret = line.split("&");
        
        return ret;		
	}

	public static String [] GetClassesAssociatedCourse(String login, String sessionId, String ip, String coid) {
		String [] ret = null;		
		
		String url = mHost
                + "/axis2/services/ws_ems/get_classes_associated_course?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&coid=" + coid;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        String line = "false";
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            
            if (outStr.equals("session_expired")) {
            	line = "session_expired";
            }
            else {
            	if (i > 0) {
	            	if (line.equals("false"))
	            		line = outStr;
	            	else
	            		line += "&" + outStr;
            	}
            }
        }
        
        ret = line.split("&");
        
        return ret;		
	}
	
	public static String [] GetScheduleFromClassCourse(String login, String sessionId, String ip, String coid, String clid) {
		String [] ret = null;		
		
		String url = mHost
                + "/axis2/services/ws_ems/get_schedule_from_class_course?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&coid=" + coid
                + "&clid=" + clid;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        String line = "false";
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            
            if (outStr.equals("session_expired")) {
            	line = "session_expired";
            }
            else {
            	if (i > 0) {
	            	if (line.equals("false"))
	            		line = outStr;
	            	else
	            		line += "&" + outStr;
            	}
            }
        }
        
        ret = line.split("&");
        
        return ret;		
	}
	
	public static String [] GetCcdtFromScheduleClassCourse(String login, String sessionId, String ip, String coid, String clid, String schid) {
		String [] ret = null;		
		
		String url = mHost
                + "/axis2/services/ws_ems/get_ccdt_from_schedule_class_course?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&coid=" + coid
                + "&clid=" + clid
                + "&schid=" + schid
                + "&show_past_dates=1";

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        String line = "false";
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            
            if (outStr.equals("session_expired")) {
            	line = "session_expired";
            }
            else {            	
            	if (line.equals("false"))
            		line = outStr;
            	else
            		line += "&" + outStr;            	
            }
        }
        
        ret = line.split("&");
        
        return ret;		
	}
	
	public static String [] GetStudentsFromEnrollment(String login, String sessionId, String ip, String clid) {
		String [] ret = null;		
		
		String url = mHost
                + "/axis2/services/ws_ems/get_students_associated_enrollment?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&clid=" + clid;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        String line = "false";
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            
            if (outStr.equals("session_expired")) {
            	line = "session_expired";
            }
            else {
            	if (i > 0) {
	            	if (line.equals("false"))
	            		line = outStr;
	            	else
	            		line += "&" + outStr;
            	}
            }
        }
        
        ret = line.split("&");
        
        return ret;		
	}
	
	static public String GetPhotoFromCandidate(String login, String sessionId, String ip, String pid) {
				
		String url = mHost
                + "/axis2/services/ws_ems/get_photo_from_candidate?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&pid=" + pid;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
        }
        
        return outStr;		
	}
	
	static public String DownloadFileFromUser(String login, String sessionId, String ip, String pid, String fid) {
		
		String url = mHost
                + "/axis2/services/ws_ems/download_thumbnail_from_user?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&pid=" + pid
                + "&fid=" + fid;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
        }
        
        return outStr;		
	}
	
static public String AnswerPresence(String login, String sessionId, String ip, String ccdtid, String stline) {
		
		String url = mHost
                + "/axis2/services/ws_ems/answer_presence?username=" + login
                + "&cookie=" + sessionId
                + "&ip=" + ip
                + "&ccdtid=" + ccdtid
                + "&stline=" + stline;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebservicesConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList nodeList = doc.getElementsByTagName("ns:return");
        // Assert.assertTrue(nodeList.getLength() == 1);
        // outStr = "length=" + new Integer(nodeList.getLength()).toString();
        int length = nodeList.getLength();
        outStr = String.valueOf(length);
        for(int i=0;i<length;i++) {
            outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
        }
        
        return outStr;		
	}
}
