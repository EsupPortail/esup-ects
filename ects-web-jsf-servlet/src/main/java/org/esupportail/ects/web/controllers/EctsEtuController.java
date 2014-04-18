/**
 * 
 */
package org.esupportail.ects.web.controllers;

import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.ComposanteOrganisatriceDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EtapeResVdiVetDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext;  

import org.apache.cxf.common.util.StringUtils;
import org.esupportail.commons.services.ldap.LdapUser;
import org.esupportail.commons.services.ldap.LdapUserService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.ects.domain.beans.Etudiant;
import org.esupportail.ects.web.beans.pojo.EtudiantPojo;
import org.esupportail.ects.web.beans.pojo.InscriptionVetPojo;
import org.esupportail.ects.web.beans.pojo.VetPojo;
import org.esupportail.wssi.services.remote.GrpResultatVetDTO;
import org.primefaces.event.SelectEvent;

/**
 * Controller pour le calcul l'echelle de notation ECTS d'un étudiant.
 * @author gmartel
 *
 */
public class EctsEtuController extends AbstractContextAwareController {

	/*
	 ******************* PROPERTIES ******************** */

	
	/**
	 * the serialization id. 
	 */
	private static final long serialVersionUID = 7939642443086162026L;

	/**
	 * Critère code étudiant.
	 */
	private String critCodEtu;
	
	/**
	 * Critère nom étudiant.
	 */
	private String critNomEtu;
	
	/**
	 * Critère date naissance étudiant.
	 */
	private String critDateNaisEtu;
	
	/**
	 * La liste des etudiants correspondant aux critères de recherche.
	 */
	private List<Etudiant> etudiants;
	
	/**
	 * L'etudiant sélectionné dans la liste.
	 */
	private Etudiant etudiantSelected;
	
	/**
	 * Liste des inscriptions.
	 */
	private List<InscriptionVetPojo> inscriptions;
	
	/**
	 * L'etapes selectionnée.
	 */
	private InscriptionVetPojo vetSelected;

	/**
	 * Controlleur chargé du calcul des relevés de notes
	 */
	private CalculatorController calculatorController;

	/**
	 * Controlleur chargé de l'édition des relevés de notes
	 */
	private PrintController printController;

