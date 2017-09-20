/**
 * 
 */

function noTitle(){
	var tmp = $("#title").val();
	if (tmp == "") {
		if (document.getElementById('title-war2') == null) {
			var tlrt = '<strong style="color:red;" id="title-war2">&nbsp;&nbsp;Морате унети наслов.</strong>';
			$(tlrt).insertAfter("#title");
			$("#title").css("border-color", "red");
		}
		return true;
	} else {
		deleteElement("title-war2");
		return false;
	}
}

function createTopic(){
	if(noTitle()){
		return false;
	}
	var g = $("#title").val();
	if(invalidCharacters(g)){
		if(document.getElementById('title-war2') == null){
    		var tlrt = '<strong style="color:red;" id="title-war2">&nbsp;&nbsp;Наслов не сме садржавати: / \ ? " < > |.</strong>';
			$(tlrt).insertAfter("#title");
			$("#title").css("border-color", "red");
    	}
		return false;
	}else{
		deleteElement("title-war2");
	}
	var content;
	if($("input[name=type]:checked").val() == "TEXT"){
		content = $("#txt").val();
	}else if($("input[name=type]:checked").val() == "PICTURE"){
		if(imageSelected()){
			content = glob.split(/,(.+)/)[1];
		}else{
			return false;
		}
	}else if($("input[name=type]:checked").val() == "LINK"){
		content = $("#link").val();
	}
	
	$.ajax({
		type : "POST",
		url : "../rest/topic/new",
		data : JSON.stringify({
			"title" : $("#title").val(),
			"content" : content,
			"type" : $("input[name=type]:checked").val(),
			"datum" : getToday()
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
				if (data.failed) {
						if (data.errCode == "ALREADY_EXISTS_ERROR") {
							if(document.getElementById('title-war') == null){
								var tlrt = '<strong style="color:red;" id="title-war">&nbsp;&nbsp;Већ постоји тема с тим насловом.</strong>';
								$(tlrt).insertAfter("#title");
								$("#title").css("border-color", "red");
							}
						}
				}else{
					if(document.getElementById('title-war') != null){
						deleteElement("title-war");
					}
					window.name = $("#topic-title")[0].innerHTML;
					window.location = "subforum.html";
				}
			},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + " " + textStatus + " " + errorThrown);
		}
	});

	return false;
}

function loadTopic(){	
	$.get("../rest/topic/get/", function(data){
		if(data != null){
			console.log(data);
			var sLikes = $('<span/>', {
				id: 'topic-likes',
				html: data.poz
			});
			var sDislikes = $('<span/>', {
				id: 'topic-dislikes',
				html: data.neg
			});
			
			$("#topic-title")[0].innerHTML = data.title + " [+" + sLikes[0].outerHTML + "]" + "[-" + sDislikes[0].outerHTML + "]";
			$("#author")[0].innerHTML = data.author + " (" + data.datum + ")"; 			
			deleteTopicBtn();
			var ct = $("#topic-content");
			if(data.type == "TEXT"){
				ct[0].innerHTML = data.content;
			}else if(data.type == "LINK"){
				var a = $("<a>");
				a.attr("href", data.content);
				a.attr("target", "_blank");
				var u = $("<u>");
				u[0].innerHTML = data.title;
				a[0].innerHTML = u[0].outerHTML;
				ct[0].innerHTML = a[0].outerHTML;
			}else if(data.type == "PICTURE"){
				var i = $("<img>");
				var string = "data:application/unknown;base64, " + data.content;
				i.attr("src", string);
				ct[0].innerHTML = i[0].outerHTML;
			}
			if(data.comments){
				console.log("ima komentara");
				var p = $("<p>");
				p.innerHTML = "Коментари";
				p.insertAfter("#topic-content")
				$.get("../rest/comments/get", function (data){
					loadComments(data);
				});
			}else{
				$("#comments-div")[0].innerHTML = "Нема коментара";
			}
			
			usrResp = data.usrResp;
			$.get("../rest/user/getHeader/user", function(data){
				user = data.value;
				if((user != undefined) && (usrResp.indexOf(user) < 0)){
					var t = $("<button>");
					t[0].innerHTML = "Коментариши";
					t.attr("onclick", "commBox(this);");
					t.attr("id", "new-comm");
					t.insertAfter("#topic-content");
					var y = $("<button>");
					y[0].innerHTML = "Свиђа ми се";
					y.attr("onclick", "likeTopic(this);");
					y.attr("id", "likeTopic");
					y.insertAfter("#new-comm");
					var u = $("<button>");
					u[0].innerHTML = "Не свиђа ми се";
					u.attr("onclick", "dislikeTopic(this);");
					u.attr("id", "dislikeTopic");
					u.insertAfter("#likeTopic");
				}else if(usrResp.indexOf(user) > -1){
					var t = $("<button>");
					t[0].innerHTML = "Коментариши";
					t.attr("onclick", "commBox(this);");
					t.attr("id", "new-comm");
					t.insertAfter("#topic-content");
				}
			});
		}
	});
}

