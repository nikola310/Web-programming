/**
 * 
 */

function loadUsers(){
	$.get("../rest/user/getUsers", function(data){
		console.log(data);
		jQuery.each(data, function(i, val){
			var table = $("#change-user-table");
			var row = table[0].insertRow(2);
			var cell1 = row.insertCell(0);
			var cell2 = row.insertCell(1);
			var cell3 = row.insertCell(2);
			
			cell1.setAttribute("class", "rowhead-center");
			cell1.setAttribute("align", "center");
			cell1.innerHTML = val.username;
			
			cell2.setAttribute("class", "rowhead-center");
			cell2.setAttribute("align", "center");
			cell2.setAttribute("id", "type-" + val.username);
			cell2.innerHTML = val.userType;
			
			var id1 = "";
			var inner1 = "";
			var id2 = "";
			var inner2 = "";
			if(val.userType == "NORMAL"){
				id1 = val.username + "-MODERATOR";
				inner1 = "MODERATOR";
				id2 = val.username + "-ADMIN";
				inner2 = "ADMIN"
			}else if(val.userType == "MODERATOR"){
				id1 = val.username + "-NORMAL";
				inner1 = "NORMAL";
				id2 = val.username + "-ADMIN";
				inner2 = "ADMIN"
			}else{
				id1 = val.username + "-MODERATOR";
				inner1 = "MODERATOR";
				id2 = val.username + "-NORMAL";
				inner2 = "NORMAL"
			}
			
			var btn1 = $('<button/>', {
				id: id1,
				html: inner1
			});
			btn1.attr("onclick", "changeUserType(this)");
			var btn2 = $('<button/>', {
				id: id2,
				html: inner2
			});
			btn2.attr("onclick", "changeUserType(this)");
			
			cell3.innerHTML = btn1[0].outerHTML + " " + btn2[0].outerHTML;
		});
	});
}

function changeUserType(e){
	var autor = e.id.split(/-(.+)/)[0];
	var type = e.id.split(/-(.+)/)[1];
	$.ajax({
		type : "POST",
		url : "../rest/user/changeUserType",
		data : JSON.stringify({
			"value" : type,
			"header" : autor
		}),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			console.log(data);
			console.log(autor + type);
			console.log(e);
			if(data.failed == false){
				e.id = autor + "-" + $("#type-" + autor)[0].innerHTML;
				e.innerHTML = $("#type-" + autor)[0].innerHTML;
				$("#type-" + autor)[0].innerHTML = type;
			}else if(data.errCode == "SUBFORUM_ERROR"){
				alert("Постоји подфорум на којем је " + autor + " једини модератор");
			}else{
				console.log("INTERNAL SERVER ERROR");
			}
		}
	});
}