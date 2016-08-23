<%@   include file="/WEB-INF/jsp/footybot/header.jsp" %>

<div style="border-radius: 20px; background: #77aa22; padding: 20px; margin: 5px; width: 700px;">

William Hill footy leagues<br/>

<p>
<c:forEach items="${eventParents}" var="ep">
  <fbTag:eventParent eventParent="${ep}" /> 
</c:forEach>
</p>

</div>



<div style="border-radius: 20px; background: #77aa22; padding: 20px; margin: 5px;">

<form:form method="post" action="${pageContext.request.contextPath}/fb/eventparents" modelAttribute="eventParent" id="addWhEventParentForm" name="addWhEventParentForm">
  <div>
    add a new league...
  </div>
  <div>
    <div style="float: left; width: 60px; padding: 5px;">name</div>
    <div style="padding: 5px;"><form:input path="name" /></div>
  </div>
  <div>
    <div style="float: left; width: 60px; padding: 5px;">url</div>
    <div style="padding: 5px;"><form:input cssStyle="width: 400px;" path="url" /></div>
  </div>
  <div>
<a href="javascript: document.addWhEventParentForm.submit()">add</a>
  </div>
</form:form>

</div>

<%@ include file="/WEB-INF/jsp/footybot/footer.jsp" %>
