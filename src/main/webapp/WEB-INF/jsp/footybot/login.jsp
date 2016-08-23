<html>
<head></head>

<body>

<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<form:form method="post" action="${pageContext.request.contextPath}/fb/login" modelAttribute="loginDetails" id="loginForm" name="loginForm">

<div style="border-radius: 20px; background: #77aa22; padding: 20px; width: 300px; margin: auto; margin-top: 150px;">
  <div>
    <div style="float: left; padding: 5px;">username</div>
    <div style="padding: 5px;"><form:input path="username" /></div>
  </div>
  <div>
    <div style="float: left; padding: 5px;">password</div>
    <div style="padding: 5px;"><form:password path="password" /></div>
  </div>
  <div>
<a href="javascript: document.loginForm.submit()">login</a>
  </div>
</div>

</form:form>

<%@ include file="/WEB-INF/jsp/footybot/footer.jsp" %>

</body>
</html>