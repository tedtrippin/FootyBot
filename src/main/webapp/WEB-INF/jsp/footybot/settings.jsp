<%@ include file="/WEB-INF/jsp/footybot/header.jsp" %>

<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<form:form method="post" action="${pageContext.request.contextPath}/fb/settings" modelAttribute="botSettings" id="settingsForm" name="settingsForm">

<div style="border-radius: 20px; background: #77aa22; padding: 20px; width: 300px;">
  <div>
    <div style="float: left; padding: 5px; width: 100px;">Lay amount</div>
    <div style="padding: 5px;"><form:input path="layAmount" /></div>
  </div>
  <div>
    <div style="float: left; padding: 5px; width: 100px;">MAX price</div>
    <div style="padding: 5px;"><form:input path="maxPrice" /></div>
  </div>
  <div>
    <div style="float: left; padding: 5px; width: 100px;">Percentage</div>
    <div style="padding: 5px;"><form:input path="percentage" /></div>
  </div>
  <div>
<a href="javascript: document.settingsForm.submit()">save</a>
  </div>
</div>

</form:form>

<%@ include file="/WEB-INF/jsp/footybot/footer.jsp" %>
