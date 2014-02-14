/**
 * 
 */
package org.esupportail.ects.web.controllers;


import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.ComposanteOrganisatriceDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.ects.domain.beans.Etudiant;
import org.esupportail.ects.web.beans.pojo.EtudiantPojo;
import org.esupportail.ects.web.beans.pojo.VetPojo;
import org.esupportail.wssi.services.remote.AnneeUniDTO;
import org.esupportail.wssi.services.remote.GrpResultatVetDTO;
import org.esupportail.wssi.services.remote.VersionDiplomeDTO;
import org.esupportail.wssi.services.remote.VersionEtapeDTO;
import org.primefaces.event.SelectEvent;

/**
 * Controller pour le calcul l'echelle de notation ECTS d'une etape.
 * @author gmartel
 *
 */
public class EctsEtpController extends AbstractContextAwareController {

	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -7604583431968998889L;
	
	/**
	 * Liste des annees universitaires.
	 */
	private List<AnneeUniDTO> annees;
	
	/**
	 * L'annee universitaire selectionnee.
	 */
	private String anneeSelected;
	
	/**
	 * Liste des etapes.
	 */
	private List<VetPojo> vets;
	
	/**
	 * L'etapes selectionnee.
	 */
	private VetPojo vetSelected;
	
	/**
	 * Liste des groupes d'etapes.
	 */
	//private List<GroupeEtp> groupeEtps;
		
	/**
	 * La liste des etudiants inscrits au groupe d'etapes pour l'annee.
	 */
	private List<Etudiant> etudiants;
	
	/**
	 * Les etudiants sélectionnés dans la liste.
	 */
	private Etudiant[] etudiantsSelected;
	
	/**
	 * Le filtre pour le libellé de la version d'étape
	 */
	private String filtreLibEtp;
	
	/**
	 * Le filtre pour le code de la version d'étape
	 */
	private String filtreCodEtp;
	

	/**
	 * Controlleur chargé du calcul des relevés de notes
	 */
	private CalculatorController calculatorController;

