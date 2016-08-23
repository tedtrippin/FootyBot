<%@ include file="/WEB-INF/jsp/header.jsp" %>

<p>
Bots...<br />
<c:forEach items="${bots}" var="bot">
  <betbot:bot bot="${bot}" /> 
</c:forEach>
</p>

<div>
</div>

<p>
Bets...<br />
<c:forEach items="${bets}" var="bet">
  <betbot:bet bet="${bet}" /> 
</c:forEach>
</p>

<div>
</div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
