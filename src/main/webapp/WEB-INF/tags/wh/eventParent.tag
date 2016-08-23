<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="eventParent" required="true" type="com.rob.betBot.model.wh.WhEventParentData" %>
<div class="listItem">
<a title="remove this event" href="deleteeventparents?id=${eventParent.id}">X</a> <a title="load this event" href="loadevents?id=${eventParent.id}"> ${eventParent.name} </a> ${eventParent.connectString}
</div> 