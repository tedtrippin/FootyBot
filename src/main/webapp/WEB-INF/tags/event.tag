<%@   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ tag body-content="empty"
%><%@ attribute name="event" required="true" type="com.rob.betBot.mvc.model.MvcEvent"
%><%@ attribute name="url" required="true" type="java.lang.String" %>
<div class="listItem">
<a href="<c:url value="${url}">	
  <c:param name="eventId" value="${event.id}" />
</c:url>">${event.startDate} ${event.name} 
<c:if test="${event.correctScore}"> (has correct score)</c:if>
<c:if test="${event.duration > 0}"> (duration: ${event.duration})</c:if> 
</a> &nbsp; <a href="prices?id=${event.id}" rel="#moduleOverlay">prices</a> 

</div> 

<script>
$(function() {
    var triggers = $("a[rel]").overlay({
        mask: '#cccccc',
        effect: 'apple',
        onBeforeLoad: function() {
            var wrap = this.getOverlay().find(".contentWrap");
            wrap.load(this.getTrigger().attr("href"));
        }
    });

    $("#contentWrap form").submit(function(e) {

      // close the overlay
      triggers.eq(1).overlay().close();

      return e.submit();
  });
});
</script> 