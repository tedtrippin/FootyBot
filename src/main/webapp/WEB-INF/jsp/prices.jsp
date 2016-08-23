<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<h2>Prices</h2><br/>

<c:forEach items="${prices}" var="runnerPrice" varStatus="propertyStatus">
  ${runnerPrice.name} - ${runnerPrice.price}<br />
</c:forEach>

<a href="">reload</a> <a href="">close</a>
