<%@ include file="/WEB-INF/jsp/footybot/header.jsp" %>

<c:if test="${loadingEvents}">
<div id="loadingEventsDiv">
<img src="${pageContext.request.contextPath}/resources/images/green_loading_circle.gif" /> loading events...<br />
</div>

<script>
var checkLoadingInterval = null;
$(document).on('ready', function(){
	checkLoadingInterval = setInterval( checkLoading, 2000);
});

function checkLoading() {		
    $.ajax({
    	type: "GET",
        url: "${pageContext.request.contextPath}/fb/eventloadingcheck",
        dataType: "json",
        success: function (data) {
            if (!data.stillLoading) {
            	$(document.getElementById("loadingEventsDiv")).remove();
            	clearInterval(checkLoadingInterval);
            	var d = document.getElementById("eventRefreshDiv");
            	d.style.visibility = "visible";
	        }
	    }
	});
}
</script>
</c:if>

<div id="eventRefreshDiv" style="visibility: hidden; ">
 Events finished loading. Click <a href="?refresh=true">refresh</a> to load see them.
</div>

<div>
  <div style="float: left; background: #77aa22; border-radius: 20px; margin: 5px; padding: 10px;">
    Events...
    <c:forEach items="${events}" var="event" varStatus="status">
      <fbTag:event event="${event}" />
    </c:forEach> 
  </div>

  <div style="float: left; background: #77aa22; border-radius: 20px; margin: 5px; padding: 10px;">
    Bots...
    <c:forEach items="${bots}" var="bot">
      <fbTag:bot bot="${bot}" /> 
    </c:forEach>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/footybot/footer.jsp" %>
