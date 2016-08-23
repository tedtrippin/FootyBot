<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="bot" required="true" type="com.rob.betBot.engine.EventBot" %>
<div class="listItem">
<a href="${pageContext.request.contextPath}/fb/cancelbot?id=${bot.id}">X</a> 
${bot.event.eventData.eventName}<br />
</div> 