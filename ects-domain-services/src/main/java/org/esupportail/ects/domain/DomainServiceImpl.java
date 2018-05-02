/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.ects.domain;


import com.googlecode.ehcache.annotations.Cacheable;
import gouv.education.apogee.commun.client.utils.WSUtils;
import gouv.education.apogee.commun.client.ws.etudiantmetier.EtudiantMetierServiceInterface;
import gouv.education.apogee.commun.client.ws.offreformationmetier.OffreFormationMetierServiceInterface;
import gouv.education.apogee.commun.client.ws.pedagogiquemetier.PedagogiqueMetierServiceInterface;
import gouv.education.apogee.commun.client.ws.referentielmetier.ReferentielMetierServiceInterface;
import gouv.education.apogee.commun.transverse.dto.WSReferentiel.recupererSignataire.SignataireWSSignataireDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.EtudiantCritereDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.EtudiantCritereListeDTO;
import gouv.education.apogee.commun.transverse.dto.etudiant.EtudiantDTO2;
import gouv.education.apogee.commun.transverse.dto.etudiant.InfoAdmEtuDTO;
import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.*;
import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.ElementPedagogiDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.*;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EtapeDTO;
import gouv.education.apogee.commun.transverse.exception.WebBaseException;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.ects.domain.beans.Etudiant;
import org.esupportail.ects.domain.beans.UtilisateurApo;
import org.esupportail.ects.utils.CacheModelConst;
import org.esupportail.wssi.services.remote.*;
import org.esupportail.wssi.services.remote.ResultatElpDTO;
import org.esupportail.wssi.services.remote.ResultatVetDTO;
import org.esupportail.wssi.services.remote.VersionDiplomeDTO;
import org.esupportail.wssi.services.remote.VersionEtapeDTO;
import org.springframework.beans.factory.InitializingBean;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of DomainService.
 * 
 * See /properties/domain/domain.xml
 */
