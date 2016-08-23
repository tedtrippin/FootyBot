<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="bot" required="true" type="com.rob.betBot.engine.EventBot" %>
<div class="listItem">
<a href="cancelbot?id=${bot.id}">X</a> 
${bot.event.eventData.eventName}<br />
  <c:forEach items="${bot.event.markets}" var="market">
    <c:url value="botwatcher" var="botWatcherUrl">
      <c:param name="eventBotId" value="${bot.id}" />
      <c:param name="marketId" value="${market.marketData.id}" />
    </c:url>
    <a href="${botWatcherUrl}">${market.marketData.marketName}</a>
    <c:if test="${bot.event.warning == null}"> <span class="warning">${bot.event.warning}</span></c:if>
  </c:forEach>
</div> 