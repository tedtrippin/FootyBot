<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div id="bot">Bot<br/>
<c:forEach items="${bot.filters}" var="botFilter" varStatus="status">
  <betbot:botFilter filter="${botFilter}" index="${status.index}" />
</c:forEach> 
<c:forEach items="${bot.betPlacers}" var="botBetPlacer" varStatus="status">
  <betbot:botBetPlacer betPlacer="${botBetPlacer}" index="${status.index}" />
</c:forEach> 
</div>

<div class="verticalDivide">&nbsp;
</div>

<div id="modules">
Modules... (${numberOfFilters})<br />
<c:forEach items="${filters}" var="filter">
  <betbot:filter filter="${filter}" />
</c:forEach>
<br />
Betters... (${numberOfBetPlacers})<br >
<c:forEach items="${betPlacers}" var="betPlacer">
  <betbot:betPlacer betPlacer="${betPlacer}" />
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
