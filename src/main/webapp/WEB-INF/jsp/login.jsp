<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<div class="login">
<form:form method="post" action="${pageContext.request.contextPath}/login" modelAttribute="loginDetails" id="loginForm" name="loginForm">
<form:input path="username" /><br/>
<form:password path="password" /><br/>
<form:select path="exchange">
	<form:options items="${exchanges}" itemValue="id" itemLabel="name" /> 
</form:select>
<br />
<a href="javascript: document.loginForm.submit()">login</a>
</form:form>
</div>