public class DomainServiceImpl implements DomainService, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8200845058340254019L;
	
	/**
	 * Type objet résultat VET.
	 */
	private static final String OBJ_RES_VET = "VET";
	
	/**
	 * wsdl du webservice Enseignement
	 */
	private String wsdlEns;

	/**
	 * wsdl du webservice Pedagogique
	 */
	private String wsdlPeda;

	/**
	 * wsdl du webservice Referentiel
	 */
	private String wsdlRef;

	/**
	 * 
	 */
	private ReadReferentiel readReferentiel;
	
	/**
	 * 
	 */
	private ReadEnseignement readEnseignement;
	
	/**
	 * 
	 */
	private ReadPedagogique readPedagogique;
	
	/**
	 * A logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public DomainServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("in afterPropertiesSet");
		Assert.notNull(this.wsdlEns, 
				"property wsdlEns of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.wsdlPeda, 
				"property wsdlPeda of class " + this.getClass().getName() + " can not be null");
		Assert.notNull(this.wsdlRef, 
				"property wsdlRef of class " + this.getClass().getName() + " can not be null");
		
		try {
			ReadReferentielImplService readReferentielService
				= new ReadReferentielImplService(new URL(wsdlRef + "?wsdl"));
			ReadEnseignementImplService readEnseignementService 
				= new ReadEnseignementImplService(new URL(wsdlEns + "?wsdl"));
			ReadPedagogiqueImplService readPedagogiqueService 
				= new ReadPedagogiqueImplService(new URL(wsdlPeda + "?wsdl"));

			readReferentiel = readReferentielService.getReadReferentielImplPort();
			readEnseignement = readEnseignementService.getReadEnseignementImplPort();
			readPedagogique = readPedagogiqueService.getReadPedagogiqueImplPort();

			final HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setAutoRedirect(true);

			((HTTPConduit) ClientProxy.getClient(readReferentiel).getConduit()).setClient(httpClientPolicy);
			((HTTPConduit) ClientProxy.getClient(readEnseignement).getConduit()).setClient(httpClientPolicy);
			((HTTPConduit) ClientProxy.getClient(readPedagogique).getConduit()).setClient(httpClientPolicy);

}
		catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * @see org.esupportail.ects.domain.DomainService#getUtilisateurApo(java.lang.String)
	 */
	@Override
	public UtilisateurApo getUtilisateurApo(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getUtilisateurApo(" + id + ")");
		}
		
		// codEtp, libWebvet, codCge, dateRct
		UtilisateurDTO userApo = readReferentiel.getUtilisateur(id.toUpperCase());
		if (userApo!=null) {
			logger.info("userApo = " + userApo.getCodUti());
		} else {
			logger.info("userApo = null");
		}
		
		if (userApo!=null) {
			UtilisateurApo util = new UtilisateurApo();
			util.setCodCge(userApo.getCodCge());
			util.setCodTut(userApo.getCodTut());
			util.setCodUti(userApo.getCodUti());
			for (UtiCmp ucmp:userApo.getUtiCmps()) {
				util.getCmps().add(ucmp.getCodCmp());
			}
			util.setLibCmtUti(userApo.getLibCmtUti());
			for (UtiCollecterCtn ucc:userApo.getUtiCollecterCtns()) {
				util.getCtns().add(ucc.getCodCtn());
			}
			return util;
		}
		
		return null;

	}


	/**
	 * @see org.esupportail.ects.domain.DomainService#getAnneeUnivs()
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.REF_DEFAULT_MODEL)
	public List<AnneeUniDTO> getAnneeUnivs() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getAnneeUnivs()");
		}
		
		CritereAnneeUniDTO critere = new CritereAnneeUniDTO();
		critere.setEtatIAE("");
		critere.setEtatIPE("");
		critere.setEtatRES("O");
		return readEnseignement.anneeUniDTO2(critere);
		
	}

	
	/**
	 * @see org.esupportail.ects.domain.DomainService#getVersionEtapes(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.ENS_DEFAULT_MODEL)
	public List<VersionEtapeDTO> getVersionEtapes(String codEtp, String libWebVet, String codCge, String annee) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getVersionEtapes(" + libWebVet + "," + codCge + "," + annee + ")");
		}

		// codEtp, libWebvet, codCge, dateRct
		return readEnseignement.getVersionEtapes1(codEtp, libWebVet, codCge, annee);

	}

	
	/**
	 * @see org.esupportail.ects.domain.DomainService#getVersionDiplome(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.ENS_DEFAULT_MODEL)
	public VersionDiplomeDTO getVersionDiplome(String anneeRct, String codEtp,
			String codeVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getVersionDiplome(" + anneeRct + "," + codEtp + "," + codeVrsVet + ")");
		}

		// codEtp, libWebvet, codCge, dateRct
		return readEnseignement.getVersionDiplome2(anneeRct, codEtp, new Integer(codeVrsVet));
	}



	/**
	 * @see org.esupportail.ects.domain.DomainService#getResultVetDTO(java.lang.String, java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public List<ResultatVetDTO> getResultVetDTO(String annee, String codEtp,
			String codeVrsVet, Boolean resultatObtenu) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getResultVetDTO(" + annee + "," + codEtp + "," 
				+ codeVrsVet + "," + resultatObtenu + ")");
		}

		CritereResultatVetDTO critere = new CritereResultatVetDTO();
		AnneeUniDTO anneeUniv = new AnneeUniDTO();
		anneeUniv.setCodAnu(annee);
		critere.setAnneeUni(anneeUniv);
		critere.setCodEtp(codEtp);
		critere.setCodVrsVet(new Integer(codeVrsVet));
		critere.setResultatObtenu(resultatObtenu);
		
		return readPedagogique.getResultVetDTO(critere);

	}

	
	
	/**
	 * @see org.esupportail.ects.domain.DomainService#getResultElpDTO(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public List<ResultatElpDTO> getResultElpDTO(String annee, String codElp,
			Boolean resultatObtenu) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getResultElpDTO(" + annee + "," + codElp + "," 
				+ resultatObtenu + ")");
		}

		CritereResultatElpDTO critere = new CritereResultatElpDTO();
		AnneeUniDTO anneeUniv = new AnneeUniDTO();
		anneeUniv.setCodAnu(annee);
		critere.setAnneeUni(anneeUniv);
		critere.setCodElp(codElp);
		critere.setResultatObtenu(resultatObtenu);
		
		return readPedagogique.getResultElpDTO(critere);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public List<Etudiant> getEtudiants(String annee, String codDip,
			String codVrsVdi, String codEtp, String codVrsVet, String typres) {

		if (logger.isDebugEnabled()) {
			logger.debug("entering getEtudiants( " + annee + ", " + codDip + ", " + codVrsVdi 
					+ ", " + codEtp + ", " + codVrsVet + "," + typres + " )");
		}
		EtudiantDTO2[] etudiants;
		try {
			EtudiantMetierServiceInterface etudiantMetierService
			=  (EtudiantMetierServiceInterface) WSUtils.getService(WSUtils.ETUDIANT_SERVICE_NAME);

			// Recherche les étudiants dans Apogee
			EtudiantCritereDTO critere = new EtudiantCritereDTO();
			
			EtudiantCritereListeDTO[] diplomes = new EtudiantCritereListeDTO[1];
			EtudiantCritereListeDTO[] etapes = new EtudiantCritereListeDTO[1];

			diplomes[0] = new EtudiantCritereListeDTO();
			diplomes[0].setCode(codDip);
			String[] lVrsVdi = new String[1];
			lVrsVdi[0]=codVrsVdi;
			diplomes[0].setListVersion(lVrsVdi);

			etapes[0] = new EtudiantCritereListeDTO();
			etapes[0].setCode(codEtp);
			String[] lVrsVet = new String[1];
			lVrsVet[0]=codVrsVet;
			etapes[0].setListVersion(lVrsVet);

			critere.setAnnee(annee);
			critere.setListDiplomes(diplomes);
			critere.setListEtapes(etapes);
			
			//Filtre par type de résultats positif/négatif à la VET
			if (typres!=null && !typres.equals("")) {
				critere.setObjetResultat(OBJ_RES_VET);
				critere.setTypeResultat(typres);
			}
			
			etudiants = etudiantMetierService.recupererListeEtudiants(critere);
			
			logger.debug("etudiants.length="+etudiants.length);
			
		} catch (RemoteException e) {
			logger.error("RemoteException in getEtudiants( " + annee + ", " + codDip + ", " + codVrsVdi 
					+ ", " + codEtp + ", " + codVrsVet + "," + typres + " ) = " + e);
			return null;
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getEtudiants( " + annee + ", " + codDip + ", " + codVrsVdi 
					+ ", " + codEtp + ", " + codVrsVet + "," + typres + " ) = " + e);
			return null;
		}
		
		List<Etudiant> letu = new ArrayList<Etudiant>();
		for (EtudiantDTO2 e : etudiants) {
			if (e!=null) {
				Etudiant etu = new Etudiant();
				etu.setCodEtu(e.getCodEtu());
				etu.setNumeroIne(e.getNumeroIne());
				etu.setNom(e.getNom());
				etu.setPrenom(e.getPrenom());
				etu.setDateNaissance(e.getDateNaissance());
				letu.add(etu);
			}
		}
		Collections.sort(letu);
		return letu;
	}
	
	
	/**
	 * @see org.esupportail.ects.domain.DomainService#getEtudiant(java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.REF_DEFAULT_MODEL)
	public Etudiant getEtudiant(String codEtu) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getEtudiant( " + codEtu + " )");
		}

		try {
			EtudiantMetierServiceInterface etudiantMetierService
			=  (EtudiantMetierServiceInterface) WSUtils.getService(WSUtils.ETUDIANT_SERVICE_NAME);

			// Recherche l'étudiant dans Apogee
			InfoAdmEtuDTO etudiant = etudiantMetierService.recupererInfosAdmEtu(codEtu);
			
			if (etudiant!=null) {
				Etudiant etu = new Etudiant();
				etu.setCodEtu(codEtu);
				etu.setDateNaissance(DateFormat.getDateInstance(
						DateFormat.SHORT).format(etudiant.getDateNaissance()));
				etu.setNom(etudiant.getNomPatronymique());
				etu.setNumeroIne(etudiant.getNumeroINE());
				etu.setPrenom(etudiant.getPrenom1());
			
				return etu;
			}
						
		} catch (RemoteException e) {
			logger.error("RemoteException in getEtudiant( " + codEtu + " ) = " + e);
			return null;
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getEtudiant( " + codEtu + " ) = " + e);
			return null;
		}
		
		return null;
		
	}

	

	/**
	 * @see org.esupportail.ects.domain.DomainService#getResultatsVetEtu(java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public ContratPedagogiqueResultatVdiVetDTO[] getResultatsVetEtu(String annee, String codEtu) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getResultatsVetEtu( " + annee + ", " + codEtu + " )");
		}
		ContratPedagogiqueResultatVdiVetDTO[] contrat;
		try {
			PedagogiqueMetierServiceInterface pedagogiqueMetierService
			=  (PedagogiqueMetierServiceInterface) WSUtils.getService(WSUtils.PEDAGOGIQUE_SERVICE_NAME);

			// Recherche les résultats de l'étudiant dans Apogee
			// recupererContratPedagogiqueResultatVdiVet(
			//		_codEtu String (8),_codAnu String (4), 
			//      _sourceRes String, _etatDelib String (max 3),
			//      _codSes String, _typeRes String)
			contrat = pedagogiqueMetierService.recupererContratPedagogiqueResultatVdiVet(codEtu, annee, "Apogee", "T", "toutes", "tous");

		} catch (RemoteException e) {
			logger.error("RemoteException in getResultatsVetEtu(" + annee + ", " + codEtu + ") = " + e);
			return null;
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getResultatsVetEtu(" + annee + ", " + codEtu + ") = " + e);
			return null;
		}
		
		return contrat;
	
	}

	/**
	 * @see org.esupportail.ects.domain.DomainService#getResultatsVetEtu(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public EtapeResVdiVetDTO getResultatsVetEtu(String annee, String codEtu,
			String codEtp, String codVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getResultatsVetEtu( " + annee + ", " + codEtu + 
					"," + codEtp + "," + codVrsVet + ")");
		}
		ContratPedagogiqueResultatVdiVetDTO[] contrat;
		EtapeResVdiVetDTO res = null;
		try {
			PedagogiqueMetierServiceInterface pedagogiqueMetierService
			=  (PedagogiqueMetierServiceInterface) WSUtils.getService(WSUtils.PEDAGOGIQUE_SERVICE_NAME);

			// Recherche les résultats de l'étudiant dans Apogee
			// recupererContratPedagogiqueResultatVdiVet(
			//		_codEtu String (8),_codAnu String (4), 
			//      _sourceRes String, _etatDelib String (max 3),
			//      _codSes String, _typeRes String)
			contrat = pedagogiqueMetierService.recupererContratPedagogiqueResultatVdiVet(codEtu, annee, "Apogee", "T", "toutes", "tous");
			
			for (int i = 0; i < contrat.length; i++) {
				if (contrat[i].getEtapes()!=null) {
					for (int j = 0; j < contrat[i].getEtapes().length; j++) {
						if (contrat[i].getEtapes()[j]!=null) {
							if (contrat[i].getEtapes()[j].getCodEtaIae()!= null 
									&& !contrat[i].getEtapes()[j].getCodEtaIae().equals("A")){
								EtapeDTO etape = contrat[i].getEtapes()[j].getEtape();
								if ((etape.getCodEtp().equalsIgnoreCase(codEtp))
									&& (etape.getCodVrsVet().equals(new Integer(codVrsVet)))) {
									res = contrat[i].getEtapes()[j];
									break;
								}
							}
						}
					}
				}
			}
			

		} catch (RemoteException e) {
			logger.error("RemoteException in getResultatsVetEtu( " + annee + ", " + codEtu + 
					"," + codEtp + "," + codVrsVet + ") = " + e);
			return null;
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getResultatsVetEtu( " + annee + ", " + codEtu + 
					"," + codEtp + "," + codVrsVet + ") = " + e);
			return null;
		}
		
		return res;
	
	}

	/**
	 * @see org.esupportail.ects.domain.DomainService#getResultatsElpEtu(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public ContratPedagogiqueResultatElpEprDTO5[] getResultatsElpEtu(
			String annee, String codEtu, String codEtp, String codVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getResultatsElpEtu( " + annee + ", " + codEtu + ", " + codEtp + ", " + codVrsVet + " )");
		}
		
		ContratPedagogiqueResultatElpEprDTO5[] contrat = null;
		try {
			PedagogiqueMetierServiceInterface pedagogiqueMetierService
			=  (PedagogiqueMetierServiceInterface) WSUtils.getService(WSUtils.PEDAGOGIQUE_SERVICE_NAME);

			// Recherche les résultats de l'étudiant dans Apogee
			// recupererContratPedagogiqueResultatElpEpr_v2(
			//		_codEtu, _codAnu, _codEtp,
			//		_codVrsVet, _sourceRes, _etatDelib,
			//		_codSes, _typeRes)
			contrat = pedagogiqueMetierService.recupererContratPedagogiqueResultatElpEpr_v6(codEtu, annee, codEtp, codVrsVet, "Apogee", "T", "toutes", "tous", "E");

		} catch (RemoteException e) {
			logger.error("RemoteException in getResultatsElpEtu( " 
					+ annee + ", " + codEtu + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
			return null;
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getResultatsElpEtu( " 
					+ annee + ", " + codEtu + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
			return null;
		}
		
		return contrat;
	}


	@Override
	@Cacheable(cacheName = CacheModelConst.REF_DEFAULT_MODEL)
	public List<ElementPedagogiDTO2> getElpsRattachesVet(String annee, String codEtp, String codVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getElpsRattachesVet( " + annee + ", " + codEtp + ", " + codVrsVet + " )");
		}
		
		DiplomeDTO3[] res = null;

		List<ElementPedagogiDTO2> lelp = new ArrayList<ElementPedagogiDTO2>();

		try {
			OffreFormationMetierServiceInterface offreFormationMetierService
			=  (OffreFormationMetierServiceInterface) WSUtils.getService(WSUtils.OFFREFORMATION_SERVICE_NAME);

			// Recherche de la SE de la VET dans Apogee
			SECritereDTO2 critere = new SECritereDTO2();
			critere.setCodAnu(annee);
			critere.setCodEtp(codEtp);
			critere.setCodVrsVet(codVrsVet);
			critere.setCodDip("aucun");
			critere.setCodElp("tous");
			critere.setCodVrsVdi("aucun");
			res = offreFormationMetierService.recupererSE_v3(critere);
			
			try {
				VersionDiplomeDTO3[] vdis = res[0].getListVersionDiplome();
				VersionEtapeDTO3[] vets = vdis[0].getOffreFormation().getListEtape()[0].getListVersionEtape();
				if ((vets[0]!=null)) {
					ListeElementPedagogiDTO2[] listesElps = vets[0].getListListeElementPedagogi();
					for (int i=0; i<listesElps.length; i++) {
						ElementPedagogiDTO2[] listeElps = listesElps[i].getListElementPedagogi();
						for (int j=0; j<listeElps.length; j++) {
							ElementPedagogiDTO2 elp = listeElps[j];
							
							// on ne garde que les éléments portant des crédits ECTS
							if (elp.getCredits()!=null) {
								lelp.add(elp);
							}
						}
						
					}
				}
				
			} catch (NullPointerException e) {
				return null;
			}
			

		} catch (RemoteException e) {
			logger.error("RemoteException in getCreditsVetEcts( " 
					+ annee + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getCreditsVetEcts( " 
					+ annee + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
		}
		
		return lelp;
	}

	/**
	 * @see org.esupportail.ects.domain.DomainService#getCreditsVetEcts(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.REF_DEFAULT_MODEL)
	public BigDecimal getCreditsVetEcts(String annee, String codEtp, String codVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getCreditsVetEcts( " + annee + ", " + codEtp + ", " + codVrsVet + " )");
		}
		
		DiplomeDTO3[] res = null;
		try {
			OffreFormationMetierServiceInterface offreFormationMetierService
			=  (OffreFormationMetierServiceInterface) WSUtils.getService(WSUtils.OFFREFORMATION_SERVICE_NAME);

			// Recherche de la SE de la VET dans Apogee
			SECritereDTO2 critere = new SECritereDTO2();
			critere.setCodAnu(annee);
			critere.setCodEtp(codEtp);
			critere.setCodVrsVet(codVrsVet);
			critere.setCodDip("aucun");
			critere.setCodElp("aucun");
			critere.setCodVrsVdi("aucun");
			res = offreFormationMetierService.recupererSE_v3(critere);

			try {
				VersionDiplomeDTO3[] vdis = res[0].getListVersionDiplome();
				VersionEtapeDTO3[] vets = vdis[0].getOffreFormation().getListEtape()[0].getListVersionEtape();
				if ((vets[0]!=null)) {
					return vets[0].getCredits();
				}
				
			} catch (NullPointerException e) {
				return null;
			}
			

		} catch (RemoteException e) {
			logger.error("RemoteException in getCreditsVetEcts( " 
					+ annee + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getCreditsVetEcts( " 
					+ annee + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
		}
		
		return null;
	}

	
	/**
	 * @see org.esupportail.ects.domain.DomainService#getCmpOrgaVet(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.REF_DEFAULT_MODEL)
	public ComposanteOrganisatriceDTO getCmpOrgaVet(String annee, String codEtp, String codVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getCmpOrgaVet( " + annee + ", " + codEtp + ", " + codVrsVet + " )");
		}
		
		DiplomeDTO3[] res = null;
		try {
			OffreFormationMetierServiceInterface offreFormationMetierService
			=  (OffreFormationMetierServiceInterface) WSUtils.getService(WSUtils.OFFREFORMATION_SERVICE_NAME);

			// Recherche de la SE de la VET dans Apogee
			SECritereDTO2 critere = new SECritereDTO2();
			critere.setCodAnu(annee);
			critere.setCodEtp(codEtp);
			critere.setCodVrsVet(codVrsVet);
			critere.setCodDip("aucun");
			critere.setCodElp("aucun");
			critere.setCodVrsVdi("aucun");
			res = offreFormationMetierService.recupererSE_v3(critere);

			try {
				VersionDiplomeDTO3[] vdis = res[0].getListVersionDiplome();
				VersionEtapeDTO3[] vets = vdis[0].getOffreFormation().getListEtape()[0].getListVersionEtape();
				if ((vets[0]!=null)) {
					return vets[0].getComposante();
				}
				
			} catch (NullPointerException e) {
				return null;
			}
			

		} catch (RemoteException e) {
			logger.error("RemoteException in getCmpOrgaVet( " 
					+ annee + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getCmpOrgaVet( " 
					+ annee + ", " + codEtp + ", " + codVrsVet + " ) = " + e);
		}
		
		return null;
	}

	/**
	 * @see org.esupportail.ects.domain.DomainService#getSignataires()
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.REF_DEFAULT_MODEL)
	public SignataireWSSignataireDTO[] getSignataires() {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getSignataires()");
		}
		
		SignataireWSSignataireDTO[] res = null;
		try {
			ReferentielMetierServiceInterface referentielMetierService
			=  (ReferentielMetierServiceInterface) WSUtils.getService(WSUtils.REFERENTIEL_SERVICE_NAME);

			// Recherche des signataires dans Apogee
			// recupererSignataire( _codSig, _temoinEnService)
			res = referentielMetierService.recupererSignataire("", "O");

		} catch (RemoteException e) {
			logger.error("RemoteException in getSignataires() = " + e);
			return null;
		} catch (WebBaseException e) {
			logger.error("WebBaseException in getSignataires() = " + e);
			return null;
		}
		
		return res;
	}
	
	/**
	 * @see org.esupportail.ects.domain.DomainService#getDelibsVet(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	@Cacheable(cacheName = CacheModelConst.PEDA_DEFAULT_MODEL)
	public List<GrpResultatVetDTO> getDelibsVet(String annee, String codEtp,
			Integer codVrsVet) {
		if (logger.isDebugEnabled()) {
			logger.debug("entering getDelibsVet(" + annee + "," + codEtp + "," 
				+ codVrsVet + ")");
		}

		return readPedagogique.getGrpResultVetDTO(annee, codEtp, codVrsVet);
	}


	//////////////////////////////////////////////////////////////
	// Misc
	//////////////////////////////////////////////////////////////

	/**
	 * @param wsdlEns 
	 */
	public void setWsdlEns(String wsdlEns) {
		this.wsdlEns = wsdlEns;
	}

	/**
	 * @param wsdlPeda the wsdlPeda to set
	 */
	public void setWsdlPeda(String wsdlPeda) {
		this.wsdlPeda = wsdlPeda;
	}

	/**
	 * @param wsdlRef the wsdlRef to set
	 */
	public void setWsdlRef(String wsdlRef) {
		this.wsdlRef = wsdlRef;
	}

}
