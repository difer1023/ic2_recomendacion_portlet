package co.com.ic2.recomendacion.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import co.com.ic2.colciencias.gruplac.ClasificacionGrupo;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.util.PortalUtil;

public class RecomendacionInicioPortlet  extends GenericPortlet{
	
	public void init() {
		viewTemplate = getInitParameter("view-template");
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
					if(!clasificacionGrupo.getClasificacionGrupo().equals((String) PortalUtil.getUser(renderRequest).getExpandoBridge().getAttribute("clasificacionObjetivo"))){
						renderRequest.setAttribute("mensajeRecomendacion", true);
					}
				}
			} catch (PortalException | SystemException e) {
				e.printStackTrace();
			}
			include(viewTemplate, renderRequest, renderResponse);
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
	private static Log _log = LogFactoryUtil.getLog(RecomendacionPortlet.class);
}
