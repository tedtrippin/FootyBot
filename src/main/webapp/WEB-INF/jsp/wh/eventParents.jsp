<%@   include file="/WEB-INF/jsp/header.jsp" 
%><%@ taglib prefix="whbetbot" tagdir="/WEB-INF/tags/wh" %>

<h2>William Hill footy leagues</h2><br/>

<p>
<c:forEach items="${eventParents}" var="ep">
  <whbetbot:eventParent eventParent="${ep}" /> 
</c:forEach>
</p>

<form:form method="post" action="eventparents" modelAttribute="eventParent" id="addWhEventParentForm" name="addWhEventParentForm">
name <form:input path="name"></form:input><br/>
url <form:input path="url"></form:input><br/>
<a href="javascript: document.addWhEventParentForm.submit()">add</a>
</form:form>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
