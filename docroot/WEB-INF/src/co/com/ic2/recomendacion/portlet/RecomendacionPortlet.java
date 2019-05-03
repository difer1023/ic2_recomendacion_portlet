package co.com.ic2.recomendacion.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import co.com.ic2.colciencias.gruplac.ClasificacionGrupo;
import co.com.ic2.colciencias.utilidades.usuario.UsuarioUtil;
import co.com.ic2.facade.GrupoInvestigacionFacade;
import co.com.ic2.facade.RecomendacionFacade;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

public class RecomendacionPortlet extends GenericPortlet{

private static Log LOG = LogFactoryUtil.getLog(RecomendacionPortlet.class);
	
	public void init() {
		viewTemplate = getInitParameter("view-template");
	}

	@ProcessAction(name = "solicitudRecomendacion")
	public void solicitudRecomendacion(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		LOG.info("processAction solicitudRecomendacion");
		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
		
//		ParametrosProperties.getInstance().limpiarParametros();
		PortletSession portletSession = actionRequest.getPortletSession();
		ClasificacionGrupo clasificacionGrupo=(ClasificacionGrupo) portletSession.getAttribute("clasificacionGrupoInvestigacion",PortletSession.APPLICATION_SCOPE);
    	
		RecomendacionFacade recomendacionFacade=new RecomendacionFacade();
		LOG.info("INFO RECOMENDACION "+ clasificacionGrupo.getProductos()+" "+clasificacionGrupo.getClasificacionGrupo()+" "+(String)user.getExpandoBridge().getAttribute("clasificacionObjetivo"));
    	String recomendacion=recomendacionFacade.ejecutarRecomendacion(clasificacionGrupo.getProductos(), clasificacionGrupo.getClasificacionGrupo(), (String)user.getExpandoBridge().getAttribute("clasificacionObjetivo"));
    	
    	user.getExpandoBridge().setAttribute("recomendacion",recomendacion);
    	
    	UsuarioUtil.INSTANCE.asignarRol(PortalUtil.getUser(actionRequest).getCompanyId(),PortalUtil.getUser(actionRequest).getUserId(),"UsuarioGrupo");
    	
    	GrupoInvestigacionFacade facade = new GrupoInvestigacionFacade();
		String tiposProductos=facade.consultarTiposProductosInvestigacion();
		
		actionRequest.setAttribute("productosGrupo", clasificacionGrupo.getProductos());
		
    	actionRequest.setAttribute("tiposProductos", tiposProductos);
    	
    	actionRequest.setAttribute("recomendacion", (String) user.getExpandoBridge().getAttribute("recomendacion"));
    	
		actionRequest.setAttribute("view",
					"/html/recomendacion/detalle_recomendacion.jsp");
		
		} catch (PortalException | SystemException e) {
			LOG.error("Error solicitando recomendacion");
			e.printStackTrace();
		}
	}
	
	@ProcessAction(name = "guardarMeta")
	public void guardarMeta(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		LOG.info("processAction guardarMeta");
		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
			user.getExpandoBridge().setAttribute("Meta",ParamUtil.get(actionRequest,"meta", ""));
			actionResponse.sendRedirect("/group/user/inicio");
		} catch (PortalException | SystemException e) {
			LOG.error("Error poniendo meta al grupo en recomendacion.");
			e.printStackTrace();
		}
	}
	
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		HttpServletRequest request = PortalUtil
				.getHttpServletRequest(renderRequest);
		String vista = (String) request.getAttribute("view");
		LOG.info("vista:"+ vista);
		if (vista != null) {
			include(vista, renderRequest, renderResponse);
		} else {
			String recomendacion="";
			try {
				recomendacion= (String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("recomendacion");
			
				PortletSession portletSession = renderRequest.getPortletSession();
				ClasificacionGrupo clasificacionGrupo=(ClasificacionGrupo) portletSession.getAttribute("clasificacionGrupoInvestigacion",PortletSession.APPLICATION_SCOPE);
				if(recomendacion.equals("")){
					LOG.info("including default "+viewTemplate);
					LOG.info(clasificacionGrupo.getClasificacionGrupo()+" "+(String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("clasificacionObjetivo"));
					if(clasificacionGrupo.getClasificacionGrupo().equals((String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("clasificacionObjetivo"))){
						LOG.info("Grupo mantiene clasificacion, enviando mensaje");
						UsuarioUtil.INSTANCE.asignarRol(PortalUtil.getUser(renderRequest).getCompanyId(),PortalUtil.getUser(renderRequest).getUserId(),"UsuarioGrupo");
						renderRequest.setAttribute("mensajeRecomendacion", true);
					}
					if(clasificacionGrupo.getClasificacionGrupo().equals("Reconocido")){
						LOG.info("Grupo clasificado reconocido, enviando mensaje");
						UsuarioUtil.INSTANCE.asignarRol(PortalUtil.getUser(renderRequest).getCompanyId(),PortalUtil.getUser(renderRequest).getUserId(),"UsuarioGrupo");
						renderRequest.setAttribute("mensajeRecomendacionReconocido", true);
					}
					include(viewTemplate, renderRequest, renderResponse);
				}else{
					GrupoInvestigacionFacade facade = new GrupoInvestigacionFacade();
					String tiposProductos=facade.consultarTiposProductosInvestigacion();
			    	renderRequest.setAttribute("productosGrupo", clasificacionGrupo.getProductos());
			    	renderRequest.setAttribute("tiposProductos", tiposProductos);
					renderRequest.setAttribute("recomendacion", recomendacion);
					include("/html/recomendacion/detalle_recomendacion.jsp", renderRequest, renderResponse);
				}
			} catch (PortalException | SystemException e) {
				e.printStackTrace();
			}
		}
	}

	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher = getPortletContext()
				.getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			LOG.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String viewTemplate;
}
