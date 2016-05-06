// JavaScript Document

var SESSION_EXPIRED_MSG = "session_expired";

function back_page(page) {
	window.location = page;
}

function refresh_page() {
	location.reload(true);
}

function check_login() {
	var usrName = document.loginFrm.txtLogin.value;
	var pass = document.loginFrm.txtPass.value;
	
	//alert("usrName: " + usrName + "\npass: " + pass);
	
	var ajax = new Ajax();
	ajax.set_receive_handler(login_receive);
	ajax.send("./doua/check_login_handler.php?user=" + usrName + "&pass=" + pass);
	
	document.loginFrm.txtLogin.value = "";
	document.loginFrm.txtPass.value = "";
}
function login_receive(responseText) {
	if (responseText != "false")
	{
		document.getElementById("loginErrMsg").innerHTML = "logado com sucesso";
		
		//creating the menu xml file
		var ajax = new Ajax();
		ajax.set_receive_handler(menu_generated_receive);
		ajax.send("./menu/menu_generator.php?user=" + responseText);
	}
	else
	{
		document.getElementById("loginErrMsg").innerHTML = "login/senha inválidos";
	}
	//alert(responseText);
}
function menu_generated_receive(responseText) {
	window.location = 'doua_menu.php?user=' + responseText;
}

function logout(username) {
	var ajax = new Ajax();
	ajax.set_receive_handler(logout_receive);
	ajax.send("./doua/logout_handler.php?user=" + username);
}
function logout_receive(responseText) {
	if (responseText = "true")
	{
		window.location = 'index.php';
		//window.location = 'index.php';
	}
	//alert(responseText);
}

function check_validity(username, signature) {
	var ajax = new Ajax();
	ajax.set_receive_handler(validity_receive);
	//alert(username+"_"+signature);
	ajax.send("./doua/check_validity_handler.php?user=" + username + "&link=" + signature);
}
function validity_receive(responseText) {
	if ((responseText == "session_expired") || (responseText == "false")) // session expired
	{
		// Goes to doua_error.php if session expired or user do not have the usecase
		window.location = 'doua_error.php';
		//alert("error");
	}	
	//alert("___"+responseText+"____");
}

function check_only_cookie(username) {
	var ajax = new Ajax();
	ajax.set_receive_handler(check_only_cookie_receive);
	//alert(username+"_"+signature);
	ajax.send("./doua/check_only_cookie_handler.php?user=" + username);
}
function check_only_cookie_receive(responseText) {
	if (responseText == "false")
	{
			window.location = 'doua_error.php';
			//alert("error");
	}	
}

function create_person_user() {
	var owner = document.frmCreate.hdOwner.value;
	var nickname = document.frmCreate.txtNick.value;
	var fullname = document.frmCreate.txtFullname.value;
	var email = document.frmCreate.txtEmail.value;
    var telephone = document.frmCreate.txtTelephone.value;
	var pass = document.frmCreate.txtPass.value;
	var passConf = document.frmCreate.txtPassConf.value;
	var scid = document.frmCreate.lstSchool.value;
	var ptid = document.frmCreate.lstPersonType.value;
	var obs = document.frmCreate.txtObs.value;
	var address = document.frmCreate.txtAddress.value;
	var district = document.frmCreate.txtDistrict.value;
	var city = document.frmCreate.txtCity.value;
	var state = document.frmCreate.txtState.value;
	var zip_code = document.frmCreate.txtZipCode.value;
	var civil_status = document.frmCreate.txtCivilStatus.value;
	var career = document.frmCreate.txtCareer.value;
	var identity = document.frmCreate.txtIdentity.value;
	var issued_by = document.frmCreate.txtIssuedBy.value;
	var cpf = document.frmCreate.txtCpf.value;
	
	// debug to see values
	//var msg = 'owner: ' + owner;	
	//msg += '\n';
	//msg += 'nickname: ' + nickname;
	//msg += '\n';
	//msg += 'fullname: ' + fullname;
	//msg += '\n';
	//msg += 'email: ' + email;
	//msg += '\n';
	//msg += 'telephone: ' + telephone;
	//msg += '\n';
	//msg += 'password: ' + pass;
	//msg += '\n';
	//msg += 'password confirmation: ' + passConf;
	//msg += '\n';
	//msg += 'scid: ' + scid;
	//msg += '\n';
	//msg += 'ptid: ' + ptid;
	//msg += '\n';
	//msg += 'obs: ' + obs;
	//alert(msg);
	//END debug to see values
		
	if (nickname == "") {
		document.getElementById("edt_err_msg").innerHTML = "login é obrigatório";	
		document.frmCreate.txtNick.focus();
	}
	else if (fullname == "") {
		document.getElementById("edt_err_msg").innerHTML = "nome completo é obrigatório";	
		document.frmCreate.txtFullname.focus();
	}
	else if (email == "") {
		document.getElementById("edt_err_msg").innerHTML = "email é obrigatório";
		document.frmCreate.txtEmail.focus();
	}
	else if (telephone == "") {
		document.getElementById("edt_err_msg").innerHTML = "telefone é obrigatório";
		document.frmCreate.txtTelephone.focus();
	}
	else if (pass == "") {
		document.getElementById("edt_err_msg").innerHTML = "senha é obrigatório";
		document.frmCreate.txtPass.focus();
	}
	else if (passConf == "") {
		document.getElementById("edt_err_msg").innerHTML = "confirmação de senha é obrigatório";
		document.frmCreate.txtPassConf.focus();
	}
	else if (address == "") {
		document.getElementById("edt_err_msg").innerHTML = "endereço é obrigatório";
		document.frmCreate.txtAddress.focus();
	}
	else if (district == "") {
		document.getElementById("edt_err_msg").innerHTML = "bairro é obrigatório";
		document.frmCreate.txtDistrict.focus();
	}
	else if (city == "") {
		document.getElementById("edt_err_msg").innerHTML = "cidade é obrigatório";
		document.frmCreate.txtCity.focus();
	}
	else if (state == "") {
		document.getElementById("edt_err_msg").innerHTML = "estado é obrigatório";
		document.frmCreate.txtState.focus();
	}
	else if (zip_code == "") {
		document.getElementById("edt_err_msg").innerHTML = "CEP é obrigatório";
		document.frmCreate.txtZipCode.focus();
	}
	else if (civil_status == "") {
		document.getElementById("edt_err_msg").innerHTML = "estado civil é obrigatório";
		document.frmCreate.txtCivilStatus.focus();
	}
	else if (career == "") {
		document.getElementById("edt_err_msg").innerHTML = "profissão é obrigatório";
		document.frmCreate.txtCareer.focus();
	}
	else if (identity == "") {
		document.getElementById("edt_err_msg").innerHTML = "identidade é obrigatório";
		document.frmCreate.txtIdentity.focus();
	}
	else if (issued_by == "") {
		document.getElementById("edt_err_msg").innerHTML = "órgão emissor é obrigatório";
		document.frmCreate.txtIssuedBy.focus();
	}
	else if (cpf == "") {
		document.getElementById("edt_err_msg").innerHTML = "cpf/cnpj é obrigatório";
		document.frmCreate.txtCpf.focus();
	}
	else { //all fields were filled
		if (pass != passConf) { //passwords must be the same
			document.getElementById("edt_err_msg").innerHTML = "senha e confirmação devem ser iguais";
			document.frmCreate.txtPass.value = "";
			document.frmCreate.txtPassConf.value = "";
			document.frmCreate.txtPass.focus();			
		}
		else { //passwords are equal			
			var ajax = new Ajax();
			ajax.set_receive_handler(create_person_user_receive);
			var newFullname = fullname.replace(/\s+/g, "%20");
			var newObs = obs.replace(/\s+/g, "%20");
			var newAddress = address.replace(/\s+/g, "%20");
			var newDistrict = district.replace(/\s+/g, "%20");
			var newCity = city.replace(/\s+/g, "%20");
			var newState = state.replace(/\s+/g, "%20");
			var newCareer = career.replace(/\s+/g, "%20");
			//alert(chg);
			var url = "./register/create_person_user_handler.php?user=" + owner + "&nickname=" + nickname + "&fullname=" + newFullname; 
			url += "&email=" + email + "&tel=" + telephone;
			url += "&pass=" + pass + "&ptid=" + ptid + "&scid=" + scid + "&obs=" + newObs;
			url += "&address=" + newAddress + "&district=" + newDistrict + "&city=" + newCity + "&state=" + newState + "&zip_code=" + zip_code;
			url += "&civil_status=" + civil_status + "&career=" + career + "&identity=" + identity + "&issued_by=" + issued_by;
			url += "&cpf=" + cpf;
			//alert(url);
			//document.getElementById("edt_err_msg").innerHTML = url;
			ajax.send(url);
		}
	}	
	
}
function create_person_user_receive(responseText) {
	//alert(responseText);
		
	if (responseText == "true") { //user created
		//document.getElementById("edt_err_msg").innerHTML = "User created"; 
		alert("pessoa criada");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "pessoa já existe no sistema";
	}
	
}

