/**
 * 
 */
package org.esupportail.ects.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.esupportail.ects.web.beans.pojo.VetPojo;

/**
 * Classe représentant les calculs d'échelle de notation pour une VET
 * @author gmartel
 *
 */
public class ResultatVet implements Serializable {

	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * serialisation ID.
	 */
	private static final long serialVersionUID = 5802445759054183704L;
	

	/**
	 * Vet.
	 */
	private VetPojo vet;
	
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
	 * A logger.
	 */
	//private final Logger logger = new LoggerImpl(getClass());
	
	
	/*
	 ******************* INIT ******************** */
    
	/**
	 * 
	 */
	public ResultatVet() {
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
		result = prime * result + bareme1;
		result = prime * result + bareme2;
		result = prime * result
				+ ((credits1 == null) ? 0 : credits1.hashCode());
		result = prime * result
				+ ((credits2 == null) ? 0 : credits2.hashCode());
		result = prime * result + ((note1 == null) ? 0 : note1.hashCode());
		result = prime * result + ((note2 == null) ? 0 : note2.hashCode());
		result = prime * result
				+ ((noteEcts1 == null) ? 0 : noteEcts1.hashCode());
		result = prime * result
				+ ((noteEcts2 == null) ? 0 : noteEcts2.hashCode());
		result = prime * result + rang;
		result = prime * result + ((res1 == null) ? 0 : res1.hashCode());
		result = prime * result + ((res2 == null) ? 0 : res2.hashCode());
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
		ResultatVet other = (ResultatVet) obj;
		if (bareme1 != other.bareme1)
			return false;
		if (bareme2 != other.bareme2)
			return false;
		if (credits1 == null) {
			if (other.credits1 != null)
				return false;
		} else if (!credits1.equals(other.credits1))
			return false;
		if (credits2 == null) {
			if (other.credits2 != null)
				return false;
		} else if (!credits2.equals(other.credits2))
			return false;
		if (note1 == null) {
			if (other.note1 != null)
				return false;
		} else if (!note1.equals(other.note1))
			return false;
		if (note2 == null) {
			if (other.note2 != null)
				return false;
		} else if (!note2.equals(other.note2))
			return false;
		if (noteEcts1 == null) {
			if (other.noteEcts1 != null)
				return false;
		} else if (!noteEcts1.equals(other.noteEcts1))
			return false;
		if (noteEcts2 == null) {
			if (other.noteEcts2 != null)
				return false;
		} else if (!noteEcts2.equals(other.noteEcts2))
			return false;
		if (rang != other.rang)
			return false;
		if (res1 == null) {
			if (other.res1 != null)
				return false;
		} else if (!res1.equals(other.res1))
			return false;
		if (res2 == null) {
			if (other.res2 != null)
				return false;
		} else if (!res2.equals(other.res2))
			return false;
		if (vet == null) {
			if (other.vet != null)
				return false;
		} else if (!vet.equals(other.vet))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResultatVet [vet=" + vet + ", note1=" + note1 + ", bareme1="
				+ bareme1 + ", res1=" + res1 + ", noteEcts1=" + noteEcts1
				+ ", credits1=" + credits1 + ", note2=" + note2 + ", bareme2="
				+ bareme2 + ", res2=" + res2 + ", noteEcts2=" + noteEcts2
				+ ", credits2=" + credits2 + ", rang=" + rang + "]";
	}



	/*
	 ******************* ACCESSORS ******************** */

	
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




}
