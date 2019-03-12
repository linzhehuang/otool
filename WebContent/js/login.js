function login() {
	var password = $("idPasswordInput").value;
	ajax({
		url: "./Login",
		data: {
			password: password
		},
		success: function(xhr) {
			var responseJSON = convertJSONString(xhr.responseText);
			if(responseJSON.success) {
				window.location.href = "./condition-query.html";	
			} else {
				$("idHint").style.display = "block";
				$("idPasswordInput").select();
				setTimeout("$('idHint').style.display='none';", 2000);
			}
		}
	});
}