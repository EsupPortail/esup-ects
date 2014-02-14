/**
 * 
 */
package org.esupportail.ects.web.controllers;

import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.ElementPedagogiDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatElpEprDTO4;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EtapeResVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatElpDTO3;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//import net.sf.ehcache.CacheManager;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.ects.domain.beans.Etudiant;
import org.esupportail.ects.utils.CacheModelConst;
import org.esupportail.ects.web.beans.CalculElp;
import org.esupportail.ects.web.beans.CalculVet;
import org.esupportail.ects.web.beans.ResultatElp;
import org.esupportail.ects.web.beans.ResultatVet;
import org.esupportail.ects.web.beans.pojo.ElpPojo;
import org.esupportail.ects.web.beans.pojo.EtudiantPojo;
import org.esupportail.ects.web.beans.pojo.NoteEctsPojo;
import org.esupportail.ects.web.beans.pojo.VetPojo;
import org.esupportail.ects.web.controllers.AbstractContextAwareController;
import org.esupportail.wssi.services.remote.ResultatElpDTO;
import org.esupportail.wssi.services.remote.ResultatVetDTO;

import com.googlecode.ehcache.annotations.Cacheable;


/**
 * @author gmartel
 * Calculator : effectue les calculs pour la génération d'un relevé de notes ECTS.
 */
public class CalculatorController extends AbstractContextAwareController {

	
	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * the serialization id. 
	 */
	private static final long serialVersionUID = -3457399692611064188L;
	
	/**
	 * Cache
	 */
//	private CacheManager cacheManager;

	/**
	 * Liste des natures affichées
	 */
	private List<String> naturesAffichees;

	/**
	 * Les etudiants à imprimer.
	 */
	private Etudiant[] etudiants;
		
	/**
	 * La VET.
	 */
	private VetPojo vet;
	
	/**
	 * L'année universitaire.
	 */
	private String annee;
	
	/**
	 * Pour le calcul de l'Echelle de notation des différents ELP rattachés à la VET
	 */
	private List<CalculElp> elpsFromVet;
	
	/**
	 * Nombre d'étudiants en dessous duquel on considère le résultat non significatif
	 */
	private Integer nbEtuSignificatif;


	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	
	/*
	 ******************* INIT ************************* */
	
	/**
	 * Constructors.
	 */
	public CalculatorController() {
		super();
	}

	
	/**
 	* @see org.esupportail.ects.web.controllers.AbstractAccessController#afterPropertiesSetInternal()
	*/
	@Override
	public void afterPropertiesSetInternal() {
		super.afterPropertiesSetInternal();
		Assert.notNull(this.naturesAffichees, 
				"property naturesAffichees of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.nbEtuSignificatif, 
				"property nbEtuSignificatif of class " + this.getClass().getName() + " can not be null");
	}
	
