<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="bet" required="true" type="com.rob.betBot.Bet" %>
<div class="listItem">
<a href="cancelbet?id=${bet.betId}">X</a>${bet}
</div> 