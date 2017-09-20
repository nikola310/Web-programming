/**
 * Validacija prilikom registracije korisnika
 */


var flag = false;

function deleteElement(elem){
	if (document.getElementById(elem) != null) {
		var s = "#" + elem;
		var a = s.split("-");
		$(a[0]).css("border-color", "");
		$(s).remove();
	}
}

function isEmailUnique(){
	var email = $("#email").val();
	$.ajax({
		type : "POST",
		url : "../rest/user/checkemail",
		data : JSON.stringify({
			email
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json"
	}).fail(function(){
		console.log("FAIL: isEmailUnique();")
	}).done(function(data) {
			// console.log(data);
			if (data) {
				// console.log("OK!");
				if (document.getElementById('email-warning') != null) {
					$("#email-warning").remove();
					$("#email").css("border-color", ""); 
				}
				flag = true;
			} else {
					 // console.log("NIJE OK!");
					 var tlrt = '<strong style="color:red;" id="email-warning">&nbsp;&nbsp;Већ постоји корисник с тим <i>Email-om</i>.</strong>'; 
					 $(tlrt).insertAfter("#email");
					 $("#email").css("border-color", "red");
					 flag = false;
			}
			// flag = data;
		});
}

function checkPassword() {
	var tmp = $("#password2").val();
	var g = $("#password").val();
	if (g != tmp) {
		// var tlrt = '<strong style="color:red;"
		// id="err-warning">&nbsp;&nbsp;Шифре нису исте.</strong>';
		if (document.getElementById('err-warning2') == null) {
			var tlrt2 = '<strong style="color:red;" id="err-warning2">&nbsp;&nbsp;Шифре нису исте.</strong>';
			$(tlrt2).insertAfter("#password2");
			// $(tlrt).insertAfter("#password");
		}
		return false;
	} else {
		if (document.getElementById('err-warning2') != null) {
			// $("#err-warning").remove();
			$("#err-warning2").remove();
		}
		return true;
	}
}

function checkUserName(conf) {
	username = conf.value;
	$.ajax({
		type : "POST",
		url : "../rest/user/exists",
		data : JSON.stringify({
			username
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success: function(data) {
			console.log(data);
			if (data) {
				// console.log("NIJE OK!");
				if(document.getElementById('user-war') == null){
					var tlrt = '<strong style="color:red;" id="user-war">&nbsp;&nbsp;Већ постоји корисник с тим именом.</strong>';
					$(tlrt).insertAfter("#username");
					$("#username").css("border-color", "red");
				}
				flag = false;
				// return data;
			} else {
				// console.log("OK!");
				deleteElement("user-war");
				// return data;
				flag = true;
			}
			// flag = !data;
		}
	});
	}


function usernameEmpty(){
	var tmp = $("#username").val();
	if(tmp==""){
		if(document.getElementById('username-war') == null){
			var tlrt = '<strong style="color:red;" id="username-war">&nbsp;&nbsp;Морате унети корисничко име.</strong>';
			$(tlrt).insertAfter("#username");
			$("#username").css("border-color", "red");
		}
		return true;
	}else{
		deleteElement("username-war");
		return false;
	}
}

function passwordEmpty(){
	var tmp = $("#password").val();
	if(tmp==""){
		if(document.getElementById('password-war') == null){
			var tlrt = '<strong style="color:red;" id="password-war">&nbsp;&nbsp;Морате унети шифру.</strong>';
			$(tlrt).insertAfter("#password");
			$("#password").css("border-color", "red");
		}
		return true;
	}else{
		deleteElement("password-war");
		return false;
	}
}

function confirmPasswordEmpty(){
	var tmp = $("#password2").val();
	if(tmp==""){
		if(document.getElementById('password2-war') == null){
			var tlrt = '<strong style="color:red;" id="password2-war">&nbsp;&nbsp;Морате потврдити шифру.</strong>';
			$(tlrt).insertAfter("#password2");
			$("#password2").css("border-color", "red");
		}
		return true;
	}else{
		deleteElement("password2-war");
		return false;
	}
}

function nameEmpty(){
	var tmp = $("#name").val();
	if(tmp==""){
		if(document.getElementById('name-war') == null){
			var tlrt = '<strong style="color:red;" id="name-war">&nbsp;&nbsp;Морате унети име.</strong>';
			$(tlrt).insertAfter("#name");
			$("#name").css("border-color", "red");
		}
		return true;
	}else{
		deleteElement("name-war");
		return false;
	}
}

function surnameEmpty(){
	var tmp = $("#surname").val();
	if(tmp==""){
		if(document.getElementById('surname-war') == null){
			var tlrt = '<strong style="color:red;" id="surname-war">&nbsp;&nbsp;Морате унети преѕиме.</strong>';
			$(tlrt).insertAfter("#surname");
			$("#surname").css("border-color", "red");
		}
		return true;
	}else{
		deleteElement("surname-war");
		return false;
	}
}

function telephoneEmpty(){
	var tmp = $("#telephone").val();
	if(tmp==""){
		if(document.getElementById('telephone-war') == null){
			var tlrt = '<strong style="color:red;" id="telephone-war">&nbsp;&nbsp;Морате унети телефон.</strong>';
			$(tlrt).insertAfter("#telephone");
			$("#telephone").css("border-color", "red");
		}
		return true;
	}else{
		deleteElement("telephone-war");
		return false;
	}
}

function emailValidation(){
	var x=document.getElementById("email").value;
    var atpos=x.indexOf("@");
    var dotpos=x.lastIndexOf(".");
    if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length)
    {
    	if(document.getElementById('email-war') == null){
    		var tlrt = '<strong style="color:red;" id="email-war">&nbsp;&nbsp;Морате унети <i>Email</i>.</strong>';
			$(tlrt).insertAfter("#email");
			$("#email").css("border-color", "red");
    	}
    	return false;
    }else{
    	deleteElement("email-war");
    }
    return true;
}

function getToday(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; // January is 0!
	var yyyy = today.getFullYear();
	if(dd < 10) {
		dd = '0'+dd;
	}
	if(mm < 10) {
		mm = '0'+mm;
	} 
	today = mm + '/' + dd + '/' + yyyy;
	console.log(today);
	return today;
}

function register() {
	if(usernameEmpty())
		return false;
	var g = $("#username").val();
	if(invalidCharacters(g)){
		if(document.getElementById('username-war2') == null){
    		var tlrt = '<strong style="color:red;" id="username-war2">&nbsp;&nbsp;Корисничко име не сме садржавати: / \ ? " < > |.</strong>';
			$(tlrt).insertAfter("#username");
			$("#username").css("border-color", "red");
    	}
		return false;
	}else{
		deleteElement("username-war2");
	}
	if(passwordEmpty())
		return false;
	if(confirmPasswordEmpty())
		return false;
	if(!checkPassword())
		return false;
	if(!emailValidation())
		return false;
	if(nameEmpty())
		return false;	
	if(surnameEmpty())
		return false;
	if(telephoneEmpty())
		return false;
	
	regDate = getToday();
	
	$.ajax({
		type : "POST",
		url : "../rest/user/register",
		data : JSON.stringify({
			"username" : $("#username").val(),
			"password" : $("#password").val(),
			"name" : $("#name").val(),
			"surname" : $("#surname").val(),
			"tel" : $("#telephone").val(),
			"email" : $("#email").val(),
			"regDate" : regDate
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success: function(data) {
			console.log(data.failed + " " + data.errCode);
			
			if(data.failed){
				// zasto nije uspelo?
				if(data.errCode == "USER_EXISTS"){
					if(document.getElementById('username-war') == null){
						var tlrt = '<strong style="color:red;" id="username-war">&nbsp;&nbsp;Већ постоји корисник с тим именом.</strong>';
						$(tlrt).insertAfter("#username");
						$("#username").css("border-color", "red");
						deleteElement("email-war");
					}	
				}else if(data.errCode == "EMAIL_EXISTS"){
					if(document.getElementById('email-war') == null){
						var tlrt = '<strong style="color:red;" id="email-war">&nbsp;&nbsp;Већ постоји корисник с тим <i>Email-om</i>.</strong>'; 
						$(tlrt).insertAfter("#email");
						$("#email").css("border-color", "red");
						deleteElement("username-war");
					}	
				}else{
					alert("Server error");
				}
			}else{
				// uspesno registrovan korisnik, sada ide brisanje upozorenja +
				// preusmeravanje na login stranicu
				deleteElement("username-war");
				deleteElement("email-war");
				
				window.name = "RegisterSuccessful";
				window.location = "login.html";
				
				// alert("SVE OK.");
			}
		}
	});
	return false;
}

function loadRating(){
	$.get("../rest/user/getRating", function(data){
		if(data != null){
			var p1 = $('<p/>', {
				html: 'Број позитивних гласова: ' + data[0]
			});
			var p2 = $('<p/>', {
				html: 'Број негативних гласова: ' + data[1]
			});
			$("#rating").html(p1.html() + '<br/>' + p2.html())
		}
	});
}

function readUsersSf(){
	$.get("../rest/user/getSubforums", function(data){
		console.log(data)
		if(!jQuery.isEmptyObject(data)){
			var table = document.getElementById("subforums-table");
			
			var t = document.getElementById("subforums-table");
			
			var r2 = t.insertRow(1);
			var c21 = r2.insertCell(0);
			var c22 = r2.insertCell(1);
			c21.setAttribute("class", "subheader");
			c22.setAttribute("class", "subheader");
			c22.setAttribute("width", "200");
			c22.setAttribute("align", "center");
			c21.innerHTML = '<strong>Назив</strong>';
			c22.innerHTML = '<strong>Иконица</strong>';
			
			var h4 = $("<h4>");
			var l = $("<a>");
			var link = "/rest/subforum";
			var div = $("<div>");
			var desc = "desc-";
			var i = $("<img>");
			for (var m in data){
				l[0].innerHTML = m;
				l.attr("href", "#");
				l.attr("onclick", "return setSubforumHeader('../rest/user/setHeader', this.id, '../subforum/subforum.html');");
				l.attr("id", m);
			    h4.attr("class", "h4-podforum");
			    
			    div.attr("class", "small-text");
			    div.attr("id", desc+m);
			    div[0].innerHTML = data[m].description;
				
			    var row = table.insertRow(2);
			    row.setAttribute("style", "height: 69px;");
			    var cell1 = row.insertCell(0);
				var cell2 = row.insertCell(1);
				cell1.setAttribute("valign", "top");
				
				var string = "data:application/unknown;base64, " + data[m].icon;
				
				i.attr("src", string);
				h4[0].innerHTML = l[0].outerHTML;
				cell1.innerHTML = h4[0].outerHTML + div[0].outerHTML;
				cell2.innerHTML = i[0].outerHTML;
				cell2.setAttribute("style", "height: 69px;");
				
				l[0].innerHTML = "";
				div[0].innerHTML = "";
				i[0].innerHTML = "";
				h4[0].innerHTML = "";
			}
		}else{
			var table = document.getElementById("subforums-table");
			var row = table.insertRow(1);
			var cell1 = row.insertCell(0);
			cell1.setAttribute("colspan", "3");
			cell1.setAttribute("class", "no-msg");
			cell1.innerHTML = "Нема подфорума које пратите";
		}
	});
}

function loadSaved(){
	$.get("../rest/user/getSaved", function(data){
		console.log(data);
		if(!jQuery.isEmptyObject(data)){
			if(data["comments"].length > 0){
				for(i = 0; i < data["comments"].length; i++){
					loadSavedComments(data["comments"][i]);
				}
			}else{
				var table = document.getElementById("saved-comments");
				var row = table.insertRow(1);
				var cell1 = row.insertCell(0);
				cell1.setAttribute("colspan", "3");
				cell1.setAttribute("class", "no-msg");
				cell1.innerHTML = "Нема сачуваних коментара";
			}
			
			if(data["messages"].length > 0){
				for(i = 0; i < data["messages"].length; i++){
					loadSavedMessages(data["messages"][i]);
				}
			}else{
				var table = document.getElementById("saved-messages");
				var row = table.insertRow(1);
				var cell1 = row.insertCell(0);
				cell1.setAttribute("colspan", "3");
				cell1.setAttribute("class", "no-msg");
				cell1.innerHTML = "Нема сачуваних порука";
			}
			
			if(!jQuery.isEmptyObject(data["topics"])){
				for(var m in data["topics"]){
					loadSavedTopics(data["topics"][m], m);
				}
			}else{
				var table = document.getElementById("saved-topics");
				var row = table.insertRow(1);
				var cell1 = row.insertCell(0);
				cell1.setAttribute("class", "no-msg");
				cell1.innerHTML = "Нема сачуваних тема";
			}
		}
	});
}

function loadSavedComments(val){
	var table = document.getElementById("saved-comments");
	
	var likeSpan = $('<span/>', {
		id: "likes-" + val.id,
		html: val.poz
	});
	
	var dislikeSpan = $('<span/>', {
		id: "dislikes-" + val.id,
		html: val.neg
	});
	
	var ch = "";
	if(val.changed){
		ch = "(измењен)";
	}
	var span = $('<span/>', {
		html: "[+" + likeSpan[0].outerHTML + "]" + "[-" + dislikeSpan[0].outerHTML + "]" + "(" + val.date + ")" + ch
	});
	
	if(val.deleted){
		var p = $('<p/>', {
			id: val.author,
			html: span[0].outerHTML
		});
	}else{
		var p = $('<p/>', {
			id: val.author,
			html: val.author + span[0].outerHTML
		});
	}
	
	if(val.deleted){
		var p = $('<p/>', {
			id: val.author,
			html: span[0].outerHTML
		});
	}else{
		var p = $('<p/>', {
			id: val.author,
			html: val.author + span[0].outerHTML
		});
	}
	p.addClass('user-info');
	var reply = "";
	if((val.parentCom != undefined) && (val.parentCom != null)){
		reply = "<b>@" + val.parentCom.author + "</b> ";
	}
	if(val.deleted){
		var pBody = $('<p/>', {
			id: "deleted",
			html: "[Обрисан коментар]"
		});

	}else{
		var pBody = $('<p/>', {
			html: reply + val.content
		});			
	}
	pBody.addClass('comment-body');
	
	var commDiv = $('<div/>', {
		id: val.id,
		html: p[0].outerHTML + pBody[0].outerHTML
	});
	
	commDiv.addClass('comment');
	
	var row = table.insertRow(1);
	var cell2 = row.insertCell(0);
	
	cell2.append(commDiv[0]);
}

function saveMessage(e){
	console.log(e);
	var id = e.id.split(/-(.+)/)[1];
	$.ajax({
		type : "POST",
		url : "../rest/user/saveMessage",
		data : JSON.stringify({
			"value" : parseInt(id)
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success: function(data) {
			if(data){
				$("#saved-messages").find("tr:gt(0)").remove();
				$.get("../rest/user/getSaved", function(data){
					for(i = 0; i < data["messages"].length; i++){
						loadSavedMessages(data["messages"][i]);
					}
				});
			}
		}
	});
}

function loadSavedMessages(val){
	var table = document.getElementById("saved-messages");
	var l = $("<strong>");
	var k = $("<h4>");
	k.attr("class", "h4-podforum");
	var d = $("<div>");
	var row = table.insertRow(1);
	var cell2 = row.insertCell(0);

	l[0].innerHTML = val.sender;
	d[0].innerHTML = val.text;

	k[0].innerHTML = l[0].outerHTML;

	d.attr("id", i.toString());

	if (val.read == false) {
		d.attr("class", "unread");
	} else {
		d.attr("class", "msg-text");
	}
	cell2.innerHTML = k[0].outerHTML + d[0].outerHTML;
}

function loadSavedTopics(data){
	var table = document.getElementById("saved-topics");
	var l = $("<a>");
	l.attr("href", "#");
	var k = $("<h4>");
	k.attr("class", "h4-podforum");
	var d = $("<div>")

	var row = table.insertRow(1);
	var cell2 = row.insertCell(0);
	l[0].innerHTML = data.title;
	l.attr("id", replaceWhiteSpace(data.parent) + "?" + replaceWhiteSpace(data.title));
	d[0].innerHTML = data.author + "(" + data.datum + ")";
	l.attr("onclick", "return setSubforumAndTopicHeader(this, '../subforum/topic.html', '../rest/user/setHeader');");
	k.attr("id", data.title);
		
	k[0].innerHTML = l[0].outerHTML;

	cell2.innerHTML = k[0].outerHTML + d[0].outerHTML;
	l[0].innerHTML = "";
	d[0].innerHTML = "";
	k[0].innerHTML = "";
}

function appendMenu(URL, HREF){
	$.get(URL, function(data){
		if(data){
			var span = $('<span/>', {
				html: "Администрација"
			});
			var a = $('<a/>', {
				title: "Администрација",
				href: HREF,
				html: span[0].outerHTML
			});
		    var li = $('<li/>', {
		    	html: a[0].outerHTML
		    });
		    li.attr("class", "menu-item");
			$("#menu").append(li);
		}
	});
}