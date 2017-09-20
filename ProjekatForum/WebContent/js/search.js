/**
 * 
 */

function getSearchResults(){
	var searchString = $("#search-string")[0].value;
	$("#search-results")[0].innerHTML = "";
	$.get("rest/subforum/search/" + searchString, function(data){
		if(!jQuery.isEmptyObject(data)){
			processSubforums(data);
		}
	});
	$.get("rest/user/search/" + searchString, function(data){
		if(!jQuery.isEmptyObject(data)){
			processUsers(data);
		}
	});
	$.get("rest/topic/search/" + searchString, function(data){
		if(!jQuery.isEmptyObject(data)){
			processTopics(data);
		}
	});
	return false;
}

function processSubforums(data){
	var t = $('<table></table>').attr({ id: "subforums-table" });
	t.addClass("main-table");
	t.attr("border", "1");
	t.css({maxWidth: "1000px;"});
	$("#search-results").append(t);
	var t = document.getElementById("subforums-table");
	var r1 = t.insertRow(0);
	var c1 = r1.insertCell(0); 
	c1.setAttribute("class", "thead");
	c1.setAttribute("colspan", "4");
	c1.innerHTML = '<strong>Подфоруми</strong>';
	
	var r2 = t.insertRow(1);
	var c21 = r2.insertCell(0);
	var c22 = r2.insertCell(1);
	c21.setAttribute("class", "subheader");
	c22.setAttribute("class", "subheader");
	c22.setAttribute("width", "200");
	c22.setAttribute("align", "center");
	c21.innerHTML = '<strong>Назив</strong>';
	c22.innerHTML = '<strong>Иконица</strong>';
	
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
		//ovo prepraviti
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
	$("#search-results").append($('<br/>')[0]);
}

function processTopics(data){
	var t = $('<table></table>').attr({ id: "topics-table" });
	t.addClass("main-table");
	t.attr("border", "1");
	t.css({maxWidth: "1000px;"});
	$("#search-results").append(t);
	var t = document.getElementById("topics-table");
	var r1 = t.insertRow(0);
	var c1 = r1.insertCell(0);
	c1.setAttribute("class", "thead");
	c1.setAttribute("colspan", "4");
	c1.innerHTML = '<strong>Теме</strong>';
	
	var table = document.getElementById("topics-table");
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
	$("#search-results").append($('<br/>')[0]);
}

function processUsers(data){
	var t = $('<table></table>').attr({ id: "users-table" });
	t.addClass("main-table");
	t.attr("border", "1");
	t.css({maxWidth: "1000px;"});
	$("#search-results").append(t);
	var t = document.getElementById("users-table");
	var r1 = t.insertRow(0);
	var c1 = r1.insertCell(0);
	c1.setAttribute("class", "thead");
	c1.setAttribute("colspan", "4");
	c1.innerHTML = '<strong>Корисници</strong>';	
	
	var table = document.getElementById("users-table");
	var h4 = $("<h4>");
	for (var m in data){
	    h4.attr("class", "h4-podforum");
	    
	    var row = table.insertRow(1);
	    row.setAttribute("style", "height: 69px;");
	    var cell1 = row.insertCell(0);
		cell1.setAttribute("valign", "top");
		
		h4[0].innerHTML = m
		cell1.innerHTML = h4[0].outerHTML;
		
		h4[0].innerHTML = "";
	}
	$("#search-results").append($('<br/>')[0]);
}

function advancedSearch(e){
	console.log(e);
	var searchString = $("#search-field")[0].value;
	$("#search-results").html("");
	if(e["srch-type"].value == "SUBFORUM"){
		if(e["subforum"][0].checked == true){
			$.get("rest/subforum/searchTitle/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processSubforums(data);
				}
			});
		}else if(e["subforum"][1].checked == true){
			$.get("rest/subforum/searchDesc/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processSubforums(data);
				}
			});
		}else{
			$.get("rest/subforum/searchResMod/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processSubforums(data);
				}
			});
		}
	}else if(e["srch-type"].value == "TOPIC"){
		if(e["topic"][0].checked == true){
			$.get("rest/topic/searchTitle/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processTopics(data);
				}
			});
		}else if(e["topic"][1].checked == true){
			$.get("rest/topic/searchContent/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processTopics(data);
				}
			});
		}else if(e["topic"][2].checked == true){
			$.get("rest/topic/searchAuthor/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processTopics(data);
				}
			});
		}else{
			$.get("rest/topic/searchSubforum/" + searchString, function(data){
				if(!jQuery.isEmptyObject(data)){
					processTopics(data);
				}
			});
		}
	}else{
		$.get("rest/user/search/" + searchString, function(data){
			if(!jQuery.isEmptyObject(data)){
				processUsers(data);
			}
		});
	}
	
	return false;
}