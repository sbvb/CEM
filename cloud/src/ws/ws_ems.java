/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ws;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinícius Heineck dos Santos
 * Date: 2011/03/15
 */
public class ws_ems {

    static String uploadDir = "/home/cem/upload/";
    static String agreementTemplateDir = "/home/cem/templates/";
    static String agreementTemplateImgDir = "/home/cem/templates/temp/";

    public String write_to_log() {
        Logger.getLogger("MYLOG", "TEST");
        return "true";
    }
    
    private String utf8_decode(String s) {
        byte bytes[];
        String s2 = "";
        try {
            bytes = s.getBytes("ISO-8859-1");
            s2 = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }

        return s2;
    }
    
    public String Hello(){
        return "Hello webservice ems";
    }

    public String get_host() {
        DatabaseOp db = new DatabaseOp();
        return db.GetConfigurations();
    }

    public String get_working_directory() {
        String ret = "false";
        try {
            File currentDirectory = new File(new File(".").getAbsolutePath());
            ret = currentDirectory.getCanonicalPath();
            //System.out.println(currentDirectory.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    public String create_new_user(String username, String cookie, String ip, String nickname, String fullname, String email, String telephone, String ptid, String scid, String obs, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewUser(utf8_decode(username), cookie, ip, utf8_decode(nickname), utf8_decode(fullname), utf8_decode(email), telephone, Integer.parseInt(ptid), Integer.parseInt(scid), utf8_decode(obs), utf8_decode(address), utf8_decode(district), utf8_decode(city), utf8_decode(state), zip_code, utf8_decode(civil_status), utf8_decode(career), identity, utf8_decode(issued_by), cpf);
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
    public String delete_user(String username, String cookie, String ip, String nickname) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteUser(utf8_decode(username), cookie, ip, utf8_decode(nickname));
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
    public String [] get_user_settings(String username, String cookie, String ip, String nickname) {
        DatabaseOp db = new DatabaseOp();
        return db.GetUserSettings(utf8_decode(username), cookie, ip, utf8_decode(nickname));
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
    public String edit_user_settings(String username, String cookie, String ip, String nickname, String fullname, String email, String telephone, int scid, int ptid, String obs, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf) {
        DatabaseOp db = new DatabaseOp();
        return db.EditUserSettings(utf8_decode(username), cookie, ip, utf8_decode(nickname), utf8_decode(fullname), utf8_decode(email), telephone, scid, ptid, utf8_decode(obs), utf8_decode(address), utf8_decode(district), utf8_decode(city), utf8_decode(state), zip_code, utf8_decode(civil_status), utf8_decode(career), identity, utf8_decode(issued_by), cpf);
    }

    /** Creates new ambiance
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String ambiance name
     * @param max_students. String max_students     
     * @return Returns true if new ambiance was created or false if the ambiance name already exists.
     *          If session expired returns session_expired
     */
    public String create_new_ambience(String username, String cookie, String ip, String name, String max_students) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewAmbience(utf8_decode(username), cookie, ip, utf8_decode(name), Integer.parseInt(max_students));
    }

    /** Deletes the ambiance
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ambid. String ambiance id
     * @return returns true if ambience was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String delete_ambience(String username, String cookie, String ip, String ambid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteAmbience(utf8_decode(username), cookie, ip, Integer.parseInt(ambid));
    }

    /** Edits the ambiance
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param max_students. String max students
     * @param ambid. String ambiance id     
     * @return returns true if ambiance was edited, else returns false
     *          If session expired returns session_expired
     */
    public String edit_ambience_settings(String username, String cookie, String ip, String max_students, String ambid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditAmbienceSettings(utf8_decode(username), cookie, ip, Integer.parseInt(max_students), Integer.parseInt(ambid));
    }

     /** Gets the ambiance settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ambid. String ambiance id
     * @return return the ambiance settings in format:
     *          <ambiance name>:<max_students>:<schedule id>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  get_ambience_settings(String username, String cookie, String ip, String ambid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetAmbienceSettings(utf8_decode(username), cookie, ip, Integer.parseInt(ambid));
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
    public String [] get_ambiences_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetAmbiencesList(utf8_decode(username), cookie, ip);
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
    public String create_new_course(String username, String cookie, String ip, String cname, String code) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewCourse(utf8_decode(username), cookie, ip, utf8_decode(cname), utf8_decode(code));
    }

    /** Deletes the course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. String course id
     * @return returns true if course was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String delete_course(String username, String cookie, String ip, String cid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteCourse(utf8_decode(username), cookie, ip, Integer.parseInt(cid));
    }

    /** Edits the course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param code. String code name
     * @param cid. String course id
     * @return returns true if course was edited, else returns false
     *          If session expired returns session_expired
     */
    public String edit_course_settings(String username, String cookie, String ip, String code, String cid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditCourseSettings(utf8_decode(username), cookie, ip, utf8_decode(code), Integer.parseInt(cid));
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
    public String get_course_settings(String username, String cookie, String ip, String cid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCourseSettings(utf8_decode(username), cookie, ip, Integer.parseInt(cid));
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
    public String [] get_courses_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCoursesList(utf8_decode(username), cookie, ip);
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
    public String create_new_schedule(String username, String cookie, String ip, String schname) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewSchedule(utf8_decode(username), cookie, ip, utf8_decode(schname));        
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
    public String delete_schedule(String username, String cookie, String ip, String schid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteSchedule(utf8_decode(username), cookie, ip, Integer.parseInt(schid));
    }

    /** Edits the schedule settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param schname. String schedule name
     * @param schid. String schedule id
     * @return returns true if schedule was edited, else returns false
     *          If session expired returns session_expired
     */
    public String edit_schedule_settings(String username, String cookie, String ip, String schname, String schid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditScheduleSettings(utf8_decode(username), cookie, ip, utf8_decode(schname), Integer.parseInt(schid));
    }

    /** Gets the schedule settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String username
     * @param schid. Integer schedule id
     * @return return the schedule settings in format:
     *          <schedule name>;<schedule_id>;<date>;<time_begin>;<time_end>;<datetime id>
     *          Else returns empty String
     */
    public String [] get_schedule_settings(String username, String cookie, String ip, String schid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetScheduleSettings(utf8_decode(username), cookie, ip, Integer.parseInt(schid));
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
    public String [] get_schedules_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetSchedulesList(utf8_decode(username), cookie, ip);
    }

    /** Creates a new class
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cname. String class name
     * * @param pid. String program id
     * @return returns true if class was created, else returns false
     *          If session expired returns session_expired
     */
    public String create_new_class(String username, String cookie, String ip, String cname, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewClass(utf8_decode(username), cookie, ip, utf8_decode(cname), Integer.parseInt(pid));
    }

    /** Deletes the class
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. String class id
     * @return returns true if class was deleted, else returns false
     *         If session expired returns session_expired
     */
    public String delete_class(String username, String cookie, String ip, String cid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteClass(utf8_decode(username), cookie, ip, Integer.parseInt(cid));
    }

    /** Edits the class settings
     *
     * @param name. String name
     * @param course_id. String course_id
     * @param schedule_id. String schedule_id
     * @param tutor_id. String tutor_id
     * @return returns true if class was edited, else returns false
     */
    public String edit_class_settings(String name, String course_id, String schedule_id, String tutor_id) {
        DatabaseOp db = new DatabaseOp();
        return db.EditClassSettings(utf8_decode(name), Integer.parseInt(course_id), Integer.parseInt(schedule_id)
                , Integer.parseInt(tutor_id));
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
    public String [] get_classes_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetClassesList(utf8_decode(username), cookie, ip);
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
    public String [] get_courses_from_tutor(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCoursesFromTutor(utf8_decode(username),cookie,ip);
    }      
    
    /** Get persons list according with person type
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param ptype. String person type
     * @param sort. String sort: name = sort by name, date = sort by date
     * @return returns a list of person in format:
     *          <person name>:<person id>:<nickname>:<date>:<reg_type>:<telephone>:<promo_code>
     *          <person name 1>:<person id 1>:<nickname 1>:<date 1>:<reg_type 1>:<telephone 1>:<promo_code 1>
     *          <person name 2>:<person id 2>:<nickname 2>:<date 2>:<reg_type 2>:<telephone 2>:<promo_code 2>
     *          If not exists any person, return null
     *          If session expiredreturns session_expired
     */
    public String [] get_person_list(String username, String cookie, String ip, String ptype, String sort) {
        DatabaseOp db = new DatabaseOp();
        return db.GetPersonList(utf8_decode(username), cookie, ip, utf8_decode(ptype), sort);
    }

    /** Creates a new datetime
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param date. String date
     * @param time_begin. String time_begin
     * @param time_end. String time_end
     * @param schid. String schedule id     
     * @return returns true if datetime was created, else returns two cases:
     *          1) excDateConflict = date conflict
     *          2) false = Registry can't be wrote
     *          If session expired returns session_expired
     */
    public String create_new_datetime(String username, String cookie, String ip, String date, String time_begin, String time_end, String schid) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewDatetime(utf8_decode(username), cookie, ip, date, time_begin, time_end, Integer.parseInt(schid));
    }     

    /** Fills the presence.
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param ccdtid. String ccdt id.
     * @param stline. String students line, format below:
     * <student_id 1>:<student_id 2>:<student_id 3>
     * @return Returns true if Presence was made, else returns false.
     *          If session expired returns session_expired
     */
    public String answer_presence(String username, String cookie, String ip, String ccdtid, String stline) {
        DatabaseOp db = new DatabaseOp();
        return db.AnswerPresence(utf8_decode(username), cookie, ip, Integer.parseInt(ccdtid), stline);
    }

    /** Gets school settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param scid. String school id
     * @return return the school settings in format:
     *          <school name>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String get_school_settings(String username, String cookie, String ip, String scid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetSchoolSettings(username, cookie, ip, Integer.parseInt(scid));
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
    public String [] get_school_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetSchoolList(utf8_decode(username), cookie, ip);
    }

    /** Creates a new school
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String school name
     * @return returns true if school was created, else returns false.
     *          If session expired returns session_expired.
     */
    public String create_new_school(String username, String cookie, String ip, String name) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewSchool(utf8_decode(username), cookie, ip, utf8_decode(name));
    }

    /** Deletes the school
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param sid. String school id
     * @return returns true if school was deleted, else returns false.
     *          If session expired returns session_expired
     */
    public String delete_school(String username, String cookie, String ip, String sid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteSchool(utf8_decode(username), cookie, ip, Integer.parseInt(sid));
    }

    /** Edits the school
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param sname. String school name
     * @param scid. String school id
     * @return If school edited returns true, else returns false
     *          If session expired returns session_expired
     */
    public String edit_school_settings(String username, String cookie, String ip, String sname, String scid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditSchoolSettings(utf8_decode(username), cookie, ip, utf8_decode(sname), Integer.parseInt(scid));
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
    public String [] get_programs_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetProgramsList(utf8_decode(username), cookie, ip);
    }

    /** Creates new program
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param name. String program name
     * @param value. String value
     * @param description. String description
     * @return If program created returns true, else returns false
     *          If session expired returns session_expired
     */
    public String create_new_program(String username, String cookie, String ip, String name, String value, String description) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewProgram(utf8_decode(username), cookie, ip, utf8_decode(name), Integer.parseInt(value), utf8_decode(description));
    }

    /** Deletes the program
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String program id
     * @return returns true if program was deleted, else returns false
     *         If session expired returns session_expired
     */
    public String delete_program(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteProgram(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Gets program settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String program id
     * @return return the program settings in format:
     *          <program name>:<value>:<description>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String get_program_settings(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetProgramSettings(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Edits the program
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pname. String program name
     * @param value. String value
     * @param description. String description
     * @param pid. String program id
     * @return If program edited returns true, else returns false
     *          If session expired returns session_expired
     */
    public String edit_program_settings(String username, String cookie, String ip, String pname, String value, String description, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditProgramSettings(utf8_decode(username), cookie, ip, utf8_decode(pname), Integer.parseInt(value), utf8_decode(description), Integer.parseInt(pid));
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
    public String [] get_datetimes_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetDatetimesList(utf8_decode(username), cookie, ip);
    }

    /** Deletes the datetime
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param dtid. String datetime id
     * @return returns true if datetime was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String delete_datetime(String username, String cookie, String ip, String dtid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteDatetime(utf8_decode(username), cookie, ip, Integer.parseInt(dtid));
    }

    /** Gets the datetime settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param dtid. String datetime id
     * @return return the datetime settings in format:
     *          <date>;<time begin>;<time end>;<schedule name>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  get_datetime_settings(String username, String cookie, String ip, String dtid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetDatetimeSettings(utf8_decode(username), cookie, ip, Integer.parseInt(dtid));
    }

    /** Associates course to program
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param pid. String program id
     * @param cline. String course id list
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String associate_course_to_program(String username, String cookie, String ip, String pid, String cline) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateCourseToProgram(utf8_decode(username), cookie, ip, Integer.parseInt(pid), cline);
    }

    /** Associates class to course
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @param schid. String schedule id
     * @param pid. String person id
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String associate_class_to_course(String username, String cookie, String ip, String coid, String clid, String schid, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateClassToCourse(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), Integer.parseInt(schid), Integer.parseInt(pid));
    }

    /** Gets All courses associated to program.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String program id
     * @return Returns courses list in format:
     *          <course name>:<course id>
     *          <course name 1>:<course id 1>
     *          <course name 2>:<course id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_courses_associated_program(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCoursesAssociatedProgram(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Gets All courses not associated to program.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String program id
     * @return Returns courses list in format:
     *          <course name>:<course id>
     *          <course name 1>:<course id 1>
     *          <course name 2>:<course id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_courses_not_associated_program(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCoursesNotAssociatedProgram(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Gets All courses and yours associatons to class.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param cid. String class id
     * @return Returns courses list in format:
     *          <course name>:<course id>:<managed OK>
     *          <course name 1>:<course id 1>:<managed OK>
     *          <course name 2>:<course id 2>:<managed OK>
     *          <managed OK> can be 1 or 0
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_courses_class_list(String username, String cookie, String ip, String cid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCoursesClassList(utf8_decode(username), cookie, ip, Integer.parseInt(cid));
    }

    /** Get class_course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. String class id
     * @param coid. String course id
     * @return return the class_course settings in format:
     *          <tutor name>:<tutor id>:<schedule name>:<schedule id>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  get_class_course_settings(String username, String cookie, String ip, String clid, String coid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetClassCourseSettings(utf8_decode(username), cookie, ip, Integer.parseInt(clid), Integer.parseInt(coid));
    }

    /** Edit class_course settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. String class id
     * @param coid. String course id
     * @param schid. String schedule id
     * @param pid. String person id
     * @return returns true if course was edited, else returns false
     *          If session expired returns session_expired
     */
    public String edit_class_to_course(String username, String cookie, String ip, String clid, String coid, String schid, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditClassToCourse(utf8_decode(username), cookie, ip, Integer.parseInt(clid), Integer.parseInt(coid), Integer.parseInt(schid), Integer.parseInt(pid));
    }

    /** Get tutor settings
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id
     * @return return the tutor settings in format:
     *          <nickname>:<fullname>:<email>:<telephone>:<obs>:<school name>:<school id>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String  get_tutor_settings(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetTutorSettings(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Gets All classes associated to course.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. String course id
     * @return Returns courses list in format:
     *          <class name>:<class id>
     *          <class name 1>:<class id 1>
     *          <class name 2>:<class id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_classes_associated_course(String username, String cookie, String ip, String coid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetClassesAssociatedCourse(utf8_decode(username), cookie, ip, Integer.parseInt(coid));
    }

    /** Gets All students associated to grade.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_students_associated_grade(String username, String cookie, String ip, String coid, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetStudentsAssociatedGrade(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid));
    }

     /** Gets All students not associated to grade.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_students_not_associated_grade(String username, String cookie, String ip, String coid, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetStudentsNotAssociatedGrade(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid));
    }

    /** Associates students to grade
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @param stline. String students id list
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String associate_students_to_grade(String username, String cookie, String ip, String coid, String clid, String stline) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateStudentsToGrade(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), stline);
    }

    /** Gets All classes from program.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String program id
     * @return Returns classes list in format:
     *          <class name>:<class id>
     *          <class name 1>:<class id 1>
     *          <class name 2>:<class id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_classes_from_program(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetClassesFromProgram(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Gets All students associated to enrollment.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. String class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_students_associated_enrollment(String username, String cookie, String ip, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetStudentsAssociatedEnrollment(utf8_decode(username), cookie, ip, Integer.parseInt(clid));
    }

    /** Gets All students not associated to enrollment.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. String class id
     * @return Returns students list in format:
     *          <student name>:<student id>
     *          <student name 1>:<student id 1>
     *          <student name 2>:<student id 2>
     *
     *          If an error occurs return null.
     *          If session expired returns session_expired
     */
    public String [] get_students_not_associated_enrollment(String username, String cookie, String ip, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetStudentsNotAssociatedEnrollment(utf8_decode(username), cookie, ip, Integer.parseInt(clid));
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
    public String associate_students_to_enrollment(String username, String cookie, String ip, String clid, String stline) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateStudentsToEnrollment(utf8_decode(username), cookie, ip, Integer.parseInt(clid), stline);
    }

    /** Gets the schedule from class_course
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param course_id. String course id.
     * @param class_id. String class id.
     * @return returns a list of schedules in format:
     *          <schedule name>:<schedule id>
     *          <schedule name>:<schedule id>
     *          <schedule name>:<schedule id>
     *          If not exists any schedule, return null
     */
    public String [] get_schedule_from_class_course(String username, String cookie, String ip, String coid, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetScheduleFromClassCourse(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid));
    }

    /** Associates event
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @param dtid. String datetime id
     * @param ambid. String ambiance id
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String associate_event(String username, String cookie, String ip, String coid, String clid, String dtid, String ambid) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateEvent(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), Integer.parseInt(dtid), Integer.parseInt(ambid));
    }

    /** Associates events
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @param evtline. String with list of ambiance id and datetime id in format below:
     *          <datetime id 1>,<ambiance id 1>:<datetime id 2>,<ambiance id 2>
     * @return Returns true if associations created, else returns false.
     *          If session expired returns session_expired
     */
    public String associate_events(String username, String cookie, String ip, String coid, String clid, String evtline) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateEvents(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), evtline);
    }

    /** Gets the ccdt list from schedule and class_course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. String course id
     * @param clid. String class id
     * @param schid. String schedule id
     * @param show_past_dates. String show past dates. If 0 do not show past dates, else shows.
     *
     * @return return the ccdt list in format:
     *          <id ccdt>;<date>;<time_begin>;<time_end>
     *          Else returns empty String
     *          If session expired returns session_expired
     */
    public String [] get_ccdt_from_schedule_class_course(String username, String cookie, String ip, String coid, String clid, String schid, String show_past_dates) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCcdtFromScheduleClassCourse(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), Integer.parseInt(schid), Integer.parseInt(show_past_dates));
    }

    /** Associates ambiance and datetime
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param ccdtid. String ccdt id
     * @param ambid. String ambiance id
     * @return Returns true if association created, else returns false.
     *          If session expired returns session_expired
     */
    public String associate_ambiance_ccdt(String username, String cookie, String ip, String ccdtid, String ambid) {
        DatabaseOp db = new DatabaseOp();
        return db.AssociateAmbianceCcdt(utf8_decode(username), cookie, ip, Integer.parseInt(ccdtid), Integer.parseInt(ambid));
    }

    /** Assigns the grade.
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. String course id.
     * @param clid. String class id.
     * @param gline. String grade line, format below:
     * <student_id 1>:<grade>;<student_id 2>:<grade>;<student_id 3>:<grade>
     * @return Returns true if grade was assigned, else returns false.
     *          If session expired returns session_expired
     */
    public String assign_grade(String username, String cookie, String ip, String coid, String clid, String gline) {
        DatabaseOp db = new DatabaseOp();
        return db.AssignGrade(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), gline);
    }

