
function setHeader(name, content) {
	$.ajax({
		type : "POST",
		url : "../rest/user/setHeader",
		data : JSON.stringify({
			"value" : content,
			"header" : name
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
		}
	});
}

function setTopicHeader(content) {
	$.ajax({
		type : "POST",
		url : "../rest/user/setHeader",
		data : JSON.stringify({
			"value" : content,
			"header" : "topic"
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
			window.location = "topic.html";
		}
	});
}

function setSubforumHeader(URL, content, location) {
	$.ajax({
		type : "POST",
		url : URL,
		data : JSON.stringify({
			"value" : content,
			"header" : "subforum"
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
			window.location = location;
		}
	});
}

function invalidCharacters(g) {
	if (g.indexOf('/') !== -1)
		return true;
	if (g.indexOf('\\') !== -1)
		return true;
	if (g.indexOf(':') !== -1)
		return true;
	if (g.indexOf('?') !== -1)
		return true;
	if (g.indexOf('"') !== -1)
		return true;
	if (g.indexOf('<') !== -1)
		return true;
	if (g.indexOf('>') !== -1)
		return true;
	if (g.indexOf('|') !== -1)
		return true;

	return false;
}

var user;

function getCurrentUser(URL){
	$.get(URL, function(data){
		user = data.value;
	});
}

function replaceWhiteSpace(string){
	return string.replace(/ /g,"/");
}

function getWhiteSpace(string){
	return string.replace(/\//g," ");
}

function setSubforumAndTopicHeader(e, location, URL){
	var subforum = e.id.split('?')[0];
	var topic = e.id.split('?')[1];
	$.ajax({
		type : "POST",
		url : URL,
		data : JSON.stringify({
			"value" : getWhiteSpace(subforum),
			"header" : "subforum"
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
			$.ajax({
				type : "POST",
				url : URL,
				data : JSON.stringify({
					"value" : getWhiteSpace(topic),
					"header" : "topic"
				}),
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					console.log(data);
					window.location = location;
				}
			});
		}
	});
}

function logOff(URL, location){
	$.ajax({
		type : "POST",
		url : URL,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
			window.location = location;
		}
	});
	return false;
}

function noUserIndexMenu(URL){
	$.get(URL, function(data){
		if(data == "" || data == null || data == undefined || data.value == ""){
			var span1 = $('<span/>', {
				html: "Регистрација"
			});
			var a1 = $('<a/>', {
				title: "Регистрација",
				href: "user/register.html",
				html: span1[0].outerHTML
			});
		    var li1 = $('<li/>', {
		    	html: a1[0].outerHTML
		    });
		    li1.attr("class", "menu-item");
		    
		    var span2 = $('<span/>', {
				html: "Пријава"
			});
			var a2 = $('<a/>', {
				title: "Пријава",
				href: "user/login.html",
				html: span2[0].outerHTML
			});
		    var li2 = $('<li/>', {
		    	html: a2[0].outerHTML
		    });
		    li2.attr("class", "menu-item");
		    
			$("#menu").append(li1);
			$("#menu").append(li2);
		}else if(data.value != ""){
			var span1 = $('<span/>', {
				html: "Одјави се"
			});
			var a1 = $('<a/>', {
				title: "Одјави се",
				href: "#",
				html: span1[0].outerHTML
			});
			a1.attr("onclick", "return logOff('rest/user/logoff', 'index.html');");
		    var li1 = $('<li/>', {
		    	html: a1[0].outerHTML
		    });
		    li1.attr("class", "menu-item");
		    
		    var span2 = $('<span/>', {
				html: "Корисничка страница"
			});
			var a2 = $('<a/>', {
				title: "Корисничка страница",
				href: "user/userpage.html",
				html: span2[0].outerHTML
			});
		    var li2 = $('<li/>', {
		    	html: a2[0].outerHTML
		    });
		    li2.attr("class", "menu-item");
		    
		    $("#menu").append(li1);
		    $("#menu").append(li2);
		}
	});
}

//noUserMenu('../rest/user/getHeader/user')
function noUserMenu(URL){
	$.get(URL, function(data){
		if(data == "" || data == null || data == undefined || data.value == ""){
			var span1 = $('<span/>', {
				html: "Регистрација"
			});
			var a1 = $('<a/>', {
				title: "Регистрација",
				href: "../user/register.html",
				html: span1[0].outerHTML
			});
		    var li1 = $('<li/>', {
		    	html: a1[0].outerHTML
		    });
		    li1.attr("class", "menu-item");
		    
		    var span2 = $('<span/>', {
				html: "Пријава"
			});
			var a2 = $('<a/>', {
				title: "Пријава",
				href: "../user/login.html",
				html: span2[0].outerHTML
			});
		    var li2 = $('<li/>', {
		    	html: a2[0].outerHTML
		    });
		    li2.attr("class", "menu-item");
		    
			$("#menu").append(li1);
			$("#menu").append(li2);
		}else if(data.value != ""){
			var span1 = $('<span/>', {
				html: "Одјави се"
			});
			var a1 = $('<a/>', {
				title: "Одјави се",
				href: "#",
				html: span1[0].outerHTML
			});
			a1.attr("onclick", "return logOff('../rest/user/logoff', '../index.html');");
		    var li1 = $('<li/>', {
		    	html: a1[0].outerHTML
		    });
		    li1.attr("class", "menu-item");
		    
		    var span2 = $('<span/>', {
				html: "Корисничка страница"
			});
			var a2 = $('<a/>', {
				title: "Корисничка страница",
				href: "../user/userpage.html",
				html: span2[0].outerHTML
			});
		    var li2 = $('<li/>', {
		    	html: a2[0].outerHTML
		    });
		    li2.attr("class", "menu-item");
		    
		    $("#menu").append(li1);
		    $("#menu").append(li2);
		}
	});
}

function giveUp(e){
	$(e).parent().remove();
	var t = $("<button>");
	if($("#new-comm").length == 0){
		t[0].innerHTML = "Коментариши";
		t.attr("onclick", "commBox(this);");
		t.attr("id", "new-comm");
		t.insertAfter("#topic-content");
	}
}