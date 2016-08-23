<%@ include file="/WEB-INF/jsp/header.jsp" %>

<p>
Results...
</p>
<canvas style="background-color: white;" id="resultGraph" width="<c:out value="${eventGraph.canvasWidth}" 
/>" height="<c:out value="${eventGraph.canvasHeight}" />"></canvas>

<c:forEach items="${eventGraph.eventEvents}" var="event">
  <betbot:eventEvent eventEvent="${event}" />
</c:forEach> 

<script type="text/javascript">

$(function () {
 var canvas = document.getElementById("resultGraph");
 if (null==canvas || !canvas.getContext) return;

 var ctx = canvas.getContext("2d");
 var xPadding = ${eventGraph.xPadding};
 var yPadding = ${eventGraph.yPadding};
 var xStep = ${eventGraph.xCanvasGap};
 var yStep = ${eventGraph.yCanvasGap};
 
 var labels = ${eventGraph.yAxisPriceStrings};
 drawAxes(ctx, ${eventGraph.numberOfXPoints}, labels, xPadding, yPadding, xStep, yStep);

 var color0 = "rgb(255,0,0)";
 var color1 = "rgb(0,255,0)";
 var color2 = "rgb(0,0,255)";
 var color3 = "rgb(255,255,0)";
 var color4 = "rgb(0,255,255)";
 var color5 = "rgb(255,0,255)";
 var color6 = "rgb(100,50,0)";
 var color7 = "rgb(0,100,50)";
 var color8 = "rgb(50,0,100)";
 var color9 = "rgb(100,0,50)";
 var prices;
 ctx.linewidth = 2;
<c:forEach items="${eventGraph.runnerPricesStrings}" var="pricesString" varStatus="status">
 prices = ${pricesString};
 drawRunner(ctx, color${status.index}, prices, xPadding, yPadding, xStep);
</c:forEach>
});

function drawAxes(ctx, noOfLines, yLabels, xPadding, yPadding, xStep, yStep) {
 var x = xPadding, h=ctx.canvas.height;
 ctx.beginPath();
 ctx.strokeStyle = "rgb(200,200,200)";
 for (var i=1; i<=noOfLines; i++) {
  ctx.moveTo(x, 0);
  ctx.lineTo(x, h);
  x+=xStep;
 }
 ctx.stroke();
 
 ctx.font = "10px Arial";
 var y = ctx.canvas.clientHeight-yPadding;
 for (var i=0; i<yLabels.length; i++) {
  ctx.fillText(yLabels[i], 2, y-10);
  y = y-yStep;
 }
}

function drawRunner(ctx, style, prices, xPadding, yPadding, xStep) {
 var x = xPadding, y, length = prices.length, price;
 var yOrigin = ctx.canvas.clientHeight-yPadding
 ctx.beginPath();
 ctx.strokeStyle = style;
 ctx.moveTo(x, yOrigin);
 for (var i=1; i<length; i++) {
  price = prices[i];
  y = yOrigin-price;
  ctx.lineTo(x, y);
  x+=xStep;
 }
 ctx.stroke();
 
 ctx.beginPath();
 ctx.fillStyle = style;
 ctx.arc(x-xStep, y, 3, 0, 2*Math.PI);
 ctx.fill();
}

</script>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
