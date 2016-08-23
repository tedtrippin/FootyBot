<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

${module.name}<br/>

<form:form method="post" action="module" modelAttribute="module">

<c:forEach items="${module.properties}" var="property" varStatus="propertyStatus">
  <form:label path="properties[${propertyStatus.index}].displayName">${property.displayName}</form:label>
  <form:input path="properties[${propertyStatus.index}].value"></form:input><br/> <br />
</c:forEach>

<input type="submit" value="submit" />
<a href="">add</a> | <a href="">cancel</a>

</form:form>