    /** Is class_course finished
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. String course id.
     * @param clid. String class id.
     * @return returns true if class_course is finished, else returns false
     *          If session expired returns session_expired
     */
    public String is_class_course_finished(String username, String cookie, String ip, String coid, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.IsClassCourseFinished(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid));
    }

    /** Gets the grade report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. String course id.
     * @param clid. String class id.
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
    public String [] show_grade_report(String username, String cookie, String ip, String coid, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.ShowGradeReport(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid));
    }

    /** Gets the presence report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.     
     * @param clid. String class id.
     * @param ccdtid. String ccdt id.
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
    public String [] show_presence_report(String username, String cookie, String ip, String clid, String ccdtid) {
        DatabaseOp db = new DatabaseOp();
        return db.ShowPresenceReport(utf8_decode(username), cookie, ip, Integer.parseInt(clid), Integer.parseInt(ccdtid));
    }

    /** Performs payment
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. String class id
     * @param sid. String student id
     * @return Returns true if payment was performed, else returns false.
     *          If session expired returns session_expired
     */
    public String perform_payment(String username, String cookie, String ip, String clid, String sid) {
        DatabaseOp db = new DatabaseOp();
        return db.PerformPayment(utf8_decode(username), cookie, ip, Integer.parseInt(clid), Integer.parseInt(sid));
    }

    /** Unlocks the class course
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param clid. String class id
     * @param coid. String course id
     * @return Returns true if class course was unlocked, else returns false.
     *          If session expired returns session_expired
     */
    public String unlock_class_course(String username, String cookie, String ip, String clid, String coid) {
        DatabaseOp db = new DatabaseOp();
        return db.UnlockClassCourse(utf8_decode(username), cookie, ip, Integer.parseInt(clid), Integer.parseInt(coid));
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
    public String get_ccdt_comments(String username, String cookie, String ip, String ccdtid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCcdtComments(utf8_decode(username), cookie, ip, Integer.parseInt(ccdtid));
    }

    /** Updates the event comments
     *
     * @param username. String username
     * @param cookie. String cookie value.
     * @param ip. String ip
     * @param ccdtid. String ccdt id
     * @param comments. String comments
     * @return Returns true if comments updated, else returns false.
     *          If session expired returns session_expired
     */
    public String comment_event(String username, String cookie, String ip, String ccdtid, String comments) {
        DatabaseOp db = new DatabaseOp();
        return db.CommentEvent(utf8_decode(username), cookie, ip, Integer.parseInt(ccdtid), utf8_decode(comments));
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
    public String get_person_type_from_user(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetPersonTypeFromUser(utf8_decode(username), cookie, ip);
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
    public String [] show_program_course_report(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.ShowProgramCourseReport(utf8_decode(username), cookie, ip);
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
    public String create_datetimes_list(String username, String cookie, String ip, String schid, String dtbegin, String dtend, String tline) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateDatetimesList(utf8_decode(username), cookie, ip, Integer.parseInt(schid), dtbegin, dtend, tline);
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
    public String [] show_report_card_report(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.ShowReportCardReport(utf8_decode(username), cookie, ip);
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
    public String [] show_historic_school_report(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.ShowHistoricSchoolReport(utf8_decode(username), cookie, ip);
    }

    /** Gets the statistical presence report
     *
     * @param username. String username.
     * @param cookie. String cookie value.
     * @param ip. String ip.
     * @param coid. String course id.
     * @param clid. String class id.
     * @param schid. String schedule id.
     *
     * @return returns presence list in format:
     *          <total datetimes>:<student>,<presence %>
     *          <total datetimes 1>:<student 1>,<presence % 1>
     *          <total datetimes 2>:<student 2>,<presence % 2>
     *
     *          If an error occurs returns empty string
     *          If session expired returns session_expired
     */
    public String [] show_statistical_presence_report(String username, String cookie, String ip, String coid, String clid, String schid) {
        DatabaseOp db = new DatabaseOp();
        return db.ShowStatisticalPresenceReport(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid), Integer.parseInt(schid));
    }

    /** Gets a list of schools for candidate
     * @return returns a list of schools in format:
     *          <school name>:<school id>
     *          <school name 1>:<school id 1>
     *          <school name 2>:<school id 2>
     *          If not exists any school, return null.
     */
    public String [] get_school_list_for_candidate() {
        DatabaseOp db = new DatabaseOp();
        return db.GetSchoolListForCandidate();
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
    public String create_new_candidate(String nickname, String fullname, String email, String uid, String scid) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewCandidate(utf8_decode(nickname), utf8_decode(fullname), utf8_decode(email), uid, Integer.parseInt(scid));
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
    public String get_candidate_settings(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCandidateSettings(utf8_decode(username), cookie, ip);
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
    public String edit_candidate_settings(String username, String cookie, String ip, String fullname, String email, String telephone, String obs, String address, String district, String city, String state, String zip_code, String civil_status, String career, String identity, String issued_by, String cpf) {
        DatabaseOp db = new DatabaseOp();
        return db.EditCandidateSettings(utf8_decode(username), cookie, ip, utf8_decode(fullname), utf8_decode(email), telephone, utf8_decode(obs), utf8_decode(address), utf8_decode(district), utf8_decode(city), utf8_decode(state), zip_code, utf8_decode(civil_status), utf8_decode(career), identity, utf8_decode(issued_by), cpf);
    }

    /** Gets the candidate information
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @return Returns the candidate information in format:
     *      <address>:<district>:<city>:<state>:<zip_code>:<civil status>:<career>:<identity>:<issued by>:<cpf>:<date>
     *      If an error occurs returns false
     *      If session expired returns session_expired
     */
    public String get_candidate_information(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCandidateInformation(utf8_decode(username), cookie, ip);
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
    public String get_additional_information_from_candidate(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetAdditionalInformationFromCandidate(utf8_decode(username), cookie, ip, pid);
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
     * @param pid. String program id
     * @return Returns true if candidate was edited, else returns false.
     *          If Session expired returns session_expired
     */
    public String edit_interested_settings(String username, String cookie, String ip, String fullname, String email, String telephone, String obs, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.EditInterestedSettings(utf8_decode(username), cookie, ip, utf8_decode(fullname), utf8_decode(email), telephone, utf8_decode(obs), Integer.parseInt(pid));
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
    public String [] get_file_format_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetFileFormatList(utf8_decode(username), cookie, ip);
    }
    
    /** Creates the new file
     * 
     * @param username. Strings username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param description. String description
     * @param permission. String permission
     * @param id_formats. String format id
     * @return Returns format below if file was created, else returns false:0
     *          true:<file id>
     *          If session expired returns session_expired
     */
    public String create_new_file(String username, String cookie, String ip, String description, String permission, String id_formats) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewFile(utf8_decode(username), cookie, ip, utf8_decode(description), permission, Integer.parseInt(id_formats));
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
    public String get_photo_from_user(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetPhotoFromUser(utf8_decode(username), cookie, ip);
    }
    
    /** Uploads the file
     * 
     * @param fdata. String file data
     * @return Returns true if data was wrote, else returns false
     */
    public String upload_file(String filename, String fdata) {
        String outputPath  = uploadDir + filename;
	
            byte[] data = Base64.decode(fdata);

            //WriteFileOutput objWriteFileOutput=new WriteFileOutput();
            String strData = new String(data);
            //strData += "\r\n";

            //System.out.println(strData);

            try{
                FileOutputStream fos = new FileOutputStream(outputPath,true);

                /*
                * To write byte array to a file, use
                * void write(byte[] bArray) method of Java FileOutputStream class.
                *
                * This method writes given byte array to a file.
                */

                fos.write(data);

                /*
                * Close FileOutputStream using,
                * void close() method of Java FileOutputStream class.
                * 
                */ 

                fos.close();
                                
            } catch (IOException ex) {
                Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
            }

        return "true";
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
    public String download_file(String username, String cookie, String ip, String fid) {
        DatabaseOp db = new DatabaseOp();
        return db.DownloadFile(utf8_decode(username), cookie, ip, fid);
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
    public String download_file_from_user(String username, String cookie, String ip, String pid, String fid) {
        DatabaseOp db = new DatabaseOp();
        return db.DownloadFileFromUser(username, cookie, ip, pid, fid);
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
    public String [] get_documents_from_user(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetDocumentsFromUser(utf8_decode(username), cookie, ip);
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
    public String hide_file_from_candidate(String username, String cookie, String ip, int fid) {
        DatabaseOp db = new DatabaseOp();
        return db.HideFileFromCandidate(utf8_decode(username), cookie, ip, fid);
    }

    /** Register the new candidate web
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
    public String register_new_candidate_to_school_web(String fullname, String email, String telephone, String scname, String uid, String reg_type) {
        DatabaseOp db = new DatabaseOp();
        return db.RegisterNewCandidateToSchool(utf8_decode(fullname), utf8_decode(email), telephone, utf8_decode(scname), uid, reg_type);
    }
    
    /** Register the new candidate
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
     *              -) err_user_id_exists - user_id already exists
     *              -) false            - unknown error
     */
    public String register_new_candidate_to_school(String email, String telephone, String scname, String uid, String reg_type) {
        DatabaseOp db = new DatabaseOp();
        return db.RegisterNewCandidateToSchool("-", utf8_decode(email), telephone, utf8_decode(scname), uid, reg_type);
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
    public String generate_agreement(String username, String cookie, String ip, String agr_id) {
        DatabaseOp db = new DatabaseOp();
        return db.GenerateAgreement(utf8_decode(username), cookie, ip, agr_id);
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
    public String download_agreement(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.DownloadAgreement(utf8_decode(username), cookie, ip);
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
    public String admin_generate_agreement(String username, String cookie, String ip, String agr_id, String nickname) {
        DatabaseOp db = new DatabaseOp();
        return db.AdminGenerateAgreement(utf8_decode(username), cookie, ip, agr_id, utf8_decode(nickname));
    }
    
    /** Download the agreement
     * 
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param nickname. String nickname
     * @return Returns the file data encoded in Base64.
     *          If an error occurs returns false
     *          If session expired returns session_expired
     */
    public String admin_download_agreement(String username, String cookie, String ip, String nickname) {
        DatabaseOp db = new DatabaseOp();
        return db.AdminDownloadAgreement(utf8_decode(username), cookie, ip, utf8_decode(nickname));
    }

    /** Gets the documents from candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id from candidate
     * @return Returns the file properties in format below, if an error occurs returns false
     *          <permission>:<file id>:<extension>:<description>
     *          If session expired returns session_expired
     */
    public String [] get_documents_from_candidate(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetDocumentsFromCandidate(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Gets the photo from candidate
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id from candidate
     * @return Returns the file properties in format below, if an error occurs returns false
     *          <permission>:<file id>:<extension>:<description>
     *          If session expired returns session_expired
     */
    public String get_photo_from_candidate(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetPhotoFromCandidate(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
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
    public String complete_registration(String username, String cookie, String ip, String nickname) {
        DatabaseOp db = new DatabaseOp();
        return db.CompleteRegistration(utf8_decode(username), cookie, ip, utf8_decode(nickname));

    }

    /** Gets the informations from candidate
    *
    * @param username. String username.
    * @param cookie. String cookie value.
    * @param ip. String ip.
    * @param pid. String person id from candidate.
    * @return returns a list with the user settings in format:
    * <nickname>:<full name>:<email>:<telephone>:<school name>:<school id>:<person type name>:<person type id>:<obs>
    *          If session expired returns session_expired
    *          If an error occurs returns false
    */
    public String get_information_from_candidate(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetInformationFromCandidate(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
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
    public String delete_file_from_candidate(String username, String cookie, String ip, String fid, String ext) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteFileFromCandidate(username, cookie, ip, Integer.parseInt(fid), ext);
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
    public String [] get_agreement_template_list(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetAgreementTemplateList(utf8_decode(username), cookie, ip);
    }

    /** Uploads the agreement template image
     *
     * @param fdata. String file data
     * @param filename . String file name of image
     * @return Returns true if data was wrote, returns false if an unknowm error occurs
     */
    public String upload_agreement_template_image(String filename, String fdata) {
        String ret = "false";
        String outputPath  = agreementTemplateImgDir + filename;

        byte[] data = Base64.decode(fdata);

        //WriteFileOutput objWriteFileOutput=new WriteFileOutput();
        String strData = new String(data);
        //strData += "\r\n";

        //System.out.println(strData);

        try{
            FileOutputStream fos = new FileOutputStream(outputPath,true);

            /*
            * To write byte array to a file, use
            * void write(byte[] bArray) method of Java FileOutputStream class.
            *
            * This method writes given byte array to a file.
            */

            fos.write(data);

            /*
            * Close FileOutputStream using,
            * void close() method of Java FileOutputStream class.
            *
            */

            fos.close();

            ret = "true";
        } catch (IOException ex) {
            Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
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
    public String check_agreement_template_image_existence(String username, String cookie, String ip, String filename) {
        DatabaseOp db = new DatabaseOp();
        return db.CheckAgreementTemplateImageExistence(utf8_decode(username), cookie, ip, filename);
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
    public String create_new_agreement_template(String username, String cookie, String ip, String name) {
        DatabaseOp db = new DatabaseOp();
        return db.CreateNewAgreementTemplate(utf8_decode(username), cookie, ip, utf8_decode(name));
    }

    /** Uploads the agreement template
     *
     * @param  filename. String filename
     * @param fdata. String file data
     * @return Returns true if data was wrote, else returns false
     */
    public String upload_agreement_template(String filename, String fdata) {
        String outputPath  = agreementTemplateDir + filename;

            byte[] data = Base64.decode(fdata);

            //WriteFileOutput objWriteFileOutput=new WriteFileOutput();
            String strData = new String(data);
            //strData += "\r\n";

            //System.out.println(strData);

            try{
                FileOutputStream fos = new FileOutputStream(outputPath,true);

                /*
                * To write byte array to a file, use
                * void write(byte[] bArray) method of Java FileOutputStream class.
                *
                * This method writes given byte array to a file.
                */

                fos.write(data);

                /*
                * Close FileOutputStream using,
                * void close() method of Java FileOutputStream class.
                *
                */

                fos.close();

            } catch (IOException ex) {
                Logger.getLogger(ws_ems.class.getName()).log(Level.SEVERE, null, ex);
            }

        return "true";
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
    public String download_agreement_template(String username, String cookie, String ip, String tid) {
        DatabaseOp db = new DatabaseOp();
        return db.DownloadAgreementTemplate(utf8_decode(username), cookie, ip, tid);
    }

     /** Deletes agreement template
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param tid. String agreement template id
     * @return Returns true if file was deleted, else returns false
     *          If session expired returns session_expired
     */
    public String delete_agreement_template(String username, String cookie, String ip, String tid) {
        DatabaseOp db = new DatabaseOp();
        return db.DeleteAgreementTemplate(utf8_decode(username), cookie, ip, Integer.parseInt(tid));
    }

    /** Gets the ambiance and date list
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param coid. Integer course id
     * @param clid. Integer class id
     * @return return the Ambiance and date list in format:
     *          <ambiance id 1>:<datetime id 1>
     *          <ambiance id 2>:<datetime id 2>
     *          Else returns false
     *          If session expired returns session_expired
     */
    public String  [] get_ambiance_and_date_list(String username, String cookie, String ip, String coid, String clid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetAmbianceAndDateList(utf8_decode(username), cookie, ip, Integer.parseInt(coid), Integer.parseInt(clid));
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
    public String get_candidate_program(String username, String cookie, String ip) {
        DatabaseOp db = new DatabaseOp();
        return db.GetCandidateProgram(utf8_decode(username), cookie, ip);
    }
    
    /** Gets the person from person
    *
    * @param username. String username.
    * @param cookie. String cookie value.
    * @param ip. String ip.
    * @param pid. String person id.
    * @return returns a list with the user settings in format:
    * <program name>:<program id>
    *          If session expired returns session_expired
    *          If an error occurs returns false
    */
    public String get_program_from_person(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetProgramFromPerson(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
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
    public String change_to_candidate(String username, String cookie, String ip, String nickname) {
        DatabaseOp db = new DatabaseOp();
        return db.ChangeToCandidate(utf8_decode(username), cookie, ip, utf8_decode(nickname));
    }
    
    public String test_send_email(String email) {
        return String.valueOf(AuxiliaryMethods.TestSendEmailGmail(email));
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
    public String get_user_payment_settings(String username, String cookie, String ip, String pid) {
        DatabaseOp db = new DatabaseOp();
        return db.GetUserPaymentSettings(utf8_decode(username), cookie, ip, Integer.parseInt(pid));
    }

    /** Sets the user's payment.
     *
     * @param username. String username
     * @param cookie. String cookie value
     * @param ip. String ip
     * @param pid. String person id
     * @param desc_value. String desc_value
     * @param desc_quota. String desc_quota
     * @return Returns true if payment was set, else returns false
     *          If session expired returns session_expired
     */
    public String set_user_payment(String username, String cookie, String ip, String pid, String desc_value, String desc_quota) {
        DatabaseOp db = new DatabaseOp();
        return db.SetUserPayment(utf8_decode(username), cookie, ip, Integer.parseInt(pid), utf8_decode(desc_value), utf8_decode(desc_quota));
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
    public String resize_image(String username, String cookie, String ip, String filename) {
        DatabaseOp db = new DatabaseOp();
        return db.ResizeImage(utf8_decode(username), cookie, ip, filename);
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
    public String download_thumbnail_from_user(String username, String cookie, String ip, String pid, String fid) {
        DatabaseOp db = new DatabaseOp();
        return db.DownloadThumbnailFromUser(utf8_decode(username), cookie, ip, pid, fid);
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
    public String download_thumbnail(String username, String cookie, String ip, String fid) {
        DatabaseOp db = new DatabaseOp();
        return db.DownloadThumbnail(utf8_decode(username), cookie, ip, fid);
    }
    
    
}
