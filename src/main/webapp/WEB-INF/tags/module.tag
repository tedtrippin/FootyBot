<%@ tag body-content="empty" %> 
<%@ attribute name="addable" required="false" %> 
<%@ attribute name="module" required="true" type="com.rob.betBot.bots.modules.Module" %> 
<div class="module">
${module.name}<br/>

<div class="buttons">
<a href="addModule?id=${module.id}" rel="#moduleOverlay">add</a> | <span>help</span> 
</div>
</div>