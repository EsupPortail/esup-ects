/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.ects.domain.beans;


import java.lang.Comparable;
import java.io.Serializable;

/**
 * La classe représentant les étudiants.
 * @author gmartel
 *
 */
public class Etudiant implements Serializable, Comparable {
	
	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * serialisation ID.
	 */
	private static final long serialVersionUID = -8234821791466668635L;

	/**
	 * Le code de l'étudiant.
	 */
	private String codEtu;
	
    /**
	 * Le numéro INE de l'étudiant.
	 */
    private String numeroIne;
    
    /**
	 * Le nom de l'étudiant.
	 */
    private String nom;
    
    /**
	 * Le prénom de l'étudiant.
	 */
    private String prenom;
    
    /**
     * La date de naissance de l'étudiant.
     */
    private String dateNaissance;

    
	/*
	 ******************* INIT ******************** */
    
	/**
	 * Bean constructor.
	 */
	public Etudiant() {
		super();
	}
	/*
	 ******************* METHODS ******************** */


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codEtu == null) ? 0 : codEtu.hashCode());
		result = prime * result
				+ ((dateNaissance == null) ? 0 : dateNaissance.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result
				+ ((numeroIne == null) ? 0 : numeroIne.hashCode());
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
		Etudiant other = (Etudiant) obj;
		if (codEtu == null) {
			if (other.codEtu != null)
				return false;
		} else if (!codEtu.equals(other.codEtu))
			return false;
		if (dateNaissance == null) {
			if (other.dateNaissance != null)
				return false;
		} else if (!dateNaissance.equals(other.dateNaissance))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (numeroIne == null) {
			if (other.numeroIne != null)
				return false;
		} else if (!numeroIne.equals(other.numeroIne))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Etudiant #" + hashCode() + " codEtu=" + getCodEtu() + ", Nom=" + getNom();
	}

	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		if (this.getNom().compareTo(((Etudiant)o).getNom())==0) {
			return this.getPrenom().compareTo(((Etudiant)o).getNom());
		}
		return this.getNom().compareTo(((Etudiant)o).getNom());
	}
	
	

	/*
	 ******************* ACCESSORS ******************** */

	/**
	 * @return the codEtu
	 */
	public String getCodEtu() {
		return codEtu;
	}


	/**
	 * @param codEtu the codEtu to set
	 */
	public void setCodEtu(String codEtu) {
		this.codEtu = codEtu;
	}


	/**
	 * @return the numeroIne
	 */
	public String getNumeroIne() {
		return numeroIne;
	}


	/**
	 * @param numeroIne the numeroIne to set
	 */
	public void setNumeroIne(String numeroIne) {
		this.numeroIne = numeroIne;
	}


	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}


	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}


	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}


	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	/**
	 * @return the dateNaissance
	 */
	public String getDateNaissance() {
		return dateNaissance;
	}


	/**
	 * @param dateNaissance the dateNaissance to set
	 */
	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	
}
