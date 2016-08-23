<%@   taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty" 
%><%@ attribute name="betPlacer" required="true" type="com.rob.betBot.bots.BetPlacer" %> 
<div class="module">
<h3>${betPlacer.name}</h3>

<c:forEach items="${betPlacer.properties}" var="property">
  <c:out value="${property.displayName}" /><br />
</c:forEach>

<div class="buttons">
<a href="addbetplacer?id=${betPlacer.id}" rel="#moduleOverlay">add</a> | <span>help</span> 
</div>
</div>