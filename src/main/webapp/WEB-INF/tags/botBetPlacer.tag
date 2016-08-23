<%@   taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="index" 
%><%@ attribute name="betPlacer" required="true" type="com.rob.betBot.bots.BetPlacer" %> 
<div class="module">
<h3>${betPlacer.name}</h3>

<c:forEach items="${betPlacer.properties}" var="property">
  <c:out value="${property.displayName}" />: <c:out value="${property.value}" /><br />
</c:forEach>

<div class="buttons">
<a href="removebetplacer?index=${index}">remove</a> | <span>help</span> 
</div>
</div>