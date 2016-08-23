<%@ include file="/WEB-INF/jsp/header.jsp" %>

<p>
Choose a test race to see what would have happened...
</p>

<div>
<c:forEach items="${events}" var="event" varStatus="status">
  <betbot:event event="${event}" url="testeventresult"/>
</c:forEach> 
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