	/*
	 ******************* METHODS ********************** */
	
	
	/**
	 * Méthode renvoyant les calculs d'échelle de notation pour un ELP
	 * @param elpPojo 
	 * @return calculElp (en cache si deja existant)
	 * 
	 */
	@Cacheable(cacheName = CacheModelConst.CTRL_DEFAULT_MODEL)
	public CalculElp getNewCalculElp(ElpPojo elpPojo) {
		List<ResultatElpDTO> resElp = getDomainService().getResultElpDTO(
				annee, elpPojo.getCodElp(), null);
		
		CalculElp calculElp = new CalculElp();
		calculElp.setElp(elpPojo);
		calculElp.setNotes(new ArrayList<BigDecimal>());
		int nbNotes = 0;
		if (resElp!=null) {
			for (ResultatElpDTO res : resElp) {
				if (res!=null) {
					BigDecimal note = new BigDecimal(0);
					if (res.getNotElp()!=null) {
						note = res.getNotElp();
						if (res.getNotPntJurElp()!=null) {
							note = note.add(res.getNotPntJurElp());
						}
						// On ne garde que les résultats supérieurs à la moyenne
						BigDecimal ratio = note.divide(
								new BigDecimal(res.getBarNotElp()),MathContext.DECIMAL128);
						if (ratio.compareTo(new BigDecimal(0.5))>=0) {
							nbNotes++;
							calculElp.getNotes().add(note);
						}
					}
				}
			}
		}
		calculElp.setNbInscrits(new Integer(nbNotes));
									
		// Calcul de l'échelle de notation
		calculElp.calculEchelle(nbEtuSignificatif);
		return calculElp;
	}
	
	
	/**
	 * Récupère les informations nécessaires à la construction du relevé de notes
	 * @return liste d'étudiants avec leurs résultats ects 
	 */
	public List<EtudiantPojo> editionSelection() {
		logger.debug("entering editionSelection()");
		
		if (elpsFromVet==null) {
			elpsFromVet = new ArrayList<CalculElp>();
		}
		// On récupère les notes à la VET et on crée l'objet CalculVet
		List<ResultatVetDTO> resVet = getDomainService().getResultVetDTO(
				annee, vet.getCodEtp(), vet.getCodVrsVet().toString(), true);
		
		CalculVet calculVet = new CalculVet();
		calculVet.setVet(vet);
		calculVet.setNotes(new ArrayList<BigDecimal>());
		int nbNotesVet = 0;
		if (resVet!=null) {
			for (ResultatVetDTO res : resVet) {
				if (res!=null) {
					BigDecimal note = new BigDecimal(0);
					if (res.getNotVet()!=null) {
						note = res.getNotVet();
						if (res.getNotPntJurVet()!=null) {
							note = note.add(res.getNotPntJurVet());
						}
					}
					calculVet.getNotes().add(note);
					nbNotesVet++;
				}
			}
		}
		calculVet.setNbInscrits(new Integer(nbNotesVet));
		
		// Calcul de l'échelle de notation
		calculVet.calculEchelle(nbEtuSignificatif);
				
		List<EtudiantPojo> etudiantsToPrint = new ArrayList<EtudiantPojo>();

		// Pour chaque étudiant, on récupère le contrat pédagogique (ELP de nature SEM + UE avec notes)
		for (Etudiant etu:etudiants) {

			if (etu!=null) {

				// RESULTATS A L'ETAPE
				EtapeResVdiVetDTO etapeRes = getDomainService().getResultatsVetEtu(annee, etu.getCodEtu(), vet.getCodEtp(), vet.getCodVrsVet().toString());
				ResultatVet r = new ResultatVet();
				vet.setTemoinSessionUnique("N");
				r.setVet(vet);
				
				if (etapeRes!=null) {
					gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatVetDTO[] resultatsEtape = etapeRes.getResultatVet();
					
					if (resultatsEtape!=null) {
		
						for (int i=0; i < resultatsEtape.length; i++) {
							gouv.education.apogee.commun.transverse.dto.pedagogique.ResultatVetDTO resEtape = resultatsEtape[i];
							if (resEtape == null) {
								continue;
							}
							
							int codsesvet = 0;
							if(resEtape.getSession() != null){
								codsesvet = new Integer(resEtape.getSession().getCodSes());
							}
			
							String result = null;
			
							//le résultat:
							if (resEtape.getNatureResultat() != null ) {
								result = resEtape.getNatureResultat().getCodAdm();
							}
	
							if (codsesvet < 2) {
								//Session 1 ou session unique
								if (codsesvet == 0) {
									r.getVet().setTemoinSessionUnique("O");
								}
								
								//barème
								if(resEtape.getBarNotVet() != null){
									r.setBareme1(resEtape.getBarNotVet());
								}
			
								// Note
								if (resEtape.getNotVet() != null) {
									BigDecimal note;
									try {
										note = new BigDecimal(resEtape.getNotVet());
										if(resEtape.getNotPntJurVet()!= null && !resEtape.getNotPntJurVet().equals(new BigDecimal(0))){
											r.setNote1(note.add(resEtape.getNotPntJurVet()));
										} else {
											r.setNote1(note);
										}
		
										r.setNoteEcts1(noteCorrespondNoteEctsVet(
												r.getNote1(), calculVet, r.getBareme1()));
									} catch (NumberFormatException e) {
										r.setNote1(null);
										r.setNoteEcts1("");
									}
								}
								
								r.setRes1(result);
								
								// Credits
								r.setCredits1(resEtape.getNbrCrdVet());				
						
							} else {
								//Session 2
								//barème
								if(resEtape.getBarNotVet() != null){
									r.setBareme2(resEtape.getBarNotVet());
								}
			
								// Note
								if (resEtape.getNotVet() != null) {
									BigDecimal note;
									try {
										note = new BigDecimal(resEtape.getNotVet());
										if(resEtape.getNotPntJurVet()!= null && !resEtape.getNotPntJurVet().equals(new BigDecimal(0))){
											r.setNote2(note.add(resEtape.getNotPntJurVet()));
										} else {
											r.setNote2(note);
										}
		
										r.setNoteEcts2(noteCorrespondNoteEctsVet(
												r.getNote2(), calculVet, r.getBareme2()));
									} catch (NumberFormatException e) {
										r.setNote2(null);
										r.setNoteEcts2("");
									}
								}
								
								r.setRes2(result);
								
								// Credits
								r.setCredits2(resEtape.getNbrCrdVet());				
						
							}
						}
					}
				}
				
				
				// RESULTATS AUX ELP
				ContratPedagogiqueResultatElpEprDTO4[] contrat = getDomainService()
					.getResultatsElpEtu(annee, etu.getCodEtu(), vet.getCodEtp(), vet.getCodVrsVet().toString());
				
				if (contrat==null) continue;

				Set<ResultatElp> listeElp = new LinkedHashSet<ResultatElp>();
				Set<String> legende = new HashSet<String>();
		
				// On parcourt le contrat
				for (int i = 0; i < contrat.length; i++ ) {
					// On ne garde que les éléments portant crédits ECTS
					if ((contrat[i].getElp().getNbrCrdElp()!=null)
						&&(contrat[i].getElp().getNbrCrdElp().compareTo(new BigDecimal(0))<=0)) {
						continue;
					}
					
					// On ne garde que les Semestres et les UEs
					if (contrat[i].getElp().getNatureElp()!=null) {
						if (!naturesAffichees.contains(contrat[i].getElp().getNatureElp().getCodNel())) {
							continue;
						}
					}
					
					// Pour chaque ELP, on calcule le CalculElp sauf si déjà calculé
					ElpPojo elpPojo = new ElpPojo(contrat[i].getElp().getCodElp(), contrat[i].getElp().getLibElp());
					CalculElp calculElp = null;
					
					// On vérifie qu'il n'est pas déjà calculé
					boolean trouve = false;
					if ((elpsFromVet!=null)&&(!elpsFromVet.isEmpty())) {
						for (CalculElp c:elpsFromVet) {
							if (c.getElp().equals(elpPojo)) {
								trouve = true;
								calculElp = c;
								break;
							}
						}
					}
					
					
					if (!trouve) {
						calculElp = getNewCalculElp(elpPojo);
						// et on l'ajoute à la liste des elpsFromVet
						elpsFromVet.add(calculElp);
					}
					
					ResultatElp resElp = new ResultatElp();
					resElp.setElp(elpPojo);
					
					
					//On récupère les résultats...
					ResultatElpDTO3[] relpdto = contrat[i].getResultatsElp();
					if (relpdto != null && relpdto.length > 0) {
						//on parcourt les résultats pour l'ELP:
						for (int j = 0; j < relpdto.length; j++ ) {
							
							int codsession = 0;
							if(relpdto[j].getSession() != null){
								codsession = new Integer(relpdto[j].getSession().getCodSes());
							} else {
								// cas des VAC: validation d'acquis
							}

							String result = null;

							//le résultat:
							if (relpdto[j].getTypResultat() != null ) {
								result = relpdto[j].getTypResultat().getCodTre();
							}
							
							if (codsession < 2) {
								//Session 1 ou session unique
								//barème
								if(relpdto[j].getBarNotElp() != null){
									resElp.setBareme1(relpdto[j].getBarNotElp());
								}

								// Note
								if (relpdto[j].getNotElp() != null) {
									BigDecimal note;
									try {
										note = new BigDecimal(relpdto[j].getNotElp());
										if(relpdto[j].getNotPntJurElp()!= null && !relpdto[j].getNotPntJurElp().equals(new BigDecimal(0))){
											resElp.setNote1(note.add(relpdto[j].getNotPntJurElp()));
										} else {
											resElp.setNote1(note);
										}
										
										resElp.setNoteEcts1(noteCorrespondNoteEctsElp(
												resElp.getNote1(), calculElp, resElp.getBareme1()));
									} catch (NumberFormatException e) {
										resElp.setNote1(null);
										resElp.setNoteEcts1("");
										if (relpdto[j].getTypResultat()!=null) {
											resElp.setNoteEcts1(relpdto[j].getTypResultat().getCodTre());
											legende.add(relpdto[j].getTypResultat().getCodTre() + ":"
													+ relpdto[j].getTypResultat().getLibTre());
										}
									}
								}
								
								resElp.setRes1(result);
								
								// Credits
								//if (relpdto[j].getTypResultat().getCodNegTre().equalsIgnoreCase("1")) {
								//	if (relpdto[j].getNbrCrdElp() != null) {
										resElp.setCredits1(relpdto[j].getNbrCrdElp());
								//	}
								//} else {
								//	resElp.setCredits1(new BigDecimal(0));
								//}
									
								
								// Année d'obtention si PRC
								if (relpdto[j].getCodAnu()!=null) {
									Integer annee2 = new Integer(relpdto[j].getCodAnu()) + 1;
									resElp.setAnnee1(relpdto[j].getCodAnu()+"/"+annee2);
								}
							} else {
								//Session 2
								//barème
								if(relpdto[j].getBarNotElp() != null){
									resElp.setBareme2(relpdto[j].getBarNotElp());
								}

								// Note
								if (relpdto[j].getNotElp() != null) {
									BigDecimal note;
									try {
										note = new BigDecimal(relpdto[j].getNotElp());
										if(relpdto[j].getNotPntJurElp()!= null && !relpdto[j].getNotPntJurElp().equals(new BigDecimal(0))){
											resElp.setNote2(note.add(relpdto[j].getNotPntJurElp()));
										} else {
											resElp.setNote2(note);
										}
										
										resElp.setNoteEcts2(noteCorrespondNoteEctsElp(
												resElp.getNote2(), calculElp, resElp.getBareme2()));
									} catch (NumberFormatException e) {
										resElp.setNote2(null);
										resElp.setNoteEcts2("");
										if (relpdto[j].getTypResultat()!=null) {
											resElp.setNoteEcts2(relpdto[j].getTypResultat().getCodTre());
											legende.add(relpdto[j].getTypResultat().getCodTre() + ":"
													+ relpdto[j].getTypResultat().getLibTre());
										}
									}

								} 

								resElp.setRes2(result);
								
								// Credits
								//if (relpdto[j].getTypResultat().getCodNegTre().equalsIgnoreCase("1")) {
								//	if (relpdto[j].getNbrCrdElp() != null) {
										resElp.setCredits2(relpdto[j].getNbrCrdElp());
								//	}
								//} else {
								//	resElp.setCredits2(new BigDecimal(0));
								//}
								
								// Année d'obtention si PRC
								if (relpdto[j].getCodAnu()!=null) {
									Integer annee2 = new Integer(relpdto[j].getCodAnu()) + 1;
									resElp.setAnnee2(relpdto[j].getCodAnu()+"/"+annee2);
								}

							}
							
							// Insertion dans la liste des résultats
							listeElp.add(resElp);
						}

					}
					
				}
				
				// dans tous les cas, on garde les infos pour l'étudiant
				EtudiantPojo etudiantPojo = new EtudiantPojo();
				// L'étudiant
				etudiantPojo.setEtudiant(etu);
				// La VET
				etudiantPojo.setVet(vet);
				// Les résultats à la VET
				etudiantPojo.setRes(r);
				// Les résultats aux éléments
				etudiantPojo.setElps(new ArrayList<ResultatElp>(listeElp));
				// La légende
				etudiantPojo.setLegende(legende);
				
				// et on l'ajoute à la liste des étudiants à imprimer
				etudiantsToPrint.add(etudiantPojo);

			}
		
		}

		return etudiantsToPrint;
	}
	
	
	/**
	 * Récupère les informations nécessaires à la construction
	 *  de la distribution ECTS niveau VET
	 * @return la distribution niveau VET
	 */
	public CalculVet editionDistribVet() {
		logger.debug("entering editionDistribVet()");
		
		// On récupère les notes à la VET et on crée l'objet CalculVet
		List<ResultatVetDTO> resVet = getDomainService().getResultVetDTO(
				annee, vet.getCodEtp(), vet.getCodVrsVet().toString(), true);
		
		CalculVet calculVet = new CalculVet();
		calculVet.setVet(vet);
		calculVet.setNotes(new ArrayList<BigDecimal>());
		int nbNotesVet = 0;
		if (resVet!=null) {
			for (ResultatVetDTO res : resVet) {
				if (res!=null) {
					BigDecimal note = new BigDecimal(0);
					if (res.getNotVet()!=null) {
						note = res.getNotVet();
						if (res.getNotPntJurVet()!=null) {
							note = note.add(res.getNotPntJurVet());
						}
					}
					calculVet.getNotes().add(note);
					nbNotesVet++;
				}
			}
		}
		calculVet.setNbInscrits(new Integer(nbNotesVet));
		
		// Calcul de l'échelle de notation
		calculVet.calculEchelle(nbEtuSignificatif);
		
		return calculVet;
	}
				
