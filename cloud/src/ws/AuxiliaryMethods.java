/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import de.nixosoft.jlr.JLRConverter;
import de.nixosoft.jlr.JLRGenerator;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Administrador
 */
public class AuxiliaryMethods {
    
   private static String templatesDir = "/home/cem/templates";
   private static String agreementDir = "/home/cem/upload/private";
   private static String uploadDir = "/home/cem/upload";
   private static String agreementTemplateDir = "/home/cem/templates";
   private static String emailTemplateDir = "/home/emsftp/upload/email";
   private static int mResizedWidth = 125;
   private static int mResizedHeight = 125;
   
   private static String utf8_decode(String s) {
        byte bytes[];
        String s2 = "";
        try {
            bytes = s.getBytes("ISO-8859-1");
            s2 = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AuxiliaryMethods.class.getName()).log(Level.SEVERE, null, ex);
        }

        return s2;
    }
    
    /** Creates the md5 hash from str
     *
     * @param str. String to be hashed.
     * @return Resturns the md5 hash.
     */
    public static String md5(String str){
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

    /** Send email to recipient
     *
     * @param recipient. String recipient
     * @param login. String login name
     * @param password. String password
     * @return returns true if email was sent, else returns false
     */
    public static boolean SendEmail(String recipient, String login, String password) {
        boolean ret = false;
        try {
            String to = recipient;
            String from = "no-reply@cloudeducationmanagement.com";

            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            Session session = Session.getInstance(props, null);

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            Address toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject("CEM - Candidate Registry Confirmation");

            String body = "Candidate " + login + ",\n"
                    + "now you are registered at CEM, and can access our system.\n"
                    + "\nTo access CEM you must enter with this password: " + password
                    + "\n\nThis email was sent automatically, please don't reply.\n"
                    + "\n[]'s\nCEM";
            message.setContent(body, "text/plain");

            Transport.send(message);

            ret = true;
        }
        catch (MessagingException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    /** Generates a random password
     *
     * @param length. Integer password length
     * @return returns the generated password
     */
    public static String RandomPassword(int length) {
        String charset = "!@#$¨*()0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }
    
    /** Converts file to utf-8 encoding
     * 
     * @param file. File to be encoded 
     * @return If file was converted returns true, else returns false 
     */
    public static boolean convert_file_utf8(File file) {
        boolean ret = false;

        try {
            File aux = new File(file.getAbsolutePath() + "~");
            FileInputStream fis = new FileInputStream(file);
            byte[] contents = new byte[fis.available()];
            fis.read(contents, 0, contents.length);
            String asString = new String(contents, "ISO8859_1");
            byte[] newBytes = asString.getBytes("UTF8");
            FileOutputStream fos = new FileOutputStream(aux);
            fos.write(newBytes);
            fos.close();
            fis.close();

            if (file.delete())
                aux.renameTo(new File(file.getAbsolutePath()));


            ret = true;
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }

        return ret;
    }
    
    public static boolean GenerateAgreement(String login, String agr_id, String fullname, String email, String telephone, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf, String desc_value, String desc_quota) {
        boolean agreementCreated = false;

        // TODO code application logic here
        File workingDirectory = new File(templatesDir);
        System.out.println(workingDirectory.getAbsolutePath());
        
        File template = new File(workingDirectory.getAbsolutePath() + File.separator + agr_id + ".tex");

        File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        String filename = login + ".tex";
        File agreement = new File(tempDir.getAbsolutePath() + File.separator + filename);

        try {
            HashMap<String, String> data = new HashMap<String, String>();

            data.put("fullname", fullname);            
            data.put("email", email);            
            data.put("telephone", telephone);
            data.put("address", address);            
            data.put("district", district);            
            data.put("city", city);
            data.put("state", state);            
            data.put("zipCode", zip_code);            
            data.put("civilStatus", civil_status);
            data.put("career", career);            
            data.put("identity", identity);
            data.put("issuedBy", issued_by);            
            data.put("cpf", cpf);
            data.put("descValue", desc_value);
            data.put("descQuota", desc_quota);

            JLRConverter converter = new JLRConverter("::", ":::");            
            if (!converter.parse(template, agreement, data)) {
                System.out.println(converter.getErrorMessage());
            }

            //String filename2 = utf8_decode(login) + "2.tex";
            //File agreement2 = new File(tempDir.getAbsolutePath() + File.separator + filename2);

            //convert_file_utf8(agreement);
            
            File outputDir = new File(agreementDir);

            JLRGenerator pdfGen = new JLRGenerator();

            pdfGen.deleteTempTexFile(true);

            // must be run twice for print the page
            if (!pdfGen.generate(agreement, outputDir, workingDirectory)) {
                System.out.println(pdfGen.getErrorMessage());
            } else
                agreementCreated = true;

            if (!pdfGen.generate(agreement, outputDir, workingDirectory)) {
                System.out.println(pdfGen.getErrorMessage());
            } else
                agreementCreated = true;
            
            
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return agreementCreated;
    }

    public static boolean AdminGenerateAgreement(String login, String agr_id, String fullname, String email, String telephone, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf, String desc_value, String desc_quota) {
        boolean agreementCreated = false;

        // TODO code application logic here
        File workingDirectory = new File(templatesDir);
        System.out.println(workingDirectory.getAbsolutePath());

        File template = new File(workingDirectory.getAbsolutePath() + File.separator + agr_id + ".tex");

        File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");
        if (!tempDir.isDirectory()) {
            tempDir.mkdir();
        }

        String filename = login + "_adm.tex";
        File agreement = new File(tempDir.getAbsolutePath() + File.separator + filename);

        try {
            HashMap<String, String> data = new HashMap<String, String>();

            data.put("fullname", fullname);
            data.put("email", email);
            data.put("telephone", telephone);
            data.put("address", address);
            data.put("district", district);
            data.put("city", city);
            data.put("state", state);
            data.put("zipCode", zip_code);
            data.put("civilStatus", civil_status);
            data.put("career", career);
            data.put("identity", identity);
            data.put("issuedBy", issued_by);
            data.put("cpf", cpf);
            data.put("descValue", desc_value);
            data.put("descQuota", desc_quota);

            JLRConverter converter = new JLRConverter("::", ":::");
            if (!converter.parse(template, agreement, data)) {
                System.out.println(converter.getErrorMessage());
            }

            //String filename2 = utf8_decode(login) + "2.tex";
            //File agreement2 = new File(tempDir.getAbsolutePath() + File.separator + filename2);

            //convert_file_utf8(agreement);

            File outputDir = new File(agreementDir);

            JLRGenerator pdfGen = new JLRGenerator();

            pdfGen.deleteTempTexFile(true);

            // must be run twice for print the page
            if (!pdfGen.generate(agreement, outputDir, workingDirectory)) {
                System.out.println(pdfGen.getErrorMessage());
            } else
                agreementCreated = true;

            if (!pdfGen.generate(agreement, outputDir, workingDirectory)) {
                System.out.println(pdfGen.getErrorMessage());
            } else
                agreementCreated = true;


        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return agreementCreated;
    }

    /** Deletes the file located in uploadDir/permission/name.extension
     *
     * @param permission. String permission
     * @param name. String file name
     * @param extension. String file extension
     * @return Returns true if file was deleted, else returns false
     */
    public static boolean DeleteFile(String permission, String name, String extension) {
        String filePath = uploadDir + File.separator + permission + File.separator + name + "." + extension;
        File file = new File(filePath);

        return file.delete();
    }

    /** Deletes the agreement template
     *
     * @param name. String file name
     * @return Returns true if file was deleted, else returns false
     */
    public static boolean DeleteAgreementTemplate(String name) {
        String filePath = agreementTemplateDir + File.separator + name + ".tex";
        File file = new File(filePath);

        return file.delete();
    }

    /** Send email to recipient using gmail
     *
     * @param recipient. String recipient
     * @param login. String login name
     * @param fullname. String full name
     * @return returns true if email was sent, else returns false
     */
    public static boolean SendEmailGmailNewInterested(String login, String fullname) {
        boolean ret = false;
        // setting up connection parameters
        String host = "smtp.gmail.com";
        int port = 587;
        String user = "selectos.cem@gmail.com";
        String pass = "selectoscem";

        try {

            // setting up security properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // setting up connection to SMTP-Server and connect
            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");

            transport.connect(host, port, user, pass);

            // building Address-Array with recipients
            // for more than one address seperate them by simple comma
            String recipient = "sbvb@sbvb.com.br,heineck@gmail.com,jhonatas_alfradique@poli.ufrj.br";
            Address[] addresses = InternetAddress.parse(recipient);

            // setting up new MimeMessage
            Message message = new MimeMessage(session);

            // setting the FROM-Header wich should equal to username
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject("CEM - Notificação de nova pré-inscrição");
            String body = "O novo usuário abaixo realizou pré-inscrição:\n"
                    + "Nome: " + fullname
                    + "\nemail: " + login
                    + "\n\nEste email foi enviado automaticamente, por favor, não responda.\n"
                    + "\n[]'s\nCEM";
            message.setText(body);
            transport.sendMessage(message, addresses);
            // closing Transport-connection clean
            transport.close();
            ret = true;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        //} catch (AddressException ex) {
        //    Logger.getLogger(ws_test.class.getName()).log(Level.SEVERE, null, ex);
        //}

        return ret;
    }
    
    public static boolean TestSendEmailGmail(String email) {
        boolean ret = false;
        // setting up connection parameters
        String host = "smtp.gmail.com";
        int port = 587;
        String user = "selectos.cem@gmail.com";
        String pass = "selectoscem";

        try {

            // setting up security properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // setting up connection to SMTP-Server and connect
            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");

            transport.connect(host, port, user, pass);

            // building Address-Array with recipients
            // for more than one address seperate them by simple comma
            String recipient = email;
            Address[] addresses = InternetAddress.parse(recipient);

            // setting up new MimeMessage
            Message message = new MimeMessage(session);

            // setting the FROM-Header wich should equal to username
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject("CEM - Email Test");
            String body = "Email send working well."                    
                    + "\n\nEste email foi enviado automaticamente, por favor, não responda.\n"
                    + "\n[]'s\nCEM";
            message.setText(body);
            transport.sendMessage(message, addresses);
            // closing Transport-connection clean
            transport.close();
            ret = true;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        //} catch (AddressException ex) {
        //    Logger.getLogger(ws_test.class.getName()).log(Level.SEVERE, null, ex);
        //}

        return ret;
    }

    /** Send email to recipient using gmail when new user realizes
     *
     * @param recipient. String recipient
     * @param name. String name
     * @return returns true if email was sent, else returns false
     */
    public static boolean SendEmailGmailPreRegistration(String recipient, String name) {
        boolean ret = false;
        // setting up connection parameters
        String host = "smtp.gmail.com";
        int port = 587;
        String user = "selectos.cem@gmail.com";
        String pass = "selectoscem";

        try {

            // setting up security properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // setting up connection to SMTP-Server and connect
            Session session = Session.getInstance(props);
            Transport transport = session.getTransport("smtp");

            transport.connect(host, port, user, pass);

            String recipients = recipient + ",sbvb@sbvb.com.br,heineck@gmail.com,jhonatas_alfradique@poli.ufrj.br";

            // building Address-Array with recipients
            // for more than one address seperate them by simple comma
            Address[] addresses = InternetAddress.parse(recipients);

            // setting up new MimeMessage
            Message message = new MimeMessage(session);

            // setting the FROM-Header wich should equal to username
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, addresses);
            message.setSubject("CEM - Confirmação de Registro de Interessado");
            String body = GetEmailPatternFromFile(emailTemplateDir + "/email_new_interested.txt");
            if (!body.equalsIgnoreCase(":|:false:|:")) {
                String auxBody = body.replace(":|||:name:|||:", name);
                body = auxBody;
            }

            message.setText(body);
            transport.sendMessage(message, addresses);
            // closing Transport-connection clean
            transport.close();
            ret = true;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        //} catch (AddressException ex) {
        //    Logger.getLogger(ws_test.class.getName()).log(Level.SEVERE, null, ex);
        //}

        return ret;
    }
    
    /** Resizes the image
     * 
     * @param originalImage. Original image
     * @param type. image type
     * @return Returns the resized image.
     */
    public static BufferedImage ResizeImage(BufferedImage originalImage, int type){
	BufferedImage resizedImage = new BufferedImage(mResizedWidth, mResizedHeight, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, mResizedWidth, mResizedHeight, null);
	g.dispose();
         
	return resizedImage;
    }
    
    /** Opens the email pattern from file.
     * 
     * @param filename. Path of the file
     * @return Returns the email at string format. If an error occurs returns ":|:false:|:"
     */
    public static String GetEmailPatternFromFile(String filename) {
       String content = ":|:false:|:";   
       try {
            File file = new File(filename);
            FileReader reader = new FileReader(file);
       
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
       } catch (FileNotFoundException ex) {
            Logger.getLogger(AuxiliaryMethods.class.getName()).log(Level.SEVERE, null, ex);
       } catch (IOException ex) {
                Logger.getLogger(AuxiliaryMethods.class.getName()).log(Level.SEVERE, null, ex);
       }
        
        return content;
    }
}
