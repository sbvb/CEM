function back_page(page) {
	window.location = page;
}

function create_candidate() {	
	var email = document.frmCreate.txtEmail.value;
	var tel	= document.frmCreate.txtTelephone.value;
	var scname = document.frmCreate.hdSchool.value;
	
	if (email == "") {
		document.getElementById('edt_err_msg').innerHTML = "email obrigat�rio";
		document.frmCreate.txtEmail.focus();
	} 
	else if (tel == "") {
		document.getElementById('edt_err_msg').innerHTML = "telefone obrigat�rio";
		document.frmCreate.txtTelephone.focus();
	} 
	else {
		var newEmail = email.replace(/^\s+|\s+$/g,"");
		var newSchool = scname.replace(/^\s+|\s+$/g,"");
		
		var ajax = new Ajax();
		ajax.set_receive_handler(create_candidate_receive);
		var url = "./register/create_candidate_handler.php?email=" + newEmail + "&tel=" + tel + "&scname=" + newSchool;
		//alert(url);
		//document.getElementById("edt_err_msg").innerHTML = url;
		ajax.send(url);
	}
}
function create_candidate_receive(responseText) {
	if (responseText == "true") {
		alert('candidato registrado com sucesso');
		back_page('index.php');
	}
	else if (responseText == "err_login_exists"){
		document.getElementById('edt_err_msg').innerHTML = "email indispon�vel, informar um email diferente";
	}
	else if (responseText == "err_user_id_exists"){
		document.getElementById('edt_err_msg').innerHTML = "user id indispon�vel";
	}
	else {
		document.getElementById('edt_err_msg').innerHTML = "candidato n�o registrado";
	}
	
	document.frmCreate.txtEmail.value = "";
	document.frmCreate.txtTelephone.value = "";
}