function delete_person_user(owner, nickname) {
	if(confirm('Você realmente deseja deletar essa pessoa?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_person_user_receive);
		var url = "./register/delete_person_user_handler.php?user=" + owner + "&nickname=" + nickname;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_person_user_receive(responseText) {
	//alert(responseText);
	
	if (responseText == "true") {
		alert("pessoa deletada");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	
}

function change_person_pass() {
	var pass =  document.frmPass.txtPass.value;
	var passConf =  document.frmPass.txtPassConf.value;
	var owner =  document.frmPass.hdOwner.value;
	var ed_user =  document.frmPass.hdEduser.value;
	
	//debug to see values
	//var msg = 'pass: ' + pass;
	//msg += '\n';
	//msg += 'passConf: ' + passConf;
	//msg += '\n';
	//msg += 'owner: ' + owner;
	//msg += '\n';
	//msg += 'ed_user: ' + ed_user;	
	//alert(msg);
	//END debug to see values
	
	if (pass == "") {
		document.getElementById("err_msg").innerHTML = "senha é obrigatório";
		document.frmPass.txtPass.focus();
	}
	else if (passConf == "") {
		document.getElementById("err_msg").innerHTML = "confirmação de senha é obrigatório";
		document.frmPass.txtPassConf.focus();
	}
	else {
		if (pass == passConf) {
			var ajax = new Ajax();
			ajax.set_receive_handler(change_person_pass_receive);
			var url = "./register/change_person_pass_handler.php?user=" + owner + "&eduser=" + ed_user + "&pass=" + pass;
			//alert(url);
			//document.getElementById("edt_err_msg").innerHTML = url;
			ajax.send(url);
		}
		else {
			document.getElementById("err_msg").innerHTML = "senha e confirmação devem ser iguais";
		}
	}
	
	document.frmPass.txtPass.value = "";
	document.frmPass.txtPassConf.value = "";
}
function change_person_pass_receive(responseText) {
	var line = responseText.split(":");
	
	if (line[0] == "true") {
		alert('senha alterada');
		var url = 'man_person.php?user=' + line[1];
		back_page(url);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("err_msg").innerHTML = "senha não pôde ser alterada";
	}
}

function edit_person_user() {
	var owner = document.frmEdit.hdOwner.value;
	var ed_user = document.frmEdit.hdEdtUsr.value;
	var fullname = document.frmEdit.txtFullname.value;
	var email = document.frmEdit.txtEmail.value;
	var telephone = document.frmEdit.txtTelephone.value;
	var ptid = document.frmEdit.lstPersonType.value;
	var scid = document.frmEdit.lstSchool.value;
	var obs = document.frmEdit.txtObs.value;
	var address = document.frmEdit.txtAddress.value;
	var district = document.frmEdit.txtDistrict.value;
	var city = document.frmEdit.txtCity.value;
	var state = document.frmEdit.txtState.value;
	var zip_code = document.frmEdit.txtZipCode.value;
	var civil_status = document.frmEdit.txtCivilStatus.value;
	var career = document.frmEdit.txtCareer.value;
	var identity = document.frmEdit.txtIdentity.value;
	var issued_by = document.frmEdit.txtIssuedBy.value;
	var cpf = document.frmEdit.txtCpf.value;
	
	//debug to see values
	//var msg = 'owner: ' + owner;
	//msg += '\n';
	//msg += 'ed_user: ' + ed_user;
	//msg += '\n';
	//msg += 'fullname: ' + fullname;
	//msg += '\n';
	//msg += 'email: ' + email;
	//msg += '\n';
	//msg += 'telephone: ' + telephone;
	//msg += '\n';
	//msg += 'ptid: ' + ptid;
	//msg += '\n';
	//msg += 'scid: ' + scid;
	//msg += '\n';
	//msg += 'obs: ' + obs;
	//alert(msg);
	//END debug to see values
	
	if (fullname == "") {
		document.getElementById("edt_err_msg").innerHTML = "nome completo é obrigatório";
		document.frmEdit.txtFullname.focus();
	}
	else if (email == "") {
		document.getElementById("edt_err_msg").innerHTML = "email é obrigatório";
		document.frmEdit.txtEmail.focus();
	}
	else if (telephone == "") {
		document.getElementById("edt_err_msg").innerHTML = "telefone é obrigatório";
		document.frmEdit.txtTelephone.focus();
	}	
	else if (address == "") {
		document.getElementById("edt_err_msg").innerHTML = "endereço é obrigatório";
		document.frmEdit.txtAddress.focus();
	}
	else if (district == "") {
		document.getElementById("edt_err_msg").innerHTML = "bairro é obrigatório";
		document.frmEdit.txtDistrict.focus();
	}
	else if (city == "") {
		document.getElementById("edt_err_msg").innerHTML = "cidade é obrigatório";
		document.frmEdit.txtCity.focus();
	}
	else if (state == "") {
		document.getElementById("edt_err_msg").innerHTML = "estado é obrigatório";
		document.frmEdit.txtState.focus();
	}
	else if (zip_code == "") {
		document.getElementById("edt_err_msg").innerHTML = "CEP é obrigatório";
		document.frmEdit.txtZipCode.focus();
	}
	else if (civil_status == "") {
		document.getElementById("edt_err_msg").innerHTML = "estado civil é obrigatório";
		document.frmEdit.txtCivilStatus.focus();
	}
	else if (career == "") {
		document.getElementById("edt_err_msg").innerHTML = "profissão é obrigatório";
		document.frmEdit.txtCareer.focus();
	}
	else if (identity == "") {
		document.getElementById("edt_err_msg").innerHTML = "identidade é obrigatório";
		document.frmEdit.txtIdentity.focus();
	}
	else if (issued_by == "") {
		document.getElementById("edt_err_msg").innerHTML = "órgão emissor é obrigatório";
		document.frmEdit.txtIssuedBy.focus();
	}
	else if (cpf == "") {
		document.getElementById("edt_err_msg").innerHTML = "cpf/cnpj é obrigatório";
		document.frmEdit.txtCpf.focus();
	}
	else { // all fields filled
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_person_user_receive);
		var newFullname = fullname.replace(/\s+/g, "%20");
		var newObs = obs.replace(/\s+/g, "%20");
		var newAddress = address.replace(/\s+/g, "%20");
		var newDistrict = district.replace(/\s+/g, "%20");
		var newCity = city.replace(/\s+/g, "%20");
		var newState = state.replace(/\s+/g, "%20");
		var newCareer = career.replace(/\s+/g, "%20");
		
		var url = "./register/edit_person_user_handler.php?user=" + owner + "&eduser=" + ed_user + "&fullname=" + newFullname + "&email=" + email + "&tel=" + telephone;
		url += "&ptid=" + ptid + "&scid=" + scid + "&obs=" + newObs;
		url += "&address=" + newAddress + "&district=" + newDistrict + "&city=" + newCity + "&state=" + newState + "&zip_code=" + zip_code;
		url += "&civil_status=" + civil_status + "&career=" + career + "&identity=" + identity + "&issued_by=" + issued_by;
		url += "&cpf=" + cpf;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function edit_person_user_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(line);
	
	if (line[0] == "true") {
		alert('pessoa editada');
		var url = 'man_person.php?user=' + line[1];
		back_page(url);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "pessoa não pôde ser editada";
	}
}

function create_ambience() {
	var owner = document.frmCreate.hdOwner.value; 
	var name = document.frmCreate.txtAmbience.value;
	var max_students = document.frmCreate.txtMaxStudents.value;	
	
	//debug to see values
	//var msg = "name: " + name;
	//msg += "\n";
	//msg += "owner: " + owner;
	//msg += "\n";
	//msg += "max_students: " + max_students;	
	//alert(msg);
	//END debug to see values
	
	if (name == "") {
		document.getElementById("edt_err_msg").innerHTML = "ambiance name is required";
		document.frmCreate.txtAmbience.focus();
	} 
	else if (max_students == "") {
		document.getElementById("edt_err_msg").innerHTML = "max students is required";
		document.frmCreate.txtMaxStudents.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(create_ambience_receive);
		var newName = name.replace(/\s+/g, "%20");
		var url = "./register/create_ambience_handler.php?name=" + owner + "&ambiance=" + newName + "&max_students=" + max_students;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function create_ambience_receive(responseText) {
	if (responseText == "true") {
		alert("ambience created");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Ambiance already exists";
	}	
}

function edit_ambience() {
	var max_students = document.frmEdit.txtMaxStudents.value;	
	var ambid = document.frmEdit.hdEdtUsr.value;	
	var owner = document.frmEdit.hdOwner.value;
	
	//debug to see values
	//var msg = "max_students: " + max_students;
	//msg += "\n";
	//msg += "ambid: " + ambid;
	//msg += "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	//END debug to see values
	
	if (max_students == "") {
		document.getElementById("edt_err_msg").innerHTML = "max students is required";
		document.frmEdit.txtMaxStudents.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_ambience_receive);		
		var url = "./register/edit_ambience_handler.php?name=" + owner + "&max_students=" + max_students + "&ambid=" + ambid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function edit_ambience_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert('ambiance edited');
		var url = 'man_ambience.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "ambiance couldn't be edited";
	}
}

function delete_ambience(owner,ambid) {
	if(confirm('Do you really want delete this ambiance?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_ambience_receive);
		var url = "./register/delete_ambience_handler.php?owner=" + owner + "&ambid=" + ambid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_ambience_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("Ambiance deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
}

function create_course() {
	var owner = document.frmCreate.hdOwner.value; 
	var name = document.frmCreate.txtCourse.value;
	var code = document.frmCreate.txtCode.value;	
			
	//debug to see values
	//var msg = "owner: " + owner + "\n";
	//msg += "course: " + name + "\n";
	//msg += "code: " + code + "\n";
	//alert(msg);
	//END debug to see values
	
	if (name == "") {
		document.getElementById("edt_err_msg").innerHTML = "name is required";
		document.frmCreate.txtCourse.focus();
	}
	else if (code == "") {
		document.getElementById("edt_err_msg").innerHTML = "code is required";
		document.frmCreate.txtCode.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(create_course_receive);
		var newName = name.replace(/\s+/g, "%20");
		var url = "./register/create_course_handler.php?user=" + owner + "&cname=" + newName + "&code=" + code;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function create_course_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("course created");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Course already exists";
	}	
}

function edit_course() {
	var code = document.frmEdit.txtCode.value;	
	var cid = document.frmEdit.hdEdtUsr.value;
	var owner = document.frmEdit.hdOwner.value;
	
	//debug to see values
	//var msg = "code: " + code + "\n";
	//msg += "pid: " + pid + "\n";
	//msg += "cid: " + cid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	//END debug to see values
	
	if (code == "") {
		document.getElementById("edt_err_msg").innerHTML = "code is required";
		document.frmEdit.txtCode.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_course_receive);
		var url = "./register/edit_course_handler.php?user=" + owner + "&code=" + code + "&cid=" + cid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function edit_course_receive(responseText) {
	var line = responseText.split(":");
	
	if (line[0] == "true") {
		alert('course edited');
		var url = 'man_course.php?user=' + line[1];
		back_page(url);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "course couldn't be edited";
	}
}

function delete_course(owner,cid) {
	if(confirm('Do you really want delete this course?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_course_receive);		
		var url = "./register/delete_course_handler.php?user=" + owner + "&cid=" + cid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_course_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("Course deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
}

function create_schedule() {
	var owner = document.frmCreate.hdOwner.value; 
	var name = document.frmCreate.txtSchedule.value;
	
	if (name == "") {
		document.getElementById("edt_err_msg").innerHTML = "schedule name is required";
		document.frmCreate.txtSchedule.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(create_schedule_receive);
		var newName = name.replace(/\s+/g, "%20");
		var url = "./register/create_schedule_handler.php?name=" + owner + "&schname=" + newName;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function create_schedule_receive(responseText) {
	//document.getElementById("edt_err_msg").innerHTML = responseText;
	var line = responseText.split(':');
	if (line[0] == "true") {
		alert("schedule created");
		back_page('crt_datetime_act_v2.php?user=' + line[1]);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "schedule already exists";
	}
	
}

function delete_schedule(owner,schid) {
	if(confirm('Do you really want delete this schedule?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_schedule_receive);
		var url = "./register/delete_schedule_handler.php?name=" + owner + "&schid=" + schid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_schedule_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("schedule deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
}

function edit_schedule() {
	var owner = document.frmEdit.hdOwner.value; 
	var name = document.frmEdit.txtSchedule.value;
	var schid = document.frmEdit.hdEdtUsr.value;
	
	if (name == "") {
		document.getElementById("edt_err_msg").innerHTML = "schedule is required";
		document.frmCreate.txtSchedule.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_schedule_receive);
		var newName = name.replace(/\s+/g, "%20");
		var url = "./register/edit_schedule_handler.php?user=" + owner + "&schname=" + newName + "&schid=" + schid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function edit_schedule_receive(responseText) {
	var line = responseText.split(":");
	
	if (line[0] == "true") {
		alert('schedule edited');
		var url = 'man_schedule.php?user=' + line[1];
		back_page(url);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "schedule couldn't be edited";
	}
}

function create_datetime() {
	var owner = document.frmCreate.hdOwner.value;	
	var date = document.frmCreate.txtDate.value;
	var time_begin = document.frmCreate.txtTimeBegin.value;
	var time_end = document.frmCreate.txtTimeEnd.value;
	var schid = document.frmCreate.lstSchedule.value;	
		
	var today = new Date();
	var d_date = new Date(date);
	var strDate = d_date.getFullYear() + "-" + d_date.getUTCMonth() + "-" + d_date.getUTCDay() + "T";
	var d_tbegin = new Date(strDate + time_begin + ":00");
	var d_tend = new Date(strDate + time_end + ":00");
	
	time_begin += ":00";
	time_end += ":00";
			
	//DEBUG 1
	//var msg = "name: " + name;
	//msg += "\ndate: " + date;
	//msg += "\ntime_begin: " + time_begin;
	//msg += "\ntime_end: " + time_end;
	//alert(msg);
	//END DEBUG 1
	
	//DEBUG 2
	//if (d_tend <= d_tbegin) { //Checks if time end is after time begin
	//	alert("time end must be after time begin");
	//}
	//else {
	//	alert("time OK");
	//}	
	
	//if (d_date <= today) { //checks if date is in future
	//	alert("date minor than today");
	//}
	//else
	//{
	//	alert("date OK");
	//}
	//END DEBUG 2
			
	if (d_date <= today) { //checks if date is in future
		document.getElementById("edt_err_msg").innerHTML = "date must be in future";
		document.frmCreate.txtDate.focus();
	}
	else if (d_tend <= d_tbegin) { //Checks if time end is after time begin
		document.getElementById("edt_err_msg").innerHTML = "time end must be after time begin";
		document.frmCreate.txtTimeEnd.focus();
	}	
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(create_datetime_receive);
		var url = "./register/create_datetime_handler.php?user=" + owner + "&schid=" + schid + "&date=" + date + "&time_begin=" + time_begin + "&time_end=" + time_end;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);		
	}	
}
function create_datetime_receive(responseText) {
	if (responseText == "true") {
		alert("datetime created");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else if (responseText == "excDateConflict"){
		document.getElementById("edt_err_msg").innerHTML = "Overlapped datetime";
	}	
	else {
		document.getElementById("edt_err_msg").innerHTML = "Datetime already exists";
	}	
}

function delete_datetime(owner,dtid) {
	if(confirm('Do you really want delete this datetime?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_datetime_receive);
		var url = "./register/delete_datetime_handler.php?user=" + owner + "&dtid=" + dtid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_datetime_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("Datetime deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}

}

function create_class() {
	var name = document.frmCreate.txtClass.value;
	var pid = document.frmCreate.lstProgram.value;
	var owner = document.frmCreate.hdOwner.value;
	
	//debug to see values
	//var msg = "name: " + name;
	//msg += "\n";
	//msg += "pid: " + pid;
	//msg += "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	//END debug to see values
		
	if (name == "") {
		document.getElementById("edt_err_msg").innerHTML = "class name is required";
		document.frmCreate.txtClass.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(create_class_receive);
		var newName = name.replace(/\s+/g, "%20");
		var url = "./register/create_class_handler.php?name=" + owner + "&cname=" + newName + "&pid=" + pid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function create_class_receive(responseText) {
	if (responseText == "true") {
		alert("class created");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Class already exists";
	}	
}

function edit_class() {
	var course_id = document.frmEdit.lstCourse.value;
	var schedule_id = document.frmEdit.lstSchedule.value;
	var tutor_id = document.frmEdit.lstTutor.value;
	var hd_class = document.frmEdit.hdEdtUsr.value;
	var owner = document.frmEdit.hdOwner.value;
	
	//debug to see values
	//var msg = "value: " + value;
	//msg += "\n";
	//msg += "course: " + course;
	//msg += "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	//END debug to see values
	
	var ajax = new Ajax();
	ajax.set_receive_handler(edit_class_receive);
	var new_class = hd_class.replace(" ", "%20");
	var url = "./register/edit_class_handler.php?name=" + new_class + "&course_id=" + course_id + "&schedule_id=" + schedule_id + "&tutor_id=" + tutor_id + "&owner=" + owner;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);

}
function edit_class_receive(responseText) {
	var line = responseText.split(":");
	
	if (line[0] == "true") {
		alert('class edited');
		var url = 'man_class.php?user=' + line[1];
		back_page(url);
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "class couldn't be edited";
	}
}

function delete_class(owner, cid) {
	if(confirm('Do you really want delete this class?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_class_receive);		
		var url = "./register/delete_class_handler.php?name=" + owner + "&cid=" + cid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_class_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("Class deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
}

function assign_student_to_class() {
	var student_id = frmCreate.lstStudent.value;
	var class_id = frmCreate.lstClass.value;
	
	var ajax = new Ajax();
	ajax.set_receive_handler(assign_student_to_class_receive);
	var url = "./register/assign_student_to_class_handler.php?student_id=" + student_id + "&class_id=" + class_id;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function assign_student_to_class_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("Student assigned to class");
		location.reload(true);
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Student already assigned to class";
	}
}

function create_school() {
	var school_name = document.frmCreate.txtSchool.value;
	var username = document.frmCreate.hdUser.value;
	
	// debug to see values
	//var msg = 'school name: ' + school_name;
	//alert(msg);
	//END debug to see values
	
	if (school_name == "") {
		document.getElementById("edt_err_msg").innerHTML = "All fields must be filled";
	}
	else { //all fields were filled
		var ajax = new Ajax();
		ajax.set_receive_handler(create_school_receive);
		var new_name = school_name.replace(/\s+/g, "%20");		
		//alert(chg);
		var url = "./register/create_school_handler.php?user=" + username + "&school=" +  new_name;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
	
	document.frmCreate.txtSchool.value = "";
}
function create_school_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") { //school created
		//document.getElementById("edt_err_msg").innerHTML = "User created"; 
		alert("School created");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "School already exists";
	}
}

function delete_school(owner, sid) {
	if(confirm('If you delete this school, all data will be lost, continue?')) {		
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_school_receive);
		var url = "./register/delete_school_handler.php?user=" + owner + "&sid=" + sid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_school_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("School deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		alert("School can't be deleted, please erase all relationships from this school");
	}	
}

function edit_school() {
	var sname = document.frmEdit.txtSchool.value;	
	var scid = document.frmEdit.hdEdtUsr.value;	
	var owner = document.frmEdit.hdOwner.value;
	
	//debug to see values
	//var msg = "sname: " + sname;
	//msg += "\n";	
	//msg += "scid: " + scid;
	//msg += "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	//END debug to see values
	
	if (sname == "") {
		document.getElementById("edt_err_msg").innerHTML = "name is required";
		document.frmEdit.txtSchool.focus();
	}
	else {		
		var newName = sname.replace(/\s+/g, "%20");
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_school_receive);		
		var url = "./register/edit_school_handler.php?user=" + owner + "&sname=" + newName + "&scid=" + scid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);		
	}	
}
function edit_school_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert('school edited');
		var url = 'man_school.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "school couldn't be edited";
	}
}

function create_program() {
	var owner = document.frmCreate.hdOwner.value;
	var pname = document.frmCreate.txtProgram.value;
	var pvalue = document.frmCreate.txtValue.value;	
	var desc = document.frmCreate.txtDesc.value;
	
	//var msg = "owner: " + owner + "\n";
	//msg += "name: " + pname + "\n";
	//msg += "value: " + pvalue + "\n";
	//msg += "desc: " + desc;
	//alert(msg);
	
	if (pname == "") {
		document.getElementById("edt_err_msg").innerHTML = "program name is required";
		document.frmCreate.txtProgram.focus();
	}
	else if (pvalue == "") {
		document.getElementById("edt_err_msg").innerHTML = "value is required";
		document.frmCreate.txtValue.focus();
	}
	else if (desc == "") {
		document.getElementById("edt_err_msg").innerHTML = "description is required";
		document.frmCreate.txtDesc.focus();
	}
	else {
		var ajax = new Ajax();
		ajax.set_receive_handler(create_program_receive);
		var new_name = pname.replace(/\s+/g, "%20");
		var new_desc = desc.replace(/\s+/g, "%20");
		//alert(chg);
		var url = "./register/create_program_handler.php?user=" + owner + "&pname=" +  new_name + "&pvalue=" + pvalue + "&desc=" + new_desc;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function create_program_receive(responseText) {
	//alert(responseText);
	
	if (responseText == "true") {
		alert("Program created");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Program name already exists";
	}	
}

function delete_program(owner,pid) {
	if(confirm('Do you really want delete this program?')) {		
		var ajax = new Ajax();
		ajax.set_receive_handler(delete_program_receive);
		var url = "./register/delete_program_handler.php?user=" + owner + "&pid=" + pid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function delete_program_receive(responseText) {
	//alert(responseText);
	if (responseText == "true") {
		alert("Program deleted");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}	
}

function edit_program() {
	var pname = document.frmEdit.txtProgram.value;	
	var pvalue = document.frmEdit.txtValue.value;	
	var desc = document.frmEdit.txtDesc.value;
	var pid = document.frmEdit.hdEdtUsr.value;	
	var owner = document.frmEdit.hdOwner.value;
	
	//debug to see values
	//var msg = "value: " + pvalue;
	//msg += "\n";	
	//msg += "pname: " + pname;
	//msg += "\n";
	//msg += "desc: " + desc;
	//msg += "\n";
	//msg += "pid: " + pid;
	//msg += "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	//END debug to see values
	
	if (pvalue == "") {
		document.getElementById("edt_err_msg").innerHTML = "value is required";
		document.frmEdit.txtValue.focus();
	}
	else if (pname == "") {
		document.getElementById("edt_err_msg").innerHTML = "name is required";
		document.frmEdit.txtProgram.focus();
	}
	else if (desc == "") {
		document.getElementById("edt_err_msg").innerHTML = "description is required";
		document.frmEdit.txtDesc.focus();
	}
	else {
		var newDesc = desc.replace(/\s+/g, "%20");
		var newName = pname.replace(/\s+/g, "%20");
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_program_receive);		
		var url = "./register/edit_program_handler.php?user=" + owner + "&pname=" + newName + "&value=" + pvalue + "&desc=" + newDesc + "&pid=" + pid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function edit_program_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert('program edited');
		var url = 'man_program.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "program couldn't be edited";
	}
}

function associate_course_to_program() {
	var pid = document.frmAssociate.hdPid.value;	
	var owner = document.frmAssociate.hdOwner.value;
	var sel1 = document.getElementById('lstSel1');
	
	var line = "";
	for(var i=0;i<sel1.length;i++) {
		if (i == 0)
			line = sel1.options[i].value;
		else
			line += ":" + sel1.options[i].value;
	}	
		
	//var msg = "pid: " + pid + "\n";
	//msg += "line: " + line + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	if (line == "")
		line = 0;
	
	if(confirm('Confirm this association?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(associate_course_to_program_receive);		
		var url = "./register/associate_prog_course_handler.php?user=" + owner + "&pid=" + pid + "&cline=" + line;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function associate_course_to_program_receive(responseText) {
	//alert(responseText);
	
	if (responseText == "true") {
		alert("Course and program associated");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't associate program and course, maybe already associated";
	}
	
}

function associate_class_to_course() {	
	var coid = document.frmAssociate.hdCourseId.value;
	var clid = document.frmAssociate.hdClassId.value;
	var schid = document.frmAssociate.lstSchedule.value;
	var pid = document.frmAssociate.lstTutor.value;
	var owner = document.frmAssociate.hdOwner.value;
	
	//var msg = "coid: " + coid + "\n";
	//msg += "clid: " + clid + "\n";
	//msg += "schid: " + schid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	if(confirm('Confirm this association?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(associate_class_to_course_receive);		
		var url = "./register/associate_class_course_handler.php?user=" + owner + "&coid=" + coid + "&clid=" + clid + "&schid=" + schid + "&pid=" + pid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function associate_class_to_course_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Course and class associated");
		var url = 'man_class_course.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't associate class and course, maybe already associated";
	}	
}

function edit_class_to_course() {	
	var coid = document.frmAssociate.hdCourseId.value;
	var clid = document.frmAssociate.hdClassId.value;
	var schid = document.frmAssociate.lstSchedule.value;
	var pid = document.frmAssociate.lstTutor.value;
	var owner = document.frmAssociate.hdOwner.value;
	
	//var msg = "coid: " + coid + "\n";
	//msg += "clid: " + clid + "\n";
	//msg += "schid: " + schid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	if(confirm('Confirm this association?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_class_to_course_receive);		
		var url = "./register/edit_class_course_handler.php?user=" + owner + "&coid=" + coid + "&clid=" + clid + "&schid=" + schid + "&pid=" + pid;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function edit_class_to_course_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Course and class edited");
		var url = 'man_class_course.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't associate class and course, maybe already associated";
	}	
}

function view_schedule() {
	var schid = document.frmAssociate.lstSchedule.value;
	var owner = document.frmAssociate.hdOwner.value;
	window.open('show_schedule.php?user=' + owner + "&schid=" + schid);
}

function view_tutor() {
	var pid = document.frmAssociate.lstTutor.value;
	var owner = document.frmAssociate.hdOwner.value;
	window.open('show_tutor.php?user=' + owner + "&pid=" + pid);
}

function associate_student_to_grade() {
	var coid = document.frmAssociate.hdCoid.value;
	var clid = document.frmAssociate.hdClid.value;
	var owner = document.frmAssociate.hdOwner.value;
	var sel1 = document.getElementById('lstSel1');
	
	var line = "";
	for(var i=0;i<sel1.length;i++) {
		if (i == 0)
			line = sel1.options[i].value;
		else
			line += ":" + sel1.options[i].value;
	}	
		
	//var msg = "coid: " + coid + "\n";
	//msg += "clid: " + clid + "\n";
	//msg += "line: " + line + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	if (line == "")
		line = 0;
	
	if(confirm('Confirm this association?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(associate_student_to_grade_receive);		
		var url = "./register/associate_student_grade_handler.php?user=" + owner + "&coid=" + coid + "&clid=" + clid + "&stline=" + line;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
	
}
function associate_student_to_grade_receive(responseText) {
	//alert(responseText);
	
	if (responseText == "true") {
		alert("Students and grade associated");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't associate students and grade, maybe already associated";
	}	
}

function associate_student_to_enrollment() {
	var clid = document.frmAssociate.hdClid.value;
	var owner = document.frmAssociate.hdOwner.value;
	var sel1 = document.getElementById('lstSel1');
	
	var line = "";
	for(var i=0;i<sel1.length;i++) {
		if (i == 0)
			line = sel1.options[i].value;
		else
			line += ":" + sel1.options[i].value;
	}	
		
	//var msg = "clid: " + clid + "\n";
	//msg += "line: " + line + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	if (line == "")
		line = 0;
	
	if(confirm('Confirm this association?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(associate_student_to_enrollment_receive);		
		var url = "./register/associate_student_enrollment_handler.php?user=" + owner + "&clid=" + clid + "&stline=" + line;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
	
}
function associate_student_to_enrollment_receive(responseText) {
	//alert(responseText);
	
	if (responseText == "true") {
		alert("Students and enrollment associated");
		location.reload(true);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't associate students and enrollment, maybe already associated";
	}	
}

function associate_event() {
	var clid = document.frmAssociate.hdClassId.value;
	var coid = document.frmAssociate.hdCourseId.value;
	var dtid = document.frmAssociate.hdDateId.value;
	var ambid = document.frmAssociate.lstAmbiance.value;
	var owner = document.frmAssociate.hdOwner.value;		
		
	//var msg = "clid: " + clid + "\n";
	//msg += "coid: " + coid + "\n";
	//msg += "dtid: " + dtid + "\n";
	//msg += "ambid: " + ambid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
			
	var ajax = new Ajax();
	ajax.set_receive_handler(associate_event_receive);		
	var url = "./register/associate_event_handler.php?user=" + owner + "&clid=" + clid + "&coid=" + coid + "&dtid=" + dtid + "&ambid=" + ambid;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
	
}
function associate_event_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Event created");
		var url = 'man_event.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't create this event";
	}
}

function associate_ambiance_ccdt() {	
	var ccdtid = document.frmAssociate.hdCcdtId.value;
	var ambid = document.frmAssociate.lstAmbiance.value;
	var owner = document.frmAssociate.hdOwner.value;		
		
	//var msg = "ccdtid: " + ccdtid + "\n";
	//msg += "ambid: " + ambid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	var ajax = new Ajax();
	ajax.set_receive_handler(associate_ambiance_ccdt_receive);		
	var url = "./register/associate_ambiance_ccdt_handler.php?user=" + owner + "&ccdtid=" + ccdtid + "&ambid=" + ambid;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
	
}
function associate_ambiance_ccdt_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Ambiance associated to datetime successful");
		var url = 'associate_ambiance_ccdt.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't associate ambiance and datetime";
	}
}

function answer_presence() {	
	var ccdtid = document.frmAssociate.hdCcdtId.value;
	var owner = document.frmAssociate.hdOwner.value;
	var line = "";	
	
	var count = document.frmAssociate.chkPresence.length;
	
	if (count == undefined) {
		if(document.frmAssociate.chkPresence.checked) {
			line = document.frmAssociate.chkPresence.value;			
		}
		
		var ajax = new Ajax();
		ajax.set_receive_handler(answer_presence_receive);		
		var url = "./register/answer_presence_handler.php?user=" + owner + "&ccdtid=" + ccdtid + "&stline=" + line;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
	else {
		for(var i=0; i < count; i++){
			if(document.frmAssociate.chkPresence[i].checked) {
				if (line == "")
					line += document.frmAssociate.chkPresence[i].value;
				else
					line += ":" + document.frmAssociate.chkPresence[i].value;
			}
		}
			
		//var msg = "ccdtid: " + ccdtid + "\n";
		//msg += "line: " + line + "\n";
		//msg += "owner: " + owner;
		//alert(msg);
		
		var ajax = new Ajax();
		ajax.set_receive_handler(answer_presence_receive);		
		var url = "./register/answer_presence_handler.php?user=" + owner + "&ccdtid=" + ccdtid + "&stline=" + line;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}// end of else undefined
	
}
function answer_presence_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Presence filled successful");
		var url = 'answer_presence.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't fill presence call";
	}
}

function assign_grade() {
	var coid = document.frmAssociate.hdCourseId.value;	
	var clid = document.frmAssociate.hdClassId.value;
	var owner = document.frmAssociate.hdOwner.value;
	var line = "";			
	
	var count = document.frmAssociate.txtGrade.length;
	
	if (count == undefined) {
		if (document.frmAssociate.txtGrade.value == "") {
			document.frmAssociate.txtGrade.focus();
			document.getElementById("edt_err_msg").innerHTML = "All students must be your grade filled";
		}
		else {
			line = document.frmAssociate.hdStudentId.value + ":" + parseFloat(document.frmAssociate.txtGrade.value);
			
			var ajax = new Ajax();
			ajax.set_receive_handler(assign_grade_receive);		
			var url = "./register/assign_grade_handler.php?user=" + owner + "&coid=" + coid + "&clid=" + clid + "&gline=" + line;
			//alert(url);
			//document.getElementById("edt_err_msg").innerHTML = url;
			ajax.send(url);
		}
	} 
	else {
					
		// checks if all grades were filled
		var allGradesFill = true;
		for(var i=0; i < count; i++) {
			if (document.frmAssociate.txtGrade[i].value == "") {
				document.frmAssociate.txtGrade[i].focus();
				document.getElementById("edt_err_msg").innerHTML = "All students must be your grade filled";
				allGradesFill = false;
				break;
			}
		}
	
		if (allGradesFill == true) { // assign the grades
			for(var i=0; i < count; i++) {
				if (line == "")
					line += document.frmAssociate.hdStudentId[i].value + ":" + parseFloat(document.frmAssociate.txtGrade[i].value);
				else
					line += ";" + document.frmAssociate.hdStudentId[i].value + ":" + parseFloat(document.frmAssociate.txtGrade[i].value);
			}
					
			//var msg = "coid: " + coid + "\n";
			//msg += "clid: " + clid + "\n";
			//msg += "line: " + line + "\n";
			//msg += "owner: " + owner;
			//alert(msg);	
			
			//alert(count);
			
			var ajax = new Ajax();
			ajax.set_receive_handler(assign_grade_receive);		
			var url = "./register/assign_grade_handler.php?user=" + owner + "&coid=" + coid + "&clid=" + clid + "&gline=" + line;
			//alert(url);
			//document.getElementById("edt_err_msg").innerHTML = url;
			ajax.send(url);
		}
	} // end undefined
	
}
function assign_grade_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Grade assigned successful");
		var url = 'assign_grade.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't assign grade";
	}
}

function perform_payment() {
	var clline = document.frmCreate.lstClass.value;
	var sline = document.frmCreate.lstStudent.value;
	var owner = document.frmCreate.hdOwner.value;
	
	var clLst = clline.split(":");
	var sLst = sline.split(":");
	
	var clid = clLst[1];
	var sid = sLst[1];
	
	//var msg = "clid: " + clid + "\n";
	//msg += "sid: " + sid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
	
	var ajax = new Ajax();
	ajax.set_receive_handler(perform_payment_receive);		
	var url = "./register/perform_payment_handler.php?user=" + owner + "&clid=" + clid + "&sid=" + sid;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
	
}
function perform_payment_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Payment performed successful");
		var url = 'perform_payment.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't perform payment, check if student already pay this class";
	}
}

function unlock_class_course() {
	var clline = document.frmCreate.lstClass.value;
	var coline = document.frmCreate.lstCourse.value;
	var owner = document.frmCreate.hdOwner.value;
	
	var clLst = clline.split(":");
	var coLst = coline.split(":");
	
	var clid = clLst[1];
	var coid = coLst[1];
	
	//var msg = "clid: " + clid + "\n";
	//msg += "coid: " + coid + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
		
	var ajax = new Ajax();
	ajax.set_receive_handler(unlock_class_course_receive);		
	var url = "./register/unlock_class_course_handler.php?user=" + owner + "&clid=" + clid + "&coid=" + coid;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function unlock_class_course_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Class course unlocked successful");
		var url = 'unlock_class_course.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't unlock class course";
	}
}

function comment_event() {	
	var ccdtid = document.frmAssociate.hdCcdtId.value;
	var comments = document.frmAssociate.txtComments.value;
	var owner = document.frmAssociate.hdOwner.value;
		
	//var msg = "ccdtid: " + ccdtid + "\n";
	//msg += "comments: " + comments + "\n";
	//msg += "owner: " + owner;
	//alert(msg);
		
	var newComments = comments.replace(/\s+/g, "%20");
	
	var ajax = new Ajax();
	ajax.set_receive_handler(comment_event_receive);		
	var url = "./register/comment_event_handler.php?user=" + owner + "&ccdtid=" + ccdtid + "&comments=" + newComments;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function comment_event_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Event commented successful");
		var url = 'answer_presence.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't update event comments";
	}
}

function create_datetimes_list() {
	var owner = document.frmCreate.hdOwner.value;
	var schid = document.frmCreate.lstSchedule.value;
	var dtbegin = document.frmCreate.txtDtBegin.value;
	var dtend = document.frmCreate.txtDtEnd.value;
	var nElem = document.frmDatetime.chkBxDay.length;
	
	//debug
	//var msg = "owner: " + owner + "\n";
	//msg += "schid: " + schid + "\n";
	//msg += "dtbegin: " + dtbegin + "\n";
	//msg += "dtend: " + dtend + "\n";
	//msg += "nElem: " + nElem;
	//alert(msg);
	
	var today = new Date();
	var d_dtbegin = new Date(dtbegin);
	var strDtbegin = d_dtbegin.getFullYear() + "-" + d_dtbegin.getUTCMonth() + "-" + d_dtbegin.getUTCDay() + "T";
	var d_dtend = new Date(dtend);
	var strDtend = d_dtend.getFullYear() + "-" + d_dtend.getUTCMonth() + "-" + d_dtend.getUTCDay() + "T";
	//var d_tbegin = new Date(strDate + time_begin + ":00");
	//var d_tend = new Date(strDate + time_end + ":00");
	
	//time_begin += ":00";
	//time_end += ":00";			
		
				
	if (d_dtbegin < today) { //checks if dtbegin is in future
		document.getElementById("edt_err_msg").innerHTML = "date begin must be in future";
		document.frmCreate.txtDtBegin.focus();
	}
	else if (d_dtend <= d_dtbegin) { //Checks if dtend is after dtbegin
		document.getElementById("edt_err_msg").innerHTML = "date end must be after date begin";
		document.frmCreate.txtDtEnd.focus();
	}	
	else {
		var tline = "";
		var tlineIsOk = true;
		for(var i = 0; i < nElem; i++) {
			if (document.frmDatetime.chkBxDay[i].checked == true) {
				var time_begin = document.frmDatetime.txttbegin[i].value;
				var time_end = document.frmDatetime.txttend[i].value;
				var d_tbegin = new Date(strDtbegin + time_begin + ":00");
				var d_tend = new Date(strDtbegin + time_end + ":00");
				
				//time_begin += ":00";
				//time_end += ":00";
				if (d_tend <= d_tbegin) { //checks if d_tbegin is before d_tend										
					tlineIsOk = false;		
					document.getElementById("edt_err_msg").innerHTML = "time end must be after time begin";
					break;
				}
				
				if (tlineIsOk == true) {
					if (tline == "")
						tline = document.frmDatetime.chkBxDay[i].value + "," + document.frmDatetime.txttbegin[i].value + "," + document.frmDatetime.txttend[i].value;
					else
						tline += ";" + document.frmDatetime.chkBxDay[i].value + "," + document.frmDatetime.txttbegin[i].value + "," + document.frmDatetime.txttend[i].value;
				}
			}
		}
				
		if (tlineIsOk == true) {
			if (tline == "") {
				document.getElementById("edt_err_msg").innerHTML = "you must define some day";
				document.frmDatetime.chkBxDay[0].focus();
			}
			else {
				//alert(tline);
				var ajax = new Ajax();
				ajax.set_receive_handler(create_datetimes_list_receive);
				var url = "./register/create_datetimes_list_handler.php?user=" + owner + "&schid=" + schid + "&dtbegin=" + dtbegin + "&dtend=" + dtend + "&tline=" + tline;
				//alert(url);
				//document.getElementById("edt_err_msg").innerHTML = url;
				ajax.send(url);		
			}
		}					
	}	
	
}
function create_datetimes_list_receive(responseText) {
	//alert(responseText);
	var line = responseText.split(':');
	if (line[0] == "true") {
		alert("datetimes created");
		back_page('man_datetime.php?user=' + line[1]);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}		
	else {
		document.getElementById("edt_err_msg").innerHTML = "Datetimes already exists";
	}
}

function create_candidate() {	
	var fullname = document.frmCreate.txtFullname.value;
	var email = document.frmCreate.txtEmail.value;
	var tel	= document.frmCreate.txtTelephone.value;
	var promo_code	= document.frmCreate.txtPromoCode.value;
	var scname = document.frmCreate.hdSchool.value;
	
	if (fullname == "") {
		document.getElementById('edt_err_msg').innerHTML = "nome é obrigatório";
		document.frmCreate.txtFullname.focus();
	} 
	else if (email == "") {
		document.getElementById('edt_err_msg').innerHTML = "email é obrigatório";
		document.frmCreate.txtEmail.focus();
	} 
	else if (tel == "") {
		document.getElementById('edt_err_msg').innerHTML = "telefone é obrigatório";
		document.frmCreate.txtTelephone.focus();
	} 
	else {
		var newFullname = fullname.replace(/^\s+|\s+$/g,"%20");
		var newEmail = email.replace(/^\s+|\s+$/g,"");
		var newSchool = scname.replace(/^\s+|\s+$/g,"%20");
		
		if (promo_code == "")
			promo_code = "0";
		
		var ajax = new Ajax();
		ajax.set_receive_handler(create_candidate_receive);
		var url = "./register/create_candidate_handler.php?email=" + newEmail + "&tel=" + tel + "&scname=" + newSchool + "&fullname=" + newFullname + "&promo_code=" + promo_code;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function create_candidate_receive(responseText) {
	if (responseText == "true") {
		alert('Pré-inscrição efetuada com sucesso');
		back_page('index.php');
	}
	else if (responseText == "err_login_exists"){
		document.getElementById('edt_err_msg').innerHTML = "Pré-inscrição não efetuada, email já existe em nosso sistema";
	}
	else if (responseText == "err_user_id_exists"){
		document.getElementById('edt_err_msg').innerHTML = "Pré-inscrição não efetuada, user id já existe em nosso sistema";
	}
	else {
		document.getElementById('edt_err_msg').innerHTML = "Pré-inscrição não efetuada, tente novamente em alguns minutos";
	}
	
	document.frmCreate.txtEmail.value = "";
	document.frmCreate.txtPromoCode.value = "";
}

function change_candidate_pass() {
	var oldPass =  document.frmPass.txtOldPass.value;
	var pass =  document.frmPass.txtPass.value;
	var passConf =  document.frmPass.txtPassConf.value;
	var owner =  document.frmPass.hdOwner.value;
	
	//debug to see values
	//var msg = 'oldPass: ' + oldPass;
	//msg += '\n';
	//msg += 'pass: ' + pass;
	//msg += '\n';
	//msg += 'passConf: ' + passConf;
	//msg += '\n';
	//msg += 'owner: ' + owner;
	//alert(msg);
	//END debug to see values
	
	if (oldPass == "") {
		document.getElementById("err_msg").innerHTML = "current password is required";
		document.frmPass.txtOldPass.focus();
	}
	else if (pass == "") {
		document.getElementById("err_msg").innerHTML = "password is required";
		document.frmPass.txtPass.focus();
	}
	else if (passConf == "") {
		document.getElementById("err_msg").innerHTML = "password confirmation is required";
		document.frmPass.txtPassConf.focus();
	}
	else {
		if (pass == passConf) {
			var ajax = new Ajax();
			ajax.set_receive_handler(change_candidate_pass_receive);
			var url = "./register/change_candidate_pass_handler.php?user=" + owner + "&pass=" + pass + "&old_pass=" + oldPass;
			//alert(url);
			//document.getElementById("edt_err_msg").innerHTML = url;
			ajax.send(url);
		}
		else {
			document.getElementById("err_msg").innerHTML = "passwords don't match";
		}
	}		
}
function change_candidate_pass_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert('Password changed');
		var url = 'man_candidate.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == "old_pass_err"){
		document.getElementById("err_msg").innerHTML = "current password is wrong";
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("err_msg").innerHTML = "password couldn't be changed";
	}
}

function edit_candidate_act() {	
	var owner = document.frmEdit.hdOwner.value;	
	var fullname = document.frmEdit.txtFullname.value;
	var email = document.frmEdit.txtEmail.value;
	var telephone = document.frmEdit.txtTelephone.value;
	var obs = document.frmEdit.txtObs.value;
	var address = document.frmEdit.txtAddress.value;
	var district = document.frmEdit.txtDistrict.value;
	var city = document.frmEdit.txtCity.value;
	var state = document.frmEdit.txtState.value;
	var zip_code = document.frmEdit.txtZipCode.value;
	var civil_status = document.frmEdit.txtCivilStatus.value;
	var career = document.frmEdit.txtCareer.value;
	var identity = document.frmEdit.txtIdentity.value;
	var issued_by = document.frmEdit.txtIssuedBy.value;
	var cpf = document.frmEdit.txtCpf.value;
	
	//debug to see values
	//var msg = 'owner: ' + owner;
	//msg += '\n';
	//msg += 'fullname: ' + fullname;
	//msg += '\n';
	//msg += 'email: ' + email;
	//msg += '\n';
	//msg += 'telephone: ' + telephone;
	//msg += '\n';
	//msg += 'obs: ' + obs;
	//alert(msg);
	//END debug to see values
	
	if (fullname == "") {
		document.getElementById("edt_err_msg").innerHTML = "fullname is required";
		document.frmEdit.txtFullname.focus();
	}
	else if (email == "") {
		document.getElementById("edt_err_msg").innerHTML = "email is required";
		document.frmEdit.txtEmail.focus();
	}
	else if (telephone == "") {
		document.getElementById("edt_err_msg").innerHTML = "telephone is required";
		document.frmEdit.txtTelephone.focus();
	}
	else if (address == "") {
		document.getElementById("edt_err_msg").innerHTML = "address is required";
		document.frmEdit.txtAddress.focus();
	}
	else if (district == "") {
		document.getElementById("edt_err_msg").innerHTML = "district is required";
		document.frmEdit.txtDistrict.focus();
	}
	else if (city == "") {
		document.getElementById("edt_err_msg").innerHTML = "city is required";
		document.frmEdit.txtCity.focus();
	}
	else if (state == "") {
		document.getElementById("edt_err_msg").innerHTML = "state is required";
		document.frmEdit.txtState.focus();
	}
	else if (zip_code == "") {
		document.getElementById("edt_err_msg").innerHTML = "zip code is required";
		document.frmEdit.txtZipCode.focus();
	}	
	else if (civil_status == "") {
		document.getElementById("edt_err_msg").innerHTML = "civil status is required";
		document.frmEdit.txtCivilStatus.focus();
	}
	else if (career == "") {
		document.getElementById("edt_err_msg").innerHTML = "career is required";
		document.frmEdit.txtCareer.focus();
	}
	else if (identity == "") {
		document.getElementById("edt_err_msg").innerHTML = "identity is required";
		document.frmEdit.txtIdentity.focus();
	}
	else if (issued_by == "") {
		document.getElementById("edt_err_msg").innerHTML = "issued by is required";
		document.frmEdit.txtIssuedBy.focus();
	}
	else if (cpf == "") {
		document.getElementById("edt_err_msg").innerHTML = "cpf is required";
		document.frmEdit.txtCpf.focus();
	}	
	else { // all fields filled
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_candidate_act_receive);
		var newFullname = fullname.replace(/\s+/g, "%20");
		var newObs = obs.replace(/\s+/g, "%20");
		var newAddress = address.replace(/\s+/g, "%20");
		var newDistrict = district.replace(/\s+/g, "%20");
		var newCity = city.replace(/\s+/g, "%20");
		var newState = state.replace(/\s+/g, "%20");
		var newCareer = career.replace(/\s+/g, "%20");
		
		var url = "./register/edit_candidate_handler.php?user=" + owner + "&fullname=" + newFullname + "&email=" + email + "&tel=" + telephone;
		url += "&obs=" + newObs;
		url += "&address=" + newAddress + "&district=" + newDistrict + "&city=" + newCity + "&state=" + newState + "&zip_code=" + zip_code;
		url += "&civil_status=" + civil_status + "&career=" + newCareer + "&identity=" + identity + "&issued_by=" + issued_by + "&cpf=" + cpf;

		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function edit_candidate_act_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(line);
	
	if (line[0] == "true") {
		alert('Candidate edited');
		var url = 'man_candidate.php?user=' + line[1];
		back_page(url);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "candidate couldn't be edited";
	}
}

function delete_document(user,file_id) {
	var ajax = new Ajax();
	ajax.set_receive_handler(delete_document_receive);
	
	var url = "./register/delete_document_handler.php?user=" + user + "&fid=" + file_id;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function delete_document_receive(responseText) {
	if (responseText == "true") {
		alert("Document deleted");
		window.location.reload();
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		alert("Couldn't delete document");
	}
}

function complete_registration(user, nickname) {
	//var msg = "user: " + user + "\n";
	//msg += "pid: " + pid;
	//alert(msg);
	
	if(confirm('Deseja realmente finalizar a matrícula de ' + nickname + '?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(complete_registration_receive);
		
		var url = "./register/complete_registration_handler.php?user=" + user + "&nickname=" + nickname;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function complete_registration_receive(responseText) {
	if (responseText == "true") {
		alert("Registration completed. Now this candidate is a student");
		window.location.reload();
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		alert("Couldn't complete registration");
	}
}

function admin_delete_document(user,file_id,ext) {
	var ajax = new Ajax();
	ajax.set_receive_handler(admin_delete_document_receive);
	
	var url = "./register/admin_delete_document_handler.php?user=" + user + "&fid=" + file_id + "&ext=" + ext;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function admin_delete_document_receive(responseText) {
	if (responseText == "true") {
		alert("Document deleted");
		window.location.reload();
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		alert("Couldn't delete document");
	}
}

function delete_agreement_template(user, tid) {
	var ajax = new Ajax();
	ajax.set_receive_handler(delete_agreement_template_receive);
	
	var url = "./register/delete_agreement_template_handler.php?user=" + user + "&tid=" + tid;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function delete_agreement_template_receive(responseText) {
	if (responseText == "true") {
		alert("File deleted");
		window.location.reload();
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		alert("Couldn't delete file");
	}
}

function associate_events() {	
	var clid = document.frmDates.hdClassId.value;
	var coid = document.frmDates.hdCourseId.value;
	var dtLstCnt = document.frmDates.hdDateId.length;
	var ambLstCnt = document.frmDates.lstAmbiance.length;
	var owner = document.frmDates.hdOwner.value;
	var eventLine = "";

	if (dtLstCnt == undefined) {// only one element
		eventLine = document.frmDates.hdDateId.value + "," + document.frmDates.lstAmbiance.value;
	}
	else { // more than one elements
		for(var i = 0; i < dtLstCnt; i++) {
			if (eventLine == "")
				eventLine = document.frmDates.hdDateId[i].value + "," + document.frmDates.lstAmbiance[i].value;
			else
				eventLine += ":" + document.frmDates.hdDateId[i].value + "," + document.frmDates.lstAmbiance[i].value;
		}
	}
	
	var ajax = new Ajax();
	ajax.set_receive_handler(associate_events_receive);		
	var url = "./register/associate_events_handler.php?user=" + owner + "&clid=" + clid + "&coid=" + coid + "&evtline=" + eventLine;
	//alert(url);	
	ajax.send(url);	
}
function associate_events_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert("Events created");
		var url = 'man_event.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Can't create this event";
	}
}

function change_interested_pass() {
	var oldPass =  document.frmPass.txtOldPass.value;
	var pass =  document.frmPass.txtPass.value;
	var passConf =  document.frmPass.txtPassConf.value;
	var owner =  document.frmPass.hdOwner.value;
	
	//debug to see values
	//var msg = 'oldPass: ' + oldPass;
	//msg += '\n';
	//msg += 'pass: ' + pass;
	//msg += '\n';
	//msg += 'passConf: ' + passConf;
	//msg += '\n';
	//msg += 'owner: ' + owner;
	//alert(msg);
	//END debug to see values
	
	if (oldPass == "") {
		document.getElementById("err_msg").innerHTML = "current password is required";
		document.frmPass.txtOldPass.focus();
	}
	else if (pass == "") {
		document.getElementById("err_msg").innerHTML = "password is required";
		document.frmPass.txtPass.focus();
	}
	else if (passConf == "") {
		document.getElementById("err_msg").innerHTML = "password confirmation is required";
		document.frmPass.txtPassConf.focus();
	}
	else {
		if (pass == passConf) {
			var ajax = new Ajax();
			ajax.set_receive_handler(change_interested_pass_receive);
			var url = "./register/change_interested_pass_handler.php?user=" + owner + "&pass=" + pass + "&old_pass=" + oldPass;
			//alert(url);
			//document.getElementById("edt_err_msg").innerHTML = url;
			ajax.send(url);
		}
		else {
			document.getElementById("err_msg").innerHTML = "passwords don't match";
		}
	}		
}
function change_interested_pass_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(responseText);
	
	if (line[0] == "true") {
		alert('Password changed');
		var url = 'man_interested.php?user=' + line[1];
		back_page(url);
	}
	else if (line[0] == "old_pass_err"){
		document.getElementById("err_msg").innerHTML = "current password is wrong";
	}
	else if (line[0] == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("err_msg").innerHTML = "password couldn't be changed";
	}
}

function edit_interested_act() {
	var owner = document.frmEdit.hdOwner.value;	
	var fullname = document.frmEdit.txtFullname.value;
	var email = document.frmEdit.txtEmail.value;
	var telephone = document.frmEdit.txtTelephone.value;
	var obs = document.frmEdit.txtObs.value;
	var pid = document.frmEdit.lstProgram.value;
	
	//debug to see values
	//var msg = 'owner: ' + owner;
	//msg += '\n';
	//msg += 'fullname: ' + fullname;
	//msg += '\n';
	//msg += 'email: ' + email;
	//msg += '\n';
	//msg += 'telephone: ' + telephone;
	//msg += '\n';
	//msg += 'obs: ' + obs;
	//alert(msg);
	//END debug to see values
	
	if (fullname == "") {
		document.getElementById("edt_err_msg").innerHTML = "fullname is required";
		document.frmEdit.txtFullname.focus();
	}
	else if (email == "") {
		document.getElementById("edt_err_msg").innerHTML = "email is required";
		document.frmEdit.txtEmail.focus();
	}
	else if (telephone == "") {
		document.getElementById("edt_err_msg").innerHTML = "telephone is required";
		document.frmEdit.txtTelephone.focus();
	}	
	else { // all fields filled
		var ajax = new Ajax();
		ajax.set_receive_handler(edit_interested_act_receive);
		var newFullname = fullname.replace(/\s+/g, "%20");
		var newObs = obs.replace(/\s+/g, "%20");
		
		var url = "./register/edit_interested_handler.php?user=" + owner + "&fullname=" + newFullname + "&email=" + email + "&tel=" + telephone + "&obs=" + newObs + "&pid=" + pid;

		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function edit_interested_act_receive(responseText) {
	var line = responseText.split(":");
	
	//alert(line);
	
	if (line[0] == "true") {
		alert('Interested edited');
		var url = 'man_interested.php?user=' + line[1];
		back_page(url);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "interested couldn't be edited";
	}
}

function change_to_candidate(owner, interested) {
	if(confirm('Deseja realmente mudar ' + interested + ' para candidato?')) {
		var ajax = new Ajax();
		ajax.set_receive_handler(change_to_candidate_receive);
		var url = "./register/change_to_candidate_handler.php?user=" + owner + "&nickname=" + interested;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function change_to_candidate_receive(responseText) {
	if (responseText == "true") {
		alert("Changed. Now this interested is a candidate");
		window.location.reload();
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		alert("Couldn't change interested to candidate");
	}
}

function set_payment() {
	var owner = document.frmEdit.hdOwner.value;
	var pid = document.frmEdit.hdPid.value;
	var value = document.frmEdit.txtDescValue.value;
	var quota = document.frmEdit.txtDescQuota.value;
	
	if (value == "") { // value is required
		document.getElementById("edt_err_msg").innerHTML = "descrição do valor é obrigatório";
		document.frmEdit.txtDescValue.focus();
	} else if (quota == "") { // quota is required
		document.getElementById("edt_err_msg").innerHTML = "descrição de parcelas é obrigatório";
		document.frmEdit.txtDescQuota.focus();
	} else {
		var newValue = value.replace(/\s+/g, "%20");
		var newQuota = quota.replace(/\s+/g, "%20");
		
		var ajax = new Ajax();
		ajax.set_receive_handler(set_payment_receive);
		var url = "./register/set_payment_handler.php?user=" + owner + "&pid=" + pid + "&value=" + newValue + "&quota=" + newQuota;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}	
}
function set_payment_receive(responseText) {
	//alert(responseText);
	var line = responseText.split(":");
	
	if (line[0] == "true") {
		alert("Forma de pagamento definida com sucesso.");
		back_page("admin_man_candidate.php?user=" + line[1]);
	}
	else if (responseText == SESSION_EXPIRED_MSG){
		back_page('doua_error.php');
	}
	else {
		document.getElementById("edt_err_msg").innerHTML = "Erro na definição da forma de pagamento";
	}
}

function download_agreement() {
	var owner = document.frmAgreement.user.value;
	var agr_id = document.frmAgreement.lstAgreement.value;
	
	//var msg = "owner: " + owner + "\n";
	//msg += "agr_id: " + agr_id;
	//alert(msg);
	
	var ajax = new Ajax();
	ajax.set_receive_handler(download_agreement_receive);
	var url = "./download_agreement_handler.php?user=" + owner + "&agr_id=" + agr_id;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
	
}
function download_agreement_receive(responseText) {
	//alert(responseText);
	var line = responseText.split(":");
		
	if (line[0] == "true") {
		back_page("download_agreement.php?user=" + line[1]);
	}
	else if (line[0] == "has_blank_field"){
		back_page("download_agreement.php?user=" + line[1]);
		alert("contrato contem campos indefinidos");
	}
	else if (line[0] == "no_payment_defined"){
		back_page("download_agreement.php?user=" + line[1]);
		alert("contrato contem forma de pagamento indefinida");
	}
	else {
		alert("contrato não pode ser gerado");
	}
}

function admin_download_agreement(index) {	
	var owner = document.frmAgreement[index-1].hdOwner.value;
	var nick = document.frmAgreement[index-1].hdNick.value;
	var agr_id = document.frmAgreement[index-1].lstAgreement.value;
		
	//var msg = "owner: " + owner + "\n";
	//msg += "nick: " + nick + "\n";
	//msg += "agr_id: " + agr_id;
	//alert(msg);
		
	var ajax = new Ajax();
	ajax.set_receive_handler(admin_download_agreement_receive);
	var url = "./admin_download_agreement_handler.php?user=" + owner + "&agr_id=" + agr_id + "&nick=" + nick;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
	
}
function admin_download_agreement_receive(responseText) {
	//alert(responseText);
	var line = responseText.split(":");
		
	if (line[0] == "true") {
		back_page("admin_download_agreement.php?user=" + line[1] + "&nick=" + line[2]);
	}
	else if (line[0] == "has_blank_field"){
		back_page("admin_download_agreement.php?user=" + line[1] + "&nick=" + line[2]);
		alert("contrato contem campos indefinidos");
	}
	else if (line[0] == "no_payment_defined"){
		back_page("admin_download_agreement.php?user=" + line[1] + "&nick=" + line[2]);
		alert("contrato contem forma de pagamento indefinida");
	}
	else {
		alert("contrato não pode ser gerado");
	}
}

function send_new_pass(login, name, email) {
	//var msg = "login: " + login + "\n";
	//msg += "name: " + name + "\n";
	//msg += "email: " + email;
	//alert(msg);
	var newName = name.replace(/\s+/g, "%20");
	var ajax = new Ajax();
	ajax.set_receive_handler(send_new_pass_receive);
	var url = "register/send_new_pass_handler.php?login=" + login + "&name=" + newName + "&email=" + email;
	//alert(url);
	//document.getElementById("edt_err_msg").innerHTML = url;
	ajax.send(url);
}
function send_new_pass_receive(responseText) {
	if (responseText == "true") {
		alert("email com nova senha foi enviada");
	}
	else {
		alert("erro na definição de nova senha");
	}
}