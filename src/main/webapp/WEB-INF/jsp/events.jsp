<%@ include file="/WEB-INF/jsp/header.jsp" %>

<p>
Choose an event to set your bot running... (<a href="<c:url value="events">
  <c:param name="refresh" value="true"/>
</c:url>">refresh</a>)
</p>

<div>
<c:forEach items="${events}" var="event" varStatus="status">
  <betbot:event event="${event}" url="addbottoevent" />
</c:forEach> 
</div>

<div id="moduleOverlay">
<div class="contentWrap">
</div>
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

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
