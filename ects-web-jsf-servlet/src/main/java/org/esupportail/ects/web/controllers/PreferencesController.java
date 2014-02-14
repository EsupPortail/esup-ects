/**
 * 
 */
package org.esupportail.ects.web.controllers;

import gouv.education.apogee.commun.transverse.dto.WSReferentiel.recupererSignataire.SignataireWSSignataireDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.ects.domain.beans.User;
import org.esupportail.ects.utils.ComparatorString;
import org.primefaces.event.SelectEvent;

/**
 * Controlleur pour les préférences de l'utilisateur.
 * @author gmartel
 *
 */
public class PreferencesController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 2503649603430502319L;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(this.getClass());
	
	/**
	 * A list of JSF components for the locales.
	 */
	private List<SelectItem> localeItems;

	/**
	 * Liste des signataires.
	 */
	private List<SignataireWSSignataireDTO> signataires;
	
	/**
	 * Le signataire selectionné.
	 */
	private SignataireWSSignataireDTO signataireSelected;

	/**
	 * Bean constructor.
	 */
	public PreferencesController() {
		super();
	}
	
	/**
	 * @see org.esupportail.ects.web.controllers.AbstractDomainAwareBean#reset()
	 */
	public void reset() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering reset()");
		}
		signataireSelected = null;

		if (signataires==null) {
			SignataireWSSignataireDTO[] signs = getDomainService().getSignataires();
			
			signataires = new ArrayList<SignataireWSSignataireDTO>();
			
			for (int i = 0; i < signs.length; i++ ) {
				SignataireWSSignataireDTO sdto = signs[i];
				signataires.add(sdto);
			}
			Collections.sort(signataires, new ComparatorString(SignataireWSSignataireDTO.class));
		}
	}
	
	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			addUnauthorizedActionMessage();
			return null;
		}
		return "navigationPreferences";
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return getCurrentUser() != null;
	}

	/**
	 * @return the localeItems
	 */
	public List<SelectItem> getLocaleItems() {
		if (localeItems == null) {
			localeItems = new ArrayList<SelectItem>();
			Iterator<Locale> iter = 
				FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
			while (iter.hasNext()) {
				Locale locale = iter.next();
				StringBuffer buf = new StringBuffer(locale.getLanguage());
				buf.append(" - ").append(locale.getDisplayLanguage(locale));
				localeItems.add(new SelectItem(locale, buf.toString()));
			}
		}
		return localeItems;
	}
	
	/**
	 * @param event
	 */
	public void selectLocale(final ValueChangeEvent event) {
		Locale l = (Locale) event.getNewValue();
		setLocale(l);
		
	}
		
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(final Locale locale) {
		User currentUser = getCurrentUser();
		if (currentUser != null) {
			// update the current user
			if (logger.isDebugEnabled()) {
				logger.debug("set language [" + locale + "] for user '" 
						+ currentUser.getDisplayName() + "'");
			}
			currentUser.setLanguage(locale.toString());
			getSessionController().resetSessionLocale();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					getI18nService().getString("PREFERENCES.MESSAGE.LANGUAGE_UPDATED"
							,getCurrentUserLocale())));
		}
	}

	
	/**
	 * callback
	 */
	public String selectSignataire(SelectEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering selectSignataire()");
		}


		return null;
	}

	
	/*
	 ******************* ACCESSORS ******************** */
	

	/**
	 * @return the signataires
	 */
	public List<SignataireWSSignataireDTO> getSignataires() {
		return signataires;
	}

	/**
	 * @param signataires the signataires to set
	 */
	public void setSignataires(List<SignataireWSSignataireDTO> signataires) {
		this.signataires = signataires;
	}

	/**
	 * @return the signataireSelected
	 */
	public SignataireWSSignataireDTO getSignataireSelected() {
		return signataireSelected;
	}

	/**
	 * @param signataireSelected the signataireSelected to set
	 */
	public void setSignataireSelected(SignataireWSSignataireDTO signataireSelected) {
		this.signataireSelected = signataireSelected;
	}

}
