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
import co.com.ic2.colciencias.utilidades.properties.ParametrosProperties;
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

private static Log _log = LogFactoryUtil.getLog(RecomendacionPortlet.class);
	
	public void init() {
		viewTemplate = getInitParameter("view-template");
	}

	@ProcessAction(name = "solicitudRecomendacion")
	public void solicitudRecomendacion(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		System.out.println("processAction");
		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
		
		ParametrosProperties.getInstance().limpiarParametros();
		PortletSession portletSession = actionRequest.getPortletSession();
		ClasificacionGrupo clasificacionGrupo=(ClasificacionGrupo) portletSession.getAttribute("clasificacionGrupoInvestigacion",PortletSession.APPLICATION_SCOPE);
//    	_log.info(usuario);
    	System.out.println(clasificacionGrupo.getProductos());
//    	ClasificacionGrupo clasificacionGrupo=usuario.getClasificacionGrupo();
    	System.out.println(clasificacionGrupo.getClasificacionGrupo());
		System.out.println(user.getExpandoBridge().getAttribute("clasificacionObjetivo"));
    	RecomendacionFacade recomendacionFacade=new RecomendacionFacade();

    	String recomendacion=recomendacionFacade.ejecutarRecomendacion(clasificacionGrupo.getProductos(), clasificacionGrupo.getClasificacionGrupo(), (String)user.getExpandoBridge().getAttribute("clasificacionObjetivo"));
    	
    	user.getExpandoBridge().setAttribute("recomendacion",recomendacion);
    	
    	GrupoInvestigacionFacade facade = new GrupoInvestigacionFacade();
		String tiposProductos=facade.consultarTiposProductosInvestigacion();
		
		actionRequest.setAttribute("productosGrupo", clasificacionGrupo.getProductos());
		
    	actionRequest.setAttribute("tiposProductos", tiposProductos);
    	
    	actionRequest.setAttribute("recomendacion", (String) user.getExpandoBridge().getAttribute("recomendacion"));
    	
		actionRequest.setAttribute("view",
					"/html/recomendacion/detalle_recomendacion.jsp");
		
		} catch (PortalException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@ProcessAction(name = "guardarMeta")
	public void guardarMeta(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		System.out.println("processAction "+ParamUtil.get(actionRequest,"meta", ""));
		User user = null;
		try {
			user = PortalUtil.getUser(actionRequest);
			user.getExpandoBridge().setAttribute("Meta",ParamUtil.get(actionRequest,"meta", ""));
			actionResponse.sendRedirect("/group/user/inicio");
		} catch (PortalException | SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		HttpServletRequest request = PortalUtil
				.getHttpServletRequest(renderRequest);
		String vista = (String) request.getAttribute("view");
		_log.info("vista:"+ vista);
		if (vista != null) {
			include(vista, renderRequest, renderResponse);
		} else {
			String recomendacion="";
			try {
				recomendacion= (String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("recomendacion");
			
				PortletSession portletSession = renderRequest.getPortletSession();
				ClasificacionGrupo clasificacionGrupo=(ClasificacionGrupo) portletSession.getAttribute("clasificacionGrupoInvestigacion",PortletSession.APPLICATION_SCOPE);
				if(recomendacion.equals("")){
					_log.info("including default "+viewTemplate);
					_log.info(clasificacionGrupo.getClasificacionGrupo()+" "+(String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("clasificacionObjetivo"));
					if(clasificacionGrupo.getClasificacionGrupo().equals((String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("clasificacionObjetivo"))){
						_log.info("enviando mensaje");
						renderRequest.setAttribute("mensajeRecomendacion", true);
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
			_log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected String viewTemplate;
}
