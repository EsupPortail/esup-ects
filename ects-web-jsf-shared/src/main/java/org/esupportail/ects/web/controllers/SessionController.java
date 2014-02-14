/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.ects.web.controllers;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.commons.utils.ContextUtils;
import org.esupportail.commons.utils.strings.StringUtils;
import org.esupportail.commons.web.controllers.ExceptionController;
import org.esupportail.ects.domain.beans.User;
import org.esupportail.ects.domain.beans.UtilisateurApo;
import org.esupportail.ects.services.authentication.Authenticator;

/**
 * A bean to memorize the context of the application.
 */
public class SessionController extends AbstractDomainAwareBean {

	/*
	 ******************* PROPERTIES ******************** */
	
	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -5936434246704000653L;

	/**
	 * The exception controller (called when logging in/out).
	 */
	private ExceptionController exceptionController;
	
	/**
	 * The authenticator.
	 */
	private Authenticator authenticator;
	
	/**
	 * true to print login/logout button in servlet mode.
	 */
	private boolean printLoginLogoutButtons = true;

	/**
	 * The CAS logout URL.
	 */
	private String casLogoutUrl;
	
	/**
	 * type utilisateur étudiant.
	 */
	public static final String USER_ETUDIANT = "etu";
	
	/**
	 * type utilisateur Gestionnaire.
	 */
	public static final String USER_GESTIONNAIRE = "apo";
	
	/**
	 * type utilisateur invité.
	 */
	public static final String USER_GUEST = "guest";
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());
	
	
	/*
	 ******************* INIT ******************** */
	
	/**
	 * Constructor.
	 */
	public SessionController() {
		super();
	}

	/**
	 * @see org.esupportail.ects.web.controllers.AbstractDomainAwareBean#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		Assert.notNull(this.exceptionController, "property exceptionController of class " 
				+ this.getClass().getName() + " can not be null");
		Assert.notNull(this.authenticator, "property authenticator of class " 
				+ this.getClass().getName() + " can not be null");
	}

	
	/*
	 ******************* CALLBACK ******************** */
	
	
	/*
	 ******************* METHODS ******************** */
	
	/**
	 * @return the current user, or null if guest.
	 */
	@Override
	public User getCurrentUser() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getCurrentUser()");
		}
		
		User user = authenticator.getUser();
		
		// Si nouvel utilisateur, on cherche son type
		if ((user.getType()==null)||(user.getType().isEmpty())) {
			if (StringUtils.isInteger(user.getId())) {
				if (logger.isDebugEnabled()) {
					logger.debug("id=" + user.getId() + USER_ETUDIANT);
				}
				user.setType(USER_ETUDIANT);
				user.setUtilisateurApo(null);
				user.setDisplayName(user.getId());
			} else {
				UtilisateurApo utilisateurApo = getDomainService().getUtilisateurApo(user.getId());
				if (utilisateurApo==null) {
					if (logger.isDebugEnabled()) {
						logger.debug("id=" + user.getId() + USER_GUEST);
					}
					user.setType(USER_GUEST);
					user.setUtilisateurApo(null);
					user.setDisplayName(user.getId());
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("id=" + user.getId() + USER_GESTIONNAIRE);
					}
					user.setType(USER_GESTIONNAIRE);
					user.setUtilisateurApo(utilisateurApo);
					user.setDisplayName(utilisateurApo.getLibCmtUti());
				}
			}
		}
		return user;
	}

	/**
	 * @return true if the login button should be printed. 
	 */
	public boolean isPrintLogin() {
		if (!printLoginLogoutButtons) {
			return false;
		}
		return ContextUtils.isServlet() && getCurrentUser() == null;
	}
	
	/**
	 * @return true if the logout button should be printed. 
	 */
	public boolean isPrintLogout() {
		if (!printLoginLogoutButtons) {
			return false;
		}
		return ContextUtils.isServlet() && getCurrentUser() != null;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName() + "#" + hashCode();
	}

	/**
	 * @param authenticator the authenticator to set
	 */
	public void setAuthenticator(final Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * JSF callback.
	 * @return a String.
	 * @throws IOException 
	 */
	public String logout() throws IOException {
		if (ContextUtils.isPortlet()) {
			throw new UnsupportedOperationException("logout() should not be called in portlet mode.");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String returnUrl = request.getRequestURL().toString().replaceFirst("/stylesheets/[^/]*$", "");
		String forwardUrl;
		Assert.hasText(
				casLogoutUrl, 
				"property casLogoutUrl of class " + getClass().getName() + " is null");
		forwardUrl = String.format(casLogoutUrl, StringUtils.utf8UrlEncode(returnUrl));
		// note: the session beans will be kept even when invalidating 
		// the session so they have to be reset (by the exception controller).
		// We invalidate the session however for the other attributes.
		request.getSession().invalidate();
		request.getSession(true);
		// calling this method will reset all the beans of the application
		exceptionController.restart();
		externalContext.redirect(forwardUrl);
		facesContext.responseComplete();
		return null;
	}

	
	
	
	
	
	
	/*
	 ******************* ACCESSORS ******************** */
	
	
	
	
	/**
	 * @param exceptionController the exceptionController to set
	 */
	public void setExceptionController(final ExceptionController exceptionController) {
		this.exceptionController = exceptionController;
	}

	/**
	 * @param printLoginLogoutButtons the printLoginLogoutButtons to set
	 */
	public void setPrintLoginLogoutButtons(final boolean printLoginLogoutButtons) {
		this.printLoginLogoutButtons = printLoginLogoutButtons;
	}

	/**
	 * @return the casLogoutUrl
	 */
	protected String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	/**
	 * @param casLogoutUrl the casLogoutUrl to set
	 */
	public void setCasLogoutUrl(final String casLogoutUrl) {
		this.casLogoutUrl = StringUtils.nullIfEmpty(casLogoutUrl);
	}
	
}
