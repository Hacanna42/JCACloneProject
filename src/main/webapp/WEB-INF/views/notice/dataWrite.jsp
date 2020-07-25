<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성 폼</title>
</head>
<body>
	<form action="/class/data/write" method="POST">
		게시판종류 <select name="boardType">
			<c:forEach items="${boardTypes }" var="item" varStatus="status">
				<option value="${item.id }">${item.title}</option>
			</c:forEach>
		</select><br> 제목 <input type="text" name="title" placeholder="제목 입력"><br>
		내용 <input type="text" name="content" placeholder="내용 입력"><br>
		<button type="submit" name="전송">전송</button>

	</form>
</body>
</html>