<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="eventParent" required="true" type="com.rob.betBot.model.wh.WhEventParentData" %>
<div style="clear: left;">

<div style="border-radius: 5px; background: white; padding: 5px; float: left; margin: 2px; ">
<a href="${pageContext.request.contextPath}/fb/loadevents?id=${eventParent.id}">
load
</a> 
</div>

<div style="margin: 2px; float: left;">
<a target="_blank" href="${eventParent.connectString}">${eventParent.name}</a>  
</div>

<div style="border-radius: 5px; background: white; padding: 5px; margin: 2px; float: right; ">
<a title="${eventParent.connectString}" href="${pageContext.request.contextPath}/fb/deleteeventparents?id=${eventParent.id}">
delete
</a>  
</div>

</div> 