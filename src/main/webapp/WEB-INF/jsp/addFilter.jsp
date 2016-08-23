<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<h2>${filter.name}</h2><br/>

<form:form method="post" action="addfilter" modelAttribute="filter" id="addFilterForm" name="addFilterForm">
<form:hidden path="id" />
<c:forEach items="${filter.properties}" var="property" varStatus="propertyStatus">
  <form:hidden path="properties[${propertyStatus.index}].name" /><br />
  <form:label path="properties[${propertyStatus.index}].displayName">${property.displayName}</form:label><br />
  <form:input path="properties[${propertyStatus.index}].value"></form:input><br/> <br /><br />
</c:forEach>

<a href="javascript: document.addFilterForm.submit()">add</a> | <a href="">cancel</a>

</form:form>