/**
 * 
 */
package org.esupportail.ects.web.beans.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe représentant une note ECTS avec echelle de distribution
 * @author gmartel
 *
 */
public class NoteEctsPojo implements Serializable {
	
	/**
	 * serialisation ID.
	 */
	private static final long serialVersionUID = 7739329412125995174L;

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * Les distributions ECTS (ou notes ECTS)
	 */
	public enum DistributionECTS {
	   A, B, C, D, E;
	}


	/**
	 * Code de la distribution ECTS (i.e. A, B, C, D ou E)
	 */
	private DistributionECTS codeDistribution;
	
	/**
	 * Note minimum pour la distribution
	 */
	private BigDecimal noteMini;
	
	/**
	 * Note maximum pour la distribution
	 */
	private BigDecimal noteMaxi;

	
	/*
	 ******************* INIT ************************* */
	
	
	/**
	 * @param codeDistribution
	 * @param noteMini
	 * @param noteMaxi
	 */
	public NoteEctsPojo(DistributionECTS codeDistribution, BigDecimal noteMini,
			BigDecimal noteMaxi) {
		super();
		this.codeDistribution = codeDistribution;
		this.noteMini = noteMini;
		this.noteMaxi = noteMaxi;
	}
	
	

	/**
	 * 
	 */
	public NoteEctsPojo() {
		super();
		// TODO Auto-generated constructor stub
	}


	/*
	 ******************* METHODS ********************** */

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codeDistribution == null) ? 0 : codeDistribution.hashCode());
		result = prime * result
				+ ((noteMaxi == null) ? 0 : noteMaxi.hashCode());
		result = prime * result
				+ ((noteMini == null) ? 0 : noteMini.hashCode());
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
		NoteEctsPojo other = (NoteEctsPojo) obj;
		if (codeDistribution == null) {
			if (other.codeDistribution != null)
				return false;
		} else if (!codeDistribution.equals(other.codeDistribution))
			return false;
		if (noteMaxi == null) {
			if (other.noteMaxi != null)
				return false;
		} else if (!noteMaxi.equals(other.noteMaxi))
			return false;
		if (noteMini == null) {
			if (other.noteMini != null)
				return false;
		} else if (!noteMini.equals(other.noteMini))
			return false;
		return true;
	}
	
	



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NoteEctsPojo [codeDistribution=" + codeDistribution
				+ ", noteMini=" + noteMini + ", noteMaxi=" + noteMaxi + "]";
	}

	/*
	 ******************* ACCESSORS ******************** */


	/**
	 * @return the codeDistribution
	 */
	public DistributionECTS getCodeDistribution() {
		return codeDistribution;
	}

	/**
	 * @param codeDistribution the codeDistribution to set
	 */
	public void setCodeDistribution(DistributionECTS codeDistribution) {
		this.codeDistribution = codeDistribution;
	}

	/**
	 * @return the noteMini
	 */
	public BigDecimal getNoteMini() {
		return noteMini;
	}

	/**
	 * @param noteMini the noteMini to set
	 */
	public void setNoteMini(BigDecimal noteMini) {
		this.noteMini = noteMini;
	}

	/**
	 * @return the noteMaxi
	 */
	public BigDecimal getNoteMaxi() {
		return noteMaxi;
	}

	/**
	 * @param noteMaxi the noteMaxi to set
	 */
	public void setNoteMaxi(BigDecimal noteMaxi) {
		this.noteMaxi = noteMaxi;
	}

	
}
