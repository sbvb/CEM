/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Date;
import java.util.Date;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Database class
 * Date: 2011/03/15
 * @author Vinícius Heineck dos Santos
 */
public class DatabaseOp {

    //database in home vinicius
    /*
    static String userName = "heineck";
    static String userPassword = "heineck";
    static String databaseUrl = "jdbc:mysql://localhost:3306/doua_db";
    */
    //database in telematica02
    ///*
    static String userName = "selectos_ems";
    static String userPassword = "ems";
    static String databaseUrl =
    "jdbc:mysql://localhost:3306/db_ems?useUnicode=yes&characterEncoding=UTF-8";
    //private String m_ws_host = "http://50.19.161.41:8081";
    private String m_ws_host = "";
    //private String m_ws_host = "http://localhost:8080";
    static String SESSION_EXPIRED_MSG = "session_expired";
    //*/
    static String userQuery = "select name from tb_person";
    static String uploadDir = "/home/cem/upload/";
    static String agreementDir = "/home/cem/upload/private/";
    static String agreementTemplateDir = "/home/cem/templates/";
    static String agreementTemplateImgDir = "/home/cem/templates/temp/";

    /**
     *
     */
    public DatabaseOp() {
        m_ws_host = GetConfigurations();
        
        System.out.println("DatabaseOp.m_ws_host = " + m_ws_host);
    }


    /** Test method of database usage
     *
     * @return Users list if ok or null if an error occurred.
     */
    public static String [] dbTest() {
        String res = "false";
        String [] strArr = null;
        try {
            //Class.forName("com.mysql.jdbc.Driver").newInstance ();
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

            
           // System.out.println("database connected");
            Statement stat = conn.createStatement();
            
            String query = userQuery;
            ResultSet result = stat.executeQuery(query);
            
            //res = result.getString("name");
            //res = resArr.toString();

            while(result.next()){
                if (res.equals("false"))
                    res = result.getString("name");
                else
                    res += " " + result.getString("name");
            }

            strArr = res.split(" ");
            /*
            System.out.println("Result(s): ");
            while(result.next()){
                System.out.println("Name:\t" + result.getString("name"));
                System.out.println("");
            }
            */
            //res = "true";
            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        return strArr;
    }

    private InputStream OpenHttpConnection(String urlString)
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

    public String GetConfigurations() {
        String outStr = "";
        try {
            //File currentDirectory = new File(new File(".").getAbsolutePath());
            //ret = currentDirectory.getCanonicalPath(); //home/ubuntu
            File file = new File("/home/ubuntu/tomcat/apache-tomcat-7.0.12/webapps/axis2/WEB-INF/services/ems_conf.xml");
            //File file = new File("ems_conf.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            
            NodeList nodeList = doc.getElementsByTagName("host");
            // Assert.assertTrue(nodeList.getLength() == 1);
            // outStr = "length=" + new Integer(nodeList.getLength()).toString();
            int length = nodeList.getLength();
            outStr = String.valueOf(length);
            for(int i=0;i<length;i++) {
                outStr = nodeList.item(i).getChildNodes().item(0).getNodeValue();
            }

                     
          } catch (Exception e) {
            e.printStackTrace();
          }

        return outStr;
    }

    public boolean CheckSession(String username, String cookie, String ip){
        String url = m_ws_host 
                + "/axis2/services/ws_doua_ems/check_session?username=" + username
                + "&cookie=" + cookie + "&ip=" + ip;

        // uncomment below to test with real web services        
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
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
        else
            ret = false;


        return ret;
    }

    public boolean CreateCandidateDOUA(String nickname, String email) {
        String url = m_ws_host
                + "/axis2/services/ws_doua_ems/create_new_candidate?name=" + nickname
                + "&email=" + email;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
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
        else
            ret = false;


        return ret;
    }

    public boolean GenerateNewPasswordDOUA(String login, String name, String email) {
        String url = m_ws_host
                + "/axis2/services/ws_doua_ems/generate_new_password?login=" + login
                + "&name=" + name.replace(" ", "%20")
                + "&email=" + email;

        // uncomment below to test with real web services
        String urlStr = url;
        String outStr = "";
        boolean ret = false;

        // URL url = new URL(urlStr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        InputStream in = null;
        try {
            in = OpenHttpConnection(urlStr);
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = null;
        try {
            doc = builder.parse(in);
        } catch (SAXException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
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
        else
            ret = false;


        return ret;
    }

    /** Creates the new user
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname
     * @param fullname. String fullname
     * @param email. String email
     * @param telephone. String telephone
     * @param ptid. Integer person type id
     * @param scid. Integer school id
     * @param obs. String obs
     * @param address. String address
     * @param district. String district
     * @param city. String city
     * @param state. String state
     * @param zip_code. String zip_code
     * @param civil_status. String civil status
     * @param career. String career
     * @param identity. String identity
     * @param issued_by. String issued by
     * @param cpf. String cpf
     * @return If user was created return true, else return false.
     *          If session expired returns session_expired
     */
    public String CreateNewUser(String username, String cookie, String ip, String nickname, String fullname, String email, String telephone, int ptid, int scid, String obs, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf) {
        String userCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "SELECT nickname FROM tb_person where nickname='" + nickname + "'";
               // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                boolean userExists = false;
                while(result.next()){
                    userExists = true;
                }

                if (!userExists) {
                    query = "INSERT INTO tb_person(nickname,name,email,telephone, school_id, person_type_id, obs, "
                            + "address, district, city, state, zip_code, "
                            + "civil_status, career, identity, issued_by, cpf) "
                            + "VALUES('" + nickname + "','" + fullname + "','" + email +
                            "','" + telephone + "', " + scid + "," + ptid + ",'" + obs 
                            + "','" + address + "','" + district + "','" + city + "','" + state + "','" + zip_code
                            + "','" + civil_status + "','" + career + "','" + identity + "','" + issued_by + "','" + cpf + "')";
                    if (stat.executeUpdate(query) > 0)
                        userCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            userCreated = SESSION_EXPIRED_MSG;
        }

        return userCreated;
    }

    /** Delete the user
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname
     * @return If user was deleted return true, else return false
     *          If session expired returns session_expired
     */
    public String DeleteUser(String username, String cookie, String ip, String nickname) {
        String userDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "DELETE FROM tb_person WHERE nickname='" + nickname + "'";
                if (stat.executeUpdate(query) > 0)
                    userDeleted = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            userDeleted = SESSION_EXPIRED_MSG;
        }

        return userDeleted;
    }

    /** Gets the user settings
    *
    * @param username. String username.
    * @param cookie. String cookie value.
    * @param ip. String ip.
    * @param nickname. String nickname.
    * @return returns a list with the user settings in format:
    * <full name>:<email>:<telephone>:<school name>:<school id>:<person type name>:<person type id>:<obs>:<person id>
    *          If session expired returns session_expired
    */
    public String [] GetUserSettings(String username, String cookie, String ip, String nickname) {
        String [] usersList = null;
        String line = SESSION_EXPIRED_MSG;
        
        if (CheckSession(username, cookie, ip)) { //session Ok

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`person_id`, `tb_person`.`name` AS `fullname`, `tb_person`.`email`, "
                        + "`tb_person`.`telephone`,  `tb_person`.`person_type_id` AS `ptid`, "
                        + "`tb_person`.`school_id` AS `scid`, `tb_school`.`name` AS `sc_name`, "
                        + "`tb_person_type`.`name` AS `pt_name`, `tb_person`.`obs` FROM tb_person "
                        + "INNER JOIN tb_school ON `tb_person`.`school_id`=`tb_school`.`school_id` "
                        + "INNER JOIN tb_person_type "
                        + "ON `tb_person`.`person_type_id`=`tb_person_type`.`person_type_id` "
                        + "WHERE `tb_person`.`nickname`='" + nickname + "'";

                ResultSet result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                        line += result.getString("fullname");
                        line += ":" + result.getString("email");
                        line += ":" + result.getString("telephone");
                        line += ":" + result.getString("sc_name");
                        line += ":" + result.getString("scid");
                        line += ":" + result.getString("pt_name");
                        line += ":" + result.getString("ptid");
                        line += ":" + result.getString("obs");
                        line += ":" + result.getString("person_id");
                }

                System.out.println(line);

                usersList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { //session expired
            usersList = line.split("&");
        }

        return usersList;
    }

    /** Edit user settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname
     * @param fullname. String fullname
     * @param email. String email
     * @param telephone. String telephone
     * @param scid. Integer school id
     * @param ptid. Integer person type id
     * @param obs. String obs
     * @param address. String address
     * @param district. String district
     * @param city. String city
     * @param state. String state
     * @param zip_code. String zip_code
     * @param civil_status. String civil status
     * @param career. String career
     * @param identity. String identity
     * @param issued_by. String issued by
     * @param cpf. String cpf
     * @return Returns true if user was edited, else returns false.
     *          If Session expired returns session_expired
     */
    public String EditUserSettings(String username, String cookie, String ip, String nickname, String fullname, String email, String telephone, int scid, int ptid, String obs, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf) {
        String userEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_person SET name='" + fullname + "', email='" + email
                        + "', telephone='" + telephone + "', school_id=" + scid
                        + " , person_type_id=" + ptid + " , obs='" + obs
                        + "', address='" + address + "', district='" + district
                        + "', city='" + city + "', state='" + state + "', zip_code='" + zip_code
                        + "', civil_status='" + civil_status + "', career='" + career
                        + "', identity='" + identity + "', issued_by='" + issued_by + "', cpf='" + cpf
                        + "' WHERE nickname='" + nickname + "'";
                if (stat.executeUpdate(query) > 0)
                    userEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            userEdited = SESSION_EXPIRED_MSG;
        }

        return userEdited;
    }

    /** Creates new ambiance
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String ambiance name
     * @param max_students. Integer max_students
     * @return Returns true if new ambiance was created or false if the ambiance name already exists.
     *          If session expired returns session_expired
     */
    public String CreateNewAmbience(String username, String cookie, String ip, String name, int max_students) {
        String ambienceCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "SELECT school_id FROM tb_person WHERE nickname='" + username + "'";
               // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                    scid = Integer.parseInt(result.getString("school_id"));
                }

                query = "SELECT name FROM tb_ambiance WHERE name='" + name + "' AND school_id=" + scid;
               // System.out.println(query);
                result = stat.executeQuery(query);

                boolean ambienceExists = false;
                while(result.next()){
                    ambienceExists = true;
                }

                if (!ambienceExists) {
                    query = "INSERT INTO tb_ambiance(name,max_students,school_id) "
                            + "VALUES('" + name + "'," + max_students + "," + scid + ")";
                    if (stat.executeUpdate(query) > 0)
                        ambienceCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ambienceCreated = SESSION_EXPIRED_MSG;
        }

        return ambienceCreated;
    }

    /** Deletes the ambiance
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ambid. Integer ambiance id
     * @return returns true if ambience was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String DeleteAmbience(String username, String cookie, String ip, int ambid) {
        String ambienceDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "DELETE FROM tb_ambiance WHERE ambiance_id=" + ambid;
                if (stat.executeUpdate(query) > 0)
                    ambienceDeleted = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ambienceDeleted = SESSION_EXPIRED_MSG;
        }

        return ambienceDeleted;
    }

    /** Edits the ambiance
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param max_students. Integer max students
     * @param ambid. Integer ambiance id     
     * @return returns true if ambiance was edited, else returns false
     *          If session expired returns session_expired
     */
    public String EditAmbienceSettings(String username, String cookie, String ip, int max_students, int ambid) {
        String ambienceEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_ambiance SET max_students=" + max_students                        
                        + " WHERE ambiance_id=" + ambid;
                if (stat.executeUpdate(query) > 0)
                    ambienceEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ambienceEdited = SESSION_EXPIRED_MSG;
        }

