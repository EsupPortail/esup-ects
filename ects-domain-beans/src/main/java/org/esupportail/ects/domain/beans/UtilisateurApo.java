/**
 * ESUP-Portail Blank Application - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-blank
 */
package org.esupportail.ects.domain.beans;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * La classe représentant les utilisateurs d'Apogée.
 * @author gmartel
 *
 */
public class UtilisateurApo implements Serializable {
	
	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * the serialization id. 
	 */
	private static final long serialVersionUID = -6344228367970537401L;


	/**
	 * Le code de l'utilisateur.
	 */
	private String codUti;
	
	/**
	 * le code du type de l'utilisateur
	 */
	private String codTut;
	
	/**
	 * code du centre de gestion
	 */
	private String codCge;

	/**
	 * nom de l'utilisateur dans Apogée
	 */
	private String libCmtUti;
	
	/**
	 * CTNs de l'utilisateurs
	 */
	private Set<String> ctns = new HashSet<String>(0);
	
	/**
	 * Composantess de l'utilisateurs
	 */
	private Set<String> cmps = new HashSet<String>(0);
	
    
	/*
	 ******************* INIT ******************** */
    
	/**
	 * Bean constructor.
	 */
	public UtilisateurApo() {
		super();
	}

	
	
	
	/*
	 ******************* METHODS ******************** */

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UtilisateurApo [codUti=" + codUti + ", codTut=" + codTut
				+ ", codCge=" + codCge + ", libCmtUti=" + libCmtUti + ", ctns="
				+ ctns + ", cmps=" + cmps + "]";
	}




	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cmps == null) ? 0 : cmps.hashCode());
		result = prime * result + ((codCge == null) ? 0 : codCge.hashCode());
		result = prime * result + ((codTut == null) ? 0 : codTut.hashCode());
		result = prime * result + ((codUti == null) ? 0 : codUti.hashCode());
		result = prime * result + ((ctns == null) ? 0 : ctns.hashCode());
		result = prime * result
				+ ((libCmtUti == null) ? 0 : libCmtUti.hashCode());
		return result;
	}




	/* (non-Javadoc)
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
		UtilisateurApo other = (UtilisateurApo) obj;
		if (cmps == null) {
			if (other.cmps != null)
				return false;
		} else if (!cmps.equals(other.cmps))
			return false;
		if (codCge == null) {
			if (other.codCge != null)
				return false;
		} else if (!codCge.equals(other.codCge))
			return false;
		if (codTut == null) {
			if (other.codTut != null)
				return false;
		} else if (!codTut.equals(other.codTut))
			return false;
		if (codUti == null) {
			if (other.codUti != null)
				return false;
		} else if (!codUti.equals(other.codUti))
			return false;
		if (ctns == null) {
			if (other.ctns != null)
				return false;
		} else if (!ctns.equals(other.ctns))
			return false;
		if (libCmtUti == null) {
			if (other.libCmtUti != null)
				return false;
		} else if (!libCmtUti.equals(other.libCmtUti))
			return false;
		return true;
	}






	/*
	 ******************* ACCESSORS ******************** */

	/**
	 * @return the codUti
	 */
	public String getCodUti() {
		return codUti;
	}





	/**
	 * @param codUti the codUti to set
	 */
	public void setCodUti(String codUti) {
		this.codUti = codUti;
	}




	/**
	 * @return the codTut
	 */
	public String getCodTut() {
		return codTut;
	}




	/**
	 * @param codTut the codTut to set
	 */
	public void setCodTut(String codTut) {
		this.codTut = codTut;
	}




	/**
	 * @return the codCge
	 */
	public String getCodCge() {
		return codCge;
	}




	/**
	 * @param codCge the codCge to set
	 */
	public void setCodCge(String codCge) {
		this.codCge = codCge;
	}




	/**
	 * @return the libCmtUti
	 */
	public String getLibCmtUti() {
		return libCmtUti;
	}




	/**
	 * @param libCmtUti the libCmtUti to set
	 */
	public void setLibCmtUti(String libCmtUti) {
		this.libCmtUti = libCmtUti;
	}




	/**
	 * @return the ctns
	 */
	public Set<String> getCtns() {
		return ctns;
	}




	/**
	 * @param ctns the ctns to set
	 */
	public void setCtns(Set<String> ctns) {
		this.ctns = ctns;
	}




	/**
	 * @return the cmps
	 */
	public Set<String> getCmps() {
		return cmps;
	}




	/**
	 * @param cmps the cmps to set
	 */
	public void setCmps(Set<String> cmps) {
		this.cmps = cmps;
	}

	
}
