/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.ects.domain;

import gouv.education.apogee.commun.transverse.dto.WSReferentiel.recupererSignataire.SignataireWSSignataireDTO;
import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.ComposanteOrganisatriceDTO;
import gouv.education.apogee.commun.transverse.dto.offreformation.recupererse.ElementPedagogiDTO2;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatElpEprDTO4;
import gouv.education.apogee.commun.transverse.dto.pedagogique.ContratPedagogiqueResultatVdiVetDTO;
import gouv.education.apogee.commun.transverse.dto.pedagogique.EtapeResVdiVetDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.esupportail.ects.domain.beans.Etudiant;
import org.esupportail.ects.domain.beans.UtilisateurApo;
import org.esupportail.wssi.services.remote.AnneeUniDTO;
import org.esupportail.wssi.services.remote.GrpResultatVetDTO;
import org.esupportail.wssi.services.remote.ResultatElpDTO;
import org.esupportail.wssi.services.remote.ResultatVetDTO;
import org.esupportail.wssi.services.remote.VersionDiplomeDTO;
import org.esupportail.wssi.services.remote.VersionEtapeDTO;

/**
 * The domain service interface.
 * @author gmartel
 *
 */
public interface DomainService extends Serializable {

	
	/**
	 * @return la liste des années universitaires
	 */
	List<AnneeUniDTO> getAnneeUnivs();

	
	/**
	 * @param codEtp
	 * @param libWebVet
	 * @param codCge
	 * @param anneeRct
	 * @return la liste des versions d'étapes pour les critères
	 */
	List<VersionEtapeDTO> getVersionEtapes(String codEtp, String libWebVet, String codCge, String anneeRct);

	
	/**
	 * @param anneeRct
	 * @param codEtp
	 * @param codeVrsVet
	 * @return la version de diplomes d'une VET
	 */
	VersionDiplomeDTO getVersionDiplome(String anneeRct, String codEtp, String codeVrsVet);

	/**
	 * @param annee
	 * @param codEtp
	 * @param codeVrsVet
	 * @param resultatObtenu
	 * @return la liste des résultats à une VET pour une année donnée
	 */
	List<ResultatVetDTO> getResultVetDTO(String annee, String codEtp, String codeVrsVet, Boolean resultatObtenu);


	/**
	 * @param annee
	 * @param codElp
	 * @param resultatObtenu
	 * @return la liste des résultats à une VET pour une année donnée
	 */
	List<ResultatElpDTO> getResultElpDTO(String annee, String codElp, Boolean resultatObtenu);


	/**
	 * @param annee
	 * @param codDip
	 * @param codVrsVdi
	 * @param codEtp
	 * @param codVrsVet
	 * @param typres
	 * @return la liste des etudiants pour les critères
	 */
	List<Etudiant> getEtudiants(String annee, String codDip, String codVrsVdi, String codEtp, String codVrsVet, String typres);
	
	
	/**
	 * @param codEtu
	 * @return les infos administratives d'un étudiant
	 */
	Etudiant getEtudiant(String codEtu);
	
	
	/**
	 * @param annee
	 * @param codEtu
	 * @return la liste des résultats VDI et VET pour un étudiant et une année donnée
	 */
	ContratPedagogiqueResultatVdiVetDTO[] getResultatsVetEtu(String annee, String codEtu);
	
	/**
	 * @param annee
	 * @param codEtu
	 * @param codEtp
	 * @param codVrsVet
	 * @return la liste des résultats VET pour un étudiant, une VET et une année
	 */
	EtapeResVdiVetDTO getResultatsVetEtu(String annee, String codEtu, String codEtp, String codVrsVet);
	
	/**
	 * @param annee
	 * @param codEtu
	 * @param codEtp
	 * @param codVrsVet
	 * @return contrat pédagogique d'une VET avec notes pour un étudiant et une année donnée
	 */
	ContratPedagogiqueResultatElpEprDTO4[] getResultatsElpEtu(String annee, String codEtu, String codEtp, String codVrsVet);
	
	/**
	 * @return la liste des signataires en service
	 */
	SignataireWSSignataireDTO[] getSignataires();
	
	/**
	 * @param annee
	 * @param codEtp
	 * @param codVrsVet
	 * @return les ELP avec crédits ECTS de la VET
	 */
	List<ElementPedagogiDTO2> getElpsRattachesVet(String annee, String codEtp, String codVrsVet);

	/**
	 * @param annee
	 * @param codEtp
	 * @param codVrsVet
	 * @return le nombre de crédits ECTS de la VET
	 */
	BigDecimal getCreditsVetEcts(String annee, String codEtp, String codVrsVet);
	
	/**
	 * @param annee
	 * @param codEtp
	 * @param codVrsVet
	 * @return la composante organisatrice de la VET
	 */
	ComposanteOrganisatriceDTO getCmpOrgaVet(String annee, String codEtp, String codVrsVet);
	
	/**
	 * @param annee
	 * @param codEtp
	 * @param codVrsVet
	 * @return la liste des délibération de l'étape
	 */
	List<GrpResultatVetDTO> getDelibsVet(String annee, String codEtp, Integer codVrsVet);
	
	/**
	 * @param id
	 * @return utilisateur au sens Apogée
	 */
	UtilisateurApo getUtilisateurApo(String id);
	
}