        return ambienceEdited;
    }

    /** Gets the ambiance settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ambid. Integer ambiance id
     * @return return the ambiance settings in format:
     *          <ambiance name>:<max_students>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  GetAmbienceSettings(String username, String cookie, String ip, int ambid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name,max_students FROM tb_ambiance WHERE ambiance_id=" + ambid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line += result.getString("name");
                        line += ":" + result.getString("max_students");                        
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Gets a list of ambiances
     *
     * @return returns a list of ambiances in format:
     *          <ambiance name>:<ambiance id>:<max students>
     *          <ambiance name 1>:<ambiance id 1>:<max students 1>
     *          <ambiance name 2>:<ambiance id 2>:<max students 2>
     *          If not exists any ambiances, return null
     *          If session expired returns session_expired
     */
    public String [] GetAmbiencesList(String username, String cookie, String ip) {
        String [] ambiencesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_ambiance`.`name`, `tb_ambiance`.`ambiance_id`, `tb_ambiance`.`max_students` "
                        + "FROM tb_ambiance INNER JOIN tb_person ON `tb_ambiance`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "' ORDER BY `tb_ambiance`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "ambiance name:ambiance_id:max students";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("ambiance_id");
                    line += ":" + result.getString("max_students");                    
                }

               // System.out.println(line);

                ambiencesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ambiencesList = line.split("&");
        }

        return ambiencesList;
    }

    /** Creates new course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cname. String course name
     * @param code. String code name
     * @return returns true if course was created, else returns false
     *          If session expired returns session_expired
     */
    public String CreateNewCourse(String username, String cookie, String ip, String cname, String code) {
        String courseCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "SELECT school_id FROM tb_person WHERE nickname='" + username + "'";
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                    scid = Integer.parseInt(result.getString("school_id"));
                }

                query = "SELECT name FROM tb_course WHERE name='" + cname + "' AND school_id=" + scid;
               // System.out.println(query);
                result = stat.executeQuery(query);

                boolean courseExists = false;
                while(result.next()){
                    courseExists = true;
                }

                if (!courseExists) {
                    query = "INSERT INTO tb_course(name,code_name,school_id) "
                        + "VALUES('" + cname + "','" + code + "'," + scid + ")";
                    if (stat.executeUpdate(query) > 0)
                        courseCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // sesison expired
            courseCreated = SESSION_EXPIRED_MSG;
        }

        return courseCreated;
    }

    /** Deletes the course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. Integer course id
     * @return returns true if course was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String DeleteCourse(String username, String cookie, String ip, int cid) {
        String courseDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "DELETE FROM tb_course WHERE course_id=" + cid;
                if (stat.executeUpdate(query) > 0)
                    courseDeleted = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            courseDeleted = SESSION_EXPIRED_MSG;
        }

        return courseDeleted;
    }

    /** Edits the course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param code. String code name
     * @param cid. Integer course id
     * @return returns true if course was edited, else returns false
     *          If session expired returns session_expired
     */
    public String EditCourseSettings(String username, String cookie, String ip, String code, int cid) {
        String courseEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_course SET code_name='" + code
                        + "' WHERE course_id=" + cid;
                if (stat.executeUpdate(query) > 0)
                    courseEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            courseEdited = SESSION_EXPIRED_MSG;
        }

        return courseEdited;
    }

    /** Gets the course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. Integer course id
     * @return return the course settings in format:
     *          <name>:<code>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  GetCourseSettings(String username, String cookie, String ip, int cid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name,code_name FROM tb_course WHERE course_id=" + cid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line += result.getString("name");
                        line += ":" + result.getString("code_name");                        
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Gets a list of courses
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return returns a list of courses in format:
     *          <course name>:<course id>:<code>
     *          <course name 1>:<course id 1>:<code 1>
     *          <course name 2>:<course id 2>:<code 2>
     *          If not exists any courses, return null
     *          If session expired returns session_expired
     */
    public String [] GetCoursesList(String username, String cookie, String ip) {
        String [] coursesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_course`.`name`, `tb_course`.`course_id`, `tb_course`.`code_name` "
                        + "FROM tb_course INNER JOIN tb_person ON `tb_course`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "' ORDER BY `tb_course`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "course name:course id:code";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("course_id");
                    line += ":" + result.getString("code_name");
                }

               // System.out.println(line);

                coursesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            coursesList = line.split("&");
        }

        return coursesList;
    }

    /** Creates a new schedule
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param String schname. String schedule name
     * @return returns true if schedule was created, else returns false:
     *          If session expired returns session_expired
     */
    public String CreateNewSchedule(String username, String cookie, String ip, String schname) {
        String scheduleCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

            Statement stat = conn.createStatement();

            //Gets school_id from username
            String query = "SELECT school_id FROM tb_person where nickname='" + username + "'";
           // System.out.println(query);
            ResultSet result = stat.executeQuery(query);

            int scid = 0;
            while(result.next()) {
                scid = Integer.parseInt(result.getString("school_id"));
            }

            //Checks schedule existence
            query = "SELECT name FROM tb_schedule WHERE name='" + schname 
                    + "' AND school_id=" + scid;
           // System.out.println(query);
            result = stat.executeQuery(query);

            boolean scheduleExists = false;
            while(result.next()){
                scheduleExists = true;
            }

            if (!scheduleExists) {
                   query = "INSERT INTO tb_schedule(name,school_id) "
                        + "VALUES('" + schname + "'," + scid + ")";
                    if (stat.executeUpdate(query) > 0) {
                        scheduleCreated = "true";
                    }                
            }
            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            scheduleCreated = SESSION_EXPIRED_MSG;
        }

        return scheduleCreated;
    }

    /** Deletes schedule
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param schid. Integer schedule id
     * @return returns true if schedule was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String DeleteSchedule(String username, String cookie, String ip, int schid) {
        String scheduleDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "DELETE FROM tb_schedule WHERE schedule_id=" + schid;
                if (stat.executeUpdate(query) > 0) {
                        scheduleDeleted = "true";
                }


                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            scheduleDeleted = SESSION_EXPIRED_MSG;
        }

        return scheduleDeleted;
    }

    /** Edits the schedule settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param schname. String schedule name
     * @param schid. Integer schedule id
     * @return returns true if schedule was edited, else returns false
     *          If session expired returns session_expired
     */
    public String EditScheduleSettings(String username, String cookie, String ip, String schname, int schid) {
        String scheduleEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_schedule SET name='" + schname
                        + "' WHERE schedule_id=" + schid;
                if (stat.executeUpdate(query) > 0)
                    scheduleEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            scheduleEdited = SESSION_EXPIRED_MSG;
        }

        return scheduleEdited;
    }

    /** Gets the schedule settings
     *
     * @param name. String name
     * @return return the schedule settings in format:
     *          <schedule name>;<schedule_id>;<date>;<time_begin>;<time_end>;<datetime id>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String [] GetScheduleSettings(String username, String cookie, String ip, int schid) {
        String line = SESSION_EXPIRED_MSG;
        String [] schedulesList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_schedule`.`name` as `schedule`, "
                        + "`tb_schedule`.`schedule_id` as `schedule_id`, "
                        + "DATE_FORMAT(`tb_datetime`.`date`,'%Y/%m/%d') as `date`, "
                        + "TIME_FORMAT(`tb_datetime`.`time_begin`,'%H:%i') as `time_begin`, "
                        + "TIME_FORMAT(`tb_datetime`.`time_end`,'%H:%i') as `time_end`, "
                        + "`tb_datetime`.`dt_id` "
                        + "FROM `tb_datetime` INNER JOIN tb_schedule "
                        + "ON `tb_datetime`.`schedule_id`=`tb_schedule`.`schedule_id` "                        
                        + "WHERE `tb_schedule`.`schedule_id`=" + schid;

                ResultSet result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.isEmpty())
                        line = result.getString("schedule");
                    else
                        line += "&" + result.getString("schedule");

                    line += ";" + result.getString("schedule_id");
                    line += ";" + result.getString("date");
                    line += ";" + result.getString("time_begin");
                    line += ";" + result.getString("time_end");
                    line += ";" + result.getString("dt_id");
                }

                schedulesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            schedulesList = line.split("&");
        }

        return schedulesList;
    }

    /** Gets a list of schedule
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return returns a list of schedules in format:
     *          <schedule name>:<schedule id>
     *          <name 1>:<schedule id 1>
     *          <name 2>:<schedule id 2>
     *          If not exists any schedules, return null
     *          If session expired returns session_expired
     */
    public String [] GetSchedulesList(String username, String cookie, String ip) {
        String [] schedulesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_schedule`.`schedule_id`, `tb_schedule`.`name` FROM tb_schedule "
                        + "INNER JOIN tb_person ON `tb_schedule`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_schedule`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "schedule name:schedule id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("schedule_id");
                }

               // System.out.println(line);

                schedulesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            schedulesList = line.split("&");
        }

        return schedulesList;
    }

    /** Creates a new class
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cname. String class name
     * * @param pid. Integer program id
     * @return returns true if class was created, else returns false
     *          If session expired returns session_expired
     */
    public String CreateNewClass(String username, String cookie, String ip, String cname, int pid) {
        String classCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                //gets school_id from username
                String query = "SELECT school_id FROM tb_person where nickname='" + username + "'";
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                    scid = Integer.parseInt(result.getString("school_id"));
                }

                query = "SELECT name FROM tb_class where name='" + cname 
                        + "' AND school_id=" + scid;
               // System.out.println(query);
                result = stat.executeQuery(query);

                boolean classExists = false;
                while(result.next()){
                    classExists = true;
                }

                if (!classExists) {
                    query = "INSERT INTO tb_class(name,school_id,program_id) "
                            + "VALUES('" + cname + "'," + scid + "," + pid + ")";
                    if (stat.executeUpdate(query) > 0)
                        classCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            classCreated = SESSION_EXPIRED_MSG;
        }

        return classCreated;
    }

    /** Deletes the class
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. Integer class id
     * @return returns true if class was deleted, else returns false
     *         If session expired returns session_expired
     */
    public String DeleteClass(String username, String cookie, String ip, int cid) {
        String classDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "DELETE FROM tb_class WHERE class_id=" + cid;
                if (stat.executeUpdate(query) > 0)
                    classDeleted = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            classDeleted = SESSION_EXPIRED_MSG;
        }

        return classDeleted;
    }

    /** Edits the class settings
     *
     * @param name. String name
     * @param course_id. int course_id
     * @param schedule_id. int schedule_id
     * @param tutor_id. int tutor_id
     * @return returns true if class was edited, else returns false
     */
    public String EditClassSettings(String name, int course_id, int schedule_id, int tutor_id) {
        String classEdited = "false";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

            Statement stat = conn.createStatement();


            String query = "UPDATE tb_class SET course_id=" + course_id + ", schedule_id=" +
                    schedule_id + ", tutor_id=" + tutor_id +
                    " WHERE name='" + name + "'";
            if (stat.executeUpdate(query) > 0)
                classEdited = "true";

            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        return classEdited;
    }
    
    /** Gets classes list
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return returns a list of class in format:
     *          <class name>:<class id>
     *          <name 1>:<class_id 1>
     *          <name 2>:<class_id 2>
     *          If not exists any class, return null.
     *          If session expired returns session_expired
     */
    public String [] GetClassesList(String username, String cookie, String ip) {
        String [] classesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_class`.`class_id`,`tb_class`.`name` FROM `tb_class` "
                        + "INNER JOIN tb_person ON `tb_class`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_class`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "class name:class_id";
                while(result.next()){
                    line += "&" + result.getString("name");                    
                    line += ":" + result.getString("class_id");
                }

               // System.out.println(line);

                classesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            classesList = line.split("&");
        }

        return classesList;
    }

    /** Gets courses from tutor
     * @param String username. String tutor's username.
     * @param String cookie. String cookie value.
     * @param String ip. String ip.
     * @return returns a list of courses in format:
     *          <course name>:<course id>
     *          <course name 1>:<course id 1>
     *          <course name 2>:<course id 2>
     *          If not exists any course, return null
     */
    public String [] GetCoursesFromTutor(String username, String cookie, String ip) {
        String [] coursesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT DISTINCT `tb_course`.`name`, `tb_course`.`course_id` "
                        + "FROM tb_course INNER JOIN tb_class_course "
                        + "ON `tb_course`.`course_id`=`tb_class_course`.`course_id` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_person`.`person_id`=`tb_class_course`.`person_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_course`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "course name:course id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("course_id");
                }

               // System.out.println(line);

                coursesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            coursesList = line.split("&");
        }

        return coursesList;
    }       

    /** Get persons list according with person type
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ptype. String person type
     * @param sort. String sort: name = sort by name, date = sort by date
     * @return returns a list of person in format:
     *          <person name>;<person id>:<nickname>:<date>:<reg_type>:<telephone>:<promo_code>
     *          <person name 1>:<person id 1>:<nickname 1>:<date 1>:<reg_type 1>:<telephone 1>:<promo_code 1>
     *          <person name 2>:<person id 2>:<nickname 2>:<date 2>:<reg_type 2>:<telephone 2>:<promo_code 2>
     *          If not exists any person, return null
     *          If session expiredreturns session_expired
     */
    public String [] GetPersonList(String username, String cookie, String ip, String ptype, String sort) {
        String [] personList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT school_id FROM tb_person WHERE nickname='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                    scid = Integer.parseInt(result.getString("school_id"));
                }

                query = "SELECT `tb_person`.`name`, `tb_person`.`person_id`, `tb_person`.`nickname`, "
                        + " DATE_FORMAT(`tb_person`.`date`, '%Y/%m/%d %H_%i_%s') AS `date`, "
                        + "`tb_person`.`reg_type`, `tb_person`.`telephone`, `tb_person`.`user_id` "
                        + "FROM tb_person "
                        + "INNER JOIN tb_person_type "
                        + "ON `tb_person`.`person_type_id`=`tb_person_type`.`person_type_id` "
                        + "WHERE `tb_person`.`school_id`=" + scid
                        + " AND `tb_person_type`.`name`='" + ptype;
                if (sort.equals("date"))
                    query += "' ORDER BY `tb_person`.`date`";
                else
                    query += "' ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                line = "person name:person id:nickname";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("person_id");
                    line += ":" + result.getString("nickname");
                    line += ":" + result.getString("date");
                    line += ":" + result.getString("reg_type");
                    line += ":" + result.getString("telephone");
                    line += ":" + result.getString("user_id");
                }

               // System.out.println(line);

                personList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            personList = line.split("&");
        }

        return personList;
    }
    
    /** Deletes the event
     *
     * @param student_id. int student_id
     * @return returns true if event was deleted, else returns false
     */
    public String DeleteEvent(int student_id) {
        String eventDeleted = "false";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

            Statement stat = conn.createStatement();


            String query = "DELETE FROM tb_event WHERE student_id=" + student_id;
            if (stat.executeUpdate(query) > 0)
                eventDeleted = "true";

            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        return eventDeleted;
    }

    /** Gets a list of event
     *
     * @return returns a list of event in format:
     *          <student id>
     *          <student id 1>
     *          <student id 2>
     *          If not exists any class, return null
     */
    public String [] GetEventsList() {
        String [] eventsList = null;

        try {
            //Class.forName("com.mysql.jdbc.Driver").newInstance ();
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

           // System.out.println("database connected");
            Statement stat = conn.createStatement();

            String query = "SELECT student_id FROM tb_event";

            ResultSet result = stat.executeQuery(query);

            String line = "student id";
            while(result.next()){
                line += "&" + result.getString("student_id");
            }

           // System.out.println(line);

            eventsList = line.split("&");

            System.out.println(line);

            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        return eventsList;
    }

    /** Creates a new datetime
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param date. String date
     * @param time_begin. String time_begin
     * @param time_end. String time_end     
     * @param schid. Integer schedule id    
     * @return returns true if datetime was created, else returns two cases:
     *          1) excDateConflict = date conflict
     *          2) false = Registry can't be wrote
     *          If session expired returns session_expired
     */
    public String CreateNewDatetime(String username, String cookie, String ip, String date, String time_begin, String time_end, int schid) {
        String dateTimeCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                //SELECT * FROM `tb_datetime` WHERE dt_date=DATE('2011-05-06') and ((time_begin > '10:01' and time_begin < '15:00') or (time_end > '10:01' and time_end < '15:00'))
                String query = "SELECT * FROM tb_datetime INNER JOIN tb_schedule "
                        + "ON `tb_datetime`.`schedule_id`=`tb_schedule`.`schedule_id` "
                        + "INNER JOIN tb_person ON `tb_schedule`.`school_id`=`tb_person`.`school_id` "
                        //+ "WHERE `tb_datetime`.`date`=DATE('" + date + "') "
                        + "WHERE `tb_datetime`.`date`=DATE('2011-05-06') "
                        + "AND ((`tb_datetime`.`time_begin` > '" + time_begin
                        + "' AND `tb_datetime`.`time_begin` < '" + time_end + "') "
                        + "OR (`tb_datetime`.`time_end` > '" + time_begin
                        + "' AND `tb_datetime`.`time_end` < '" + time_end + "')) "                        
                        + " AND `tb_person`.`nickname`='" + username + "'";
               // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                    dateTimeCreated = "excDateConflict";
                }

                if (!dateTimeCreated.equals("excDateConflict")) {
                    query = "INSERT INTO tb_datetime(date,time_begin, time_end, schedule_id) "
                            + "VALUES('" + date + "','" + time_begin + "','" + time_end + "'," + schid + ")";
                    if (stat.executeUpdate(query) > 0)
                        dateTimeCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            dateTimeCreated = SESSION_EXPIRED_MSG;
        }
       
        return dateTimeCreated;   
    }
    
    /** Fills the presence.
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param ccdtid. Integer ccdt id.
     * @param stline. String students line, format below:
     * <student_id 1>:<student_id 2>:<student_id 3>
     * @return Returns true if Presence was made, else returns false.
     *          If session expired returns session_expired
     */
    public String AnswerPresence(String username, String cookie, String ip, int ccdtid, String stline) {
        String presenceAnswered = "false";
        String [] presenceList = stline.split(":");
        int totalStudents = presenceList.length;
        int studentsProceeded = 0;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                String query = "";
                Statement stat = conn.createStatement();

                query = "SELECT id_person_present "
                        + "FROM tb_person_present "
                        + "WHERE id_ccdt=" + ccdtid;

                ResultSet result = stat.executeQuery(query);

                boolean presenceExistence = false;
                while(result.next()){
                        presenceExistence = true;
                }

                if (!presenceExistence) {
                    for(int i = 0; i<totalStudents;i++) {
                        query = "INSERT INTO tb_person_present(id_ccdt,person_id) "
                                + "VALUES(" + ccdtid + "," + presenceList[i] + ")";
                        if (stat.executeUpdate(query) > 0)
                            studentsProceeded++;
                    }

                if (totalStudents == studentsProceeded)
                    presenceAnswered = "true";

                }

                conn.close();
                
            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            presenceAnswered = SESSION_EXPIRED_MSG;
        }

        return presenceAnswered;
    }

    /** Gets school settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param scid. Integer school id
     * @return return the school settings in format:
     *          <school name>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String GetSchoolSettings(String username, String cookie, String ip, int scid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name FROM tb_school WHERE school_id=" + scid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line = result.getString("name");                        
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Gets a list of schools
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return returns a list of schools in format:
     *          <school name>:<school id>
     *          <school name 1>:<school id 1>
     *          <school name 2>:<school id 2>
     *          If not exists any school, return null.
     *          If session expired returns session_expired
     */
    public String [] GetSchoolList(String username, String cookie, String ip) {
        String [] schoolList = null;

        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session is Ok

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the person type name from user
                String query = "SELECT `tb_person_type`.`name` FROM tb_person_type "
                        + "INNER JOIN tb_person "
                        + "ON `tb_person_type`.`person_type_id`=`tb_person`.`person_type_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                String ptypename = "";
                while(result.next()){
                    ptypename = result.getString("name");
                }

                if (ptypename.equals("School Manager"))
                    query = "SELECT name, school_id FROM tb_school ORDER BY name";
                else
                    query = "SELECT `tb_school`.`name`, `tb_school`.`school_id` FROM tb_school "
                            + "INNER JOIN tb_person "
                            + "ON `tb_school`.`school_id`=`tb_person`.`school_id` "
                            + "WHERE `tb_person`.`nickname`='" + username + "' ORDER BY name";

                result = stat.executeQuery(query);

                line = "school name:school id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("school_id");
                }

               // System.out.println(line);

                schoolList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            schoolList = line.split("&");
        }

        return schoolList;
    }

    /** Creates a new school
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String name     
     * @return returns true if school was created, else returns false.
     *          If session expired returns session_expired.
     */
    public String CreateNewSchool(String username, String cookie, String ip, String name) {
        String schoolCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session is Ok
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "SELECT name FROM tb_school where name='" + name + "'";
               // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                boolean schoolExists = false;
                while(result.next()){
                    schoolExists = true;
                }

                if (!schoolExists) {
                    query = "INSERT INTO tb_school(name) "
                            + "VALUES('" + name + "')";
                    if (stat.executeUpdate(query) > 0)
                        schoolCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { //session expired
            schoolCreated = SESSION_EXPIRED_MSG;
        }

        return schoolCreated;
    }

    /** Deletes the school
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param sid. Integer school id
     * @return returns true if school was deleted, else returns false.
     *          If session expired returns session_expired
     */
    public String DeleteSchool(String username, String cookie, String ip, int sid) {
        String schoolDeleted = "false";

        if (CheckSession(username, cookie, ip)) { //session is Ok

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "DELETE FROM tb_school WHERE school_id=" + sid;
                if (stat.executeUpdate(query) > 0)
                    schoolDeleted = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { //session expired
            schoolDeleted = SESSION_EXPIRED_MSG;
        }

        return schoolDeleted;
    }
   
    /** Edits the school
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param sname. String school name
     * @param scid. Integer school id
     * @return If school edited returns true, else returns false
     *          If session expired returns session_expired
     */
    public String EditSchoolSettings(String username, String cookie, String ip, String sname, int scid) {
        String schoolEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_school SET name='" + sname
                        + "' WHERE school_id=" + scid;
                if (stat.executeUpdate(query) > 0)
                    schoolEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            schoolEdited = SESSION_EXPIRED_MSG;
        }

        return schoolEdited;
    }

    /** Deletes the datetime
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param dtid. Integer datetime id
     * @return returns true if datetime was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String DeleteDatetime(String username, String cookie, String ip, int dtid) {
        String datetimeDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "DELETE FROM tb_datetime WHERE dt_id=" + dtid;
                if (stat.executeUpdate(query) > 0) {
                    datetimeDeleted = "true";
                }


                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            datetimeDeleted = SESSION_EXPIRED_MSG;
        }

        return datetimeDeleted;
    }

    /** Gets programs list
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return returns a list of programs in format:
     *          <program name>:<program id>:<value>
     *          <name 1>:<program_id 1>:<value 1>
     *          <name 2>:<program_id 2>:<value 2>
     *          If not exists any program, return null.
     *          If session expired returns session_expired
     */
    public String [] GetProgramsList(String username, String cookie, String ip) {
        String [] programsList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_program`.`program_id`,`tb_program`.`name`,"
                        + " `tb_program`.`value` FROM `tb_program` "
                        + "INNER JOIN tb_person ON `tb_program`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_program`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "program name:program_id:value";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("program_id");
                    line += ":" + result.getString("value");
                }

               // System.out.println(line);

                programsList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            programsList = line.split("&");
        }

        return programsList;
    }

    /** Creates new program
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String program name
     * @param value. Integer value     
     * @param description. String description
     * @return If program created returns true, else returns false
     *          If session expired returns session_expired
     */
    public String CreateNewProgram(String username, String cookie, String ip, String name, int value, String description) {
        String programCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                //gets school_id from username
                String query = "SELECT school_id FROM tb_person where nickname='" + username + "'";
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                    scid = Integer.parseInt(result.getString("school_id"));
                }

                query = "SELECT name FROM tb_program where name='" + name
                        + "' AND school_id=" + scid;
               // System.out.println(query);
                result = stat.executeQuery(query);

                boolean programExists = false;
                while(result.next()){
                    programExists = true;
                }

                if (!programExists) {
                    query = "INSERT INTO tb_program(name,school_id,value,description) "
                            + "VALUES('" + name + "'," + scid + "," + value + ",'" + description + "')";
                    if (stat.executeUpdate(query) > 0)
                        programCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            programCreated = SESSION_EXPIRED_MSG;
        }

        return programCreated;
    }

    /** Deletes the program
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer program id
     * @return returns true if program was deleted, else returns false
     *         If session expired returns session_expired
     */
    public String DeleteProgram(String username, String cookie, String ip, int pid) {
        String programDeleted = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "DELETE FROM tb_program WHERE program_id=" + pid;
                if (stat.executeUpdate(query) > 0)
                    programDeleted = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            programDeleted = SESSION_EXPIRED_MSG;
        }

        return programDeleted;
    }

    /** Gets program settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer program id
     * @return return the program settings in format:
     *          <program name>:<value>:<description>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String GetProgramSettings(String username, String cookie, String ip, int pid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name,value,description FROM tb_program WHERE program_id=" + pid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line += result.getString("name");
                        line += ":" + result.getString("value");                        
                        line += ":" + result.getString("description");
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Edits the program
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pname. String program name
     * @param value. Integer value
     * @param description. String description
     * @param pid. Integer program id
     * @return If program edited returns true, else returns false
     *          If session expired returns session_expired
     */
    public String EditProgramSettings(String username, String cookie, String ip, String pname, int value, String description, int pid) {
        String programEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_program SET value=" + value
                        + ", description='" + description
                        + "', name='" + pname
                        + "' WHERE program_id=" + pid;
                if (stat.executeUpdate(query) > 0)
                    programEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            programEdited = SESSION_EXPIRED_MSG;
        }

        return programEdited;
    }

    /** Gets a list of datetimes
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return returns a list of datetimes in format:
     *          <date>:<datetime id>
     *          <date 1>:<datetime id 1>
     *          <date 2>:<datetime id 2>
     *          If not exists any datetimes, return null
     *          If session expired returns session_expired
     */
    public String [] GetDatetimesList(String username, String cookie, String ip) {
        String [] datetimesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();
                    
                String query = "SELECT  DATE_FORMAT(`tb_datetime`.`date`,'%Y/%m/%d') AS `date`,"
                        + " `tb_datetime`.`dt_id` FROM tb_datetime INNER JOIN tb_schedule "
                        + "ON `tb_datetime`.`schedule_id`=`tb_schedule`.`schedule_id` "
                        + "INNER JOIN tb_person ON `tb_schedule`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_datetime`.`date`";

                ResultSet result = stat.executeQuery(query);

                line = "date:datetime id";
                while(result.next()){
                    line += "&" + result.getString("date");
                    line += ":" + result.getString("dt_id");
                }

               // System.out.println(line);

                datetimesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            datetimesList = line.split("&");
        }

        return datetimesList;
    }

    /** Gets the datetime settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param dtid. Integer datetime id
     * @return return the datetime settings in format:
     *          <date>;<time begin>;<time end>;<schedule name>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  GetDatetimeSettings(String username, String cookie, String ip, int dtid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT DATE_FORMAT(`tb_datetime`.`date`, '%Y/%m/%d') AS `date`, "
                        + "TIME_FORMAT(`tb_datetime`.`time_begin`, '%H:%i') AS `tbegin`, "
                        + "TIME_FORMAT(`tb_datetime`.`time_end`, '%H:%i') AS `tend`, "
                        + "`tb_schedule`.`name` AS `schname` "
                        + "FROM tb_datetime "
                        + "INNER JOIN tb_schedule "
                        + "ON `tb_datetime`.`schedule_id`=`tb_schedule`.`schedule_id` "                        
                        + "WHERE `tb_datetime`.`dt_id`=" + dtid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line += result.getString("date");
                        line += ";" + result.getString("tbegin");
                        line += ";" + result.getString("tend");
                        line += ";" + result.getString("schname");                        
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Associates course to program
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param pid. Integer program id
     * @param cline. Integer courses id list
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateCourseToProgram(String username, String cookie, String ip, int pid, String cline) {
        String associationCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "DELETE FROM tb_program_course WHERE program_id=" + pid;
                stat.executeUpdate(query);

                if (!cline.equals("0")) {

                    String [] cList = cline.split(":");
                    int cont = 0;
                    for(int i = 0; i<cList.length;i++) {
                        query = "INSERT INTO tb_program_course(program_id,course_id) "
                                + "VALUES(" + pid + "," + cList[i] + ")";
                        if (stat.executeUpdate(query) > 0) {
                                cont++;
                        }
                    }
                
                
                conn.close();

                if (cont == cList.length)
                    associationCreated = "true";

                }
                else {
                    conn.close();
                    associationCreated = "true";

                }

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationCreated = SESSION_EXPIRED_MSG;
        }

        return associationCreated;
    }

    /** Associates class to course
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @param schid. Integer schedule id
     * @param pid. Integer person id
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateClassToCourse(String username, String cookie, String ip, int coid, int clid, int schid, int pid) {
        String associationCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                boolean associationExists = false;
                while(result.next()){
                    associationExists = true;
                }

                if (!associationExists) {
                    query = "INSERT INTO tb_class_course(course_id,class_id,schedule_id,person_id,finished) "
                            + "VALUES(" + coid + "," + clid + "," + schid + "," + pid + ",0)";
                    if (stat.executeUpdate(query) > 0)
                        associationCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationCreated = SESSION_EXPIRED_MSG;
        }

        return associationCreated;
    }

    /** Gets All courses associated to program.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer program id
     * @return Returns courses list in format:
     *          <course name>:<course id>
     *          <course name 1>:<course id 1>
     *          <course name 2>:<course id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetCoursesAssociatedProgram(String username, String cookie, String ip, int pid) {
        String [] coursesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_course`.`name`,`tb_course`.`course_id` "
                        + "FROM tb_course INNER JOIN tb_program_course "
                        + "ON `tb_course`.`course_id`=`tb_program_course`.`course_id` "
                        + "WHERE `tb_program_course`.`program_id`=" + pid;

                ResultSet result = stat.executeQuery(query);

                line = "course name:course id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("course_id");
                }

               // System.out.println(line);

                coursesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            coursesList = line.split("&");
        }

        return coursesList;
    }

    /** Gets All courses not associated to program.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer program id
     * @return Returns courses list in format:
     *          <course name>:<course id>
     *          <course name 1>:<course id 1>
     *          <course name 2>:<course id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetCoursesNotAssociatedProgram(String username, String cookie, String ip, int pid) {
        String [] coursesList = null;
        String [] coursesAssociatedList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                // gets the associated courses
                String query = "SELECT `tb_course`.`name`,`tb_course`.`course_id` "
                        + "FROM tb_course INNER JOIN tb_program_course "
                        + "ON `tb_course`.`course_id`=`tb_program_course`.`course_id` "
                        + "WHERE `tb_program_course`.`program_id`=" + pid;

                ResultSet result = stat.executeQuery(query);

                line = "course name:course id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("course_id");
                }

               // System.out.println(line);

                coursesAssociatedList = line.split("&");

                //gets all courses
                query = "SELECT `tb_course`.`name`, `tb_course`.`course_id` "
                        + "FROM tb_course INNER JOIN tb_person ON `tb_course`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "' ORDER BY `tb_course`.`name`";

                result = stat.executeQuery(query);

                //serch if course is associated, if not associated, insert this in list.
                line = "course name:course id";
                while(result.next()){
                    boolean found = false;
                    for(int i = 0; i < coursesAssociatedList.length; i++) {
                        String [] lst = coursesAssociatedList[i].split(":");
                        if (lst[1].equals(result.getString("course_id"))) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        line += "&" + result.getString("name");
                        line += ":" + result.getString("course_id");
                    }
                }

                System.out.println(line);

                coursesList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            coursesList = line.split("&");
        }

        return coursesList;
    }

    /** Gets All courses and yours associatons to class.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. Integer class id
     * @return Returns courses list in format:
     *          <course name>:<course id>:<managed OK>
     *          <course name 1>:<course id 1>:<managed OK>
     *          <course name 2>:<course id 2>:<managed OK>
     *          <managed OK> can be 1 or 0
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetCoursesClassList(String username, String cookie, String ip, int cid) {
        String [] coursesList = null;
        String [] coursesAssociatedList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                // gets the associated courses
                String query = "SELECT `tb_course`.`name`, `tb_course`.`course_id` "
                        + "FROM tb_course INNER JOIN tb_class_course "
                        + "ON `tb_course`.`course_id`=`tb_class_course`.`course_id` "
                        + "WHERE `tb_class_course`.`class_id`=" + cid;

                ResultSet result = stat.executeQuery(query);

                line = "course name:course id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("course_id");
                }

               // System.out.println(line);

                coursesAssociatedList = line.split("&");

                //gets all courses
                query = "SELECT `tb_course`.`name`, `tb_course`.`course_id` "
                        + "FROM tb_course INNER JOIN tb_person ON `tb_course`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "' ORDER BY `tb_course`.`name`";

                result = stat.executeQuery(query);

                //serch if course is associated, if not associated, insert this in list.
                line = "course name:course id";
                while(result.next()){
                    boolean found = false;
                    for(int i = 0; i < coursesAssociatedList.length; i++) {
                        String [] lst = coursesAssociatedList[i].split(":");
                        if (lst[1].equals(result.getString("course_id")))
                            found = true;
                    }

                    line += "&" + result.getString("name");
                    line += ":" + result.getString("course_id");

                    if (!found) {
                        line += ":0";
                    }
                    else
                        line += ":1";
                }

                System.out.println(line);

                coursesList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            coursesList = line.split("&");
        }

        return coursesList;
    }

    /** Get class_course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. Integer class id
     * @param coid. Integer course id
     * @return return the class_course settings in format:
     *          <tutor name>:<tutor id>:<schedule name>:<schedule id>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  GetClassCourseSettings(String username, String cookie, String ip, int clid, int coid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`name` AS `tutor`, `tb_person`.`person_id` AS `pid`, "
                        + "`tb_schedule`.`name` AS `schedule`, `tb_schedule`.`schedule_id` AS `schid` "
                        + "FROM tb_class_course INNER JOIN tb_person "
                        + "ON `tb_person`.`person_id`=`tb_class_course`.`person_id` "
                        + "INNER JOIN tb_schedule ON `tb_schedule`.`schedule_id`=`tb_class_course`.`schedule_id` "
                        + "WHERE `tb_class_course`.`class_id`=" + clid + " AND `tb_class_course`.`course_id`=" + coid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line += result.getString("tutor");
                        line += ":" + result.getString("pid");
                        line += ":" + result.getString("schedule");
                        line += ":" + result.getString("schid");
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Edit class_course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. Integer class id
     * @param coid. Integer course id
     * @param schid. Integer schedule id
     * @param pid. Integer person id
     * @return returns true if course was edited, else returns false
     *          If session expired returns session_expired
     */
    public String EditClassToCourse(String username, String cookie, String ip, int clid, int coid, int schid, int pid) {
        String courseEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_class_course SET schedule_id=" + schid
                        + ", person_id=" + pid
                        + " WHERE course_id=" + coid + " AND class_id=" + clid;
                if (stat.executeUpdate(query) > 0)
                    courseEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            courseEdited = SESSION_EXPIRED_MSG;
        }

        return courseEdited;
    }    

    /** Get tutor settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer person id     
     * @return return the tutor settings in format:
     *          <nickname>:<fullname>:<email>:<telephone>:<obs>:<school name>:<school id>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  GetTutorSettings(String username, String cookie, String ip, int pid) {
        String line = "";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`nickname`, `tb_person`.`name` AS `fullname`, "
                        + "`tb_person`.`email`, `tb_person`.`telephone`, `tb_person`.`obs`, "
                        + "`tb_person`.`school_id`, `tb_school`.`name` AS `school_name` "
                        + "FROM tb_person INNER JOIN tb_school ON `tb_person`.`school_id`=`tb_school`.`school_id` "
                        + "WHERE `tb_person`.`person_id`=" + pid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        line += result.getString("nickname");
                        line += ":" + result.getString("fullname");
                        line += ":" + result.getString("email");
                        line += ":" + result.getString("telephone");
                        line += ":" + result.getString("obs");
                        line += ":" + result.getString("school_name");
                        line += ":" + result.getString("school_id");
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Creates new grade
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. Integer class id
     * @param coid. Integer course id
     * @param pid. Integer person id from student     
     * @return If grade created returns true, else returns false
     *          If session expired returns session_expired
     */
    public String CreateNewGrade(String username, String cookie, String ip, int clid, int coid, int pid) {
        String gradeCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                //gets school_id from username
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE class_id=" + clid + " AND course_id=" + coid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                int clcoid = 0;
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                query = "SELECT grade FROM tb_grade WHERE class_course_id=" + clcoid
                        + "' AND person_id=" + pid;
               // System.out.println(query);
                result = stat.executeQuery(query);

                boolean gradeExists = false;
                while(result.next()){
                    gradeExists = true;
                }

                if (!gradeExists) {
                    query = "INSERT INTO tb_grade(class_course_id,person_id,grade) "
                            + "VALUES(" + clcoid + "," + pid + ",0.0)";
                    if (stat.executeUpdate(query) > 0)
                        gradeCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            gradeCreated = SESSION_EXPIRED_MSG;
        }

        return gradeCreated;
    }

    /** Gets All classes associated to course.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. Integer course id
     * @return Returns courses list in format:
     *          <class name>:<class id>
     *          <class name 1>:<class id 1>
     *          <class name 2>:<class id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetClassesAssociatedCourse(String username, String cookie, String ip, int coid) {
        String [] classesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_class`.`name`,`tb_class`.`class_id` "
                        + "FROM tb_class INNER JOIN tb_class_course "
                        + "ON `tb_class`.`class_id`=`tb_class_course`.`class_id` "
                        + "WHERE `tb_class_course`.`course_id`=" + coid;

                ResultSet result = stat.executeQuery(query);

                line = "class name:class id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("class_id");
                }

               // System.out.println(line);

                classesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            classesList = line.split("&");
        }

        return classesList;
    }

    /** Gets All students associated to grade.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetStudentsAssociatedGrade(String username, String cookie, String ip, int coid, int clid) {
        String [] studentsList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                // gets the class_course id
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" +  clid;

                ResultSet result = stat.executeQuery(query);
                
                int clcoid = 0;
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                //gets the students associated with grade
                query = "SELECT `tb_person`.`name` AS `student`, `tb_person`.`person_id` AS `pid` "
                        + "FROM tb_person INNER JOIN tb_grade "
                        + "ON `tb_person`.`person_id`=`tb_grade`.`person_id` "
                        + "WHERE `tb_grade`.`class_course_id`=" + clcoid + " ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                line = "student name:student id";
                while(result.next()){
                    line += "&" + result.getString("student");
                    line += ":" + result.getString("pid");
                }

               // System.out.println(line);

                studentsList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            studentsList = line.split("&");
        }

        return studentsList;
    }

    /** Gets All students not associated to grade.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetStudentsNotAssociatedGrade(String username, String cookie, String ip, int coid, int clid) {
        String [] studentsList = null;
        String [] studentsAssociatedList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                // gets the class_course id
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;

                ResultSet result = stat.executeQuery(query);
                
                int clcoid = 0;
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }                
                
                //gets the students associated with grade
                query = "SELECT `tb_person`.`name` AS `student`, `tb_person`.`person_id` AS `pid` "
                        + "FROM tb_person INNER JOIN tb_grade "
                        + "ON `tb_person`.`person_id`=`tb_grade`.`person_id` "
                        + "WHERE `tb_grade`.`class_course_id`=" + clcoid + " ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                line = "student name:student id";
                while(result.next()){
                    line += "&" + result.getString("student");
                    line += ":" + result.getString("pid");
                }

               // System.out.println(line);

                studentsAssociatedList = line.split("&");

                //gets all students
                query = "SELECT `tb_person`.`name` AS `student`, `tb_person`.`person_id` AS `pid` "
                        + "FROM tb_person INNER JOIN tb_person_type "
                        + "ON `tb_person`.`person_type_id`=`tb_person_type`.`person_type_id` "
                        + "WHERE `tb_person_type`.`name`='Student' ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                //serch if student is associated, if not associated, insert this in list.
                line = "student name:student id";
                while(result.next()){
                    boolean found = false;
                    for(int i = 0; i < studentsAssociatedList.length; i++) {
                        String [] lst = studentsAssociatedList[i].split(":");
                        if (lst[1].equals(result.getString("pid"))) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        line += "&" + result.getString("student");
                        line += ":" + result.getString("pid");
                    }
                }

                System.out.println(line);

                studentsList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            studentsList = line.split("&");
        }

        return studentsList;
    }

    /** Associates students to grade
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @param stline. Integer students id list
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateStudentsToGrade(String username, String cookie, String ip, int coid, int clid, String stline) {
        String associationCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                // gets the class_course id
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;

                ResultSet result = stat.executeQuery(query);

                int clcoid = 0;
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }
                

                query = "DELETE FROM tb_grade WHERE class_course_id=" + clcoid;
                stat.executeUpdate(query);

                if (!stline.equals("0")) {
                    String [] stList = stline.split(":");
                    int cont = 0;
                    for(int i = 0; i<stList.length;i++) {
                        query = "INSERT INTO tb_grade(class_course_id,person_id,grade) "
                                + "VALUES(" + clcoid + "," + stList[i] + ",0.0)";
                        if (stat.executeUpdate(query) > 0) {
                                cont++;
                        }
                    }
                
                    conn.close();

                    if (cont == stList.length)
                        associationCreated = "true";

                }
                else {
                    conn.close();
                    associationCreated = "true";
                }

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationCreated = SESSION_EXPIRED_MSG;
        }

        return associationCreated;
    }    

    /** Gets All classes from program.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer program id
     * @return Returns classes list in format:
     *          <class name>:<class id>
     *          <class name 1>:<class id 1>
     *          <class name 2>:<class id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetClassesFromProgram(String username, String cookie, String ip, int pid) {
        String [] classesList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the school_id from system user
                String query = "SELECT school_id FROM tb_person "
                        + "WHERE nickname='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                int schid = 0;
                while(result.next()){
                    schid = Integer.parseInt(result.getString("school_id"));                    
                }

                // getting the classes from program
                query = "SELECT `tb_class`.`name`,`tb_class`.`class_id` "
                        + "FROM tb_class WHERE program_id =" + pid
                        + " AND school_id=" + schid
                        + " ORDER BY `tb_class`.`name`";

                result = stat.executeQuery(query);

                line = "class name:class id";
                while(result.next()){
                    line += "&" + result.getString("name");
                    line += ":" + result.getString("class_id");
                }

               // System.out.println(line);

                classesList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            classesList = line.split("&");
        }

        return classesList;
    }

    /** Gets All students associated to enrollment.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip     
     * @param clid. Integer class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetStudentsAssociatedEnrollment(String username, String cookie, String ip, int clid) {
        String [] studentsList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();                

                //gets the students associated with enrollment
                String query = "SELECT `tb_person`.`name` AS `student`, `tb_person`.`person_id` AS `pid` "
                        + "FROM tb_person INNER JOIN tb_enrollment "
                        + "ON `tb_person`.`person_id`=`tb_enrollment`.`person_id` "
                        + "WHERE `tb_enrollment`.`class_id`=" + clid + " ORDER BY `tb_person`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "student name:student id";
                while(result.next()){
                    line += "&" + result.getString("student");
                    line += ":" + result.getString("pid");
                }

               // System.out.println(line);

                studentsList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            studentsList = line.split("&");
        }

        return studentsList;
    }

    /** Gets All students not associated to enrollment.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. Integer class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] GetStudentsNotAssociatedEnrollment(String username, String cookie, String ip, int clid) {
        String [] studentsList = null;
        String [] studentsAssociatedList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();
                                
                //gets the students associated with grade
                String query = "SELECT `tb_person`.`name` AS `student`, `tb_person`.`person_id` AS `pid` "
                        + "FROM tb_person INNER JOIN tb_enrollment "
                        + "ON `tb_person`.`person_id`=`tb_enrollment`.`person_id` "
                        + "WHERE `tb_enrollment`.`class_id`=" + clid + " ORDER BY `tb_person`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "student name:student id";
                while(result.next()){
                    line += "&" + result.getString("student");
                    line += ":" + result.getString("pid");
                }

               // System.out.println(line);

                studentsAssociatedList = line.split("&");

                //gets the school id
                query = "SELECT `tb_person`.`school_id` "
                        + "FROM tb_person "                        
                        + "WHERE `tb_person`.`nickname`='" + username + "'";

                result = stat.executeQuery(query);
                
                int schid = 0;
                while(result.next()){
                    schid = Integer.parseInt(result.getString("school_id"));
                }

                //gets all students
                query = "SELECT `tb_person`.`name` AS `student`, `tb_person`.`person_id` AS `pid` "
                        + "FROM tb_person INNER JOIN tb_person_type "
                        + "ON `tb_person`.`person_type_id`=`tb_person_type`.`person_type_id` "
                        + "WHERE `tb_person_type`.`name`='Student' "
                        + "AND `tb_person`.`school_id`=" + schid
                        + " ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                //serch if student is associated, if not associated, insert this in list.
                line = "student name:student id";
                while(result.next()){
                    boolean found = false;
                    for(int i = 0; i < studentsAssociatedList.length; i++) {
                        String [] lst = studentsAssociatedList[i].split(":");
                        if (lst[1].equals(result.getString("pid"))) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        line += "&" + result.getString("student");
                        line += ":" + result.getString("pid");
                    }
                }

                System.out.println(line);

                studentsList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            studentsList = line.split("&");
        }

        return studentsList;
    }

    /** Associates students to enrollment
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip     
     * @param clid. Integer class id
     * @param stline. Integer students id list in format:
     *          <student id 1>:<student id 2>:<student id 3>
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateStudentsToEnrollment(String username, String cookie, String ip, int clid, String stline) {
        String associationCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();                

                // gets the school id
                String query = "SELECT school_id FROM tb_person "
                        + "WHERE nickname='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                int schid = 0;
                while(result.next()){
                    schid = Integer.parseInt(result.getString("school_id"));
                }

                query = "DELETE FROM tb_enrollment WHERE class_id=" + clid;
                stat.executeUpdate(query);

                if (!stline.equals("0")) {
                    String [] stList = stline.split(":");
                    int cont = 0;
                    for(int i = 0; i<stList.length;i++) {
                        query = "INSERT INTO tb_enrollment(class_id,person_id,school_id,payment_ok) "
                                + "VALUES(" + clid + "," + stList[i] + "," + schid + ",0)";
                        if (stat.executeUpdate(query) > 0) {
                                cont++;
                        }
                    }

                    conn.close();

                    if (cont == stList.length)
                        associationCreated = "true";

                }
                else {
                    conn.close();
                    associationCreated = "true";
                }

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationCreated = SESSION_EXPIRED_MSG;
        }

        return associationCreated;
    }

    /** Gets the schedule from class_course
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param course_id. Integer course id.
     * @param class_id. Integer class id.     
     * @return returns a list of schedules in format:
     *          <schedule name>:<schedule id>
     *          <schedule name>:<schedule id>
     *          <schedule name>:<schedule id>
     *          If not exists any schedule, return null
     */
    public String [] GetScheduleFromClassCourse(String username, String cookie, String ip, int coid, int clid) {
        String [] scheduleList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_schedule`.`name`, `tb_schedule`.`schedule_id` "
                        + "FROM tb_schedule INNER JOIN tb_class_course "
                        + "ON `tb_schedule`.`schedule_id`=`tb_class_course`.`schedule_id` "
                        + "WHERE class_id=" + clid + " AND course_id=" + coid;

                ResultSet result = stat.executeQuery(query);

                line = "schedule name:schedule id";
                while(result.next()){
                        line += "&" + result.getString("name");
                        line += ":" + result.getString("schedule_id");
                }

               // System.out.println(line);

                scheduleList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            scheduleList = line.split("&");
        }

        return scheduleList;
    }

    /** Associates event
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @param dtid. Integer datetime id
     * @param ambid. Integer ambiance id
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateEvent(String username, String cookie, String ip, int coid, int clid, int dtid, int ambid) {
        String associationCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                //getting the class_course_id
                int clcoid = 0;
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                query = "SELECT id_ccdt FROM tb_ccdt "
                        + "WHERE class_course_id=" + clcoid + " AND dt_id=" + dtid;
                // System.out.println(query);
                result = stat.executeQuery(query);

                int ccdtid = 0;
                boolean associationExists = false;
                while(result.next()){
                    associationExists = true;
                    ccdtid = Integer.parseInt(result.getString("id_ccdt"));
                }

                if (!associationExists) { // creates a new ccdt, then creates a new ccdta
                    query = "INSERT INTO tb_ccdt(class_course_id,dt_id,event_comments) "
                        + "VALUES(" + clcoid + "," + dtid + ",NULL)";
                    if (stat.executeUpdate(query) > 0) {
                        query = "SELECT id_ccdt FROM tb_ccdt "
                                + "WHERE class_course_id=" + clcoid + " AND dt_id=" + dtid;
                        // System.out.println(query);
                        result = stat.executeQuery(query);

                        ccdtid = 0;
                        while(result.next()){
                            ccdtid = Integer.parseInt(result.getString("id_ccdt"));
                        }

                        query = "INSERT INTO tb_ccdta(id_ccdt,ambiance_id) "
                            + "VALUES(" + ccdtid + "," + ambid + ")";
                        if (stat.executeUpdate(query) > 0) {
                            associationCreated = "true";
                        }
                    }                        
                } else { // gets the ccdtid and creates the ccdta
                    query = "SELECT id_ccdta FROM tb_ccdta "
                            + "WHERE id_ccdt=" + ccdtid + " AND ambiance_id=" + ambid;
                    // System.out.println(query);
                    result = stat.executeQuery(query);

                    int ccdtaid = 0;
                    boolean ccdtaExists = false;
                    while(result.next()){
                        ccdtaid = Integer.parseInt(result.getString("id_ccdta"));
                        ccdtaExists = true;
                    }

                    if (ccdtaExists) {
                        query = "UPDATE tb_ccdta SET "
                        + "ambiance_id=" + ambid
                        + " WHERE id_ccdta=" + ccdtaid;
                    } else {
                        query = "INSERT INTO tb_ccdta(id_ccdt,ambiance_id) "
                        + "VALUES(" + ccdtid + "," + ambid + ")";
                    }
                    
                    if (stat.executeUpdate(query) > 0) {
                        associationCreated = "true";
                    }
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationCreated = SESSION_EXPIRED_MSG;
        }

        return associationCreated;
    }

    /** Associates events
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @param evtline. String with list of ambiance id and datetime id in format below:
     *          <datetime id 1>,<ambiance id 1>:<datetime id 2>,<ambiance id 2>     
     * @return Returns true if associations created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateEvents(String username, String cookie, String ip, int coid, int clid, String evtline) {
        String associationsCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                //getting the class_course_id
                int clcoid = 0;
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                // spliting evtline
                int associationCount = 0;
                String [] evtLst = evtline.split(":");
                for(int i = 0; i < evtLst.length; i++) {
                    String [] evtProp = evtLst[i].split(",");
                    query = "SELECT id_ccdt FROM tb_ccdt "
                            + "WHERE class_course_id=" + clcoid + " AND dt_id=" + evtProp[0];
                    // System.out.println(query);
                    result = stat.executeQuery(query);

                    int ccdtid = 0;
                    boolean associationExists = false;
                    while(result.next()){
                        associationExists = true;
                        ccdtid = Integer.parseInt(result.getString("id_ccdt"));
                    }

                    if (!associationExists) { // creates a new ccdt, then creates a new ccdta
                        query = "INSERT INTO tb_ccdt(class_course_id,dt_id,event_comments) "
                            + "VALUES(" + clcoid + "," + evtProp[0] + ",NULL)";
                        if (stat.executeUpdate(query) > 0) {
                            query = "SELECT id_ccdt FROM tb_ccdt "
                                    + "WHERE class_course_id=" + clcoid + " AND dt_id=" + evtProp[0];
                            // System.out.println(query);
                            result = stat.executeQuery(query);

                            ccdtid = 0;
                            while(result.next()){
                                ccdtid = Integer.parseInt(result.getString("id_ccdt"));
                            }

                            query = "INSERT INTO tb_ccdta(id_ccdt,ambiance_id) "
                                + "VALUES(" + ccdtid + "," + evtProp[1] + ")";
                            if (stat.executeUpdate(query) > 0) {
                                associationCount++;
                            }
                        }
                    } else { // gets the ccdtid and creates the ccdta
                        query = "SELECT id_ccdta FROM tb_ccdta "
                                + "WHERE id_ccdt=" + ccdtid;
                        // System.out.println(query);
                        result = stat.executeQuery(query);

                        int ccdtaid = 0;
                        boolean ccdtaExists = false;
                        while(result.next()){
                            ccdtaid = Integer.parseInt(result.getString("id_ccdta"));
                            ccdtaExists = true;
                        }

                        if (ccdtaExists) {
                            query = "UPDATE tb_ccdta SET "
                            + "ambiance_id=" + evtProp[1]
                            + " WHERE id_ccdta=" + ccdtaid;
                        } else {
                            query = "INSERT INTO tb_ccdta(id_ccdt,ambiance_id) "
                            + "VALUES(" + ccdtid + "," + evtProp[1] + ")";
                        }

                        if (stat.executeUpdate(query) > 0) {
                            associationCount++;
                        }
                    }
                }

                // checks if associations created is equal event line count
                if (associationCount == evtLst.length)
                    associationsCreated = "true";
                
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationsCreated = SESSION_EXPIRED_MSG;
        }

        return associationsCreated;
    }

    /** Gets the ccdt list from schedule and class_course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @param schid. Integer schedule id
     * @param show_past_dates. Integer show past dates. If 0 do not show past dates, else shows.
     *
     * @return return the ccdt list in format:
     *          <id ccdt>;<date>;<time_begin>;<time_end>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String [] GetCcdtFromScheduleClassCourse(String username, String cookie, String ip, int coid, int clid, int schid, int show_past_dates) {
        String line = SESSION_EXPIRED_MSG;
        String [] ccdtList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the class_course_id
                int clcoid = 0;
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                //getting the current date
                SimpleDateFormat sdf1= new SimpleDateFormat("yyyy/MM/dd");
                Date curdate= new Date();
                String curDateStr = sdf1.format(curdate);

                if (show_past_dates != 0) { // show past dates
                    query = "SELECT `tb_ccdt`.`id_ccdt`, "
                            + "DATE_FORMAT(`tb_datetime`.`date`,'%Y/%m/%d') as `date`, "
                            + "TIME_FORMAT(`tb_datetime`.`time_begin`,'%H:%i') as `time_begin`, "
                            + "TIME_FORMAT(`tb_datetime`.`time_end`,'%H:%i') as `time_end` "
                            + "FROM tb_ccdt INNER JOIN tb_datetime "
                            + "ON `tb_datetime`.`dt_id`=`tb_ccdt`.`dt_id` "
                            + "WHERE `tb_ccdt`.`class_course_id`=" + clcoid
                            + " AND `tb_datetime`.`schedule_id`=" + schid
                            + " ORDER BY `tb_datetime`.`date`";
                } else { //do not show past dates
                    query = "SELECT `tb_ccdt`.`id_ccdt`, "
                            + "DATE_FORMAT(`tb_datetime`.`date`,'%Y/%m/%d') as `date`, "
                            + "TIME_FORMAT(`tb_datetime`.`time_begin`,'%H:%i') as `time_begin`, "
                            + "TIME_FORMAT(`tb_datetime`.`time_end`,'%H:%i') as `time_end` "
                            + "FROM tb_ccdt INNER JOIN tb_datetime "
                            + "ON `tb_datetime`.`dt_id`=`tb_ccdt`.`dt_id` "
                            + "WHERE `tb_ccdt`.`class_course_id`=" + clcoid
                            + " AND `tb_datetime`.`schedule_id`=" + schid
                            + " AND `tb_datetime`.`date`>='" + curDateStr
                            + "' ORDER BY `tb_datetime`.`date`";
                }

                result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.equals(""))
                        line += result.getString("id_ccdt");
                    else
                        line += "&" + result.getString("id_ccdt");

                    line += ";" + result.getString("date");
                    line += ";" + result.getString("time_begin");
                    line += ";" + result.getString("time_end");
                }

                ccdtList = line.split("&");

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ccdtList = line.split("&");
        }

        return ccdtList;
    }

    /** Associates ambiance and datetime
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param ccdtid. Integer ccdt id
     * @param ambid. Integer ambiance id
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String AssociateAmbianceCcdt(String username, String cookie, String ip, int ccdtid, int ambid) {
        String associationCreated = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();
                
                String query = "SELECT id_ccdta FROM tb_ccdta "
                        + "WHERE id_ccdt=" + ccdtid + " AND ambiance_id=" + ambid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                boolean associationExists = false;
                while(result.next()){
                    associationExists = true;
                }

                if (!associationExists) {
                    query = "INSERT INTO tb_ccdta(id_ccdt,ambiance_id) "
                            + "VALUES(" + ccdtid + "," + ambid + ")";
                    
                    if (stat.executeUpdate(query) > 0)
                        associationCreated = "true";
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            associationCreated = SESSION_EXPIRED_MSG;
        }

        return associationCreated;
    }

    /** Assigns the grade.
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. Integer course id.
     * @param clid. Integer class id.
     * @param gline. String grade line, format below:
     * <student_id 1>:<grade>;<student_id 2>:<grade>;<student_id 3>:<grade>
     * @return Returns true if grade was assigned, else returns false.
     *          If session expired returns session_expired
     */
    public String AssignGrade(String username, String cookie, String ip, int coid, int clid, String gline) {
        String gradeAssigned = "false";
        String [] gradeList = gline.split(";");
        int totalGrades = gradeList.length;
        int studentsProceeded = 0;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                String query = "";
                Statement stat = conn.createStatement();

                //getting class_course_id
                int clcoid = 0;
                query = "SELECT class_course_id "
                        + "FROM tb_class_course "
                        + "WHERE course_id=" + coid + " AND class_id=" + clid;

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                for(int i = 0; i<totalGrades;i++) {
                    String [] lineGrade = gradeList[i].split(":");

                    //search if student already had your grade assigned
                    boolean found = false;
                    query = "SELECT grade_id "
                        + "FROM tb_grade "
                        + "WHERE class_course_id=" + clcoid + " AND person_id=" + lineGrade[0];

                    result = stat.executeQuery(query);

                    while(result.next()){
                        found = true;
                    }

                    if (found) {
                        query = "UPDATE tb_grade SET grade=" + Float.parseFloat(lineGrade[1])
                                + " WHERE person_id=" + lineGrade[0]
                                + " AND class_course_id=" + clcoid;
                    } else {
                        query = "INSERT INTO tb_grade(class_course_id,person_id,grade)"
                                + " VALUES(" + clcoid + "," + lineGrade[0] + "," + Float.parseFloat(lineGrade[1]) + ")";
                    }

                    if (stat.executeUpdate(query) > 0)
                        studentsProceeded++;
                }

                if (totalGrades == studentsProceeded) {
                    query = "UPDATE tb_class_course SET finished=1"
                            + " WHERE class_course_id=" + clcoid;
                    if (stat.executeUpdate(query) > 0)
                        gradeAssigned = "true";
                }

                

                conn.close();
                
            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            gradeAssigned = SESSION_EXPIRED_MSG;
        }

        return gradeAssigned;
    }

    /** Is class_course finished
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. Integer course id.
     * @param clid. Integer class id.
     * @return returns true if class_course is finished, else returns false
     *          If session expired returns session_expired
     */
    public String IsClassCourseFinished(String username, String cookie, String ip, int coid, int clid) {
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_class_course`.`finished` "
                        + "FROM tb_class_course INNER JOIN tb_person "
                        + "ON `tb_person`.`person_id`=`tb_class_course`.`person_id` "
                        + "WHERE class_id =" + clid
                        + " AND course_id=" + coid
                        + " AND `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                line = "false";
                while(result.next()){
                    if (!result.getString("finished").equals("0"))
                        line = "true";
                }

               // System.out.println(line);             

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Gets the grade report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. Integer course id.
     * @param clid. Integer class id.
     * @return returns grade list in format:
     *          <student>:<grade>:<situation>
     *          <student 1>:<grade 1>:<situation 1>
     *          <student 2>:<grade 2>:<situation 2>
     *          <situation> = 1 grade >= grade_min
     *          <situation> = 0 grade < grade_min
     *
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] ShowGradeReport(String username, String cookie, String ip, int coid, int clid) {
        String line = SESSION_EXPIRED_MSG;
        String [] gradeList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the grade_min
                String query = "SELECT grade_min FROM tb_globals";
                        
                ResultSet result = stat.executeQuery(query);

                float grade_min = 0;
                while(result.next()){
                    grade_min = Float.parseFloat(result.getString("grade_min"));                        
                }

                query = "SELECT `tb_person`.`name` AS `student`, `tb_grade`.`grade` "
                        + "FROM tb_grade INNER JOIN tb_person "
                        + "ON `tb_grade`.`person_id`=`tb_person`.`person_id` "
                        + "INNER JOIN tb_class_course "
                        + "ON `tb_grade`.`class_course_id`=`tb_class_course`.`class_course_id` "
                        + "WHERE `tb_class_course`.`class_id`=" + clid
                        + " AND `tb_class_course`.`course_id`=" + coid
                        + " ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                line = "student:grade:situation";
                while(result.next()){
                    line += "&" + result.getString("student");
                    line += ":" + result.getString("grade");

                    if (Float.parseFloat(result.getString("grade")) >= grade_min)
                        line += ":1";
                    else
                        line += ":0";
                }

               // System.out.println(line);

                System.out.println(line);

                gradeList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            gradeList = line.split("&");
        }

        return gradeList;
    }

    /** Gets the presence report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param clid. Integer class id.
     * @param ccdtid. Integer ccdt id.
     *
     * @return returns presence list in format:
     *          <student>:<presence>
     *          <student 1>:<presence 1>
     *          <student 2>:<presence 2>
     *          <presence> = 1 present
     *          <presence> = 0 absent
     *
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] ShowPresenceReport(String username, String cookie, String ip, int clid, int ccdtid) {
        String line = SESSION_EXPIRED_MSG;
        String [] presenceList = null;
        String [] gradeList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the present persons id list
                String query = "SELECT person_id FROM tb_person_present WHERE id_ccdt=" + ccdtid;

                ResultSet result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.equals(""))
                        line += result.getString("person_id");
                    else
                        line += ":" + result.getString("person_id");
                }

                presenceList = line.split(":");

                query = "SELECT `tb_person`.`name` AS `student`,  `tb_person`.`person_id` AS `sid` "
                        + "FROM tb_enrollment INNER JOIN tb_person "
                        + "ON `tb_person`.`person_id`=`tb_enrollment`.`person_id` "                        
                        + "WHERE `tb_enrollment`.`class_id`=" + clid
                        + " ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                line = "student:presence";
                while(result.next()){
                    line += "&" + result.getString("student");
                    
                    boolean found = false;
                    for(int i = 0; i < presenceList.length; i++) {
                        if (result.getString("sid").equals(presenceList[i]))
                            found = true;
                    }

                    if (found)
                        line += ":1";
                    else
                        line += ":0";
                }

               // System.out.println(line);

                System.out.println(line);

                gradeList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            gradeList = line.split("&");
        }

        return gradeList;
    }

    /** Performs payment
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. Integer class id
     * @param sid. Integer student id
     * @return Returns true if payment was performed, else returns false.
     *          If session expired returns session_expired
     */
    public String PerformPayment(String username, String cookie, String ip, int clid, int sid) {
        String paymentPerformed = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                // getting the payment_ok value
                boolean payment_ok = false;
                String query = "SELECT payment_ok FROM tb_enrollment "
                        + "WHERE class_id=" + clid
                        + " AND person_id=" + sid;
               // System.out.println(query);
                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    if (!result.getString("payment_ok").equals("0")) {
                        payment_ok = true;
                    }
                }
                
                if (!payment_ok) {
                    query = "UPDATE tb_enrollment SET payment_ok=1"
                            + " WHERE class_id=" + clid
                            + " AND person_id=" + sid;
                    if (stat.executeUpdate(query) > 0) {
                        paymentPerformed = "true";
                        
                        //getting the fullname of the sid and class name of the clid
                        String fullname = "";
                        String class_name = "";
                        query = "SELECT `tb_person`.`name` AS `student`, `tb_class`.`name` AS `class` "
                                + "FROM tb_person INNER JOIN tb_class "
                                + "WHERE person_id=" + sid
                                + " AND class_id=" + clid;
                        // System.out.println(query);
                        result = stat.executeQuery(query);

                        while(result.next()){
                            fullname = result.getString("student");
                            class_name = result.getString("class");
                        }

                        //Add log
                        String event = "Payment performed for student " + fullname
                                + " and class " + class_name;

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String curtime = dateFormat.format(date);

                        query = "INSERT INTO tb_log(username,ip,event,curtime) "
                            + "VALUES('" + username + "','" + ip + "','" + event + "','" + curtime + "')";
                        stat.executeUpdate(query);
                    }
                }
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            paymentPerformed = SESSION_EXPIRED_MSG;
        }

        return paymentPerformed;
    }

    /** Unlocks the class course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. Integer class id
     * @param coid. Integer course id
     * @return Returns true if class course was unlocked, else returns false.
     *          If session expired returns session_expired
     */
    public String UnlockClassCourse(String username, String cookie, String ip, int clid, int coid) {
        String classCourseUnlocked = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();
                
                String query = "UPDATE tb_class_course SET finished=0"
                        + " WHERE class_id=" + clid
                        + " AND course_id=" + coid;
                if (stat.executeUpdate(query) > 0) {
                    classCourseUnlocked = "true";
                }
                
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            classCourseUnlocked = SESSION_EXPIRED_MSG;
        }

        return classCourseUnlocked;
    }

    /** Gets the ccdt comments
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ccdtid. Integer ccdt id
     *
     * @return return the ccdt comments, else return empty string
     *         If session expired returns session_expired
     */
    public String GetCcdtComments(String username, String cookie, String ip, int ccdtid) {
        String line = SESSION_EXPIRED_MSG;
        
        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                line = "";
                String query = "SELECT event_comments FROM tb_ccdt "
                        + "WHERE id_ccdt=" + ccdtid;
                // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                    line = result.getString("event_comments");
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Updates the event comments
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param ccdtid. Integer ccdt id
     * @param comments. String comments
     * @return Returns true if comments updated, else returns false.
     *          If session expired returns session_expired
     */
    public String CommentEvent(String username, String cookie, String ip, int ccdtid, String comments) {
        String eventCommented = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "UPDATE tb_ccdt SET event_comments='" + comments
                    + "' WHERE id_ccdt=" + ccdtid;

                if (stat.executeUpdate(query) > 0)
                    eventCommented = "true";


                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            eventCommented = SESSION_EXPIRED_MSG;
        }

        return eventCommented;
    }

    /** Gets the person type from username
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns false if an error occurs.
     *          Returns person type in format:
     *          <person type name>:<person type id>
     *          If session expired returns session_expired
     */
    public String GetPersonTypeFromUser(String username, String cookie, String ip) {
        String personTypeLine = "false";

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person_type`.`name`, `tb_person_type`.`person_type_id` "
                        + "FROM tb_person_type INNER JOIN tb_person "
                        + "ON `tb_person_type`.`person_type_id`=`tb_person`.`person_type_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                    personTypeLine = result.getString("name");
                    personTypeLine += ":" + result.getString("person_type_id");
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            personTypeLine = SESSION_EXPIRED_MSG;
        }

        return personTypeLine;
    }

    /** Gets the program_course report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.     
     *
     * @return returns program_course list in format:
     *          <program 1>,<program 1 id>;<course 1>,<course 1 id>:<course 2>,<course 2 id>
     *          <program 2>,<program 2 id>;<course 2>,<course 2 id>
     *
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] ShowProgramCourseReport(String username, String cookie, String ip) {
        String line = SESSION_EXPIRED_MSG;
        String [] programCourseList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the programs list
                String query = "SELECT `tb_program`.`name` AS `program`, `tb_program`.`program_id` AS `program_id` "
                        + "FROM tb_program INNER JOIN tb_person "
                        + "ON `tb_person`.`school_id`=`tb_program`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "' ORDER BY `tb_program`.`name`";

                ResultSet result = stat.executeQuery(query);

                line = "";

                while(result.next()){
                    String query2 = "SELECT `tb_course`.`name` AS  `course`, `tb_course`.`course_id` AS `course_id` "
                            + "FROM tb_course INNER JOIN tb_program_course "
                            + "ON `tb_program_course`.`course_id`=`tb_course`.`course_id` "
                            + "WHERE `tb_program_course`.`program_id`=" + result.getString("program_id")
                            + " ORDER BY `tb_course`.`name`";
                    Statement stat2 = conn.createStatement();
                    ResultSet result2 = stat2.executeQuery(query2);

                    if (line.equals(""))
                        line = result.getString("program") + "," + result.getString("program_id");
                    else
                        line += "&" + result.getString("program") + "," + result.getString("program_id");

                    String line2 = "";
                    while(result2.next()) {
                        if (line2.isEmpty())
                            line2 = ";" + result2.getString("course") + "," + result2.getString("course_id");
                        else
                            line2 += ":" + result2.getString("course") + "," + result2.getString("course_id");
                    }
                    line += line2;
                }
              
                // System.out.println(line);

                System.out.println(line);

                                
                //line = "a&b";

                programCourseList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            programCourseList = line.split("&");
        }

        return programCourseList;
    }

    /** Creates a datetimes list in interval between date begin and date end
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param schid. Integer schedule id
     * @param dtbegin. String date begin
     * @param dtend. String date end
     * @param tline. String days of week thats datetimes will be created, format below:
     *          <day of week id 1>,<time begin 1>,<time end 1>;<day of week id 2>,<time begin 2>,<time end 2>
     *          day of week id is:
     *              1 - sunday
     *              2 - monday
     *              3 - tuesday
     *              4 - wednesday
     *              5 - thursday
     *              6 - friday
     *              7 - saturday
     * @return Returns true if the datetimes list was created, else returns false
     *          If session expired returns session_expired
     */
    public String CreateDatetimesList(String username, String cookie, String ip, int schid, String dtbegin, String dtend, String tline) {
        String datetimesCreated = "false";
        boolean datetimesExists = false;

        if (CheckSession(username, cookie, ip)) { //session OK
            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);


               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //String days_of_week = "3,08:00,10:00;5,10:00,12:00;6,10:00,12:00";
                String days_of_week = tline;

                String DATE_FORMAT = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

                String dateBegin = dtbegin;
                String[] line = dateBegin.split("/");
                GregorianCalendar gcBegin = new GregorianCalendar(Integer.parseInt(line[0]), Integer.parseInt(line[1]) - 1, Integer.parseInt(line[2]));
                //System.out.println(gc.get(gcBegin.DAY_OF_WEEK)); //Mostra qual o dia da semana 1=domingo, 2=segunda, etc

                String dateFinal = dtend;
                line = dateFinal.split("/");
                GregorianCalendar gcFinal = new GregorianCalendar(Integer.parseInt(line[0]), Integer.parseInt(line[1]) - 1, Integer.parseInt(line[2]));

                int totalDays = 0;
                int totalWrote = 0;
                while ((gcFinal.after(gcBegin) || gcFinal.equals(gcBegin))) {
                    //parse stream and check if day is the same of the day of week wished
                    String tbegin = "";
                    String tend = "";

                    String[] dwList = days_of_week.split(";");
                    boolean found = false;
                    for (int i = 0; i < dwList.length; i++) {
                        String[] dList = dwList[i].split(",");

                        //day of week was found
                        if (gcBegin.get(GregorianCalendar.DAY_OF_WEEK) == Integer.parseInt(dList[0])) {
                            found = true;
                            tbegin = dList[1];
                            tend = dList[2];
                            break;
                        }
                    }

                    if (found) {
                        totalDays++;

                        String query = "SELECT dt_id FROM tb_datetime "
                                + "WHERE date=DATE_FORMAT('" + sdf.format(gcBegin.getTime()) + "','%Y/%m/%d') "
                                + "AND schedule_id=" + schid;
                        ResultSet result = stat.executeQuery(query);
                
                        while(result.next()){
                            datetimesExists = true;
                        }

                        if (!datetimesExists) {
                            query = "INSERT INTO tb_datetime(date,time_begin,time_end,schedule_id) "
                                    + "VALUES(DATE_FORMAT('" + sdf.format(gcBegin.getTime()) + "','%Y/%m/%d'),"
                                    + "TIME_FORMAT('" + tbegin + "','%H:%i'),"
                                    + "TIME_FORMAT('" + tend + "','%H:%i'),"
                                    + schid + ")";

                            if (stat.executeUpdate(query) > 0)
                                totalWrote++;
                        } else { // datetime already exists
                            break;
                        }                        
                    }

                    gcBegin.add(GregorianCalendar.DAY_OF_WEEK, 1);
                    gcBegin.set(GregorianCalendar.HOUR, 0);
                }

                if (!datetimesExists) {
                    if (totalDays == totalWrote)
                        datetimesCreated = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { // session expired
            datetimesCreated = SESSION_EXPIRED_MSG;
        }

        return datetimesCreated;
    }

    /** Gets the report card report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @return returns report card list in format:
     *          <class name>:<grade>:<situation>
     *          <class name 1>:<grade 1>:<situation 1>
     *          <class name 2>:<grade 2>:<situation 2>
     *          <situation> = 1 grade >= grade_min
     *          <situation> = 0 grade < grade_min
     *          <situation> = - no grade assigned
     *
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] ShowReportCardReport(String username, String cookie, String ip) {
        String line = SESSION_EXPIRED_MSG;
        String [] gradeList = null;
        String [] classList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the grade_min
                String query = "SELECT grade_min FROM tb_globals";

                ResultSet result = stat.executeQuery(query);

                float grade_min = 0;
                while(result.next()){
                    grade_min = Float.parseFloat(result.getString("grade_min"));
                }

                query = "SELECT `tb_class`.`name` AS `class`, `tb_class`.`class_id` AS `clid` "
                        + "FROM tb_class INNER JOIN tb_enrollment "
                        + "ON `tb_class`.`class_id`=`tb_enrollment`.`class_id` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_enrollment`.`person_id`=`tb_person`.`person_id` "
                        + "WHERE `tb_person`.`nickname`='" + username 
                        + "' ORDER BY `tb_class`.`name`";

                result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.isEmpty())
                        line = result.getString("class") + ":" + result.getString("clid");
                    else
                        line += "&" + result.getString("class") + ":" + result.getString("clid");                    
                }

                if (line.isEmpty() == false) {
                    classList = line.split("&"); 
                }

                line = "class:grade:situation";
                if (classList != null) {
                    for(int i = 0; i < classList.length; i++) {
                        String [] clLst = classList[i].split(":");

                        query = "SELECT DISTINCT `tb_class_course`.`class_id`, `tb_grade`.`grade` "
                                + "FROM tb_grade INNER JOIN tb_class_course "
                                + "ON `tb_grade`.`class_course_id`=`tb_class_course`.`class_course_id` "
                                + "INNER JOIN tb_person ON `tb_person`.`person_id`=`tb_grade`.`person_id` "
                                + "WHERE `tb_person`.`nickname`='" + username
                                + "' AND `tb_class_course`.`class_id`=" + clLst[1];

                        result = stat.executeQuery(query);

                        float grade = 0;
                        boolean found = false;
                        while(result.next()){
                            found = true;
                            grade = Float.parseFloat(result.getString("grade"));
                        }

                        line += "&" + clLst[0] + ":" + grade;

                        if (!found)
                            line += ":-";
                        else {
                            if (grade >= grade_min)
                                line += ":1";
                            else
                                line += ":0";
                        }
                    }
                }
                
                gradeList = line.split("&");
 
                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            gradeList = line.split("&");
        }

        return gradeList;
        //return classList;
    }

    /** Gets the historic school report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @return returns historic school list in format:
     *          <class name>:<grade>:<situation>
     *          <class name 1>:<grade 1>:<situation 1>
     *          <class name 2>:<grade 2>:<situation 2>
     *          <situation> = 1 grade >= grade_min
     *          <situation> = 0 grade < grade_min
     *          <situation> = - no grade assigned
     *
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] ShowHistoricSchoolReport(String username, String cookie, String ip) {
        String line = SESSION_EXPIRED_MSG;
        String [] gradeList = null;
        String [] classList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                
               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the grade_min
                String query = "SELECT grade_min FROM tb_globals";

                ResultSet result = stat.executeQuery(query);

                float grade_min = 0;
                while(result.next()){
                    grade_min = Float.parseFloat(result.getString("grade_min"));
                }

                
                query = "SELECT `tb_class`.`name` AS `class`, `tb_class`.`class_id` AS `clid` "
                        + "FROM tb_class INNER JOIN tb_enrollment "
                        + "ON `tb_class`.`class_id`=`tb_enrollment`.`class_id` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_enrollment`.`person_id`=`tb_person`.`person_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_class`.`name`";

                result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.isEmpty())
                        line = result.getString("class") + ":" + result.getString("clid");
                    else
                        line += "&" + result.getString("class") + ":" + result.getString("clid");
                }

                System.out.println("line = " + line);
                if (line.isEmpty() == false) {
                    classList = line.split("&");
                }
                
                //System.out.println("classList.length = " + classList.length);
                
                line = "class:grade:situation";
                if (classList != null) {
                    for(int i = 0; i < classList.length; i++) {
                        String [] clLst = classList[i].split(":");

                        query = "SELECT DISTINCT `tb_class_course`.`class_id`, `tb_grade`.`grade` "
                                + "FROM tb_grade INNER JOIN tb_class_course "
                                + "ON `tb_grade`.`class_course_id`=`tb_class_course`.`class_course_id` "
                                + "INNER JOIN tb_person ON `tb_person`.`person_id`=`tb_grade`.`person_id` "
                                + "WHERE `tb_person`.`nickname`='" + username
                                + "' AND `tb_class_course`.`class_id`=" + clLst[1];

                        result = stat.executeQuery(query);

                        float grade = 0;
                        boolean found = false;
                        while(result.next()){
                            found = true;
                            grade = Float.parseFloat(result.getString("grade"));
                        }

                        if (found) {
                            if (grade >= grade_min)
                                line += "&" + clLst[0] + ":" + grade + ":1";                        
                        }
                    }
                }

                gradeList = line.split("&");
                
                conn.close();

            }catch (SQLException e) {
                line = e.getMessage() + "&SQLException";
                gradeList = line.split("&");
            }
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {
                line = e.getMessage() + "&ClassNotFoundException";
                gradeList = line.split("&");
            }

        } else { // session expired
            gradeList = line.split("&");
        }

        return gradeList;
        //return classList;
    }

     /** Gets the statistical presence report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. Integer course id.
     * @param clid. Integer class id.
     * @param schid. Integer schedule id.
     *
     * @return returns presence list in format:
     *          <total datetimes>:<student>,<presence %>
     *          <total datetimes 1>:<student 1>,<presence % 1>
     *          <total datetimes 2>:<student 2>,<presence % 2>
     *          
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] ShowStatisticalPresenceReport(String username, String cookie, String ip, int coid, int clid, int schid) {
        String line = SESSION_EXPIRED_MSG;
        String [] studentsList = null;
        String [] presenceList = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the max_datetimes
                String query = "SELECT COUNT(*) AS `max_datetimes` FROM tb_datetime "
                        + "WHERE schedule_id=" + schid;

                ResultSet result = stat.executeQuery(query);

                float max_datetimes = 0;
                while(result.next()){
                    max_datetimes = Float.parseFloat(result.getString("max_datetimes"));
                }


                //getting the students from class
                query = "SELECT `tb_person`.`name` AS `student`,  `tb_person`.`person_id` AS `sid` "
                        + "FROM tb_enrollment INNER JOIN tb_person "
                        + "ON `tb_person`.`person_id`=`tb_enrollment`.`person_id` "
                        + "WHERE `tb_enrollment`.`class_id`=" + clid
                        + " ORDER BY `tb_person`.`name`";

                result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.isEmpty())
                        line = result.getString("student") + ":" + result.getString("sid");
                    else
                        line += "&" + result.getString("student") + ":" + result.getString("sid");
                    
                }
                studentsList = line.split("&");

                line = "";
                for(int i = 0; i < studentsList.length; i++) {
                    //getting the presence percent for each student
                    float max_presences = 0;
                    String [] stdList = studentsList[i].split(":");
                    
                    query = "SELECT COUNT(*) AS `max_presences` FROM tb_person_present "
                            + "INNER JOIN tb_ccdt "
                            + "ON `tb_person_present`.`id_ccdt`=`tb_ccdt`.`id_ccdt` "
                            + "INNER JOIN tb_datetime "
                            + "ON `tb_ccdt`.`dt_id`=`tb_datetime`.`dt_id` "
                            + "INNER JOIN tb_class_course "
                            + "ON `tb_datetime`.`schedule_id`=`tb_class_course`.`schedule_id` "
                            + "WHERE `tb_person_present`.`person_id`=" + stdList[1]
                            + " AND `tb_class_course`.`course_id`=" + coid
                            + " AND `tb_class_course`.`class_id`=" + clid
                            + " AND `tb_class_course`.`schedule_id`=" + schid;

                    result = stat.executeQuery(query);

                    
                    while(result.next()){
                         max_presences = (Float.parseFloat(result.getString("max_presences"))/max_datetimes) * 100;
                    }

                    if (line.isEmpty())
                        line = String.valueOf(max_datetimes) + ":" + stdList[0] + "," + max_presences + "%";
                    else
                        line += "&" + String.valueOf(max_datetimes) + ":" + stdList[0] + "," + max_presences + "%";
                }
                presenceList = line.split("&");
                

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            presenceList = line.split("&");
        }

        return presenceList;
    }

    /** Gets a list of schools for candidate
     * @return returns a list of schools in format:
     *          <school name>:<school id>
     *          <school name 1>:<school id 1>
     *          <school name 2>:<school id 2>
     *          If not exists any school, return null.
     */
    public String [] GetSchoolListForCandidate() {
        String [] schoolList = null;

        String line = "";

        try {
            //Class.forName("com.mysql.jdbc.Driver").newInstance ();
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

           // System.out.println("database connected");
            Statement stat = conn.createStatement();

            String query = "SELECT name, school_id FROM tb_school ORDER BY name";
            ResultSet result = stat.executeQuery(query);

            line = "school name:school id";
            while(result.next()){
                line += "&" + result.getString("name");
                line += ":" + result.getString("school_id");
            }
           
            schoolList = line.split("&");

            System.out.println(line);

            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        return schoolList;
    }

    /** Creates the new candidate
     *
     * @param nickname. String nickname
     * @param fullname. String fullname
     * @param email. String email
     * @param uid. String promo_code
     * @param scid. Integer school id
     * @return If candidate was created return true, else return false.
     */
    public String CreateNewCandidate(String nickname, String fullname, String email, String uid, int scid) {
        String userCreated = "false";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

            Statement stat = conn.createStatement();

            String query = "SELECT nickname FROM tb_person WHERE nickname='" + nickname + "'";
           // System.out.println(query);
            ResultSet result = stat.executeQuery(query);

            boolean userExists = false;
            while(result.next()){
                userExists = true;
            }

            if (!userExists) {

                // Getting the gid from actor candidate
                query = "SELECT person_type_id FROM tb_person_type WHERE name='Candidate'";
                // System.out.println(query);
                result = stat.executeQuery(query);

                int gid = 0;
                while(result.next()){
                    gid = Integer.parseInt(result.getString("person_type_id"));
                }

                query = "INSERT INTO tb_person(nickname,name,email,school_id,person_type_id,telephone,obs,user_id) "
                        + "VALUES('" + nickname + "','" + fullname + "','" + email +
                        "'," + scid + "," + gid + ",'-',NULL,'" + uid + "')";
                if (stat.executeUpdate(query) > 0)
                    userCreated = "true";
            }
            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        //catch (InstantiationException e) {System.out.println("InstantiationException");}
        //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
        catch (ClassNotFoundException e) {e.printStackTrace();}

        return userCreated;
    }

    /** Gets the candidate settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the candidate settings in format:
     *      <nickname>:<full name>:<candidate id>:<email>:<telephone>:<obs>
     *      If an error occurs returns false
     *      If session expired returns session_expired
     */
    public String GetCandidateSettings(String username, String cookie, String ip) {
        String line = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`nickname`, `tb_person`.`name`, "
                        + "`tb_person`.`person_id`, `tb_person`.`email`, "
                        + "`tb_person`.`telephone`, `tb_person`.`obs` "
                        + "FROM tb_person "                        
                        + "WHERE `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    line = result.getString("nickname");
                    line += ":" + result.getString("name");
                    line += ":" + result.getString("person_id");
                    line += ":" + result.getString("email");
                    line += ":" + result.getString("telephone");
                    line += ":" + result.getString("obs");                    
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;

    }

    /** Gets the candidate information
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the candidate settings in format:
     *      <address>:<district>:<city>:<state>:<zip_code>:<civil status>:<career>:<identity>:<issued by>:<cpf>:<date>
     *      If an error occurs returns false
     *      If session expired returns session_expired
     */
    public String GetCandidateInformation(String username, String cookie, String ip) {
        String line = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`address`, `tb_person`.`district`, "
                        + "`tb_person`.`city`, `tb_person`.`state`, `tb_person`.`career`, "
                        + "`tb_person`.`zip_code`, `tb_person`.`civil_status`, "
                        + "`tb_person`.`identity`, `tb_person`.`issued_by`, `tb_person`.`cpf`, "
                        + "DATE_FORMAT(date, '%Y/%m/%d %H_%i_%s') AS `date` "
                        + "FROM tb_person "                        
                        + "WHERE `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    line = result.getString("address");
                    line += ":" + result.getString("district");
                    line += ":" + result.getString("city");
                    line += ":" + result.getString("state");
                    line += ":" + result.getString("zip_code");
                    line += ":" + result.getString("civil_status");
                    line += ":" + result.getString("career");
                    line += ":" + result.getString("identity");
                    line += ":" + result.getString("issued_by");
                    line += ":" + result.getString("cpf");
                    line += ":" + result.getString("date");
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;

    }

    /** Gets the candidate information
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id
     * @return Returns the candidate settings in format:
     *      <address>:<district>:<city>:<state>:<zip_code>:<civil status>:<career>:<identity>:<issued by>:<cpf>
     *      If an error occurs returns false
     *      If session expired returns session_expired
     */
    public String GetAdditionalInformationFromCandidate(String username, String cookie, String ip, String pid) {
        String line = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`address`, `tb_person`.`district`, "
                        + "`tb_person`.`city`, `tb_person`.`state`, `tb_person`.`career`, "
                        + "`tb_person`.`zip_code`, `tb_person`.`civil_status`, "
                        + "`tb_person`.`identity`, `tb_person`.`issued_by`, `tb_person`.`cpf` "
                        + "FROM tb_person "
                        + "WHERE `tb_person`.`person_id`=" + pid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                    line = result.getString("address");
                    line += ":" + result.getString("district");
                    line += ":" + result.getString("city");
                    line += ":" + result.getString("state");
                    line += ":" + result.getString("zip_code");
                    line += ":" + result.getString("civil_status");
                    line += ":" + result.getString("career");
                    line += ":" + result.getString("identity");
                    line += ":" + result.getString("issued_by");
                    line += ":" + result.getString("cpf");
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;

    }

    /** Edit candidate settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param fullname. String fullname
     * @param email. String email
     * @param telephone. String telephone
     * @param obs. String obs
     * @param address. String address
     * @param district. String district
     * @param city. String city
     * @param state. String state
     * @param zip_code. String zip code
     * @param civil_status. String civil status
     * @param career. String career
     * @param identity. String identity
     * @param issued_by. String issued by
     * @param cpf. String cpf
     * @return Returns true if candidate was edited, else returns false.
     *          If Session expired returns session_expired
     */
    public String EditCandidateSettings(String username, String cookie, String ip, String fullname, String email, String telephone, String obs, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf) {
        String userEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_person SET name='" + fullname + "', email='" + email
                        + "', telephone='" + telephone + "' , obs='" + obs
                        + "', address='" + address+ "', district='" + district + "', city='" + city
                        + "', state='" + state + "', zip_code='" + zip_code
                        + "', civil_status='" + civil_status + "', career='" + career
                        + "', identity='" + identity + "', issued_by='" + issued_by
                        + "', cpf='" + cpf
                        + "' WHERE nickname='" + username + "'";
                if (stat.executeUpdate(query) > 0)
                    userEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            userEdited = SESSION_EXPIRED_MSG;
        }

        return userEdited;
    }
    
    /** Edit interested settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param fullname. String fullname
     * @param email. String email
     * @param telephone. String telephone
     * @param obs. String obs
     * @param pid. Integer program id
     * @return Returns true if candidate was edited, else returns false.
     *          If Session expired returns session_expired
     */
    public String EditInterestedSettings(String username, String cookie, String ip, String fullname, String email, String telephone, String obs, int pid) {
        String userEdited = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

                Statement stat = conn.createStatement();


                String query = "UPDATE tb_person SET name='" + fullname + "', email='" + email
                        + "', telephone='" + telephone + "' , obs='" + obs
                        + "', program_id=" + pid
                        + " WHERE nickname='" + username + "'";
                if (stat.executeUpdate(query) > 0)
                    userEdited = "true";

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            userEdited = SESSION_EXPIRED_MSG;
        }

        return userEdited;
    }
    
    /** Gets the file format list
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the file format list in format:
     *          <format name>:<format id>:<extension>
     *          If an error occurs return empty string
     *          If session expired returns session_expired
     */
    public String [] GetFileFormatList(String username, String cookie, String ip) {
        String [] formatLst = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK
            
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name, id_formats, extension FROM tb_formats ORDER BY name";
                ResultSet result = stat.executeQuery(query);

                line = "";
                while(result.next()){
                    if (line.isEmpty())
                        line = result.getString("name") + ":" + result.getString("id_formats") + ":" + result.getString("extension");
                    else
                        line += "&" + result.getString("name") + ":" + result.getString("id_formats") + ":" + result.getString("extension");
                }

                formatLst = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            
        } else { // session expired
            formatLst = line.split("&");
        }

        return formatLst;
    }
    
    /** Creates the new file
     * 
     * @param username. Strings username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param description. String description
     * @param permission. String permission
     * @param id_formats. Integer format id
     * @return Returns format below if file was created, else returns false:0
     *          true:<file id>
     *          If session expired returns session_expired
     */
    public String CreateNewFile(String username, String cookie, String ip, String description, String permission, int id_formats) {
        String imageCreated = "false:0";
        
        if (CheckSession(username, cookie, ip)) { //session OK
        
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Getting the person id from username
                String query = "SELECT person_id FROM tb_person WHERE nickname='" + username + "'";
                ResultSet result = stat.executeQuery(query);

                int pid = 0;                
                while(result.next()){
                   pid = Integer.parseInt(result.getString("person_id"));                   
                }
                
                if (permission.equals("private")) {
                    query = "INSERT INTO tb_file(description, permission, id_formats,person_id,empty_for_user) "
                        + "VALUES('" + description + "','" + permission + "'," + id_formats + "," + pid + ",0)";
                } else {
                    query = "SELECT id_file FROM tb_file "
                            + "WHERE permission='" + permission
                            + "' AND person_id=" + pid;

                    result = stat.executeQuery(query);

                    String imgid = "0";
                    boolean found = false;
                    while(result.next()){
                       imgid = result.getString("id_file");
                       found = true;
                    }

                    if (found) {
                        query = "DELETE FROM tb_file WHERE id_file=" + imgid;
                        stat.executeUpdate(query);
                    }

                    query = "INSERT INTO tb_file(description, permission, id_formats,person_id,empty_for_user) "
                        + "VALUES('" + description + "','" + permission + "'," + id_formats + "," + pid + ",0)";                
                }

                int ret = stat.executeUpdate(query);
                if (ret > 0) {

                    query = "SELECT id_file FROM tb_file "
                            + "WHERE description='" + description
                            + "' AND id_formats=" + id_formats
                            + " AND permission='" + permission + "'";

                    result = stat.executeQuery(query);

                    String imgid = "0";
                    while(result.next()){
                       imgid = result.getString("id_file");
                    }

                    imageCreated = "true:" + imgid;
                }


                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            
        } else { // session expired
            imageCreated = SESSION_EXPIRED_MSG;
        }

        return imageCreated;
    }
    
    /** Gets the photo from user
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the file properties in format below, if an error occurs returns false
     *          <permission>:<file id>:<extension>:<description>
     *          If session expired returns session_expired
     */
    public String GetPhotoFromUser(String username, String cookie, String ip) {
        String photoPath = "false";

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_file`.`id_file` AS `filename`,"
                        + " `tb_formats`.`extension` AS `ext`, "
                        + " `tb_file`.`description` "
                        + "FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_file`.`person_id`=`tb_person`.`person_id` "
                        + "WHERE `tb_file`.`permission`='public' AND `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                   photoPath = "public:" + result.getString("filename") + ":" + result.getString("ext") + ":" + result.getString("description");
                }

                conn.close();
            
            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            
        } else { // session expired
            photoPath = SESSION_EXPIRED_MSG;
        }

        return photoPath;
    }

    /** Gets the photo from candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer person id from candidate
     * @return Returns the file properties in format below, if an error occurs returns false
     *          <permission>:<file id>:<extension>:<description>
     *          If session expired returns session_expired
     */
    public String GetPhotoFromCandidate(String username, String cookie, String ip, int pid) {
        String photoPath = "false";

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_file`.`id_file` AS `filename`,"
                        + " `tb_formats`.`extension` AS `ext`, "
                        + " `tb_file`.`description` "
                        + "FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "
                        + "WHERE `tb_file`.`permission`='public' AND `tb_file`.`person_id`=" + pid;

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                   photoPath = "public:" + result.getString("filename") + ":" + result.getString("ext") + ":" + result.getString("description");
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            photoPath = SESSION_EXPIRED_MSG;
        }

        return photoPath;
    }

    /** Gets the documents from user
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the file properties in format below, if an error occurs returns false
     *          <permission>:<file id>:<extension>:<description>
     *          If session expired returns session_expired
     */
    public String [] GetDocumentsFromUser(String username, String cookie, String ip) {
        String [] docPathList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_file`.`id_file` AS `filename`,"
                        + " `tb_formats`.`extension` AS `ext`, "
                        + "`tb_file`.`description` "
                        + "FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_file`.`person_id`=`tb_person`.`person_id` "
                        + "WHERE `tb_file`.`permission`='private' AND `tb_person`.`nickname`='" + username 
                        + "' AND `tb_file`.`empty_for_user`=0 ORDER BY `tb_file`.`description`";

                ResultSet result = stat.executeQuery(query);
                line= "false";
                while(result.next()){
                    if (line.equals("false"))
                        line = "private:" + result.getString("filename") + ":" + result.getString("ext") + ":" + result.getString("description");
                    else
                        line += "&private:" + result.getString("filename") + ":" + result.getString("ext") + ":" + result.getString("description");
                }

                docPathList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            docPathList = line.split("&");
        }

        return docPathList;
    }

    /** Gets the documents from candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the file properties in format below, if an error occurs returns false
     *          <permission>:<file id>:<extension>:<description>
     *          If session expired returns session_expired
     */
    public String [] GetDocumentsFromCandidate(String username, String cookie, String ip, int pid) {
        String [] docPathList = null;
        String line = SESSION_EXPIRED_MSG;

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_file`.`id_file` AS `filename`,"
                        + " `tb_formats`.`extension` AS `ext`, "
                        + "`tb_file`.`description` "
                        + "FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "                        
                        + "WHERE `tb_file`.`permission`='private' AND `tb_file`.`person_id`=" + pid
                        + " ORDER BY `tb_file`.`description`";

                ResultSet result = stat.executeQuery(query);
                line= "false";
                while(result.next()){
                    if (line.equals("false"))
                        line = "private:" + result.getString("filename") + ":" + result.getString("ext") + ":" + result.getString("description");
                    else
                        line += "&private:" + result.getString("filename") + ":" + result.getString("ext") + ":" + result.getString("description");
                }

                docPathList = line.split("&");

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            docPathList = line.split("&");
        }

        return docPathList;
    }

    /** Hide file from candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param fid. Integer file id
     * @return Returns true if file was hidden, else returns false
     *          If session expired returns session_expired
     */
    public String HideFileFromCandidate(String username, String cookie, String ip, int fid) {
        String fileDeleted = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();
                
                String query = "UPDATE tb_file SET empty_for_user=1 "
                        + "WHERE id_file=" + fid;                                
                if (stat.executeUpdate(query) > 0) {
                    fileDeleted = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            fileDeleted = SESSION_EXPIRED_MSG;
        }

        return fileDeleted;
    }
    
    /** Register the new candidate passing promo_code or not
     *
     * @param fullname. String fullname
     * @param email. String email
     * @param telephone. String telephone
     * @param scname. String school name
     * @param uid. String user_id
     * @param reg_type. String register type, can be:
     *          -) web - site
     *          -) cel - cel
     * @return If candidate was created return true.
     *          Errors can be:
     *              -) err_login_exists - Login already exists
     *              -) false            - unknown error
     */
    public String RegisterNewCandidateToSchool(String fullname, String email, String telephone, String scname, String uid, String reg_type) {
        String userCreated = "false";
        String actor = "Interested";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection (databaseUrl,
                                                        userName, userPassword);

            Statement stat = conn.createStatement();


            if (CreateCandidateDOUA(email, email)) { // candidate created

                String query = "SELECT nickname FROM tb_person WHERE nickname='" + email + "'";
               // System.out.println(query);
                ResultSet result = stat.executeQuery(query);

                boolean userExists = false;
                while(result.next()){
                    userExists = true;
                }

                if (!userExists) {
                    // Getting the gid from actor
                    query = "SELECT person_type_id FROM tb_person_type WHERE name='" + actor + "'";
                    // System.out.println(query);
                    result = stat.executeQuery(query);

                    int gid = 0;
                    while(result.next()){
                        gid = Integer.parseInt(result.getString("person_type_id"));
                    }

                    // Getting the school id from school name
                    query = "SELECT school_id FROM tb_school WHERE name='" + scname + "'";
                    // System.out.println(query);
                    result = stat.executeQuery(query);

                    int scid = 0;
                    while(result.next()){
                        scid = Integer.parseInt(result.getString("school_id"));
                    }

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    String curtime = dateFormat.format(date);

                    //check if fullname is empty
                    if (fullname.isEmpty()) {
                        query = "INSERT INTO tb_person(nickname,name,email,school_id,person_type_id,telephone,obs,user_id,reg_type,date) "
                                + "VALUES('" + email.trim() + "','-','" + email +
                                "'," + scid + "," + gid + ",'" + telephone
                                + "',NULL,'" + uid + "','" + reg_type
                                + "','"+ curtime + "')";
                    } else {
                        query = "INSERT INTO tb_person(nickname,name,email,school_id,person_type_id,telephone,obs,user_id,reg_type,date) "
                                + "VALUES('" + email + "','" + fullname + "','" + email +
                                "'," + scid + "," + gid + ",'" + telephone
                                + "',NULL,'" + uid + "','" + reg_type
                                + "','"+ curtime + "')";
                    }
                    if (stat.executeUpdate(query) > 0) {
                        userCreated = "true";

                        AuxiliaryMethods.SendEmailGmailNewInterested(email, fullname);
                        AuxiliaryMethods.SendEmailGmailPreRegistration(email, fullname);
                    }
                } else { //user already registered, resending email
                    userCreated = "err_login_exists";
                        
                    AuxiliaryMethods.SendEmailGmailNewInterested(email, fullname);
                    AuxiliaryMethods.SendEmailGmailPreRegistration(email, fullname);
                }
            } else { // email already exists
                userCreated = "err_login_exists";
                            
                AuxiliaryMethods.SendEmailGmailNewInterested(email, fullname);
                AuxiliaryMethods.SendEmailGmailPreRegistration(email, fullname);
            }
            
            conn.close();

        }catch (SQLException e) {e.printStackTrace();}
        catch (ClassNotFoundException e) {e.printStackTrace();}        

        return userCreated;
    }

    /** Download the file data
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param fid. String file id
     * @return Returns the file data encoded in Base64
     *          If an error occurs return false
     *          If session expired returns session_expired
     */
    public String DownloadFile(String username, String cookie, String ip, String fid) {
        String fileData = "false";
        
        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Check if user can download this file.
                String query = "SELECT `tb_file`.`permission`, `tb_formats`.`extension` FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_file`.`person_id`=`tb_person`.`person_id` "
                        + "WHERE `tb_file`.`empty_for_user`=0"
                        + " AND `tb_person`.`nickname`='" + username
                        + "' AND `tb_file`.`id_file`=" + fid;
                ResultSet result = stat.executeQuery(query);

                boolean allowedDownload = false;
                String permission = "";
                String extension = "";
                while(result.next()){
                    allowedDownload = true;
                    permission = result.getString("permission");
                    extension = result.getString("extension");
                }

                if (permission.equals("public")) {
                    String filePath = uploadDir + permission + "/" + fid + "." + extension;
                    File file = new File(filePath);
                    FileInputStream fis = new FileInputStream(file);

                    byte fileContent[] = new byte[(int)file.length()];

                    /*
                     * To read content of the file in byte array, use
                     * int read(byte[] byteArray) method of java FileInputStream class.
                     *
                     */
                    fis.read(fileContent);


                    fis.close();

                    //create string from byte array
                    //String strFileContent = new String(fileContent);

                    //System.out.println("File content : ");
                    //System.out.println(strFileContent);

                    fileData = Base64.encode(fileContent);
                    
                } else { //file is private

                    if (allowedDownload) {
                        String filePath = uploadDir + permission + "/" + fid + "." + extension;
                        File file = new File(filePath);
                        FileInputStream fis = new FileInputStream(file);

                        byte fileContent[] = new byte[(int)file.length()];

                        /*
                         * To read content of the file in byte array, use
                         * int read(byte[] byteArray) method of java FileInputStream class.
                         *
                         */
                        fis.read(fileContent);


                        fis.close();

                        //create string from byte array
                        //String strFileContent = new String(fileContent);

                        //System.out.println("File content : ");
                        //System.out.println(strFileContent);

                        fileData = Base64.encode(fileContent);

                    }

                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }
    
    /** Download the file data from user
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id
     * @param fid. String file id
     * @return Returns the file data encoded in Base64
     *          If an error occurs return false
     *          If session expired returns session_expired
     */
    public String DownloadFileFromUser(String username, String cookie, String ip, String pid, String fid) {
        String fileData = "false";
        
        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Check if user can download this file.
                String query = "SELECT `tb_file`.`permission`, `tb_formats`.`extension` FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "                        
                        + "WHERE `tb_file`.`person_id`=" + pid
                        + " AND `tb_file`.`id_file`=" + fid;
                ResultSet result = stat.executeQuery(query);
                
                String permission = "";
                String extension = "";
                while(result.next()){
                    permission = result.getString("permission");
                    extension = result.getString("extension");
                }
                
                String filePath = uploadDir + permission + "/" + fid + "." + extension;
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);

                byte fileContent[] = new byte[(int)file.length()];

                /*
                 * To read content of the file in byte array, use
                 * int read(byte[] byteArray) method of java FileInputStream class.
                 *
                 */
                fis.read(fileContent);


                fis.close();

                //create string from byte array
                //String strFileContent = new String(fileContent);

                //System.out.println("File content : ");
                //System.out.println(strFileContent);

                fileData = Base64.encode(fileContent);                    

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }

    /** Generates the agreement 
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param agr_id. String agreement id
     * @return Returns true if agreement was generated.
     *          If agreement contains blank fields returns has_blank_field
     *          If agreement contains indefined payment returns no_payment_defined
     *          If session expired returns session_expired
     *          If agreement could not be generated returns false
     */
    public String GenerateAgreement(String username, String cookie, String ip, String agr_id) {
        String agreementCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name,email,telephone,"
                        + "address,district,city,state,zip_code,"
                        + "civil_status,career,"
                        + "identity,issued_by,cpf,"
                        + "desc_value,desc_quota "
                        + "FROM `tb_person` "
                        + "WHERE nickname='" + username +"'";

                ResultSet result = stat.executeQuery(query);

                boolean hasBlankField = false;
                boolean noPayment = false;
                String fullname = "";
                String email = "";
                String telephone = "";
                String address = "";
                String district = "";
                String city = "";
                String state = "";
                String zip_code = "";
                String civil_status = "";
                String career = "";
                String identity = "";
                String issued_by = "";
                String cpf = "";
                String desc_value = "";
                String desc_quota = "";

                while(result.next()){
                    fullname = result.getString("name");
                    email = result.getString("email");
                    telephone = result.getString("telephone");
                    address = result.getString("address");
                    district = result.getString("district");
                    city = result.getString("city");
                    state = result.getString("state");
                    zip_code = result.getString("zip_code");
                    civil_status = result.getString("civil_status");
                    career = result.getString("career");
                    identity = result.getString("identity");
                    issued_by = result.getString("issued_by");
                    cpf = result.getString("cpf");
                    desc_value = result.getString("desc_value");
                    desc_quota = result.getString("desc_quota");
                }

                if (fullname == null) {
                    fullname = "(null)";
                    hasBlankField = true;
                } else
                    fullname = fullname.replace("_", "\\_");
                if (email == null) {
                    email = "(null)";
                    hasBlankField = true;
                } else
                    email = email.replace("_", "\\_");
                if (telephone == null) {
                    telephone = "(null)";
                    hasBlankField = true;
                } else
                    telephone = telephone.replace("_", "\\_");
                if (address == null) {
                    address = "(null)";
                    hasBlankField = true;
                } else
                    address = address.replace("_", "\\_");
                if (district == null) {
                    district = "(null)";
                    hasBlankField = true;
                } else
                    district = district.replace("_", "\\_");
                if (city == null) {
                    city = "(null)";
                    hasBlankField = true;
                } else
                    city = city.replace("_", "\\_");
                if (state == null) {
                    state = "(null)";
                    hasBlankField = true;
                } else
                    state = state.replace("_", "\\_");
                if (zip_code == null) {
                    zip_code = "(null)";
                    hasBlankField = true;
                } else
                    zip_code = zip_code.replace("_", "\\_");
                if (civil_status == null) {
                    civil_status = "(null)";
                    hasBlankField = true;
                } else
                    civil_status = civil_status.replace("_", "\\_");
                if (career == null) {
                    career = "(null)";
                    hasBlankField = true;
                } else
                    career = career.replace("_", "\\_");
                if (identity == null) {
                    identity = "(null)";
                    hasBlankField = true;
                } else
                    identity = identity.replace("_", "\\_");
                if (issued_by == null) {
                    issued_by = "(null)";
                    hasBlankField = true;
                } else
                    issued_by = issued_by.replace("_", "\\_");
                if (cpf == null) {
                    cpf = "(null)";
                    hasBlankField = true;
                } else
                    cpf = cpf.replace("_", "\\_");
                if (desc_value == null) {
                    desc_value = "(null)";
                    noPayment = true;
                } else
                    desc_value = desc_value.replace("$", "\\$");
                if (desc_quota == null) {
                    desc_quota = "(null)";
                    noPayment = true;
                } else
                    desc_quota = desc_quota.replace("_", "\\_");

                if (AuxiliaryMethods.GenerateAgreement(username, agr_id, fullname, email, telephone, address, district, city, state, zip_code, civil_status, career, identity, issued_by, cpf, desc_value, desc_quota)) {
                    if (hasBlankField)
                        agreementCreated = "has_blank_field";
                    else if (noPayment)
                        agreementCreated = "no_payment_defined";
                    else
                        agreementCreated = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { //session expired
            agreementCreated = SESSION_EXPIRED_MSG;
        }

        return agreementCreated;
    }
    
    /** Download the agreement
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the file data encoded in Base64.
     *          If an error occurs returns false
     *          If session expired returns session_expired
     */
    public String DownloadAgreement(String username, String cookie, String ip) {
        String filePath = agreementDir + username + ".pdf";
        String fileData = "false";

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);

                byte fileContent[] = new byte[(int)file.length()];

                /*
                 * To read content of the file in byte array, use
                 * int read(byte[] byteArray) method of java FileInputStream class.
                 *
                 */
                fis.read(fileContent);
                fis.close();
                
                fileData = Base64.encode(fileContent);
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }

    /** Admin Generates the agreement 
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param agr_id. String agreement id
     * @param nickname. String nickname
     * @return Returns true if agreement was generated.
     *          If agreement contains blank fields returns has_blank_field
     *          If agreement contains indefined payment returns no_payment_defined
     *          If session expired returns session_expired
     *          If agreement could not be generated returns false
     */
    public String AdminGenerateAgreement(String username, String cookie, String ip, String agr_id, String nickname) {
        String agreementCreated = "false";

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT name,email,telephone,"
                        + "address,district,city,state,zip_code,"
                        + "civil_status,career,"
                        + "identity,issued_by,cpf,"
                        + "desc_value,desc_quota "
                        + "FROM `tb_person` "
                        + "WHERE nickname='" + nickname +"'";

                ResultSet result = stat.executeQuery(query);

                boolean hasBlankField = false;
                boolean noPayment = false;
                String fullname = "";
                String email = "";
                String telephone = "";
                String address = "";
                String district = "";
                String city = "";
                String state = "";
                String zip_code = "";
                String civil_status = "";
                String career = "";
                String identity = "";
                String issued_by = "";
                String cpf = "";
                String desc_value = "";
                String desc_quota = "";

                while(result.next()){
                    fullname = result.getString("name");
                    email = result.getString("email");
                    telephone = result.getString("telephone");
                    address = result.getString("address");
                    district = result.getString("district");
                    city = result.getString("city");
                    state = result.getString("state");
                    zip_code = result.getString("zip_code");
                    civil_status = result.getString("civil_status");
                    career = result.getString("career");
                    identity = result.getString("identity");
                    issued_by = result.getString("issued_by");
                    cpf = result.getString("cpf");
                    desc_value = result.getString("desc_value");
                    desc_quota = result.getString("desc_quota");
                }

                if (fullname == null) {
                    fullname = "(null)";
                    hasBlankField = true;
                } else
                    fullname = fullname.replace("_", "\\_");
                if (email == null) {
                    email = "(null)";
                    hasBlankField = true;
                } else
                    email = email.replace("_", "\\_");
                if (telephone == null) {
                    telephone = "(null)";
                    hasBlankField = true;
                } else
                    telephone = telephone.replace("_", "\\_");
                if (address == null) {
                    address = "(null)";
                    hasBlankField = true;
                } else
                    address = address.replace("_", "\\_");
                if (district == null) {
                    district = "(null)";
                    hasBlankField = true;
                } else
                    district = district.replace("_", "\\_");
                if (city == null) {
                    city = "(null)";
                    hasBlankField = true;
                } else
                    city = city.replace("_", "\\_");
                if (state == null) {
                    state = "(null)";
                    hasBlankField = true;
                } else
                    state = state.replace("_", "\\_");
                if (zip_code == null) {
                    zip_code = "(null)";
                    hasBlankField = true;
                } else
                    zip_code = zip_code.replace("_", "\\_");
                if (civil_status == null) {
                    civil_status = "(null)";
                    hasBlankField = true;
                } else
                    civil_status = civil_status.replace("_", "\\_");
                if (career == null) {
                    career = "(null)";
                    hasBlankField = true;
                } else
                    career = career.replace("_", "\\_");
                if (identity == null) {
                    identity = "(null)";
                    hasBlankField = true;
                } else
                    identity = identity.replace("_", "\\_");
                if (issued_by == null) {
                    issued_by = "(null)";
                    hasBlankField = true;
                } else
                    issued_by = issued_by.replace("_", "\\_");
                if (cpf == null) {
                    cpf = "(null)";
                    hasBlankField = true;
                } else
                    cpf = cpf.replace("_", "\\_");
                if (desc_value == null) {
                    desc_value = "(null)";
                    noPayment = true;
                } else
                    desc_value = desc_value.replace("$", "\\$");
                if (desc_quota == null) {
                    desc_quota = "(null)";
                    noPayment = true;
                } else
                    desc_quota = desc_quota.replace("_", "\\_");

                if (AuxiliaryMethods.AdminGenerateAgreement(nickname, agr_id, fullname, email, telephone, address, district, city, state, zip_code, civil_status, career, identity, issued_by, cpf, desc_value, desc_quota)) {
                    if (hasBlankField)
                        agreementCreated = "has_blank_field";
                    else if (noPayment)
                        agreementCreated = "no_payment_defined";
                    else
                        agreementCreated = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
        } else { //session expired
            agreementCreated = SESSION_EXPIRED_MSG;
        }

        return agreementCreated;
    }
    
    /** Admin Download the agreement
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname
     * @return Returns the file data encoded in Base64.
     *          If an error occurs returns false
     *          If session expired returns session_expired
     */
    public String AdminDownloadAgreement(String username, String cookie, String ip, String nickname) {
        String filePath = agreementDir + nickname + "_adm.pdf";
        String fileData = "false";

        if (CheckSession(username, cookie, ip)) { // session OK
            try {
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);

                byte fileContent[] = new byte[(int)file.length()];

                /*
                 * To read content of the file in byte array, use
                 * int read(byte[] byteArray) method of java FileInputStream class.
                 *
                 */
                fis.read(fileContent);
                fis.close();
                
                fileData = Base64.encode(fileContent);
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }

    /** Complete the candidate registration
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname from candidate
     * @return Returns true if registration was completed, else returns false
     *          If session expired returns session_expired
     */
    public String CompleteRegistration(String username, String cookie, String ip, String nickname) {
        String ret = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the student actor id
                String query = "SELECT person_type_id FROM tb_person_type "
                        + "WHERE name='Student'";

                ResultSet result = stat.executeQuery(query);

                int ptid = 0;
                while(result.next()){
                    ptid = Integer.parseInt(result.getString("person_type_id"));
                }

                query = "UPDATE tb_person SET person_type_id=" + ptid
                        + " WHERE nickname='" + nickname + "'";
                if (stat.executeUpdate(query) > 0) {
                    ret = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ret = SESSION_EXPIRED_MSG;
        }

        return ret;
    }

    /** Gets the informations from candidate
    *
    * @param username. String username.
    * @param cookie. String cookie value.
    * @param ip. String ip.
    * @param pid. Integer person id from candidate.
    * @return returns a list with the user settings in format:
    * <nickname>:<full name>:<email>:<telephone>:<school name>:<school id>:<person type name>:<person type id>:<obs>
    *          If session expired returns session_expired
    *          If an error occurs returns false
    */
    public String GetInformationFromCandidate(String username, String cookie, String ip, int pid) {
       String line = "false";

        if (CheckSession(username, cookie, ip)) { //session Ok

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`nickname`, `tb_person`.`name` AS `fullname`, `tb_person`.`email`, "
                        + "`tb_person`.`telephone`,  `tb_person`.`person_type_id` AS `ptid`, "
                        + "`tb_person`.`school_id` AS `scid`, `tb_school`.`name` AS `sc_name`, "
                        + "`tb_person_type`.`name` AS `pt_name`, `tb_person`.`obs` FROM tb_person "
                        + "INNER JOIN tb_school ON `tb_person`.`school_id`=`tb_school`.`school_id` "
                        + "INNER JOIN tb_person_type "
                        + "ON `tb_person`.`person_type_id`=`tb_person_type`.`person_type_id` "
                        + "WHERE `tb_person`.`person_id`=" + pid;

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                        if (line.equals("false"))
                            line = result.getString("nickname");
                        line += ":" + result.getString("fullname");
                        line += ":" + result.getString("email");
                        line += ":" + result.getString("telephone");
                        line += ":" + result.getString("sc_name");
                        line += ":" + result.getString("scid");
                        line += ":" + result.getString("pt_name");
                        line += ":" + result.getString("ptid");
                        line += ":" + result.getString("obs");
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { //session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }

    /** Deletes file from candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param fid. Integer file id
     * @param ext. String extension of file
     * @return Returns true if file was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String DeleteFileFromCandidate(String username, String cookie, String ip, int fid, String ext) {
        String fileDeleted = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "DELETE FROM tb_file "
                        + "WHERE id_file=" + fid;
                if (stat.executeUpdate(query) > 0) {
                    if (AuxiliaryMethods.DeleteFile("private", String.valueOf(fid), ext))
                        fileDeleted = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            fileDeleted = SESSION_EXPIRED_MSG;
        }

        return fileDeleted;
    }

    /** Gets the agreement template list
    *
    * @param username. String username.
    * @param cookie. String cookie value.
    * @param ip. String ip.
    * @return returns a list with the agreement in format:
    * <agreement>:<agreeent id>
    *          If session expired returns session_expired
    *          If an error occurs returns false
    */
    public String [] GetAgreementTemplateList(String username, String cookie, String ip) {
        String [] agreementList = null;
        String line = "false";

        if (CheckSession(username, cookie, ip)) { //session Ok

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_contract`.`id_contract`, `tb_contract`.`name` "
                        + "FROM `tb_contract` "
                        + "INNER JOIN tb_school "
                        + "ON `tb_contract`.`school_id`=`tb_school`.`school_id` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_school`.`school_id`=`tb_person`.`school_id` "
                        + "WHERE `tb_person`.`nickname`='" + username
                        + "' ORDER BY `tb_contract`.`name`";

                ResultSet result = stat.executeQuery(query);

                while(result.next()){
                        if (line.equals("false"))
                            line = result.getString("name") + ":" + result.getString("id_contract");
                        else
                            line += "&" + result.getString("name") + ":" + result.getString("id_contract");
                }

                System.out.println(line);

                conn.close();                

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { //session expired
            line = SESSION_EXPIRED_MSG;
        }

        agreementList = line.split("&");
        return agreementList;
    }

    /** Creates the new agreement template
     *
     * @param username. Strings username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String name
     * @return Returns format below if file was created, else returns false:0
     *          true:<file id>
     *          If session expired returns session_expired
     */
    public String CreateNewAgreementTemplate(String username, String cookie, String ip, String name) {
        String imageCreated = "false:0";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Getting the school id from username
                String query = "SELECT school_id FROM tb_person WHERE nickname='" + username + "'";
                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                   scid = Integer.parseInt(result.getString("school_id"));
                }

                //Checking if name is already used
                query = "SELECT id_contract FROM tb_contract "
                        + "WHERE name='" + name 
                        + "' AND school_id=" +scid;
                result = stat.executeQuery(query);

                boolean nameExists = false;
                while(result.next()){
                   nameExists = true;
                }

                if (!nameExists) {
                    query = "INSERT INTO tb_contract(name, school_id) "
                        + "VALUES('" + name + "'," + scid + ")";
                    if (stat.executeUpdate(query) > 0) {

                        query = "SELECT id_contract FROM tb_contract "
                                + "WHERE name='" + name
                                + "' AND school_id=" + scid;

                        result = stat.executeQuery(query);

                        String atid = "0";
                        while(result.next()){
                           atid = result.getString("id_contract");
                        }

                        imageCreated = "true:" + atid;
                    }
                }


                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            imageCreated = SESSION_EXPIRED_MSG;
        }

        return imageCreated;
    }

    /** Download the agreement template
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param tid. String file id
     * @return Returns the file data encoded in Base64
     *          If an error occurs return false
     *          If session expired returns session_expired
     */
    public String DownloadAgreementTemplate(String username, String cookie, String ip, String tid) {
        String fileData = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Getting the school id.
                String query = "SELECT school_id FROM tb_person WHERE nickname='" + username + "'";
                ResultSet result = stat.executeQuery(query);

                int scid = 0;
                while(result.next()){
                    scid = Integer.parseInt(result.getString("school_id"));
                }

                //Check if user can download this file.
                query = "SELECT `tb_contract`.`name` FROM tb_contract "
                        + "WHERE school_id=" + scid
                        + " AND id_contract=" + tid;
                result = stat.executeQuery(query);

                boolean allowedDownload = false;                
                while(result.next()){
                    allowedDownload = true;
                }

                if (allowedDownload) {
                    String filePath = agreementTemplateDir + tid + ".tex";
                    File file = new File(filePath);
                    FileInputStream fis = new FileInputStream(file);

                    byte fileContent[] = new byte[(int)file.length()];

                    /*
                     * To read content of the file in byte array, use
                     * int read(byte[] byteArray) method of java FileInputStream class.
                     *
                     */
                    fis.read(fileContent);


                    fis.close();

                    //create string from byte array
                    //String strFileContent = new String(fileContent);

                    //System.out.println("File content : ");
                    //System.out.println(strFileContent);

                    fileData = Base64.encode(fileContent);

                } 

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }

    /** Deletes agreement template
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param tid. Integer agreement template id
     * @return Returns true if file was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String DeleteAgreementTemplate(String username, String cookie, String ip, int tid) {
        String fileDeleted = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "DELETE FROM tb_contract "
                        + "WHERE id_contract=" + tid;
                if (stat.executeUpdate(query) > 0) {
                    if (AuxiliaryMethods.DeleteAgreementTemplate(String.valueOf(tid)))
                        fileDeleted = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            fileDeleted = SESSION_EXPIRED_MSG;
        }

        return fileDeleted;
    }

    /** Gets the ambiance and date list
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. Integer course id
     * @return return the Ambiance and date list in format:
     *          <ambiance id 1>:<datetime id 1>
     *          <ambiance id 2>:<datetime id 2>
     *          Else returns false
     *          If session expired returns session_expired
     */
    public String  [] GetAmbianceAndDateList(String username, String cookie, String ip, int coid, int clid) {
        String line = "false";
        String [] retLst = null;

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                // getting class_course_id
                String query = "SELECT class_course_id FROM tb_class_course "
                        + "WHERE course_id=" + coid
                        + " AND class_id=" + clid;

                ResultSet result = stat.executeQuery(query);

                int clcoid = 0;
                while(result.next()){
                    clcoid = Integer.parseInt(result.getString("class_course_id"));
                }

                // getting ambiance/datetime list
                query = "SELECT `tb_ccdt`.`dt_id`, `tb_ccdta`.`ambiance_id` "
                        + "FROM tb_ccdt "
                        + "INNER JOIN tb_ccdta "
                        + "ON `tb_ccdt`.`id_ccdt`=`tb_ccdta`.`id_ccdt` "
                        + "WHERE `tb_ccdt`.`class_course_id`=" + clcoid;

                result = stat.executeQuery(query);

                while(result.next()){
                    if (line.equals("false"))
                        line = result.getString("ambiance_id") + ":" + result.getString("dt_id");
                    else
                        line += "&" + result.getString("ambiance_id") + ":" + result.getString("dt_id");
                }

                conn.close();                

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        retLst = line.split("&");
        return retLst;
    }
    
    /** Gets the candidate program wanted
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the candidate program in format:
     *      <program name>:<program id>
     *      If an error occurs returns false
     *      If session expired returns session_expired
     */
    public String GetCandidateProgram(String username, String cookie, String ip) {
        String line = "false";

        if (CheckSession(username, cookie, ip)) { // session OK

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`program_id`, `tb_program`.`name` "                        
                        + "FROM tb_person INNER JOIN tb_program "
                        + "ON `tb_person`.`program_id`=`tb_program`.`program_id` "
                        + "WHERE `tb_person`.`nickname`='" + username + "'";

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    line = result.getString("name");
                    line += ":" + result.getString("program_id");                    
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;

    }
    
    /** Gets the person from person
    *
    * @param username. String username.
    * @param cookie. String cookie value.
    * @param ip. String ip.
    * @param pid. Integer person id.
    * @return returns a list with the user settings in format:
    * <program name>:<program id>
    *          If session expired returns session_expired
    *          If an error occurs returns false
    */
    public String GetProgramFromPerson(String username, String cookie, String ip, int pid) {
       String line = "false";

        if (CheckSession(username, cookie, ip)) { //session Ok

            try {
                //Class.forName("com.mysql.jdbc.Driver").newInstance ();
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "SELECT `tb_person`.`program_id`, `tb_program`.`name` "
                        + "FROM tb_person "
                        + "INNER JOIN tb_program ON `tb_person`.`program_id`=`tb_program`.`program_id` "                        
                        + "WHERE `tb_person`.`person_id`=" + pid;

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                        if (line.equals("false"))
                            line = result.getString("name");
                        line += ":" + result.getString("program_id");                        
                }

                System.out.println(line);

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { //session expired
            line = SESSION_EXPIRED_MSG;
        }

        return line;
    }
    
    /** Changes interested user to candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname from interested
     * @return Returns true if user was changed, else returns false
     *          If session expired returns session_expired
     */
    public String ChangeToCandidate(String username, String cookie, String ip, String nickname) {
        String ret = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the candidate actor id
                String query = "SELECT person_type_id FROM tb_person_type "
                        + "WHERE name='Candidate'";

                ResultSet result = stat.executeQuery(query);

                int ptid = 0;
                while(result.next()){
                    ptid = Integer.parseInt(result.getString("person_type_id"));
                }

                query = "UPDATE tb_person SET person_type_id=" + ptid
                        + " WHERE nickname='" + nickname + "'";
                if (stat.executeUpdate(query) > 0) {
                    ret = "true";

                    //getting the fullname, email
                    query = "SELECT name, email FROM tb_person "
                            + "WHERE nickname='" + nickname + "'";

                    result = stat.executeQuery(query);

                    String name = "";
                    String email = "";
                    while(result.next()){
                        name = result.getString("name");
                        email = result.getString("email");
                    }

                    GenerateNewPasswordDOUA(nickname, name, email);
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ret = SESSION_EXPIRED_MSG;
        }

        return ret;
    }

    /** Checks the agreement template image existence
     *
     * @param username . String username
     * @param cookie . String cookie value
     * @param ip . String ip
     * @param filename . String file name of image
     * @return Returns true if file exists, false if file not exists
     *          or returns file_not_allowed if file is not the image required
     *          If session expired returns session_expired
     */
    public String CheckAgreementTemplateImageExistence(String username, String cookie, String ip, String filename) {
        String ret = "false";
        String outputPath  = agreementTemplateImgDir + filename;

        if (CheckSession(username, cookie, ip)) { //Session OK

            String [] filenameLst = filename.split("\\.");
            int last = filenameLst.length - 1;
            if (filenameLst[last].equals("pdf") || filenameLst[last].equals("eps") ||
                    filenameLst[last].equals("jpg") || filenameLst[last].equals("gif") ||
                    filenameLst[last].equals("png")) {

                File f = new File(outputPath);
                if (f.exists()) {
                    ret = "true";
                } else {
                    ret = "false";
                }
            } else { // file not allowed
                ret = "file_not_allowed";
            }
        } else { // session expired
            ret = SESSION_EXPIRED_MSG;
        }

        return ret;
    }

    /** Gets the user payment settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id
     * @return Returns payment settings in format:
     *          <fullname>:<desc_value>:<desc_quota>
     *          If session expired returns session_expired
     *          If an error occurs return false
     */
    public String GetUserPaymentSettings(String username, String cookie, String ip, int pid) {
        String ret = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //getting the user payment settings
                String query = "SELECT name, desc_value, desc_quota FROM tb_person "
                        + "WHERE person_id=" + pid;

                ResultSet result = stat.executeQuery(query);
                
                while(result.next()){
                    ret = result.getString("name") + ":" + result.getString("desc_value") + ":" + result.getString("desc_quota");
                }                

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ret = SESSION_EXPIRED_MSG;
        }

        return ret;
    }

    /** Sets the user's payment.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. Integer person id
     * @param desc_value. String desc_value
     * @param desc_quota. String desc_quota
     * @return Returns true if payment was set, else returns false
     *          If session expired returns session_expired
     */
    public String SetUserPayment(String username, String cookie, String ip, int pid, String desc_value, String desc_quota) {
        String ret = "false";

        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                String query = "UPDATE tb_person SET desc_value='" + desc_value
                        + "', desc_quota='" + desc_quota 
                        + "' WHERE person_id=" + pid;
                if (stat.executeUpdate(query) > 0) {
                    ret = "true";
                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}

        } else { // session expired
            ret = SESSION_EXPIRED_MSG;
        }

        return ret;
    }
    
    /** Resizes the image
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param filename. String filename
     * @return Returns true if image was resized, else returns false
     *          If session expired returns session_expired
     */
    public String ResizeImage(String username, String cookie, String ip, String filename) {
        String ret = "false";
        
        if (CheckSession(username, cookie, ip)) { // session Ok
            try {
            
                String [] fileProp = filename.split("\\.");
                
                String formatName = "jpg";
                if (fileProp[1].equalsIgnoreCase("jpg") || fileProp[1].equalsIgnoreCase("jpeg"))
                    formatName = "jpg";
                else if (fileProp[1].equalsIgnoreCase("png"))
                    formatName = "png";
                else if (fileProp[1].equalsIgnoreCase("gif"))
                    formatName = "gif";


                BufferedImage originalImage = ImageIO.read(new File(uploadDir + filename));
                int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                String newFilename = fileProp[0] + "_r." + fileProp[1];
                BufferedImage resizeImageJpg = AuxiliaryMethods.ResizeImage(originalImage, type);
                ImageIO.write(resizeImageJpg, formatName, new File(uploadDir + newFilename));

                ret = newFilename;

            } catch (IOException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { // session expired
            ret = SESSION_EXPIRED_MSG;
        }
        
        return ret;
    }
    
    /** Download the thumbnail data from user
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id
     * @param fid. String file id
     * @return Returns the file data encoded in Base64
     *          If an error occurs return false
     *          If session expired returns session_expired
     */
    public String DownloadThumbnailFromUser(String username, String cookie, String ip, String pid, String fid) {
        String fileData = "false";
        
        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Check if user can download this file.
                String query = "SELECT `tb_file`.`permission`, `tb_formats`.`extension` FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "                        
                        + "WHERE `tb_file`.`person_id`=" + pid
                        + " AND `tb_file`.`id_file`=" + fid;
                ResultSet result = stat.executeQuery(query);
                
                String permission = "";
                String extension = "";
                while(result.next()){
                    permission = result.getString("permission");
                    extension = result.getString("extension");
                }
                
                String filePath = uploadDir + permission + "/" + fid + "." + extension;                
                
                String [] fileProp = filePath.split("\\.");
                
                String formatName = "jpg";
                if (fileProp[1].equalsIgnoreCase("jpg") || fileProp[1].equalsIgnoreCase("jpeg"))
                    formatName = "jpg";
                else if (fileProp[1].equalsIgnoreCase("png"))
                    formatName = "png";
                else if (fileProp[1].equalsIgnoreCase("gif"))
                    formatName = "gif";

                BufferedImage originalImage = ImageIO.read(new File(filePath));
                int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                String newFilePath = uploadDir + permission + "/" + fid + "_r." + extension;
                BufferedImage resizeImageJpg = AuxiliaryMethods.ResizeImage(originalImage, type);
                ImageIO.write(resizeImageJpg, formatName, new File(newFilePath));
                
                File file = new File(newFilePath);
                FileInputStream fis = new FileInputStream(file);

                byte fileContent[] = new byte[(int)file.length()];

                /*
                 * To read content of the file in byte array, use
                 * int read(byte[] byteArray) method of java FileInputStream class.
                 *
                 */
                fis.read(fileContent);


                fis.close();

                //create string from byte array
                //String strFileContent = new String(fileContent);

                //System.out.println("File content : ");
                //System.out.println(strFileContent);

                fileData = Base64.encode(fileContent);                    

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }
    
    /** Download the thumbnail data
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param fid. String file id
     * @return Returns the file data encoded in Base64
     *          If an error occurs return false
     *          If session expired returns session_expired
     */
    public String DownloadThumbnail(String username, String cookie, String ip, String fid) {
        String fileData = "false";
        
        if (CheckSession(username, cookie, ip)) { //session OK

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection (databaseUrl,
                                                            userName, userPassword);

               // System.out.println("database connected");
                Statement stat = conn.createStatement();

                //Check if user can download this file.
                String query = "SELECT `tb_file`.`permission`, `tb_formats`.`extension` FROM tb_file "
                        + "INNER JOIN tb_formats "
                        + "ON `tb_file`.`id_formats`=`tb_formats`.`id_formats` "
                        + "INNER JOIN tb_person "
                        + "ON `tb_file`.`person_id`=`tb_person`.`person_id` "
                        + "WHERE `tb_file`.`empty_for_user`=0"
                        + " AND `tb_person`.`nickname`='" + username
                        + "' AND `tb_file`.`id_file`=" + fid;
                ResultSet result = stat.executeQuery(query);

                boolean allowedDownload = false;
                String permission = "";
                String extension = "";
                while(result.next()){
                    allowedDownload = true;
                    permission = result.getString("permission");
                    extension = result.getString("extension");
                }

                String filePath = uploadDir + permission + "/" + fid + "." + extension;
                String [] fileProp = filePath.split("\\.");
                
                String formatName = "jpg";
                if (fileProp[1].equalsIgnoreCase("jpg") || fileProp[1].equalsIgnoreCase("jpeg"))
                    formatName = "jpg";
                else if (fileProp[1].equalsIgnoreCase("png"))
                    formatName = "png";
                else if (fileProp[1].equalsIgnoreCase("gif"))
                    formatName = "gif";

                BufferedImage originalImage = ImageIO.read(new File(filePath));
                int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                String newFilePath = uploadDir + permission + "/" + fid + "_r." + extension;
                BufferedImage resizeImageJpg = AuxiliaryMethods.ResizeImage(originalImage, type);
                ImageIO.write(resizeImageJpg, formatName, new File(newFilePath));
                
                if (permission.equals("public")) {                    
                    File file = new File(newFilePath);
                    FileInputStream fis = new FileInputStream(file);

                    byte fileContent[] = new byte[(int)file.length()];

                    /*
                     * To read content of the file in byte array, use
                     * int read(byte[] byteArray) method of java FileInputStream class.
                     *
                     */
                    fis.read(fileContent);


                    fis.close();

                    //create string from byte array
                    //String strFileContent = new String(fileContent);

                    //System.out.println("File content : ");
                    //System.out.println(strFileContent);

                    fileData = Base64.encode(fileContent);
                    
                } else { //file is private

                    if (allowedDownload) {
                        File file = new File(newFilePath);
                        FileInputStream fis = new FileInputStream(file);

                        byte fileContent[] = new byte[(int)file.length()];

                        /*
                         * To read content of the file in byte array, use
                         * int read(byte[] byteArray) method of java FileInputStream class.
                         *
                         */
                        fis.read(fileContent);


                        fis.close();

                        //create string from byte array
                        //String strFileContent = new String(fileContent);

                        //System.out.println("File content : ");
                        //System.out.println(strFileContent);

                        fileData = Base64.encode(fileContent);

                    }

                }

                conn.close();

            }catch (SQLException e) {e.printStackTrace();}
            //catch (InstantiationException e) {System.out.println("InstantiationException");}
            //catch (IllegalAccessException e) {System.out.println("IllegalAccessException");}
            catch (ClassNotFoundException e) {e.printStackTrace();}
            catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(DatabaseOp.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else { // session expired
            fileData = SESSION_EXPIRED_MSG;
        }

        return fileData;
    }

} // END of Class DatabaseOp
