<%@include file="init.jsp"%>
<portlet:actionURL name="solicitudRecomendacion" var="actionForm"></portlet:actionURL>

<%
Boolean mensajeRecomendacion=(Boolean)request.getAttribute("mensajeRecomendacion");
Boolean mensajeRecomendacionReconocido=(Boolean)request.getAttribute("mensajeRecomendacionReconocido");
%>

<div class="row">
	<%if(mensajeRecomendacion!=null){ %>
	<div class="col-md-4 col-centered">
		<div class="box box-solid">
			<div class="box-header with-border">
				<h3 class="box-title">Bienvenido</h3>
			</div>
			<!-- /.box-header -->
			<div class="box-body">
				<p>En este momento, la clasificaci�n estimada del grupo se mantendr� para la convocatoria vigente, si quieres establecer una meta manualmente, puedes utilizar el simulador.</p>
				
			</div>
			<!-- /.box-body -->
			<div class="box-footer">
		          <button type="button" class="btn btn-block btn-default btn-lg" onclick="irSimulador()">Ir al simulador de productos</button>
	        </div>
		</div>
		<!-- /.box -->
	</div>
	<%}else if(mensajeRecomendacionReconocido!=null){%>
		<div class="col-md-4 col-centered">
		<div class="box box-solid">
			<div class="box-header with-border">
				<h3 class="box-title">Bienvenido</h3>
			</div>
			<!-- /.box-header -->
			<div class="box-body">
				<p>En este momento, la clasificaci�n no esta disponible para grupos con clasificaci�n Reconocido, si quieres establecer una meta manualmente, puedes utilizar el simulador.</p>

			</div>
			<!-- /.box-body -->
			<div class="box-footer">
		          <button type="button" class="btn btn-block btn-default btn-lg" onclick="irSimulador()">Ir al simulador de productos</button>
	        </div>
		</div>
		<!-- /.box -->
	</div>
	<%}else{ %>
	<div class="col-md-4 col-centered">
		<div class="box box-solid">
			<div class="box-header with-border">
				<h3 class="box-title">Bienvenido</h3>
			</div>
			<!-- /.box-header -->
			<div class="box-body">
				<p>En esta secci�n podr� obtener una recomendaci�n de productos
					basada en la situaci�n actual del grupo de investigaci�n.</p>

				<form id="formularioExtraccion" action="<%=actionForm%>"
					method="POST">
					<button class="btn btn-block btn-primary btn-sm" id="automatico"
						name="automatico" onclick="mostrarLoader()">Continuar</button>
				</form>
			</div>
			<div class="overlay hide" id="loader">
              <i class="fa fa-spinner fa-spin"></i>
            </div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>
	<%} %>
</div>
<script type="text/javascript">
function mostrarLoader(){
	document.getElementById("loader").classList.remove("hide");
}

function irSimulador(){
	window.location.replace("/group/user/simulador");
}
</script>