	/**
	 * Controlleur chargé de l'édition des relevés de notes
	 */
	private PrintController printController;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());


	/*
	 ******************* INIT ******************** */

	/**
	 * Bean constructor.
	 */
	public EctsEtpController() {
		super();
	}
	
	/**
 	* @see org.esupportail.ects.web.controllers.AbstractAccessController#afterPropertiesSetInternal()
	*/
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(this.printController, 
				"property printController of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.calculatorController, 
				"property calculatorController of class " + this.getClass().getName() + " can not be null");
	}
	
	/*
	 ******************* METHODS ******************** */

	/**
	 * @see org.esupportail.ects.web.controllers.AbstractDomainAwareBean#reset()
	 */
	public void reset() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering reset()");
		}
		anneeSelected = "";
		vetSelected = null;
		filtreLibEtp = "";
		filtreCodEtp = "";
		vets = new ArrayList<VetPojo>();
		etudiants = new ArrayList<Etudiant>();
	}
	
	/**
	 * @return la liste des items Années universitaires 
	 */
	public List<SelectItem> getAnneesItems() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getAnneesItems()");
		}
		List<SelectItem> l = new ArrayList<SelectItem>();
		annees = getDomainService().getAnneeUnivs();
		for (AnneeUniDTO a : annees) {
			l.add(new SelectItem(a.getCodAnu(), a.getLibAnu()));
		}
		return l;
	}
	
	
	/**
	 * callback
	 */
	public String selectAnnee() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering selectAnnee()");
		}

		List<VersionEtapeDTO> versionEtapes = getDomainService().getVersionEtapes(
				filtreCodEtp, filtreLibEtp, getCurrentUser().getUtilisateurApo().getCodCge(), anneeSelected);
		vets.clear();
		for (VersionEtapeDTO v : versionEtapes) {
			vets.add(new VetPojo(v.getCodEtp(),v.getCodVrsVet(),v.getLibWebVet(),v.getLicEtp()));
		}
		return null;
	}
	
	/**
	 * callback
	 */
	public String selectVet(SelectEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering selectVet()");
		}
		
		// On vérifie que la VET fait partie des composantes de l'utilisateur gestionnaire
		if (getCurrentUser().getType().equalsIgnoreCase(SessionController.USER_GESTIONNAIRE)) {
			Set<String> cmps = getCurrentUser().getUtilisateurApo().getCmps();
			ComposanteOrganisatriceDTO cmpOrga = getDomainService().getCmpOrgaVet(anneeSelected
					, vetSelected.getCodEtp()
					, vetSelected.getCodVrsVet().toString());
			Boolean trouve = false;
			if(cmpOrga != null){
				for (String cmp:cmps) {
					if (cmp.equals(cmpOrga.getCodComposante())) {
						trouve = true;
					}
				}
				if (!trouve) {
					FacesContext.getCurrentInstance().addMessage(null
							, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
									,getI18nService().getString("ECTS.MESSAGE.PAS_HABILITE_COMPO_ORGA")
									 + cmpOrga.getLibComposante()));
					return null;
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null
						, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
								,getI18nService().getString("ECTS.MESSAGE.ETAPE_SANS_ECTS")));
				return null;
			}
		}

		
		// On vérifie que la VET porte des crédits ECTS
		BigDecimal credits = getDomainService().getCreditsVetEcts(anneeSelected
				, vetSelected.getCodEtp()
				, vetSelected.getCodVrsVet().toString());
		if (credits==null || credits.compareTo(new BigDecimal(0))==0) {
			FacesContext.getCurrentInstance().addMessage(null
					, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
							,getI18nService().getString("ECTS.MESSAGE.ETAPE_SANS_ECTS")));
			return null;
		}
		vetSelected.setCredits(credits);
		
		// On vérifie que la VET est délibérée
		List<GrpResultatVetDTO> ldelibs = getDomainService().getDelibsVet(anneeSelected, vetSelected.getCodEtp(), vetSelected.getCodVrsVet());
		for (GrpResultatVetDTO d : ldelibs) {
			if ((!d.getEtaAvcVet().equalsIgnoreCase("T"))&&(d.getCodAdm().equalsIgnoreCase("1"))) {
				FacesContext.getCurrentInstance().addMessage(null
						, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
								,getI18nService().getString("ECTS.MESSAGE.DELIB_NON_TERMINEE")));
				return null;
			}
		}
		
		// On récupère la version de diplome de la VET
		VersionDiplomeDTO vdi = getDomainService().getVersionDiplome(
				anneeSelected, vetSelected.getCodEtp(), vetSelected.getCodVrsVet().toString());
		
		// On récupère tous les étudiants
		etudiants = getDomainService().getEtudiants(anneeSelected, vdi.getCodDip()
				, vdi.getCodVrsVdi().toString(), vetSelected.getCodEtp()
				, vetSelected.getCodVrsVet().toString(), null);
		
		if (etudiants!=null) {
			logger.debug("etudiants.size()=" + etudiants.size());
		}
		
		return "go_etp_inscrits";
	}
	
	/**
	 * Récupère les informations nécessaires à la construction du relevé de notes
	 * Sélection d'étudiants
	 * callback
	 */
	public String editionSelection() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering editionSelection()");
		}
		
		calculatorController.setAnnee(anneeSelected);
		calculatorController.setEtudiants(etudiantsSelected);
		calculatorController.setVet(vetSelected);
		List<EtudiantPojo> etudiantsToPrint = calculatorController.editionSelection();
		
		if (!etudiantsToPrint.isEmpty()) {
			return printController.genereRvn(etudiantsToPrint, anneeSelected);
		}
		FacesContext.getCurrentInstance().addMessage(getI18nService().getString("ECTS.MESSAGE.ATTENTION")
				, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.AUCUN_RESULTAT")));
		return null;
	}
	
	
	/**
	 * Récupère les informations nécessaires à la construction de la distribution
	 * ECTS de l'étape
	 * callback
	 */
	public String editionDistrib() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering editionDistrib()");
		}
		
		calculatorController.setAnnee(anneeSelected);
		calculatorController.setEtudiants(null);
		calculatorController.setVet(vetSelected);
		
		return printController.genereDistrib(calculatorController.editionDistribVet()
				, calculatorController.editionDistribElp()
				, anneeSelected);
		
	}
	
	
	/**
	 * Récupère les informations nécessaires à la construction du relevé de notes
	 * Tous les étudiants
	 * callback
	 */
	public String editionTous() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering editionTous()");
		}
		
		// On vérifie que la VET porte des crédits ECTS
		BigDecimal credits = getDomainService().getCreditsVetEcts(anneeSelected
				, vetSelected.getCodEtp()
				, vetSelected.getCodVrsVet().toString());
		if (credits==null || credits.compareTo(new BigDecimal(0))==0) {
			FacesContext.getCurrentInstance().addMessage(null
					, new FacesMessage(getI18nService().getString(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE"),
							getI18nService().getString("ECTS.MESSAGE.ETAPE_SANS_ECTS"))));
			return null;
		}
		vetSelected.setCredits(credits);
		
		calculatorController.setAnnee(anneeSelected);
		calculatorController.setEtudiants(etudiants.toArray(new Etudiant[etudiants.size()]));
		calculatorController.setVet(vetSelected);
		List<EtudiantPojo> etudiantsToPrint = calculatorController.editionSelection();
		
		if (!etudiantsToPrint.isEmpty()) {
			return printController.genereRvn(etudiantsToPrint, anneeSelected);
		}
		FacesContext.getCurrentInstance().addMessage("Attention", new FacesMessage(
				getI18nService().getString("ECTS.MESSAGE.AUCUN_RESULTAT")));
		return null;
	}
	
		
	/**
	 * callback
	 */
	public String modifRecherche() {
		logger.debug("entering modifRecherche()");
		
		//reset();
		return "go_etp";
	}
	
	/*
	 ******************* ACCESSORS ******************** */
	
	/**
	 * @return la liste des annees universitaires
	 */
	public List<AnneeUniDTO> getAnnees() {
		annees = getDomainService().getAnneeUnivs();
		return annees;
	}

	
	/**
	 * @param annees the annees to set
	 */
	public void setAnnees(List<AnneeUniDTO> annees) {
		this.annees = annees;
	}


	/**
	 * @return the anneeSelected
	 */
	public String getAnneeSelected() {
		return anneeSelected;
	}


	/**
	 * @param anneeSelected the anneeSelected to set
	 */
	public void setAnneeSelected(String anneeSelected) {
		this.anneeSelected = anneeSelected;
	}


	/**
	 * @return the vets
	 */
	public List<VetPojo> getVets() {
		return vets;
	}


	/**
	 * @param vets the vets to set
	 */
	public void setVets(List<VetPojo> vets) {
		this.vets = vets;
	}


	/**
	 * @return the vetSelected
	 */
	public VetPojo getVetSelected() {
		return vetSelected;
	}


	/**
	 * @param vetSelected the vetSelected to set
	 */
	public void setVetSelected(VetPojo vetSelected) {
		this.vetSelected = vetSelected;
	}


	/**
	 * @return the filtreLibEtp
	 */
	public String getFiltreLibEtp() {
		return filtreLibEtp;
	}


	/**
	 * @param filtreLibEtp the filtreLibEtp to set
	 */
	public void setFiltreLibEtp(String filtreLibEtp) {
		this.filtreLibEtp = filtreLibEtp;
	}



	/**
	 * @return the etudiants
	 */
	public List<Etudiant> getEtudiants() {
		return etudiants;
	}


	/**
	 * @param etudiants the etudiants to set
	 */
	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}


	/**
	 * @param etudiantsSelected the etudiantsSelected to set
	 */
	public void setEtudiantsSelected(Etudiant[] etudiantsSelected) {
		this.etudiantsSelected = etudiantsSelected;
	}


	/**
	 * @return the etudiantsSelected
	 */
	public Etudiant[] getEtudiantsSelected() {
		return etudiantsSelected;
	}


	/**
	 * @param printController the printController to set
	 */
	public void setPrintController(PrintController printController) {
		this.printController = printController;
	}

	/**
	 * @return the printController
	 */
	public PrintController getPrintController() {
		return printController;
	}

	/**
	 * @param calculatorController the calculatorController to set
	 */
	public void setCalculatorController(CalculatorController calculatorController) {
		this.calculatorController = calculatorController;
	}

	/**
	 * @return the calculatorController
	 */
	public CalculatorController getCalculatorController() {
		return calculatorController;
	}

	/**
	 * @param filtreCodEtp the filtreCodEtp to set
	 */
	public void setFiltreCodEtp(String filtreCodEtp) {
		this.filtreCodEtp = filtreCodEtp;
	}

	/**
	 * @return the filtreCodEtp
	 */
	public String getFiltreCodEtp() {
		return filtreCodEtp;
	}


}
