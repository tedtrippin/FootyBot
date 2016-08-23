<%@   taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form"
%><%@ taglib prefix="betbot" tagdir="/WEB-INF/tags"
%><html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/main.css" />">
<script src="http://cdn.jquerytools.org/1.2.7/full/jquery.tools.min.js"></script>
</head>

<body>

<div class="main">

<div class="topSection section">
  <div class="top panel" style="overflow:hidden">
    <div style="margin: 5px 5px 0px 5px; height: 70px; border:5px solid 9e9e9e; border-radius: 10px;">
      <div style="padding: 15px 0px 0px 30px; width: 300px; float: left;">the footinator</div>
      <div style="float: right;">
<c:choose>
  <c:when test="${sessionScope.LOGIN_DETAILS == null}">
<%@ include file="/WEB-INF/jsp/login.jsp" %>
  </c:when>
  <c:otherwise>
<%@ include file="/WEB-INF/jsp/logout.jsp" %>
  </c:otherwise>
</c:choose>
      </div>
    </div>
  </div>
</div>

<c:if test="${not empty errorMessages}">
<div class="section">
  <div id="errorPanel" class="panel">
  	<ul>
<c:forEach items="${errorMessages}" var="errorMessage">
		<li>${errorMessage}</li>
</c:forEach>
	</ul>  
  </div>
</div>
</c:if>

<div class="menuSection section">
  <div class="menu panel">
<jsp:include page="/WEB-INF/jsp/menu.jsp" />
  </div>
</div>

<div class="middleSection section">
  <div class="middle panel">