	/**
	 * Récupère les informations nécessaires à la construction
	 *  de la distribution ECTS niveau ELP
	 * @return liste des distribution par ELP 
	 */
	public List<CalculElp> editionDistribElp() {
		logger.debug("entering editionDistrib()");
		
		if (elpsFromVet==null) {
			elpsFromVet = new ArrayList<CalculElp>();
		}
				
		// On parcourt les ELP
		List<CalculElp> lcalcElp = null;
		
		List<ElementPedagogiDTO2> lelp = getDomainService().getElpsRattachesVet(annee, vet.getCodEtp(), vet.getCodVrsVet().toString());
		
		if ((lelp!=null)&&(!lelp.isEmpty())) {
			
			lcalcElp = new ArrayList<CalculElp>();
			
			for (ElementPedagogiDTO2 elp: lelp) {
				CalculElp calculElp = new CalculElp();

				ElpPojo elpPojo = new ElpPojo(elp.getCodElp(), elp.getLibElp());

				List<ResultatElpDTO> resElp = getDomainService().getResultElpDTO(
						annee, elpPojo.getCodElp(), null);
				
				calculElp.setElp(elpPojo);
				calculElp.setNotes(new ArrayList<BigDecimal>());
				int nbNotes = 0;
				for (ResultatElpDTO res : resElp) {
					if (res!=null) {
						BigDecimal note = new BigDecimal(0);
						if (res.getNotElp()!=null) {
							note = res.getNotElp();
							if (res.getNotPntJurElp()!=null) {
								note = note.add(res.getNotPntJurElp());
							}
							// On ne garde que les résultats supérieurs à la moyenne
							BigDecimal ratio = note.divide(
									new BigDecimal(res.getBarNotElp()),MathContext.DECIMAL128);
							if (ratio.compareTo(new BigDecimal(0.5))>=0) {
								nbNotes++;
								calculElp.getNotes().add(note);
							}
						}
					}
				}
				calculElp.setNbInscrits(new Integer(nbNotes));
											
				// Calcul de l'échelle de notation
				calculElp.calculEchelle(nbEtuSignificatif);
				
				lcalcElp.add(calculElp);

			}
		}

		return lcalcElp;
	}
	
	
	/**
	 * Retourne la distibution ects correspondant à une note pour un elp
	 * @param note
	 * @param c
	 * @param bareme
	 * @return
	 */
	public String noteCorrespondNoteEctsElp(BigDecimal note, CalculElp c, int bareme) {
		logger.debug("entering noteCorrespondNoteEctsElp(" + note + "," + c + "," + bareme + ")");
		
		if (c.getEchelleNotation()!=null) {
			for (NoteEctsPojo n : c.getEchelleNotation()) {
				/*
				if (logger.isDebugEnabled()) {
					logger.debug("n="+n);
				}
				*/
				if ( ((note.compareTo(n.getNoteMaxi())<=0))
					&& ((note.compareTo(n.getNoteMini())>=0)) ) {

					return n.getCodeDistribution().toString();
				}
			}
		}
		if (bareme!=0) {
			BigDecimal median = new BigDecimal(bareme).divide(new BigDecimal(2), MathContext.DECIMAL128);
			if ( ((note.compareTo(median)==-1))
					&& ((note.compareTo(median.multiply(new BigDecimal(0.8), MathContext.DECIMAL128))==1)
							||(note.compareTo(median.multiply(new BigDecimal(0.8), MathContext.DECIMAL128))==0)) ) {

				return "Fx";
			}
			if (note.compareTo(median.multiply(new BigDecimal(0.8), MathContext.DECIMAL128))==-1) {
				
				return "F";
			}
		}
		
		return getI18nService().getString("ECTS.MESSAGE.NON_SIGNIFICATIF");
	}
	
	
	

