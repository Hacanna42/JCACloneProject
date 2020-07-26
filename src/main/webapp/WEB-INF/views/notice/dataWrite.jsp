<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="<c:url value="/resources/js/jquery-1.12.1.min.js"/>"></script>
<script type="text/javascript">
function insertBoard(){
	var url = $("form").attr("action");
	var param = $("form").serialize();
	
	if(confirm("작성하시겠습니까?")){
		$.ajax({
			url : url,
			data: param,
			type: "POST",
			dataType: "json"
		}).done(function(json){
			if(json.result > 0){
				alert("작성 완료!");
				window.location.replace("/class/data/list?boardType="+json.boardType);
			}
		});
	}
}
</script>
<title>게시글 작성 폼</title>
</head>
<body>
	<c:set var="actionUrl" value="/class/data/write/"/>
	<c:if test="${board.id gt 0 }">
		<c:set var="actionUrl" value="/class/data/edit/"/>
	</c:if>
	<form action="<c:url value="${actionUrl }"/>" method="POST">
		게시판종류 <select name="boardType">
			<c:forEach items="${boardTypes }" var="item" varStatus="status">
				<option value="${item.id }"<c:if test="${item.id eq board.boardType}">selected</c:if>>${item.title}</option>
			</c:forEach>
		</select><br> 제목 <input value="${board.title }" type="text" name="title" placeholder="제목 입력"><br>
		내용 <input value=${board.content} type="text" name="content" placeholder="내용 입력"><br>
		<input type="hidden" name="id" value="${board.id }"/>
		<button type="submit" name="전송" onclick="insertBoard();">전송</button>

	</form>
</body>
</html>