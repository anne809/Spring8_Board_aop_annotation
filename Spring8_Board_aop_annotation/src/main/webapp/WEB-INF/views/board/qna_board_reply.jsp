<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="header.jsp" />
<script src="js/reply.js"></script>
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
</style>
<title>MVC 게시판</title>
</head>
<body>
   <div class="container">
      <form action="BoardReplyAction.bo" method="post" name="boardform">
         <input type="hidden" name="BOARD_NUM" value="${boarddata.BOARD_NUM}"> <%--글번호 --%>
         <input type="hidden" name="BOARD_RE_REF" value="${boarddata.BOARD_RE_REF}"><%--같은 레퍼런스--%>
         <input type="hidden" name="BOARD_RE_LEV" value="${boarddata.BOARD_RE_LEV}"><%--글깊이 레벨 --%>
         <input type="hidden" name="BOARD_RE_SEQ" value="${boarddata.BOARD_RE_SEQ}"><%--시퀀스 --%>
         <h1>MVC 게시판 - Reply</h1>
         <div class="form-group">
            <label for="board_name">글쓴이</label> 
            <input name="BOARD_NAME" id="board_name" value="${id}" type="text" 
            class="form-control" readOnly>
         </div>
         <div class="form-group">
            <label for="board_pass">비밀번호</label> 
            <input name="BOARD_PASS" id="board_pass" type="password"
               class="form-control" placeholder="Enter board_pass">
         </div>
         <div class="form-group">
            <label for="board_subject">제목</label>
            <input name="BOARD_SUBJECT" id="board_subject" type="text" size="50" maxlength="100"
               class="form-control" value="Re: ${boarddata.BOARD_SUBJECT}">
         </div>
         <div class="form-group">
            <label for="board_content">내용</label>
            <textarea name="BOARD_CONTENT" id="board_content" cols="67"
               rows="15" class="form-control"></textarea>
         </div>
         <div class="form-group">
            <button type="submit" class="btn btn-primary">등록</button>
            <button type="reset" class="btn btn-danger"
               onClick="history.go(-1)">취소</button>
         </div>
      </form>
   </div>
</body>
</html>