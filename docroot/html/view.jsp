<%@include file="init.jsp"%>
<portlet:actionURL name="solicitudRecomendacion" var="actionForm"></portlet:actionURL>

<%
Boolean mensajeRecomendacion=(Boolean)request.getAttribute("mensajeRecomendacion");
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
				<p>En este momento, la clasificacion estimada del grupo se mantendrá para la convocatoria vigente, si quieres establecer una meta manualmente, puedes utilizar el simulador.</p>

			</div>
			<!-- /.box-body -->
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
				<p>En esta seccion podrá obtener una recomendacion de productos
					basada en la situacion actual del grupo de investigacion.</p>

				<form id="formularioExtraccion" action="<%=actionForm%>"
					method="POST">
					<button class="btn btn-block btn-primary btn-sm" id="automatico"
						name="automatico">Continuar</button>
				</form>
			</div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>
	<%} %>
</div>