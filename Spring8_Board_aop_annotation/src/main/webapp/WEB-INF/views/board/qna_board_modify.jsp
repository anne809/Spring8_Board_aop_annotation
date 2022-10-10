<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="header.jsp" />
<script src="resources/js/modifyform.js"></script>
<style>
h1 {
	font-size: 1.5rem;
	text-align: center;
	color: #1a92b9
}

.container {
	width: 60%
}

label {
	font-weight: bold
}

#upfile {
	display: none
}

img {
	width: 20px;
}
</style>
<title>Insert title here</title>
</head>
<body>
	<!-- 게시판 수정 -->
	<div class="container">
		<form action="BoardModifyAction.bo" method="post"
			enctype="multipart/form-data" name="modifyform">
			<input type="hidden" name="BOARD_NUM" 	value="${boarddata.BOARD_NUM }">
			<input type="hidden" name="BOARD_FILE"	value="${boarddata.BOARD_FILE}">
			
			<!--  비교대상으로 추가해봅니다.. -->
			<input type="hidden" name="before_file"	value="${boarddata.BOARD_FILE}">
			
			
			
			<h1>MVC 게시판 - 수정</h1>
			<div class="form-group">
				<label for="board_name">글쓴이</label> <input name="BOARD_NAME"
					value="${boarddata.BOARD_NAME}" readOnly type="text" class="form-control">
			</div>
			<div class="form-group">
				<label for="board_subject">제목</label> <input name="BOARD_SUBJECT"
					id="board_subject" type="text" maxlength="100" class="form-control"
					value="${boarddata.BOARD_SUBJECT}">
			</div>
			<div class="form-group">
				<label for="board_content">내용</label>
				<textarea name="BOARD_CONTENT" id="board_content" rows="15"
					class="form-control">${boarddata.BOARD_CONTENT}</textarea>
			</div>
			<%-- 원문글인 경우에만 파일 첨부 수정 가능합니다. --%>
			<c:if test="${boarddata.BOARD_RE_LEV==0 }">
				<div class="form-group read">
					<label for="board_file">파일 첨부</label> <label for="upfile">
					
						<img src="resources/image/attach.png" alt="파일첨부" width="20px">
						
					</label> 
					
					<input type="file" id="upfile" name="uploadfile"> 
					<span id="filevalue">${boarddata.BOARD_FILE}</span> 
					<img src="resources/image/remove.png" alt="파일삭제" width="10px" class="remove">
				</div>
			</c:if>
			<div class="form-group">
				<label for="board_pass">비밀번호</label> <input name="BOARD_PASS"
					id="board_pass" type="password" size="10" maxlength="30"
					class="form-control" placeholder="Enter board_pass" value="">
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary">수정</button>
				<button type="reset" class="btn btn-danger" onClick="history.go(-1)">취소</button>
			</div>
		</form>
		<!-- 게시판 수정 -->
	</div>
</body>
</html>