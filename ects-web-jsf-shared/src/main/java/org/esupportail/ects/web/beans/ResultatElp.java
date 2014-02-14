/**
 * 
 */
package org.esupportail.ects.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.esupportail.ects.web.beans.pojo.ElpPojo;

/**
 * Classe représentant les calculs d'échelle de notation pour un ELP
 * @author gmartel
 *
 */
public class ResultatElp implements Serializable {

	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * serialisation ID.
	 */
	private static final long serialVersionUID = 5802445759054183704L;
	

	/**
	 * ELP.
	 */
	private ElpPojo elp;
	
	/**
	 * Année du résultat (pour les PRC)
	 */
	private String annee1;
	/**
	 * note session 1 (ou unique).
	 */
	private BigDecimal note1;
	/**
	 * bareme pour la session 1.
	 */
	private int bareme1;
	/**
	 * résultat session 1.
	 */
	private String res1;
	/**
	 * note ECTS session 1 (ou unique).
	 */
	private String noteEcts1;	
	/**
	 * Credits obtenus session 1.
	 */
	private BigDecimal credits1;	
	/**
	 * Année du résultat (pour les PRC)
	 */
	private String annee2;	
	/**
	 * note session 2.
	 */
	private BigDecimal note2;
	/**
	 * bareme pour la session 2.
	 */
	private int bareme2;
	/**
	 * résultat session 2.
	 */
	private String res2;
	/**
	 * note ECTS session 2.
	 */
	private String noteEcts2;	
	/**
	 * Credits obtenus session 2.
	 */
	private BigDecimal credits2;	
	/**
	 * rang.
	 */
	private int rang;
	/**
	 * elp supérieur (père).
	 */
	private String codElpSup;
	
	/**
	 * A logger.
	 */
	//private final Logger logger = new LoggerImpl(getClass());
	
	
	/*
	 ******************* INIT ******************** */
    
	/**
	 * 
	 */
	public ResultatElp() {
		super();
	}



	/*
	 ******************* METHODS ******************** */

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codElpSup == null) ? 0 : codElpSup.hashCode());
		result = prime * result + ((elp == null) ? 0 : elp.hashCode());
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
		ResultatElp other = (ResultatElp) obj;
		if (codElpSup == null) {
			if (other.codElpSup != null)
				return false;
		} else if (!codElpSup.equals(other.codElpSup))
			return false;
		if (elp == null) {
			if (other.elp != null)
				return false;
		} else if (!elp.equals(other.elp))
			return false;
		return true;
	}



	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultatElp [elp=" + elp + ", note1=" + note1 + ", bareme1="
				+ bareme1 + ", res1=" + res1 + ", noteEcts1=" + noteEcts1
				+ ", note2=" + note2 + ", bareme2=" + bareme2 + ", res2="
				+ res2 + ", rang=" + rang + ", codElpSup=" + codElpSup + "]";
	}

	
	
	/*
	 ******************* ACCESSORS ******************** */

	
	/**
	 * @return the elp
	 */
	public ElpPojo getElp() {
		return elp;
	}

	/**
	 * @param elp the elp to set
	 */
	public void setElp(ElpPojo elp) {
		this.elp = elp;
	}

	/**
	 * @return the note1
	 */
	public BigDecimal getNote1() {
		return note1;
	}

	/**
	 * @param note1 the note1 to set
	 */
	public void setNote1(BigDecimal note1) {
		this.note1 = note1;
	}

	/**
	 * @return the bareme1
	 */
	public int getBareme1() {
		return bareme1;
	}

	/**
	 * @param bareme1 the bareme1 to set
	 */
	public void setBareme1(int bareme1) {
		this.bareme1 = bareme1;
	}

	/**
	 * @return the res1
	 */
	public String getRes1() {
		return res1;
	}

	/**
	 * @param res1 the res1 to set
	 */
	public void setRes1(String res1) {
		this.res1 = res1;
	}

	/**
	 * @return the noteEcts1
	 */
	public String getNoteEcts1() {
		return noteEcts1;
	}

	/**
	 * @param noteEcts1 the noteEcts1 to set
	 */
	public void setNoteEcts1(String noteEcts1) {
		this.noteEcts1 = noteEcts1;
	}

	/**
	 * @return the note2
	 */
	public BigDecimal getNote2() {
		return note2;
	}

	/**
	 * @param note2 the note2 to set
	 */
	public void setNote2(BigDecimal note2) {
		this.note2 = note2;
	}

	/**
	 * @return the bareme2
	 */
	public int getBareme2() {
		return bareme2;
	}

	/**
	 * @param bareme2 the bareme2 to set
	 */
	public void setBareme2(int bareme2) {
		this.bareme2 = bareme2;
	}

	/**
	 * @return the res2
	 */
	public String getRes2() {
		return res2;
	}

	/**
	 * @param res2 the res2 to set
	 */
	public void setRes2(String res2) {
		this.res2 = res2;
	}

	/**
	 * @return the noteEcts2
	 */
	public String getNoteEcts2() {
		return noteEcts2;
	}

	/**
	 * @param noteEcts2 the noteEcts2 to set
	 */
	public void setNoteEcts2(String noteEcts2) {
		this.noteEcts2 = noteEcts2;
	}

	/**
	 * @return the rang
	 */
	public int getRang() {
		return rang;
	}

	/**
	 * @param rang the rang to set
	 */
	public void setRang(int rang) {
		this.rang = rang;
	}

	/**
	 * @return the codElpSup
	 */
	public String getCodElpSup() {
		return codElpSup;
	}

	/**
	 * @param codElpSup the codElpSup to set
	 */
	public void setCodElpSup(String codElpSup) {
		this.codElpSup = codElpSup;
	}

	/**
	 * @return the annee
	 */
	public String getAnnee1() {
		return annee1;
	}

	/**
	 * @param annee the annee to set
	 */
	public void setAnnee1(String annee1) {
		this.annee1 = annee1;
	}

	/**
	 * @return the annee2
	 */
	public String getAnnee2() {
		return annee2;
	}

	/**
	 * @param annee2 the annee2 to set
	 */
	public void setAnnee2(String annee2) {
		this.annee2 = annee2;
	}


	/**
	 * @return the credits1
	 */
	public BigDecimal getCredits1() {
		return credits1;
	}


	/**
	 * @param credits1 the credits1 to set
	 */
	public void setCredits1(BigDecimal credits1) {
		this.credits1 = credits1;
	}


	/**
	 * @return the credits2
	 */
	public BigDecimal getCredits2() {
		return credits2;
	}


	/**
	 * @param credits2 the credits2 to set
	 */
	public void setCredits2(BigDecimal credits2) {
		this.credits2 = credits2;
	}



}
