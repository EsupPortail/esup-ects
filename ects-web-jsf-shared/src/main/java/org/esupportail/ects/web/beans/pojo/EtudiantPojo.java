package org.esupportail.ects.web.beans.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.esupportail.ects.domain.beans.Etudiant;
import org.esupportail.ects.web.beans.ResultatElp;
import org.esupportail.ects.web.beans.ResultatVet;



/**
 * @author gmartel
 */
public class EtudiantPojo implements Serializable {


	/*
	 ******************* PROPERTIES ******************* */
	/**
	 * serialization Id. 
	 */
	private static final long serialVersionUID = 4732700106342628512L;

	/**
	 * Etudiant.
	 */
	private Etudiant etudiant;
	
	/**
	 * VET sur laquelle le calcul est fait.
	 */
	private VetPojo vet;
	
	/**
	 * Resultat à la VET.
	 */
	private ResultatVet res;
	
	/**
	 * Elp et résultats rattachés à la VET.
	 */
	private List<ResultatElp> elps;

	/**
	 * Légende des abréviations résultat
	 */
	private Set<String> legende;
	

	
	/*
	 ******************* INIT ************************* */
	
	/**
	 * Constructeur
	 */
	public EtudiantPojo() {
		super();
		// TODO Auto-generated constructor stub
	}



	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elps == null) ? 0 : elps.hashCode());
		result = prime * result
				+ ((etudiant == null) ? 0 : etudiant.hashCode());
		result = prime * result + ((vet == null) ? 0 : vet.hashCode());
		return result;
	}



	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EtudiantPojo other = (EtudiantPojo) obj;
		if (elps == null) {
			if (other.elps != null)
				return false;
		} else if (!elps.equals(other.elps))
			return false;
		if (etudiant == null) {
			if (other.etudiant != null)
				return false;
		} else if (!etudiant.equals(other.etudiant))
			return false;
		if (vet == null) {
			if (other.vet != null)
				return false;
		} else if (!vet.equals(other.vet))
			return false;
		return true;
	}


	
	/*
	 ******************* METHODS ********************** */


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EtudiantPojo [etudiant=" + etudiant + ", vet=" + vet
				+ ", elps=" + elps + "]";
	}


	/*
	 ******************* ACCESSORS ******************** */



	/**
	 * @return the etudiant
	 */
	public Etudiant getEtudiant() {
		return etudiant;
	}




	/**
	 * @param etudiant the etudiant to set
	 */
	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
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
	 * @return the elps
	 */
	public List<ResultatElp> getElps() {
		return elps;
	}



	/**
	 * @param elps the elps to set
	 */
	public void setElps(List<ResultatElp> elps) {
		this.elps = elps;
	}



	/**
	 * @return the res
	 */
	public ResultatVet getRes() {
		return res;
	}



	/**
	 * @param res the res to set
	 */
	public void setRes(ResultatVet res) {
		this.res = res;
	}



	/**
	 * @return the legende
	 */
	public Set<String> getLegende() {
		return legende;
	}



	/**
	 * @param legende the legende to set
	 */
	public void setLegende(Set<String> legende) {
		this.legende = legende;
	}


}
