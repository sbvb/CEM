<?php //constants.php

//index.php
//$ems_title = _("CEM - Cloud Education Management");
$ems_title = "CEM - Cloud Education Management";
$titlePrefixMsg = _("CEM - ");
$loginMsg = _("Login:");
$pwdMsg = _("Password:");
$loginBtnMsg = _("login");
$helloMsg = _("Hello,");
$helpMsg = _("help");
$newCandidateMsg = _("new candidate");

//doua_menu.php
$logoutMsg = _("Logout");
$actorIsMsg = _("is");

$menuTitle[] = _("manage");
$menuTitle[] = _("report");
$menuTitle[] = _("payment");
$menuTitle[] = _("presence");
$menuTitle[] = _("grade");
$menuTitle[] = _("services");
$menuTitle[] = _("logout");

$menuMsg[] = _("see login report");
$menuMsg[] = _("manage person");
$menuMsg[] = _("manage ambiance");
$menuMsg[] = _("manage course");
$menuMsg[] = _("manage schedule");
$menuMsg[] = _("manage class");
$menuMsg[] = _("presence call");
$menuMsg[] = _("assign student to class");
$menuMsg[] = _("manage school");
$menuMsg[] = _("manage program");
$menuMsg[] = _("manage datetime");
$menuMsg[] = _("associate course to program");
$menuMsg[] = _("manage class_course");
$menuMsg[] = _("manage enrollment");
$menuMsg[] = _("associate ambiance to event");
$menuMsg[] = _("assign grade");
$menuMsg[] = _("see grade report");
$menuMsg[] = _("see presence report");
$menuMsg[] = _("perform payment");
$menuMsg[] = _("unlock class course");
$menuMsg[] = _("program_course report");
$menuMsg[] = _("report card report");
$menuMsg[] = _("historic school report");
$menuMsg[] = _("statistical presence report");
$menuMsg[] = _("manage candidate");
$menuMsg[] = _("manage documents");
$menuMsg[] = _("manage candidates");
$menuMsg[] = _("manage agreement template");
$menuMsg[] = _("manage interested");
$menuMsg[] = _("manage interesteds");
$menuMsg[] = _("presence map report");

//doua_logout.php
$logoutInfMsg = _("is logging off");

//login_report.php
$loginRepMsg = _("Login Report");
$dateRepMsg = _("Date");
$usernameRepMsg = _("User name");
$eventRepMsg[] = _("Event");
$eventRep[] = _("logout (clicked)");
$eventRep[] = _("logout (timeout)");

//man_person.php
$manPersonTitle = _("Manage person");
$crtNewPersonMsg = _("create new person");
$actorMsg = _("Actor:");
$lstUserGrpMsg = _("select...");
$usrGrpMsg = _("User group");
$chgPwdMsg = _("change password");
$editMsg = _("edit");
$delMsg = _("delete");

//crt_person_act.php
$crtPersonActMsg = _("Create person account");
$edtPersonActMsg = _("Edit person account");
$fullnameMsg = _("Full name:");
$telMsg = _("telephone:");
$pwdMsg = _("password:");
$pwdConfMsg = _("password confirmation:");
$schoolMsg = _("school:");
$obsMsg = _("Obs (optional):");
$backBtnMsg = _("back");
$clearBtnMsg = _("clear");
$backBtnMsg = _("back");
$createBtnMsg = _("create");
$saveBtnMsg = _("save");
$curUserMsg = _("Current user:");

//reset_person_pass.php
$resetPersonPassTitle = _("Reset Password");

//man_ambience.php
$manAmbienceTitle = _("Manage ambiance");
$crtAmbMsg = _("create new ambiance");
$ambCap[] = _("ambiance name");
$ambCap[] = _("max students");

//crt_ambience_act.php
$crtAmbActMsg = _("Create ambiance");
$edtAmbActMsg = _("Edit ambiance");
$curAmbMsg = _("Current ambiance:");

