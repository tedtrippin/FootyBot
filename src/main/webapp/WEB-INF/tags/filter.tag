<%@   taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty" 
%><%@ attribute name="filter" required="true" type="com.rob.betBot.bots.Filter" %> 
<div class="module">
<h3>${filter.name}</h3>

<c:forEach items="${filter.properties}" var="property">
  <c:out value="${property.displayName}" /><br />
</c:forEach>

<div class="buttons">
<a href="/addfilter?id=${filter.id}" rel="#moduleOverlay">add</a> | <span>help</span> 
</div>
</div>