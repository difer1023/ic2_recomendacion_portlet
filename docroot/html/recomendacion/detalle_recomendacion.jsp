<%@include file="../init.jsp" %>

<portlet:actionURL name="guardarMeta" var="guardarMetaAction" />
<%String recomendacion=(String) request.getAttribute("recomendacion");
	String tiposProductos=(String) request.getAttribute("tiposProductos");
	String productosGrupo=(String) request.getAttribute("productosGrupo");
	%>
	
<style>
.listaProductos{
	columns: 2;
  -webkit-columns: 2;
  -moz-columns: 2;
}
</style>
<div class="row">
 <div class="col-md-12">
      <div class="box box-solid">
        <div class="box-header with-border">
          <p>Estos son los productos que te recomendamos realizar para alcanzar a categoría deseada.</p>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <ul id="productosRecomendados" class="listaProductos">
          </ul>
        </div>
        <!-- /.box-body -->
        <div class="box-footer">
        	<div class="row">
        		<div class="col-lg-6">
		          <button type="button" class="btn btn-block btn-primary btn-lg" onclick="guardarMeta()">Guardar meta</button>
        		</div>
        		<div class="col-lg-6">
		          <button type="button" class="btn btn-block btn-default btn-lg" onclick="irSimulador()">Ir al simulador de productos</button>
        		</div>
        	</div>
        </div>
      </div>
      <!-- /.box -->
    </div>
    <form action="<%=guardarMetaAction%>" method="post" id="formMeta">
    	<input type="hidden" id="meta" name='<portlet:namespace/>meta'>
    </form>
</div>

<script>
var recomendacion=JSON.parse('[<%=recomendacion %>]');
var productos=JSON.parse('<%=tiposProductos%>');
var productosGrupo=JSON.parse('<%=productosGrupo%>');

if(recomendacion){
	recomendacion.forEach(function(productoRecomendacion){
		var producto=buscarProducto(productos, productoRecomendacion.c_categoria);
		if(producto.length>0){
			$("#productosRecomendados").append('<li><b>'+producto[0].n_nombre+':</b> '+productoRecomendacion.i_num_productos+'</li>');
		}
	});
}

function buscarProducto(productosFiltro,busqueda){
	return productosFiltro.filter(producto => producto.k_codigo===busqueda);
}

function buscarProductoGrupo(productosFiltro,busqueda){
	return productosFiltro.filter(producto => producto.c_categoria===busqueda);
}

function guardarMeta(){
	var meta=[];
	recomendacion.forEach(function(productoRecomendacion){
		var productoGrupo=buscarProductoGrupo(productosGrupo, productoRecomendacion.c_categoria);
		if(productoGrupo.length>0){
			productoGrupo[0].i_num_productos+=productoRecomendacion.i_num_productos;
			meta.push(productoGrupo[0]);
		}else{
			meta.push(productoRecomendacion);
		}
	});
	$("#meta").val(JSON.stringify(meta));
	$("#formMeta").submit();
}

function irSimulador(){
	window.location.replace("/group/user/simulador");
}

</script>