	/**
	 * The LDAP service.
	 */
	private LdapUserService ldapService;

	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());


	/*
	 ******************* INIT ******************** */

	/**
	 * Bean constructor.
	 */
	public EctsEtuController() {
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
		critCodEtu = "";
		critDateNaisEtu = "";
		critNomEtu = "";
		etudiants = new ArrayList<Etudiant>();
		etudiantSelected = null;
	}
	
	
	/**
	 * callback
	 */
	public String rechercheEtu() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering rechercheEtu()");
		}
		// Contrôle des critères
		// Au moins un des critères doit être saisi
		if (StringUtils.isEmpty(critCodEtu) && StringUtils.isEmpty(critNomEtu)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					getI18nService().getString("ECTS_ETU.MESSAGE.CRITERE_OBLIG")));
			return null;
		}
		
		// Le numéro étudiant doit avoir exactement 8 caractères numériques
		if (critCodEtu != null){
			critCodEtu = critCodEtu.trim();
			if (!critCodEtu.equals("")) {
				if (!Pattern.matches("^[0-9]*",critCodEtu)) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							getI18nService().getString("ECTS_ETU.MESSAGE.NO_ETU_NUM")));
					return null;
				}
				if (critCodEtu.length()!=8) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							getI18nService().getString("ECTS_ETU.MESSAGE.NO_ETU_8_CAR")));
					return null;				
				}
			}
		}
		
		// Le nom doit avoir au moins 3 caractères
		if (critNomEtu != null && !critNomEtu.equals("")) {
			critNomEtu.trim();
			if (critNomEtu.length()<3) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						getI18nService().getString("ECTS_ETU.MESSAGE.NOM_3_CAR_MIN")));
				return null;				
			}
		}
		
		// La date de naissance doit être au format jjmmaaaa
		// TODO
		
		// On vide la liste des étudiants
		etudiants.clear();
		
		// On recherche l'étudiant
		// Si code étudiant renseigné
		if (!StringUtils.isEmpty(critCodEtu)) {
			Etudiant etu = getDomainService().getEtudiant(critCodEtu);
			if (etu==null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(getI18nService().getString("ECTS_ETU.MESSAGE.PAS_D_ETUDIANT")));
			} else {
				etudiants.add(etu);
			}
		}
		
		// Si nom renseigné => recherche dans le ldap
		if (!critNomEtu.isEmpty()) {
			String filter = "&(objectClass=ur1person)(supannEtuId=*)(sn=*"+ critNomEtu +"*)" ;

			List<LdapUser> listeuser = ldapService.getLdapUsersFromFilter(filter);
			
			//pour chaque utilisateur du ldap retenu:
			if (listeuser!=null && !listeuser.isEmpty()) {
				for (LdapUser lu : listeuser) {
					Map<String,List<String>> mattributs = lu.getAttributes();
					
					List<String> uid = (List<String>) mattributs.get(ldapService.getIdAttribute());
					Etudiant etu = getDomainService().getEtudiant(uid.get(0));
					if (etu!=null) {
						etudiants.add(etu);
					}
				}
			}
		}
		return "";
	}

	/**
	 * callback
	 */
	public String identifierEtu() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering identifierEtu()");
		}
		// On nettoie
		reset();
		
		// On recherche l'étudiant
		etudiantSelected = getDomainService().getEtudiant(getCurrentUser().getId());
		
		// On enchaine sur les inscriptions
		renseigneInscriptions();
		
		return "go_etu_inscriptions";
	}

	/**
	 * callback
	 */
	public String selectEtu(SelectEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering selectEtu()");
		}

		// On enchaine sur les inscriptions
		renseigneInscriptions();

		return "go_etu_inscriptions";
	}
	
	/**
	 * Récupère les inscriptions VET de l'étudiant sélectionné
	 */
	public void renseigneInscriptions() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering renseigneInscriptions()");
		}
		
		// Recherche du contrat pédagogique niveau diplome/etape 
		ContratPedagogiqueResultatVdiVetDTO[] contrat = getDomainService().getResultatsVetEtu("toutes", etudiantSelected.getCodEtu());
		
		inscriptions = new ArrayList<InscriptionVetPojo>();

		for (int i = 0; i < contrat.length; i++ ) {
			// Diplome
			ContratPedagogiqueResultatVdiVetDTO rdto = contrat[i];
			
			// Etapes rattachées
			EtapeResVdiVetDTO[] etapes = rdto.getEtapes();
			if (etapes != null && etapes.length > 0) {

				for (int j = 0; j < etapes.length; j++ ) {
					
					EtapeResVdiVetDTO etape = etapes[j];
					
					// On ne garde pas les étapes annulées
					if ((etape.getCodEtaIae()== null) || (etape.getCodEtaIae().equals("A"))) {
						continue;
					}

					if (etape.getEtape()!=null) {
						// On recherche le nombre de crédits
						BigDecimal credits = getDomainService().getCreditsVetEcts(etape.getCodAnu()
								, etape.getEtape().getCodEtp()
								, etape.getEtape().getCodVrsVet().toString());					
					
						InscriptionVetPojo ins = new InscriptionVetPojo();

						VetPojo vet = new VetPojo();
						vet.setCodEtp(etape.getEtape().getCodEtp());
						vet.setCodVrsVet(etape.getEtape().getCodVrsVet());
						vet.setLibWebVet(etape.getEtape().getLibWebVet());
						vet.setCredits(credits);
						ins.setVet(vet);
						ins.setAnnee(etape.getCodAnu());
						
						inscriptions.add(ins);
					}
				}
				
			}
		}

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
			ComposanteOrganisatriceDTO cmpOrga = getDomainService().getCmpOrgaVet(vetSelected.getAnnee()
					, vetSelected.getVet().getCodEtp()
					, vetSelected.getVet().getCodVrsVet().toString());
			Boolean trouve = false;
			if (cmpOrga != null) {
				for (String cmp:cmps) {
					if (cmp.equals(cmpOrga.getCodComposante())) {
						trouve = true;
						break;
					}
				}
				if (!trouve) {
					FacesContext.getCurrentInstance().addMessage(null
							, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
									,getI18nService().getString("ECTS.MESSAGE.PAS_HABILITE_COMPO_ORGA")
									 + cmpOrga.getLibComposante()));
					return null;
				}
			}
		}

		// On vérifie que la VET porte des crédits ECTS
		BigDecimal credits = vetSelected.getVet().getCredits();
		if (credits==null || credits.compareTo(new BigDecimal(0))==0) {
			FacesContext.getCurrentInstance().addMessage(null
					, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
							,getI18nService().getString("ECTS.MESSAGE.ETAPE_SANS_ECTS")));
			return null;
		}
		
		// On vérifie que l'utilisateur gestionnaire est autorisé à consulter
		if (getCurrentUser().getType().equalsIgnoreCase(SessionController.USER_GESTIONNAIRE)) {
			Set<String> cmps = getCurrentUser().getUtilisateurApo().getCmps();
			ComposanteOrganisatriceDTO cmpOrga = getDomainService().getCmpOrgaVet(vetSelected.getAnnee()
					, vetSelected.getVet().getCodEtp(), vetSelected.getVet().getCodVrsVet().toString());
			Boolean trouve = false;
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
		}

		
		// On vérifie que la VET est délibérée
		List<GrpResultatVetDTO> ldelibs = getDomainService().getDelibsVet(vetSelected.getAnnee()
				, vetSelected.getVet().getCodEtp(), vetSelected.getVet().getCodVrsVet());
		for (GrpResultatVetDTO d : ldelibs) {
			if ((!d.getEtaAvcVet().equalsIgnoreCase("T"))&&(d.getCodAdm().equalsIgnoreCase("1"))) {
				FacesContext.getCurrentInstance().addMessage(null
						, new FacesMessage(getI18nService().getString("ECTS.MESSAGE.EDIT_IMPOSSIBLE")
								,getI18nService().getString("ECTS.MESSAGE.DELIB_NON_TERMINEE")));
				return null;
			}
		}
				
		// Si tout est OK, on calcule le relevé de notes ECTS
		Etudiant[] etudiantsSelected = new Etudiant[1];
		etudiantsSelected[0] = etudiantSelected;
		calculatorController.setAnnee(vetSelected.getAnnee());
		calculatorController.setEtudiants(etudiantsSelected);
		calculatorController.setVet(vetSelected.getVet());
		List<EtudiantPojo> etudiantsToPrint = calculatorController.editionSelection();
		
		if (!etudiantsToPrint.isEmpty()) {
			return printController.genereRvn(etudiantsToPrint, vetSelected.getAnnee());
		} else {
			FacesContext.getCurrentInstance().addMessage(null
					, new FacesMessage(FacesMessage.SEVERITY_INFO,getI18nService().getString("ECTS.MESSAGE.ATTENTION")
							,getI18nService().getString("ECTS.MESSAGE.AUCUN_RESULTAT")));
		}

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
	 * @return the inscriptions
	 */
	public List<InscriptionVetPojo> getInscriptions() {
		return inscriptions;
	}

	
	/**
	 * @param inscriptions the inscriptions to set
	 */
	public void setInscriptions(List<InscriptionVetPojo> inscriptions) {
		this.inscriptions = inscriptions;
	}

	
	/**
	 * @return the vetSelected
	 */
	public InscriptionVetPojo getVetSelected() {
		return vetSelected;
	}


	/**
	 * @param vetSelected the vetSelected to set
	 */
	public void setVetSelected(InscriptionVetPojo vetSelected) {
		this.vetSelected = vetSelected;
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
	 * @return the critCodEtu
	 */
	public String getCritCodEtu() {
		return critCodEtu;
	}

	/**
	 * @param critCodEtu the critCodEtu to set
	 */
	public void setCritCodEtu(String critCodEtu) {
		this.critCodEtu = critCodEtu;
	}

	/**
	 * @return the critNomEtu
	 */
	public String getCritNomEtu() {
		return critNomEtu;
	}

	/**
	 * @param critNomEtu the critNomEtu to set
	 */
	public void setCritNomEtu(String critNomEtu) {
		this.critNomEtu = critNomEtu;
	}

	/**
	 * @return the critDateNaisEtu
	 */
	public String getCritDateNaisEtu() {
		return critDateNaisEtu;
	}

	/**
	 * @param critDateNaisEtu the critDateNaisEtu to set
	 */
	public void setCritDateNaisEtu(String critDateNaisEtu) {
		this.critDateNaisEtu = critDateNaisEtu;
	}

	/**
	 * @return the etudiantSelected
	 */
	public Etudiant getEtudiantSelected() {
		return etudiantSelected;
	}

	/**
	 * @param etudiantSelected the etudiantSelected to set
	 */
	public void setEtudiantSelected(Etudiant etudiantSelected) {
		this.etudiantSelected = etudiantSelected;
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
	 * @param ldapService the ldapService to set
	 */
	public void setLdapService(LdapUserService ldapService) {
		this.ldapService = ldapService;
	}


}