function loadComments(data){
	//ucitavanje komentara
	var y = $("#comments-div");
	jQuery.each(data, function(i, val) {
		console.log(val);
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
		p.addClass('user-info');
		var reply = "";
		if((val.parentCom != undefined) && (val.parentCom != null)){
			reply = "<b>@" + val.parentCom.author + "</b> ";
		}
		if(val.deleted){
			var pBody = $('<p/>', {
				id: "deleted",
				html: reply + "[Обрисан коментар]"
			});

		}else{
			var pBody = $('<p/>', {
				html: reply + val.content
			});			
		}
		pBody.addClass('comment-body');
		
		// ------------------------------------------------------------
		if(val.deleted){
			
			var reply = $('<p/>');
			var like = $('<p/>');
			var dislike = $('<p/>');
			var del = $('<p/>');
			var save = $('<p/>');
			
		}else{
			// ------------------------------------------------------------
			var reply = $('<button/>', {
				id: "reply-" + val.id,
				html: "Одговори"
			});
			reply.attr("onclick", "commBox(this)");
			
			var like = $('<button/>', {
				id: "like-" + val.id,
				html: "Свиђа ми се"
			});
			like.attr("onclick", "likeComm(this)");
			
			var dislike = $('<button/>', {
				id: "dislike-" + val.id,
				html: "Не свиђа ми се"
			});
			dislike.attr("onclick", "dislikeComm(this)");
			
			var del = $('<button/>', {
				id: "delete-" + val.id,
				html: "Обриши"
			});
			del.attr("onclick", "deleteComm(this)");
			
			var save = $('<button/>', {
				id: "save-" + val.id,
				html: "Сачувај"
			});
			save.attr("onclick", "saveComment(this)");
			// ------------------------------------------------------------
		}
		
		//potrebno pojednostaviti...
		if(user == undefined){
			var commDiv = $('<div/>', {
				id: val.id,
				html: p[0].outerHTML + pBody[0].outerHTML
			});
		}else if((val.usrResp.indexOf(user) > -1) && (val.author == user)){
			
			var commDiv = $('<div/>', {
				id: val.id,
				html: p[0].outerHTML + pBody[0].outerHTML + reply[0].outerHTML + del[0].outerHTML + save[0].outerHTML
			});
		}else if((val.usrResp.indexOf(user) > -1)){
			var commDiv = $('<div/>', {
				id: val.id,
				html: p[0].outerHTML + pBody[0].outerHTML + reply[0].outerHTML + save[0].outerHTML
			});
		}else{
			var commDiv = $('<div/>', {
				id: val.id,
				html: p[0].outerHTML + pBody[0].outerHTML + reply[0].outerHTML + like[0].outerHTML + dislike[0].outerHTML + save[0].outerHTML
			});
		}
		commDiv.addClass('comment');
		if((val.author == user) && (val.deleted == false)){
			commDiv.append(del);
		}
		if(val.author == user){
			var edit = $('<button/>', {
				id: "edit-" + val.id,
				html: "Измени"
			});
			edit.attr("onclick", "editComm(this)");
			commDiv.append(edit);
		}
		
		y.append(commDiv);
		y.append($("<br/>"));		
	});
	
	$.get("../rest/subforum/isResMod", function(data){
		if(data){
			var array = $("#comments-div").children();
			jQuery.each(array, function(i, val){
				console.log(val);
				if($(val).is(".comment")){
					if($(val).find("#deleted")[0] != undefined)
						return;
					if($(val).find("#edit-" + val.id)[0] == undefined){
						var del = $('<button/>', {
							id: "edit-" + val.id,
							html: "Измени"
						});
						del.attr("onclick", "editComm(this)");
						$(del).insertAfter($(val).children().last())
					}
				}
			});
		}
	});
	
	
	$.get("../rest/user/hasPriviledge", function(data){
		if(data){
			var array = $("#comments-div").children();
			jQuery.each(array, function(i, val){
				console.log(val);
				if($(val).is(".comment")){
					if($(val).find("#deleted")[0] != undefined)
						return;
					if($(val).find("#delete-" + val.id)[0] == undefined){
						var del = $('<button/>', {
							id: "delete-" + val.id,
							html: "Обриши"
						});
						del.attr("onclick", "deleteComm(this)");
						$(del).insertAfter($(val).children().last())
					}
				}
			});
		}
	});
}

