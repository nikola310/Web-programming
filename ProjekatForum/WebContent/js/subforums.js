/**
 * Operacije nad podforumima
 */

function titleEmpty() {
	var tmp = $("#title").val();
	if (tmp == "") {
		if (document.getElementById('title-war2') == null) {
			var tlrt = '<strong style="color:red;" id="title-war2">&nbsp;&nbsp;Морате унети име подфорума.</strong>';
			$(tlrt).insertAfter("#title");
			$("#title").css("border-color", "red");
		}
		return true;
	} else {
		deleteElement("title-war2");
		return false;
	}
}

function imageSelected(){
	if(glob != null){
		if (document.getElementById('icon-war') != null)
			deleteElement("icon-war");
		return true;
	}else{
		if (document.getElementById('icon-war') == null) {
			var tlrt = '<strong style="color:red;" id="icon-war">&nbsp;&nbsp;Морате одабрати иконицу.</strong>';
			$(tlrt).insertAfter("#icon");
			$("#icon").css("border-color", "red");
		}
		return false;
	}
}

var glob;

function loadImage() {
	var FR = new FileReader();
	FR.addEventListener("load", function(e) {
		glob = e.target.result;
	});
	FR.readAsDataURL($("#icon")[0].files[0]);
}

function createSubforum() {
	if (titleEmpty())
		return false;
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
	if(imageSelected()){
		glob = glob.split(/,(.+)/)[1];
	}else{
		return false;
	}
	$.ajax({
		type : "POST",
		url : "../rest/subforum/new",
		data : JSON.stringify({
			"title" : $("#title").val(),
			"description" : $("#description").val(),
			"rules" : $("#rules").val(),
			"icon" : glob
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
				if (data.failed) {
						if (data.errCode == "ALREADY_EXISTS_ERROR") {
							if(document.getElementById('title-war') == null){
								var tlrt = '<strong style="color:red;" id="title-war">&nbsp;&nbsp;Већ постоји подфорум с тим именом.</strong>';
								$(tlrt).insertAfter("#title");
								$("#title").css("border-color", "red");
							}
						}
				}else{
					if(document.getElementById('title-war') != null){
						deleteElement("title-war");
					}
						//window.name = $("#title").val();
						window.location = "../subforum/subforum.html";
					
				}
			},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log(XMLHttpRequest + " " + textStatus + " " + errorThrown);
		}
	});

	return false;
}

function loadSubscribed(){
	$.get("rest/subforum/getSubscribed", function(data) {
		//if(!jQuery.isEmptyObject(data)){
			//$("#subforums-table").remove();
			if (!jQuery.isEmptyObject(data)) {
				var table = document.getElementById("topics");
				var l = $("<a>");
				l.attr("href", "#");
				var k = $("<h4>");
				k.attr("class", "h4-podforum");
				var d = $("<div>");
				for (var m in data){

					var row = table.insertRow(1);
					var cell2 = row.insertCell(0);

					l[0].innerHTML = m;
					l.attr("id", replaceWhiteSpace(data[m].parent) + "?" + replaceWhiteSpace(m));
					d[0].innerHTML = data[m].author + "(" + data[m].datum + ")";
					l.attr("onclick", "return setSubforumAndTopicHeader(this, 'subforum/topic.html', 'rest/user/setHeader');");
					k.attr("id", m);
					
					k[0].innerHTML = l[0].outerHTML;

					cell2.innerHTML = k[0].outerHTML + d[0].outerHTML;

					l[0].innerHTML = "";
					d[0].innerHTML = "";
					k[0].innerHTML = "";
				}
			} else {
				/*var table = document.getElementById("topics");
				var row = table.insertRow(1);
				var cell1 = row.insertCell(0);
				cell1.setAttribute("class", "no-msg");
				cell1.innerHTML = "Нема тема";*/
				$("#topics").remove();
			}
		//}else{
	});
}


function loadSubforums(){
	$.get("rest/subforum/getSubforums", function(data) {
		if(!jQuery.isEmptyObject(data)){
			writeSubforums(data);
		}else{
			var table = document.getElementById("subforums-table");
			var row = table.insertRow(2);
			var cell1 = row.insertCell(0);
			cell1.setAttribute("colspan", "3");
			cell1.setAttribute("class", "no-msg");
			cell1.innerHTML = "Нема подфорума";
		}
		//$("#topics").remove();
	});
}