//man_course.php
$manCourseTitle = _("Manage course");
$crtCourseMsg = _("create new course");
$courseCap[] = _("course name");
$courseCap[] = _("code");

//crt_course_act.php
$crtCourseActMsg = _("Create course");
$edtCourseActMsg = _("Edit course");
$curCourseMsg = _("Current course:");

//man_schedule.php
$manScheduleTitle = _("Manage schedule");
$crtScheduleMsg = _("create new schedule");
$scheduleCap = _("schedule name");

//crt_schedule_act.php
$crtScheduleActMsg = _("Create schedule");
$edtScheduleActMsg = _("Edit schedule");
$curScheduleMsg = _("Current schedule");

//man_class.php
$manClassTitle = _("Manage class");
$crtClassMsg = _("create new class");
$classCap = _("class name");

//crt_class_act.php
$crtClassActMsg = _("Create class");

//man_program.php
$manProgramTitle = _("Manage program");
$crtProgramMsg = _("create new program");
$programCap[] = _("program name");
$programCap[] = _("value");
$programCap[] = _("description");

//crt_program_act.php
$edtProgramActMsg = _("Edit program");
$crtProgramActMsg = _("Create program");
$currProgramMsg = _("Current program:");

//man_datetime.php
$manDatetimeTitle = _("Manage datetime");
$crtDatetimeMsg = _("create new datetime");
$viewMsg = _("view");
$datetimeCap[] = _("date");
$datetimeCap[] = _("time begin");
$datetimeCap[] = _("time end");
$datetimeCap[] = _("schedule");

//crt_datetime_act.php
$crtDatetimeActMsg = _("Create datetime");

//show_datetime.php
$shDatetimeMsg = _("Show datetime");

//man_prog_course.php
$manProgCourseTitle = _("Manage program_course");

//associate_prog_course.php
$assocProgCourseTitle[] = _("Associate \"");
$assocProgCourseTitle[] = _("\" to course");
$isAssociatedMsg = _("is associated");
$isNotAssociatedMsg = _("is not associated");
$associateBtnMsg = _("associate");

//man_class_course.php
$manClassCourseTitle = _("Select class_course");
$classMsg = _("class");

//associate_class_course.php
$assocClassCourseTitle = _("Class_courses of");
$assocClassCourseCap[] = _("course");
$assocClassCourseCap[] = _("manage Ok");
$assocClassCourseCap[] = _("class");

//fill_class_course.php
$fillClCoTitle = _("Manage class_course");
$tutorCap = _("tutor");

//show_schedule.php
$timeCap = _("time");

//man_enrollment.php
$manEnrollmentTitle = _("Manage enrollment");
$selProgramMsg = _("select program");
$selClassMsg = _("select class");

//associate_student_to_enrollment.php
$assocStudEnrTitle = _("Associate student to enrollment");

//man_event.php
$manEventTitle = _("Associate ambiance to event");
$selCourseMsg = _("select course");
$selScheduleMsg = _("select schedule");

//associate_event.php
$assocEventTitle = _("Associate event of");
$setAllAmbMsg = _("Set All ambiances");
$ambMsg = _("ambiance");

//fill_event.php
$fillEventTitle = _("Fill event");

//perform_payment.php
$performPaymentTitle = _("Perform payment");
$studentCap = _("student");
$selStudentMsg = _("select student");

//unlock_class_course.php
$unlockClassCourseTitle = _("Unlock class_course");

//doua_error.php
$douaErrorMsg = _("You do not have permission to access this page.");

//answer_presence.php
$answerPresenceTitle = _("Presence call");

//choose_ccdt_presence.php
$addCommentsMsg = _("add comments");

//fill_person_present.php
$presenceCap = _("presence");

//comment_event.php
$commentEventTitle = _("Event comments");
$commentsMsg = _("comments");

//assign_grade.php
$manGradeTitle = _("Assign grade");
$gradeCap = _("grade");
$gradeFinishedMsg = _("Grade already assigned and class was finished");

//grade_report.php
$gradeReportTitle = _("Grade Report");