function newComment(e){
	$.ajax({
		type : "POST",
		url : "../rest/comments/new",
		data : JSON.stringify({
			"content" : $("#content").val(),
			"date" : getToday()
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
			if(data){
				$(e).remove();
				$("#comments-div").html("");
				$.get("../rest/comments/get", function (data){
					loadComments(data);
				});
			}
		}
	});
	return false;
}

function commReply(e){
	var PCID =  e.id.split(/-(.+)/)[1];
	$.ajax({
		type : "POST",
		url : "../rest/comments/reply",
		data : JSON.stringify({
			"content" : $("#content").val(),
			"date" : getToday(),
			"parentComId" : parseInt(PCID)
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data)
			$(e).remove();
			$("#comments-div").html("");
			$.get("../rest/comments/get", function (data){
				loadComments(data);
			});
			//window.location.reload(true);
		}
	});	
	return false;
}

function likeComm(e){
	console.log(e.id.split(/-(.+)/)[1]);
	var id = e.id.split(/-(.+)/)[1];
	var t = $("#" + e.id);
	var g = t.parent()[0];
	console.log(g.firstChild.id);
	$.ajax({
		type : "POST",
		url : "../rest/comments/like",
		data : JSON.stringify({
			"id" : id,
			"author" : g.firstChild.id
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data)
			$("#likes-" + id)[0].innerHTML++;
			$("#like-" + id).remove();
			$("#dislike-" + id).remove();
		}
	});
}

function dislikeComm(e){
	console.log(e);
	console.log(e.id.split(/-(.+)/)[1]);
	var id = e.id.split(/-(.+)/)[1];
	var t = $("#" + e.id);
	var g = t.parent()[0];
	console.log(g.firstChild.id);
	$.ajax({
		type : "POST",
		url : "../rest/comments/dislike",
		data : JSON.stringify({
			"id" : id,
			"author" : g.firstChild.id
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data)
			$("#dislikes-" + id)[0].innerHTML++;
			$("#like-" + id).remove();
			$("#dislike-" + id).remove();
		}
	});
}

function likeTopic(e){
	$.post( "../rest/topic/like", function( data ) {
		$("#topic-likes")[0].innerHTML++;
		$("#likeTopic").remove();
		$("#dislikeTopic").remove();
	});
}

function deleteComm(e){
	//komentar se obrise na serveru i ponovo se ucita stranica
	var id = e.id.split(/-(.+)/)[1];
	var t = $("#" + e.id);
	var g = t.parent()[0];
	$.ajax({
		type : "POST",
		url : "../rest/comments/delete",
		data : JSON.stringify({
			"id" : parseInt(id),
			"author" : g.firstChild.id
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data)
			//$(e).remove();
			$("#comments-div").html("");
			$.get("../rest/comments/get", function (data){
				loadComments(data);
			});
			//window.location.reload(true); 
		}
	});
}

function dislikeTopic(e){
	$.post( "../rest/topic/dislike", function( data ) {
		$("#topic-dislikes")[0].innerHTML++;
		$("#likeTopic").remove();
		$("#dislikeTopic").remove();
	});
}

function deleteTopicBtn(){
	//proverava da li korisnik ima pravo brisanja teme i dodaje odgovarajuce dugme
	$.get("../rest/topic/canDelete", function(data){
		if(data){
			var btn = $('<button/>', {
				id: 'delete-topic',
				html: 'Обриши тему'
			});
			btn.attr("onclick", "deleteTopic();");
			$("#topic-title").append(btn);
			var btn = $('<button/>', {
				id: 'edit-topic',
				html: 'Измени тему'
			});
			btn.attr("onclick", "editTopicWindow();");
			$("#topic-title").append(btn);
		}
	});
}

function deleteTopic(){
	$.post("../rest/topic/delete", function(data){
		if(data){
			window.location = "subforum.html";
		}
	});
}

function editComm(e){
		console.log(e);
		var id = e.id.split(/-(.+)/)[1];
		var div = $("<div>");
		div.attr("class", "usertext-edit");
		var forma = $("<form>");
		forma.attr("method", "post");
		forma.attr("onsubmit", "return editComment(this);");
		forma.attr("id", id);
		var txt = $("<textarea>");
		txt.attr("id", "content");
		txt.css("max-width", "600px");
		txt.css("max-height", "100px");
		txt.css("height", "100px");
		txt.css("width", "600px");
		txt[0].innerHTML = $("#"+id).children(".comment-body")[0].innerHTML;
		var br = $("<br>");
		var potvrdi = $("<input>");
		var odustani = $("<input>");
		potvrdi.attr("type", "submit");
		odustani.attr("type", "submit");
		potvrdi.attr("value", "Потврди");
		odustani.attr("value", "Одустани");
		potvrdi.attr("style", "margin: 5px");
		forma[0].innerHTML = txt[0].outerHTML + br[0].outerHTML + potvrdi[0].outerHTML + odustani[0].outerHTML;
		forma.insertAfter(e);
		$(e).remove();
}

function editComment(e){
	console.log(e);
	$.ajax({
		type : "POST",
		url : "../rest/comments/edit",
		data : JSON.stringify({
			"id" : parseInt(e.id),
			"content" : e.children[0].value
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
				if(data){
					$(e).remove();
					$("#comments-div").html("");
					$.get("../rest/comments/get", function (data){
						loadComments(data);
					});
				}
			}
		});
	return false;
}

function saveComment(e){
	console.log(e);
	var id = e.id.split(/-(.+)/)[1];
	$.ajax({
		type : "POST",
		url : "../rest/user/saveComment",
		data : JSON.stringify({
			"id" : parseInt(id)
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
				if(data){
					console.log(e);
					//e.innerHTML = "Обриши";
					//e.setAttribute('onclick', 'deleteComment(this)');
				}
			}
		});
}

/*function deleteComment(e){
	console.log(e);
	/*var id = e.id.split(/-(.+)/)[1];
	$.ajax({
		type : "POST",
		url : "../rest/user/saveComment",
		data : JSON.stringify({
			"id" : parseInt(id)
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
				if(data){
					console.log(e);
					e.innerHTML = "Обриши";
					e.setAttribute('onclick', 'deleteComment(this)');
				}
			}
		});
}*/

function editTopicWindow(){
	window.location = "edittopic.html"
}

function setEditFields(){
	$.get("../rest/topic/editType", function(data){
		if(data.value == "ADMIN"){
			var table = document.getElementById("edit-table");
			var row = table.insertRow(2);
			var cell1 = row.insertCell(0)
			var cell2 = row.insertCell(1);
			cell1.className = "rowhead";
			cell1.setAttribute("width", "30%");
			cell1.setAttribute("align", "right");
			cell1.innerHTML = "Подфорум:";
			cell2.setAttribute("width", "70%");
			cell2.setAttribute("align", "left");
			cell2.setAttribute("id", "subforum");
			
			row = table.insertRow(3);
			var cell1 = row.insertCell(0)
			var cell2 = row.insertCell(1);
			cell1.className = "rowhead";
			cell1.setAttribute("width", "30%");
			cell1.setAttribute("align", "right");
			cell1.innerHTML = "Измени датум креирања:";
			cell2.setAttribute("width", "70%");
			cell2.setAttribute("align", "left");
			cell2.innerHTML = '<input type="radio" name="date" value="YES">Да<input type="radio" name="date" value="NO" checked>Не';
		}else if(data.value == "MODERATOR"){
			var table = document.getElementById("edit-table");
			var row = table.insertRow(2);
			var cell1 = row.insertCell(0)
			var cell2 = row.insertCell(1);
			cell1.className = "rowhead";
			cell1.setAttribute("width", "30%");
			cell1.setAttribute("align", "right");
			cell1.innerHTML = "Подфорум:";
			cell2.setAttribute("width", "70%");
			cell2.setAttribute("align", "left");
			cell2.setAttribute("id", "subforum");
			
			row = table.insertRow(3);
			var cell1 = row.insertCell(0)
			var cell2 = row.insertCell(1);
			cell1.className = "rowhead";
			cell1.setAttribute("width", "30%");
			cell1.setAttribute("align", "right");
			cell1.innerHTML = "Измени датум креирања:";
			cell2.setAttribute("width", "70%");
			cell2.setAttribute("align", "left");
			cell2.innerHTML = '<input type="radio" name="date" value="YES">Да<input type="radio" name="date" value="NO" checked>Не';			
		}else{
			var table = document.getElementById("edit-table");
			var row = table.insertRow(2);
			var cell1 = row.insertCell(0)
			var cell2 = row.insertCell(1);
			cell1.className = "rowhead";
			cell1.setAttribute("width", "30%");
			cell1.setAttribute("align", "right");
			cell1.innerHTML = "Подфорум:";
			cell2.setAttribute("width", "70%");
			cell2.setAttribute("align", "left");
			cell2.setAttribute("id", "subforum");
		}
		getSubforumName();
		getEditTopic();
	});
}

function getEditTopic(){
	$.get("../rest/topic/get", function(data){
		if(data.type == "TEXT"){
			var txtArea = $('<textarea/>',{
				id : "txt",
				html : data.content
			});
			txtArea.css({width: "600px", height: "250px"});			
			$("#content").append(txtArea);			
		}else if(data.type == "LINK"){
			var link = $('<input/>',{
				type : "url",
				id : "link"
			});
			link[0].value = data.content
			link.css({width: "600px"});
			$("#content").append(link);
		}else if(data.type == "PICTURE"){
			var i = $("<img>", {
				id : "img-preview"
			});
			var string = "data:application/unknown;base64, " + data.content;
			i.attr("src", string);
			i.css({maxWidth: "500px", maxHeight: "500px"});
			glob = string;
			var br = $('<br/>');
			
			var pic = $('<input/>', {
				id : "icon",
				type : "file"
			});
			pic.addClass("filechooser");
			pic.attr("onchange", "loadImagePreview();");
			
			$("#content").append(i);
			$("#content").append(br);
			$("#content").append(pic);
		}
	});
}

function setTopicTitle(){
	$.get("../rest/user/getHeader/topic", function(data){
		$("#topic").html(data.value);
	});
}

function getSubforumName(){
	$.get("../rest/user/getHeader/subforum", function(data){
		$("#subforum").html(data.value);
	});
}

function editTopic(){
	var content;
	if($("#icon")[0] != undefined){
			content = glob.split(/,(.+)/)[1];
	}else if($("#link")[0] != undefined){
		content = $("#link")[0].value;
	}else if($("#txt")[0] != undefined){
		content = $("#txt").value;
	}
	var datum = "";
	if($("input[name=date]:checked").val() == "YES"){
		datum = getToday();
	}
	
	$.ajax({
		type : "POST",
		url : "../rest/topic/edit",
		data : JSON.stringify({
			"content" : content,
			"datum" : datum
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			window.location = "topic.html";
			},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + " " + textStatus + " " + errorThrown);
		}
	});

	return false;
}