function writeSubforums(data){
	var table = document.getElementById("subforums-table");
	var h4 = $("<h4>");
	var l = $("<a>");
	var link = "/rest/subforum";
	var div = $("<div>");
	var desc = "desc-";
	var i = $("<img>");
	for (var m in data){
		l[0].innerHTML = m;
		l.attr("href", "#");
		l.attr("onclick", "return setSubforumHeader('/ProjekatForum/rest/user/setHeader', this.id, '/ProjekatForum/subforum/subforum.html');");
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
}

function loadTopics() {
		$.ajax({
			type : "GET",
			url : "../rest/topic/getTopics",
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success: function(data) {
				if (!jQuery.isEmptyObject(data)) {
					var table = document.getElementById("topics");
					var l = $("<a>");
					l.attr("href", "#");
					var k = $("<h4>");
					k.attr("class", "h4-podforum");
					var d = $("<div>");
					for (var m in data){

						var row = table.insertRow(1);
						var cell2 = row.insertCell(0);

						l[0].innerHTML = m;
						l.attr("id", m);
						d[0].innerHTML = data[m].author + "(" + data[m].datum + ")";
						l.attr("onclick", "return setTopicHeader(this.id);");
						k.attr("id", m);
						
						k[0].innerHTML = l[0].outerHTML;

						cell2.innerHTML = k[0].outerHTML + d[0].outerHTML;

						l[0].innerHTML = "";
						d[0].innerHTML = "";
						k[0].innerHTML = "";
					}
					saveTopicBtns();
				} else {
					var table = document.getElementById("topics");
					var row = table.insertRow(1);
					var cell1 = row.insertCell(0);
					cell1.setAttribute("class", "no-msg");
					cell1.innerHTML = "Нема тема";
				}
			}
		});

		$.get("../rest/user/getHeader/subforum", function(data){
			document.getElementById("subforum-title").innerHTML = data.value;
		});
}

function setTopicWindow(e){
	console.log(e);
	window.name = e.innerHTML;
	return true;
}

function setCreateTopic(){
	$.get("../rest/user/getHeader/user", function(data){
		if(data.value != undefined){
			var a = $('<a/>', {
				href: 'newtopic.html',
				html: 'Додај нову тему',
				class: 'button'
			});
			$("#p-new-topic").append(a);
		}
	});
}

function deleteSubforumBtn(){
	$.get("../rest/user/sfDelete", function(data){
		if(data){
			var btn = $('<button/>', {
				id: 'delete-subforum',
				html: 'Обриши подфорум'
			});
			btn.attr("onclick", "deleteSubforum();");
			$("#p-new-topic").append(btn);	
		}
	});
}

function deleteSubforum(){
	$.post("../rest/subforum/delete", function(data){
		if(data)
			window.location = "../index.html";
	});
}

function createSubforumBtn(){
	$.get("/ProjekatForum/rest/user/hasPriviledge", function(data){
		if(data){
			$("#p-new-subforum")[0].innerHTML = '<a href="main/newsubforum.html" class="button">Додај нови подфорум</a>';
		}
	});
}

function subscribeBtn(){
	$.get("../rest/user/subscribed", function(data){
		if(data){
			var btn = $('<button/>', {
				id: 'subscribe-btn',
				html: 'Већ пратите'
			});
			btn.attr("class", "subscribed-btn");
			btn.attr("onclick", "unsubscribe(this);");
		}else{
			var btn = $('<button/>', {
				id: 'subscribe-btn',
				html: 'Пратите'
			});
			btn.attr("class", "not-subscribed-btn");
			btn.attr("onclick", "subscribe(this);");
		}
		$("#p-new-topic").append(btn);
	});
}

function subscribe(e){
	$.post("../rest/user/subscribe", function(data){
		if(data){
			e.innerHTML = "Већ пратите";
			e.setAttribute("class", "subscribed-btn");
			e.setAttribute('onclick', 'unsubscribe(this)')
		}
	});
}

function unsubscribe(e){
	$.post("../rest/user/unsubscribe", function(data){
		if(data){
			e.innerHTML = "Пратите";
			e.setAttribute("class", "unsubscribed-btn");
			e.setAttribute('onclick', 'subscribe(this)')
		}
	});
}

function saveTopicBtns(){
	$.get("../rest/user/getHeader/user", function(data){
		if(data != null){
			var table = $("#topics");
			for(i = 1; i < table[0].rows.length; i++){
				var btn = $('<button/>', {
					id: "save-" + table[0].rows[i].cells[0].children[0].id,
					html: "Сачувај"
				});
				btn.attr("onclick", "saveTopic(this)");
				btn.insertAfter(table[0].rows[i].cells[0].children[0])
			}
		}
	});
}

function saveTopic(e){
	console.log(e);
	var value = e.id.split(/-(.+)/)[1];
	$.ajax({
		type : "POST",
		url : "../rest/user/saveTopic",
		data : JSON.stringify({
			"value" : value
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
		}
	});
}