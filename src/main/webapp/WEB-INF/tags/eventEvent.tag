<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="eventEvent" required="true" type="com.rob.betBot.mvc.model.TimelineEvent" %>
<div class="raceTime">
${eventEvent.timeString}
</div>
<div class="raceDescription">
${eventEvent.description}
</div> 
