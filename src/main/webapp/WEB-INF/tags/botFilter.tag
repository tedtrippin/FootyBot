<%@   taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="index" 
%><%@ attribute name="filter" type="com.rob.betBot.bots.Filter" %> 
<div class="module">
<h3>${filter.name}</h3>

<c:forEach items="${filter.properties}" var="property">
  <c:out value="${property.displayName}" />: <c:out value="${property.value}" /><br />
</c:forEach>

<div class="buttons">
<a href="removefilter?index=${index}">remove</a> | <span>help</span> 
</div>
</div>