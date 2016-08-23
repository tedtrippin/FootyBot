<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div>
<c:forEach items="${events}" var="event">
  <div class="listItem">
    ${event.startDate} ${event.name}
  </div>
</c:forEach> 
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
