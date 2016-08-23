<%@ include file="/WEB-INF/jsp/header.jsp" %>

<p>
<h3>Log files and stuff</h3>
<ul>
<li> Log file at C:\betnow2\logs\betbot.log</li>
<li> Journal at C:\betnow2\logs\journal.log</li>
<li> Properties at c:\betnow2\betbot.properties</li>
<li> Team names file at c:\betnow2\teamnames.properties</li>
</ul>

<p>
<h3>To start a bot...</h3>
<ul>
<li> Logon using Betfair credentials, keep 'BetBillHill' selected</li>
<li> Goto 'bot' and add a 'Winner percentage layer better'</li>
<li> Goto 'events'. If empty, try clicking 'refresh'</li>
<li> Choose an event to add the bot</li>
<li> You can see bets and bots by going to the 'active' page</li>
<li> Or check the journal for bet activity</li>
</ul>

<p>
<h3>Loading William hill events</h3>
<ul>
<li> <a href="${pageContext.request.contextPath}/wh/eventparents">CLICK HERE</a>
 to go to the William Hill event loading page</li>
<li> If the required league is not present then add it. The URL can be obtained
 by going to the William Hill website and navigating to the league and copying
 the URL in your browser.
<li> Click on the name of the required league in order to start scraping events.
<li> <b>important</b> if the team names on William Hill/Betfair are different
 it is necessary to add an entry in c:\betnow2\teamnames.properties so they can 
 be linked. This usually happens to obscure teams and those with eg. F.C. or A.F. in the name.
</ul>

<p>
<h3>Betfair events (not for footy)</h3>
<ul>
<li> <a href="${pageContext.request.contextPath}/betfair/loadevents">CLICK HERE</a> to go to the Betfair event loading page</li>
</ul>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
