<%@include file="../init.jsp"%>
<%
Boolean mensajeRecomendacion=(Boolean)request.getAttribute("mensajeRecomendacion");
System.out.println("mensaje"+ mensajeRecomendacion);
%>

<%if(mensajeRecomendacion!=null){ %>
<div class="container">
	<!-- Modal -->
	<div class="modal fade" id="modalRecomendacion" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-body">
	        <p>No tienes ninguna recomendaci�n, solicita una y potencia la capacidad de tu grupo de investigaci�n.</p>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-info" onclick="irRecomendacion()">Ir a Recomendaci�n</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
	      </div>
	    </div>
	  </div>
	</div>
</div>
	<script>
		$(document).ready(function(){
			$("#modalRecomendacion").modal("show");
		});
		
		function irRecomendacion(){
			window.location.replace("/group/user/recomendacion");
		}
	</script>
<%} %>