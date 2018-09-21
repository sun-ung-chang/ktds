<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="/HelloSpring/js/jquery-3.3.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function(){
		$("#email").keyup(function(){
			
			//Ajax 요청하기
			$.post("/HelloSpring/member/check/duplicate" //URL
					, {
						"email": $(this).val() //Request Parameter
					}, function(response){
						if ( response.duplicated ) {
							$("#email-error").slideDown(100);
						} else {
							$("#email-error").slideUp(100);
							
						}
						console.log(response) //Response Call back
					})
			
		})
	})
</script>
</head>
<body>
	<h1>회원가입</h1>
	

	<form:form 	modelAttribute="memberVO"
				autocomplete="off"				
				method="post" 
				action="/HelloSpring/member/signup">
		<div class="errors">
			<ul>
				<li><form:errors path="email" /></li>
				<li><form:errors path="name" /></li>
				<li><form:errors path="password" /></li>
			</ul>
		</div>
	
		<div>
			<input type="email" name="email" id="email" placeholder="email을 입력하세요" value="${memberVO.email}">
			<div id="email-error" style="display: none;">
				이 메일은 사용할 수 없습니다.
			</div>
		</div>
		<div>
			<input type="text" name="name" placeholder="이름을 입력하세요" value="${memberVO.name}">
		</div>
		<div>
			<input type="password" name="password" placeholder="비밀번호를 입력하세요" value="${memberVO.password}">
		</div>
		<div>
			<input type="submit" value="회원가입">
			<a href="/HelloSpring/member/login">뒤로가기</a>
		</div>
	</form:form>

</body>
</html>