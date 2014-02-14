/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.ects.web.controllers;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.ects.web.utils.NavigationRulesConst;


/**
 * A visual bean for the welcome page.
 */
public class WelcomeController  extends AbstractContextAwareController {

	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -239570715531002003L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(this.getClass());


	/*
	 ******************* INIT ******************** */

	/**
	 * Bean constructor.
	 */
	public WelcomeController() {
		super();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @see org.esupportail.ects.web.controllers.AbstractDomainAwareBean#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		
	}

	/**
	 * @see org.esupportail.ects.web.controllers.AbstractContextAwareController#afterPropertiesSetInternal()
	 */
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
	}

	/*
	 ******************* CALLBACK ******************** */


	/**
	 * @return String
	 */
	public String goEctsEtp() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering goEctsEtp return " + NavigationRulesConst.ECTS_ETP);
		}
		return NavigationRulesConst.ECTS_ETP;
	}

	/**
	 * @return String
	 */
	public String goEctsElp() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering goEctsElp return " + NavigationRulesConst.ECTS_ELP);
		}
		return NavigationRulesConst.ECTS_ELP;
	}

	/**
	 * @return String
	 */
	public String goEctsEtuEtp() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering goEctsEtuEtp return " + NavigationRulesConst.ECTS_ETU_ETP);
		}
		return NavigationRulesConst.ECTS_ETU_ETP;
	}

	/**
	 * @return String
	 */
	public String goEctsPreferences() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering goEctsPreferences return " + NavigationRulesConst.ECTS_PREFERENCES);
		}
		return NavigationRulesConst.ECTS_PREFERENCES;
	}

	/*
	 ******************* METHODS ******************** */




	/**
	 * Test ExceptionHandler JSF.
	 */
	public void testException() {
		throw new ConfigException("testException");
	}




	/**
	 * @return l'id de l'utilisateur connecté
	 */
	public String getCurrentUserId() {
		return getCurrentUser().getId();
	}

	
	/**
	 * @return le type de l'utilisateur connecté
	 */
	public String getCurrentUserType() {
		return getCurrentUser().getType();
	}

	
	/**
	 * @return le nom + prénom de l'utilisateur connecté
	 */
	public String getCurrentUserNom() {
		return getCurrentUser().getDisplayName();
	}

	
	/*
	 ******************* ACCESSORS ******************** */



}