//show_grade_report.php
$situationCap = _("situation");
$approvedMsg = _("approved");
$failedMsg = _("failed");

//presence_report.php
$presenceReportTitle = _("Presence report");

//man_school.php
$manSchoolTitle = _("Manage school");
$crtSchoolMsg = _("create new school");
$schoolCap = _("school name");

//crt_school.php
$crtSchoolTitle = _("Create school");
$edtSchoolTitle = _("Edit school");
$currSchoolMsg = _("current school");

//show_program_course_report.php
$programCourseReportTitle = _("Program_course report");

//crt_datetime_act_v2.php
$crtDatetimesActMsg = _("Create datetimes");
$frmDatetimeCap[] = _("day");
$frmDatetimeCap[] = _("sunday");
$frmDatetimeCap[] = _("monday");
$frmDatetimeCap[] = _("tuesday");
$frmDatetimeCap[] = _("wednesday");
$frmDatetimeCap[] = _("thursday");
$frmDatetimeCap[] = _("friday");
$frmDatetimeCap[] = _("saturday");
$frmDatetimeCap[] = _("date begin");
$frmDatetimeCap[] = _("date end");

//show_report_card_report.php
$reportCardReportTitle = _("Report card report");

//show_historic_school_report.php
$historicSchoolReportTitle = _("Historic school report");

//stat_presence_report.php
$statPresenceReportTitle = _("Statistical presence report");
$statPresenceCap = _("frequency");
$totalClassesMsg = _("total classes");

//new_candidate.php
$newCandidateTitle = _("New candidate");

//man_candidate.php
$manCandidateTitle = _("Manage candidate");
$addPhotoMsg = _("add photo");
$downloadAgreementMsg = _("download agreement");

//edit_candidate.php
$editCandidateTitle = _("Edit candidate account");
$addressMsg = _("address");
$districtMsg = _("district");
$cityMsg = _("city");
$stateMsg = _("state");
$zipCodeMsg = _("zip code");
$civilStatusMsg = _("civil status");
$careerMsg = _("career");
$identityMsg = _("identity");
$issuedByMsg = _("issued by");

//reset_candidate_pass.php
$actualPwdMsg = _("current password");
$newPwdMsg = _("new password");
$newPwdConfMsg = _("new password confirmation");

//add_candidate_photo.php
$addCandidatePhotoTitle = _("Add Photo");

//upload_photo.php
$uploadPhotoTitle = _("Upload photo");
$uploadPhotoCap[] = _("description (optional)");
$uploadPhotoCap[] = _("image");

//view_candidate.php
$viewCandidateTitle = _("View candidate");

//man_documents.php
$manDocumentsTitle = _("Manage documents");
$uploadDocumentMsg = _("upload document");

//upload_codument.php
$uploadDocumentTitle = _("Upload document");

//admin_man_candidate.php
$fullnameCaption = _("fullname");
$documentsMsg = _("documents");
$completeRegMsg = _("complete registration");
$defPayMsg = _("set payment");
$sendNewPassMsg = _("send new password");

//man_agreement_template.php
$manAgreementTemplateTitle = _("Manage agreement template");
$uploadAgreementTemplateMsg = _("upload agreement template");

//upload_agreement_template.php
$uploadAgreementTemplateTitle = _("Upload agreement template");
$uploadAgreementTemplateCap = _("name");

//man_interested.php
$manInterestedTitle = _("Manage interested");

//edit_interested.php
$editInterestedTitle = _("Edit interested account");

//view_interested.php
$viewInterestedTitle = _("View interested");

//admin_man_interested.php
$chgCandidateMsg = _("change to candidate");
$admManInterestedCap[] = _("registry type");
$admManInterestedCap[] = _("telephone");

//set_payment.php
$setPaymentTitle = _("Set payment");
$descValueMsg = _("description of value");
$descQuotaMsg = _("description of quota");

//presence_map_report.php
$presMapRepTitle = _("Presence Map Report");

?>