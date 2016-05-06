/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Emstest;

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


public class Main {
    static String host = "http://50.19.161.41:8081";    
    static String ip = "127.0.0.1";
    static String username = "";
    static String session_id = "";
    static int user_group_id = 0;
    static int school_id = 0;

    //Create md5 hash from password
    public static String md5(String password){
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
        BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));
        sen = hash.toString(16);
        return sen;
    }

    static InputStream OpenHttpConnection(String urlString)
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

    static void testCreateSchool(String school_name) {
        System.out.println("=== testCreateSchool");
        String schoolName = school_name;
        
        // call web services
        String url = host
                + "/axis2/services/ws_ems/create_new_school?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip + "&name=" + schoolName;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
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


            if (outStr.equals("false"))
                System.out.println("Can't create school");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");



        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    static void testDeleteSchool(String school_name) {
        System.out.println("=== testDeleteSchool");
        String schoolName = school_name;
        int schoolId = 0;

        // call web services to get school id
        String url = host
                + "/axis2/services/ws_ems/get_school_list?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
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
                //System.out.println(outStr);
                String [] schoolList = outStr.split(":");
                if (schoolList[0].equals(schoolName))
                    schoolId = Integer.parseInt(schoolList[1].toString());                        
            }

            //System.out.println(schoolId);

            // call web services to delete school
            url = host
                    + "/axis2/services/ws_ems/delete_school?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&sid=" + schoolId;

            // uncomment below to test with real web services
            urlStr = url;
            outStr = "";
           
            // URL url = new URL(urlStr);
            
            in = OpenHttpConnection(urlStr);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            // Assert.assertTrue(nodeList.getLength() == 1);
            // outStr = "length=" + new Integer(nodeList.getLength()).toString();
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
            }

            if (outStr.equals("false"))
                System.out.println("Can't delete school");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");



        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static void login(String user, String pwd) {

        username = user;
        String url = host
                + "/axis2/services/ws_doua_ems/check_login?username=" + username
                + "&pwd_hash=" + md5(pwd) + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        System.out.println("=== login: " + username);

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
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


            if (outStr.equals("false"))
                System.out.println("Can't connect");
            else
                session_id = outStr;
            

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void logout() {
        String url = host
                + "/axis2/services/ws_doua_ems/logout_session?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        System.out.println("=== logout: " + username);

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
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


            if (!outStr.equals("true"))
                System.out.println("Error while logout");



        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testCreatePerson(String user_group, String school_name, String user) {
        System.out.println("=== testCreatePerson");
        System.out.println("user: " + user);
        System.out.println("user group: " + user_group);

        // call web services to get school id
        String schoolName = school_name;
        int schoolId = 0;
        String url = host
                + "/axis2/services/ws_ems/get_school_list?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        
        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(urlStr);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");            
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
                String [] schoolList = outStr.split(":");
                if (schoolList[0].equals(schoolName))
                    schoolId = Integer.parseInt(schoolList[1].toString());
            }
            school_id = schoolId;
            //System.out.println("schoolId: " + schoolId);
            
            // call web services doua to gets user groups
            int gid = 0;
            url = host
                    + "/axis2/services/ws_doua_ems/get_user_groups?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            urlStr = url;
            outStr = "";

            // URL url = new URL(urlStr);

            in = OpenHttpConnection(urlStr);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            // Assert.assertTrue(nodeList.getLength() == 1);
            // outStr = "length=" + new Integer(nodeList.getLength()).toString();
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] ugList = outStr.split(":");
                if (ugList[1].equals(user_group))
                    gid = Integer.parseInt(ugList[0].toString());
            }
            user_group_id = gid;
            //System.out.println("gid: " + gid);

            // creates the new user in doua webservices           
            url = host
                    + "/axis2/services/ws_doua_ems/create_new_user?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&name=" + user + "&pass=" + md5(user) + "&gid=" + gid;

            // uncomment below to test with real web services
            urlStr = url;
            outStr = "";

            // URL url = new URL(urlStr);

            in = OpenHttpConnection(urlStr);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            // Assert.assertTrue(nodeList.getLength() == 1);
            // outStr = "length=" + new Integer(nodeList.getLength()).toString();
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
            }

            if (outStr.equals("true")) {
                // creates the new user in ems webservices
                url = host
                        + "/axis2/services/ws_ems/create_new_user?username=" + username
                        + "&cookie=" + session_id + "&ip=" + ip
                        + "&nickname=" + user + "&fullname=" + user
                        + "&email=" + user + "&telephone=(11)1111-1111&ptid=" + gid
                        + "&scid=" + schoolId + "&obs=0";

                // uncomment below to test with real web services
                urlStr = url;
                outStr = "";

                System.out.println(url);

                // URL url = new URL(urlStr);

                in = OpenHttpConnection(urlStr);
                doc = builder.parse(in);
                nodeList = doc.getElementsByTagName("ns:return");
                // Assert.assertTrue(nodeList.getLength() == 1);
                // outStr = "length=" + new Integer(nodeList.getLength()).toString();
                length = nodeList.getLength();
                outStr = String.valueOf(length);
                for(int i=0;i<length;i++) {
                    outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                }
            }
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");
            else
                System.out.println("Can't create person");



        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testDeletePerson(String user) {
        System.out.println("=== testDeletePerson: " + user);
        //System.out.println("user: " + user);

        // call web services to delete user
        String url = host
                + "/axis2/services/ws_doua_ems/delete_user?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip + "&name=" + user;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(urlStr);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
            }

            if (outStr.equals("true")) {
                url = host
                    + "/axis2/services/ws_ems/delete_user?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&nickname=" + user;

                // uncomment below to test with real web services
                urlStr = url;
                outStr = "";

                // URL url = new URL(urlStr);

                in = OpenHttpConnection(urlStr);
                doc = builder.parse(in);
                nodeList = doc.getElementsByTagName("ns:return");
                // Assert.assertTrue(nodeList.getLength() == 1);
                // outStr = "length=" + new Integer(nodeList.getLength()).toString();
                length = nodeList.getLength();
                outStr = String.valueOf(length);
                for(int i=0;i<length;i++) {
                    outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                    
                }
            }
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testCreateProgram(String program_name) {
        System.out.println("=== testCreateProgram: " + program_name);

        // call web services to create program
        String url = host
                + "/axis2/services/ws_ems/create_new_program?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip + "&name=" + program_name
                + "&value=111&description=" + program_name;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(urlStr);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't create program");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testCreateCourse(String course_name) {
        System.out.println("=== testCreateCourse: " + course_name);

        // call web services to create course
        String url = host
                + "/axis2/services/ws_ems/create_new_course?username=" + username
                + "&cookie=" + session_id + "&ip=" + ip + "&name=" + course_name
                + "&code=" + course_name;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(urlStr);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't create course");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void testAssociateCourseToProgram(String program_name, String course_name) {
        System.out.println("=== testAssociateCourseToProgram");
        System.out.println("Program name: " + program_name);
        System.out.println("Course name: " + course_name);

        try {
            // call web services to get program id
            String url = host
                    + "/axis2/services/ws_ems/get_programs_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int pid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] pList = outStr.split(":");
                if (pList[0].equals(program_name))
                    pid = Integer.parseInt(pList[1]);
            }

            // call web services to get course id
            url = host
                    + "/axis2/services/ws_ems/get_courses_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;
           
            outStr = "";
            int cid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] cList = outStr.split(":");
                if (cList[0].equals(course_name))
                    cid = Integer.parseInt(cList[1]);
            }

            // call web services to associate program to course
            url = host
                    + "/axis2/services/ws_ems/associate_course_to_program?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&pid=" + pid + "&cline=" + cid;
           
            outStr = "";
            
            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
            }
            
            if (outStr.equals("false"))
                System.out.println("Can't associate course to program");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

    public static void testCreateClass(String class_name, String program_name) {
        System.out.println("=== testCreateClass: " + class_name);
        
        try {
            // call web services to get program id
            String url = host
                    + "/axis2/services/ws_ems/get_programs_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int pid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] pList = outStr.split(":");
                if (pList[0].equals(program_name))
                    pid = Integer.parseInt(pList[1]);
            }    

            // call web services to create class
            url = host
                    + "/axis2/services/ws_ems/create_new_class?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&cname=" + class_name + "&pid=" + pid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't create class");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testCreateSchedule(String schedule_name) {
        System.out.println("=== testCreateSchedule: " + schedule_name);

        try {
            // call web services to create schedule
            String url = host
                    + "/axis2/services/ws_ems/create_new_schedule?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&schname=" + schedule_name;

            // uncomment below to test with real web services
            String outStr = "";
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();            
            }
            
            if (outStr.equals("false"))
                System.out.println("Can't create schedule");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testCreateAmbiance(String ambiance_name) {
        System.out.println("=== testCreateAmbiance: " + ambiance_name);

        try {
            // call web services to create schedule
            String url = host
                    + "/axis2/services/ws_ems/create_new_ambience?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&name=" + ambiance_name + "&max_students=32";

            // uncomment below to test with real web services
            String outStr = "";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't create schedule");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testCreateDatetime(String schedule_name, String date) {
        System.out.println("=== testCreateDatetime:");
        System.out.println("Schedule: " + schedule_name);        

        try {
            // call web services to get schedule id
            String url = host
                    + "/axis2/services/ws_ems/get_schedules_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int schid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] schList = outStr.split(":");
                if (schList[0].equals(schedule_name))
                    schid = Integer.parseInt(schList[1]);
            }
            
            // call web services to create datetime            
            String time_begin = "08:00";
            String time_end = "09:00";
            url = host
                    + "/axis2/services/ws_ems/create_new_datetime?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&date=" + date + "&time_begin=" + time_begin
                    + "&time_end=" + time_end + "&schid=" + schid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't create datetime");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testAssociateClassToCourse(String course_name, String class_name, String schedule_name, String tutor_name) {
        System.out.println("=== testAssociateClassToCourse");        
        System.out.println("Course name: " + course_name);
        System.out.println("Class name: " + class_name);
        System.out.println("Schedule name: " + schedule_name);
        System.out.println("Tutor name: " + tutor_name);

        try {
            // call web services to get course id
            String url = host
                    + "/axis2/services/ws_ems/get_courses_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int coid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(course_name))
                    coid = Integer.parseInt(coList[1]);
            }
            //System.out.println("course id: " + coid);

            // call web services to get class id
            url = host
                    + "/axis2/services/ws_ems/get_classes_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;
           
            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] clList = outStr.split(":");
                if (clList[0].equals(class_name))
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to get schedule id
             url = host
                    + "/axis2/services/ws_ems/get_schedules_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;         
           
            outStr = "";
            int schid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] schList = outStr.split(":");
                if (schList[0].equals(schedule_name))
                    schid = Integer.parseInt(schList[1]);
            }
            //System.out.println("schedule id: " + schid);

            // call web services to get tutor id
            url = host
                    + "/axis2/services/ws_ems/get_person_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&ptype=Tutor";

            outStr = "";
            int pid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] prList = outStr.split(":");
                if (prList[0].equals(tutor_name))
                    pid = Integer.parseInt(prList[1]);
            }

            // call web services to associate class to course
            url = host
                    + "/axis2/services/ws_ems/associate_class_to_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip 
                    + "&coid=" + coid + "&clid=" + clid + "&schid=" + schid + "&pid=" + pid;
           
            outStr = "";
            
            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
            }
            
            if (outStr.equals("false"))
                System.out.println("Can't associate class to course");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

    public static void testAddAmbianceToDatetime(String ambiance_name, String course_name) {
        System.out.println("=== testAddAmbianceToDatetime");
        System.out.println("Ambiance name: " + ambiance_name);
        System.out.println("Course name: " + course_name);                

        try {
            // call web services to get ambiance id
            String url = host
                    + "/axis2/services/ws_ems/get_ambiences_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int ambid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(ambiance_name))
                    ambid = Integer.parseInt(coList[1]);
            }
            //System.out.println("ambiance id: " + ambid);

            // call web services to get course id
            url = host
                    + "/axis2/services/ws_ems/get_courses_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            outStr = "";
            int coid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(course_name))
                    coid = Integer.parseInt(coList[1]);
            }
            //System.out.println("course id: " + coid);

            // call web services to get class id from course id
            url = host
                    + "/axis2/services/ws_ems/get_classes_associated_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid;

            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
               // System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to get schedule id from class_course
             url = host
                    + "/axis2/services/ws_ems/get_schedule_from_class_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid + "&clid=" + clid;

            outStr = "";
            int schid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] schList = outStr.split(":");
                if (i > 0)
                    schid = Integer.parseInt(schList[1]);
            }
            //System.out.println("schedule id: " + schid);

            // call web services to get datetime id from class_course
            url = host
                    + "/axis2/services/ws_ems/get_schedule_settings?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&schid=" + schid;

            //System.out.println(url);

            outStr = "";
            int dtid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] schList = outStr.split(";");
                dtid = Integer.parseInt(schList[5]);
            }
            //System.out.println("datetime id: " + dtid);

            // call web services to associate event
            url = host
                    + "/axis2/services/ws_ems/associate_event?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip
                    + "&coid=" + coid + "&clid=" + clid + "&dtid=" + dtid + "&ambid=" + ambid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't associate event");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testAddStudentsToEnrollment(String program_name, String student_name) {
        System.out.println("=== testAddStudentsToEnrollment");
        System.out.println("Program name: " + program_name);
        System.out.println("Student name: " + student_name);

        try {
            // call web services to get student id
            String url = host
                    + "/axis2/services/ws_ems/get_person_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&ptype=Student";

            // uncomment below to test with real web services
            String outStr = "";
            int sid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(student_name))
                    sid = Integer.parseInt(coList[1]);
            }
            //System.out.println("ambiance id: " + ambid);

            // call web services to get program id
            url = host
                    + "/axis2/services/ws_ems/get_programs_list?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            outStr = "";
            int pid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(program_name))
                    pid = Integer.parseInt(coList[1]);
            }
            //System.out.println("course id: " + coid);

            // call web services to get class id from program id
            url = host
                    + "/axis2/services/ws_ems/get_classes_from_program?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&pid=" + pid;

            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to associate students to enrollment
            url = host
                    + "/axis2/services/ws_ems/associate_students_to_enrollment?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip
                    + "&clid=" + clid + "&stline=" + sid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't associate student to enrollment");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testPresenceCall(String course_name) {
        System.out.println("=== testPresenceCall: " + course_name);
        
        try {
            // call web services to get course id
            String url = host
                    + "/axis2/services/ws_ems/get_courses_from_tutor?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int coid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(course_name))
                    coid = Integer.parseInt(coList[1]);
            }
            //System.out.println("ambiance id: " + ambid);
            
            // call web services to get class id from course id
            url = host
                    + "/axis2/services/ws_ems/get_classes_associated_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid;

            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to get schedule id from class_course
             url = host
                    + "/axis2/services/ws_ems/get_schedule_from_class_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid + "&clid=" + clid;

            outStr = "";
            int schid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);            
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
                String [] schList = outStr.split(":");
                if (i > 0)
                    schid = Integer.parseInt(schList[1]);
            }
            //System.out.println("schedule id: " + schid);

            // call web services to get ccdt id from class_course
            url = host
                    + "/axis2/services/ws_ems/get_ccdt_from_schedule_class_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid + "&clid=" + clid + "&schid=" + schid + "&show_past_dates=1";

            //System.out.println(url);

            outStr = "";
            int ccdtid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);            
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
                String [] schList = outStr.split(";");
                ccdtid = Integer.parseInt(schList[0]);
            }
            //System.out.println("datetime id: " + dtid);

            // call web services to get students id from enrollment
            url = host
                    + "/axis2/services/ws_ems/get_students_associated_enrollment?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&clid=" + clid;

            outStr = "";
            int sid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    sid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to answer presence
            url = host
                    + "/axis2/services/ws_ems/answer_presence?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip
                    + "&ccdtid=" + ccdtid + "&stline=" + sid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't do presence call");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testPresenceCallReport(String course_name) {
        System.out.println("=== testPresenceCallReport: " + course_name);
        
        try {
            // call web services to get course id
            String url = host
                    + "/axis2/services/ws_ems/get_courses_from_tutor?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int coid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(course_name))
                    coid = Integer.parseInt(coList[1]);
            }
            //System.out.println("ambiance id: " + ambid);
            
            // call web services to get class id from course id
            url = host
                    + "/axis2/services/ws_ems/get_classes_associated_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid;

            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to get schedule id from class_course
             url = host
                    + "/axis2/services/ws_ems/get_schedule_from_class_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid + "&clid=" + clid;

            outStr = "";
            int schid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);            
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
                String [] schList = outStr.split(":");
                if (i > 0)
                    schid = Integer.parseInt(schList[1]);
            }
            //System.out.println("schedule id: " + schid);

            // call web services to get ccdt id from class_course
            url = host
                    + "/axis2/services/ws_ems/get_ccdt_from_schedule_class_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid + "&clid=" + clid + "&schid=" + schid + "&show_past_dates=1";

            //System.out.println(url);

            outStr = "";
            int ccdtid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);            
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();                
                String [] schList = outStr.split(";");
                ccdtid = Integer.parseInt(schList[0]);
            }
            //System.out.println("datetime id: " + dtid);

            // call web services to show presence report
            url = host
                    + "/axis2/services/ws_ems/show_presence_report?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip
                    + "&ccdtid=" + ccdtid + "&clid=" + clid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't show presence call report");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testAssignGrade(String course_name, String grade) {
        System.out.println("=== testAssignGrade: " + course_name);

        try {
            // call web services to get course id
            String url = host
                    + "/axis2/services/ws_ems/get_courses_from_tutor?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int coid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(course_name))
                    coid = Integer.parseInt(coList[1]);
            }
            //System.out.println("ambiance id: " + ambid);

            // call web services to get class id from course id
            url = host
                    + "/axis2/services/ws_ems/get_classes_associated_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid;

            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to get students id from enrollment
            url = host
                    + "/axis2/services/ws_ems/get_students_associated_enrollment?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&clid=" + clid;

            outStr = "";
            int sid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    sid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);

            // call web services to assign grade
            String gline = sid + ":" + grade;
            url = host
                    + "/axis2/services/ws_ems/assign_grade?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip
                    + "&coid=" + coid + "&clid=" + clid + "&gline=" + gline;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

            if (outStr.equals("false"))
                System.out.println("Can't assign grade");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testAssignGradeReport(String course_name) {
        System.out.println("=== testAssignGradeReport: " + course_name);

        try {
            // call web services to get course id
            String url = host
                    + "/axis2/services/ws_ems/get_courses_from_tutor?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip;

            // uncomment below to test with real web services
            String outStr = "";
            int coid = 0;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputStream in = OpenHttpConnection(url);
            Document doc = builder.parse(in);
            NodeList nodeList = doc.getElementsByTagName("ns:return");
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                String [] coList = outStr.split(":");
                if (coList[0].equals(course_name))
                    coid = Integer.parseInt(coList[1]);
            }
            //System.out.println("ambiance id: " + ambid);

            // call web services to get class id from class id
            url = host
                    + "/axis2/services/ws_ems/get_classes_associated_course?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip + "&coid=" + coid;

            outStr = "";
            int clid = 0;

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                //System.out.println(outStr);
                String [] clList = outStr.split(":");
                if (i > 0)
                    clid = Integer.parseInt(clList[1]);
            }
            //System.out.println("class id: " + clid);
            
            // call web services to show grade report            
            url = host
                    + "/axis2/services/ws_ems/show_grade_report?username=" + username
                    + "&cookie=" + session_id + "&ip=" + ip
                    + "&coid=" + coid + "&clid=" + clid;

            outStr = "";

            in = OpenHttpConnection(url);
            doc = builder.parse(in);
            nodeList = doc.getElementsByTagName("ns:return");
            length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
                if (i > 0)
                    System.out.println(outStr);
            }

            if (outStr.equals("false"))
                System.out.println("Can't show grade report");
            else if (outStr.equals("session_expired"))
                System.out.println("Session expired");

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SAXException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("====== EMS test");

        login("schoolman","man123");
        //testDeleteSchool("tstNewSchool");
        testCreateSchool("tstNewSchool");        
        testCreatePerson("Administrator", "tstNewSchool","tstadmin2");
        logout();
        login("tstadmin2","tstadmin2");
        testCreateProgram("tstProgram1");
        testCreateProgram("tstProgram2");
        testCreateProgram("tstProgram3");
        testCreateCourse("tstCourse1");
        testCreateCourse("tstCourse2");
        testCreateCourse("tstCourse3");
        testAssociateCourseToProgram("tstProgram1", "tstCourse1");
        testAssociateCourseToProgram("tstProgram2", "tstCourse2");
        testAssociateCourseToProgram("tstProgram3", "tstCourse3");
        testCreateClass("tstClass1","tstProgram1");
        testCreateClass("tstClass2","tstProgram2");
        testCreateClass("tstClass3","tstProgram3");
        testCreatePerson("Tutor", "tstNewSchool","tstTutor1");
        testCreatePerson("Tutor", "tstNewSchool","tstTutor2");
        testCreatePerson("Tutor", "tstNewSchool","tstTutor3");
        testCreateAmbiance("tstAmbiance1");
        testCreateAmbiance("tstAmbiance2");
        testCreateAmbiance("tstAmbiance3");
        testCreateSchedule("tstSchedule1");
        testCreateSchedule("tstSchedule2");
        testCreateSchedule("tstSchedule3");        
        testCreateDatetime("tstSchedule1", "2011/07/07");
        testCreateDatetime("tstSchedule2", "2011/07/08");
        testCreateDatetime("tstSchedule3", "2011/07/09");        
        testAssociateClassToCourse("tstCourse1", "tstClass1", "tstSchedule1", "tstTutor1");
        testAssociateClassToCourse("tstCourse2", "tstClass2", "tstSchedule2", "tstTutor2");
        testAssociateClassToCourse("tstCourse3", "tstClass3", "tstSchedule3", "tstTutor3");        
        testAddAmbianceToDatetime("tstAmbiance1", "tstCourse1");
        testAddAmbianceToDatetime("tstAmbiance2", "tstCourse2");
        testAddAmbianceToDatetime("tstAmbiance3", "tstCourse3");        
        testCreatePerson("Student", "tstNewSchool","tstStudent1");
        testCreatePerson("Student", "tstNewSchool","tstStudent2");
        testCreatePerson("Student", "tstNewSchool","tstStudent3");
        testAddStudentsToEnrollment("tstProgram1", "tstStudent1");
        testAddStudentsToEnrollment("tstProgram2", "tstStudent2");
        testAddStudentsToEnrollment("tstProgram3", "tstStudent3");
        logout();
        login("tstTutor1","tstTutor1");
        testPresenceCall("tstCourse1");        
        testAssignGrade("tstCourse1", "5.0");
        testPresenceCallReport("tstCourse1");
        testAssignGradeReport("tstCourse1");
        logout();
        login("tstTutor2","tstTutor2");
        testPresenceCall("tstCourse2");        
        testAssignGrade("tstCourse2", "5.0");
        testPresenceCallReport("tstCourse2");
        testAssignGradeReport("tstCourse2");
        logout();
        login("tstTutor3","tstTutor3");
        testPresenceCall("tstCourse3");
        testAssignGrade("tstCourse3", "4.9");
        testPresenceCallReport("tstCourse3");
        testAssignGradeReport("tstCourse3");
        logout();
        login("tstadmin2","tstadmin2");
        testDeletePerson("tstTutor1");
        testDeletePerson("tstTutor2");
        testDeletePerson("tstTutor3");
        testDeletePerson("tstStudent1");
        testDeletePerson("tstStudent2");
        testDeletePerson("tstStudent3");
        logout();
        login("schoolman","man123");
        testDeletePerson("tstadmin2");
        testDeleteSchool("tstNewSchool");
        logout();
        
    }

}