	/**
	 * Retourne la distibution ects correspondant à une note pour une VET
	 * @param note
	 * @param c 
	 * @param bareme
	 * @return
	 */
	public String noteCorrespondNoteEctsVet(BigDecimal note, CalculVet c, int bareme) {
		logger.debug("entering noteCorrespondNoteEctsVet(" + note + "," + c + "," + bareme + ")");
		
		if (c.getEchelleNotation()!=null) {
			for (NoteEctsPojo n : c.getEchelleNotation()) {
				/*
				if (logger.isDebugEnabled()) {
					logger.debug("n="+n);
				}
				*/
				if ( ((note.compareTo(n.getNoteMaxi())<=0))
					&& ((note.compareTo(n.getNoteMini())>=0)) ) {

					return n.getCodeDistribution().toString();
				}
			}
		}
		if (bareme!=0) {
			BigDecimal median = new BigDecimal(bareme).divide(new BigDecimal(2), MathContext.DECIMAL128);
			if ( ((note.compareTo(median)==-1))
					&& ((note.compareTo(median.multiply(new BigDecimal(0.8), MathContext.DECIMAL128))==1)
							||(note.compareTo(median.multiply(new BigDecimal(0.8), MathContext.DECIMAL128))==0)) ) {

				return "Fx";
			}
			if (note.compareTo(median.multiply(new BigDecimal(0.8), MathContext.DECIMAL128))==-1) {
				
				return "F";
			}
		}
		
		return "Non significatif";
	}
	
	
	

