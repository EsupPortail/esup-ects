package org.esupportail.ects.web.beans.pojo;

import java.io.Serializable;



/**
 * @author gmartel
 */
public class InscriptionVetPojo implements Serializable {

	/*
	 ******************* PROPERTIES ******************* */
	/**
	 * the serialization id. 
	 */
	private static final long serialVersionUID = -4310223464663032136L;

	/**
	 * Année inscription.
	 */
	private String annee;

	/**
	 * Version étape.
	 */
	private VetPojo vet;

	/*
	 ******************* INIT ************************* */
	
	/**
	 * Constructeur.
	 */
	public InscriptionVetPojo() {
		super();
	}


	/*
	 ******************* METHODS ********************** */

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annee == null) ? 0 : annee.hashCode());
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
		InscriptionVetPojo other = (InscriptionVetPojo) obj;
		if (annee == null) {
			if (other.annee != null)
				return false;
		} else if (!annee.equals(other.annee))
			return false;
		if (vet == null) {
			if (other.vet != null)
				return false;
		} else if (!vet.equals(other.vet))
			return false;
		return true;
	}


	/**)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InscriptionVetPojo [annee=" + annee + ", vet=" + vet + "]";
	}

	
	/*
	 ******************* ACCESSORS ******************** */

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


}
