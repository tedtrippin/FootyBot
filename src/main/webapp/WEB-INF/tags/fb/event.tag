<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="event" required="true" type="com.rob.betBot.mvc.model.MvcEvent" %>

<div style="background: #77aa22; border-radius: 20px; margin: 5px; padding: 10px;">

<div style="float: left; width: 90px">${event.startDate}</div>
<div style="float: left; width: 300px">&nbsp;
<a href="<c:url value="/fb/addbottoevent">	
  <c:param name="eventId" value="${event.id}" />
</c:url>">${event.name}</a> 
</div>

</div>
 