	/*
	 ******************* ACCESSORS ******************** */
	
	
//	/**
//	 * @return the cacheManager
//	 */
//	public CacheManager getCacheManager() {
//		return cacheManager;
//	}
//
//
//	/**
//	 * @param cacheManager the cacheManager to set
//	 */
//	public void setCacheManager(CacheManager cacheManager) {
//		this.cacheManager = cacheManager;
//	}


	/**
	 * @param naturesAffichees the naturesAffichees to set
	 */
	public void setNaturesAffichees(List<String> naturesAffichees) {
		this.naturesAffichees = naturesAffichees;
	}

	
	/**
	 * @return the naturesAffichees
	 */
	public List<String> getNaturesAffichees() {
		return naturesAffichees;
	}


	/**
	 * @return the etudiantsSelected
	 */
	public Etudiant[] getEtudiants() {
		return etudiants;
	}


	/**
	 * @param etudiantsSelected the etudiantsSelected to set
	 */
	public void setEtudiants(Etudiant[] etudiants) {
		this.etudiants = etudiants;
	}


	/**
	 * @return the vet
	 */
	public VetPojo getVet() {
		return vet;
	}


	/**
	 * @param vet the vet to set
	 */
	public void setVet(VetPojo vet) {
		this.vet = vet;
	}


	/**
	 * @return the annee
	 */
	public String getAnnee() {
		return annee;
	}


	/**
	 * @param annee the annee to set
	 */
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	

	/**
	 * @return the elpsFromVet
	 */
	public List<CalculElp> getElpsFromVet() {
		return elpsFromVet;
	}


	/**
	 * @param elpsFromVet the elpsFromVet to set
	 */
	public void setElpsFromVet(List<CalculElp> elpsFromVet) {
		this.elpsFromVet = elpsFromVet;
	}


	/**
	 * @return the nbEtuSignificatif
	 */
	public Integer getNbEtuSignificatif() {
		return nbEtuSignificatif;
	}


	/**
	 * @param nbEtuSignificatif the nbEtuSignificatif to set
	 */
	public void setNbEtuSignificatif(Integer nbEtuSignificatif) {
		this.nbEtuSignificatif = nbEtuSignificatif;
	}



}
