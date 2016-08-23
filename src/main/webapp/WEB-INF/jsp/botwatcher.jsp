<%@ include file="/WEB-INF/jsp/header.jsp" %>

<p>
</p>

<div>
<c:forEach items="${events}" var="event" varStatus="status">
  <betbot:event event="${event}" url="addbottoevent" />
</c:forEach> 
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
