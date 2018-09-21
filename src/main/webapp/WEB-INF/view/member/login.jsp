<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script src="/HelloSpring/js/jquery-3.3.1.min.js" type="text/javascript"></script>
<script type="text/javascript" >
$().ready(function(){
	$("#loginButton").click(function() {
		$.post("/HelloSpring/member/login", $("#loginForm").serialize(), function(data) {
			if(data == "OK") {
	            alert("로그인이 완료되었습니다. 페이지를 새로고침합니다.");
	            location.href="/board/list";
	         }
	         else if ( data == "BLOCK_ACCOUNT"){
	            alert("비밀번호가 3회 이상 틀려 계정이 잠겼습니다. 1시간 후 다시 시도해 주세요.")
	         }
	         else {
	            alert("로그인이 실패했습니다. 아이디 혹은 비밀번호를 확인해 주세요.");
	            $("#email").focus();
	         }
		});
	})
})
</script>
</head>
<body>
	<h1>로그인</h1>

	
	<form:form	modelAttribute="memberVO"
				method="post" 
				action="/HelloSpring/member/login"
				id="loginForm">
		<div class="errors">
			<ul>
				<li><form:errors path="email"></form:errors></li>
				<li><form:errors path="password"></form:errors></li>
			</ul>
		</div>
		<div>
			<input type="email" name="email" placeholder="email을 입력하세요" value="${memberVO.email}">
		</div>
		<div>
			<input type="password" name="password" placeholder="비밀번호를 입력하세요" value="${memberVO.password}">
		</div>
		<div>
			<input type="submit" value="로그인" id="loginButton">
			<a href="/HelloSpring/member/signup">회원가입</a>
		</div>
	</form:form>
</body>
</html>