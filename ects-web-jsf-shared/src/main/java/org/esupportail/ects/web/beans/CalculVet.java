/**
 * 
 */
package org.esupportail.ects.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.esupportail.ects.web.beans.pojo.NoteEctsPojo;
import org.esupportail.ects.web.beans.pojo.VetPojo;

/**
 * Classe représentant les calculs d'échelle de notation pour une VET
 * @author gmartel
 *
 */
public class CalculVet implements Serializable {

	/*
	 ******************* PROPERTIES ******************** */

	/**
	 * serialisation ID.
	 */
	private static final long serialVersionUID = 7199489187285511380L;

	/**
	 * VET
	 */
	private VetPojo vet;
	
	/**
	 * Liste des notes des Admis
	 */
	private List<BigDecimal> notes;
	
	/**
	 * Nombre d'inscrits à l'étape
	 */
	private Integer nbInscrits;
	
	/**
	 * Echelle de notation ECTS de l'étape
	 */
	private List<NoteEctsPojo> echelleNotation;
	
	/**
	 * A logger.
	 */
	//private final Logger logger = new LoggerImpl(getClass());
	
	
	/*
	 ******************* INIT ******************** */
    
	/**
	 * 
	 */
	public CalculVet() {
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
				+ ((echelleNotation == null) ? 0 : echelleNotation.hashCode());
		result = prime * result
				+ ((nbInscrits == null) ? 0 : nbInscrits.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((vet == null) ? 0 : vet.hashCode());
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
		CalculVet other = (CalculVet) obj;
		if (echelleNotation == null) {
			if (other.echelleNotation != null)
				return false;
		} else if (!echelleNotation.equals(other.echelleNotation))
			return false;
		if (nbInscrits == null) {
			if (other.nbInscrits != null)
				return false;
		} else if (!nbInscrits.equals(other.nbInscrits))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (vet == null) {
			if (other.vet != null)
				return false;
		} else if (!vet.equals(other.vet))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CalculVet [vet=" + vet + ", notes=" + notes + ", nbInscrits="
				+ nbInscrits + ", echelleNotation=" + echelleNotation + "]";
	}
	
	public void calculEchelle(Integer nbEtuSignificatif) {
		
		// Pas de notes
		if ((notes==null)||(notes.isEmpty())) {
			return;
		}
		
		// Nombre d'inscrits avec notes non-significatif
		if (nbInscrits.compareTo(nbEtuSignificatif) < 0) {
			return;
		}
		
		echelleNotation = new ArrayList<NoteEctsPojo>();
		// On ordonne la liste
		Collections.sort(notes, Collections.reverseOrder());
		// On parcourt du premier au dernier et on découpe en 5 niveaux
		// 0%  < Rang <= 10% = A
		NoteEctsPojo noteEctsA = new NoteEctsPojo();
		noteEctsA.setCodeDistribution(NoteEctsPojo.DistributionECTS.A);
		BigDecimal noteMin = new BigDecimal(0);
		BigDecimal noteMax = new BigDecimal(0);
		BigDecimal a,b,ratio;
		b=new BigDecimal(nbInscrits);
		for (int i=0; i<=nbInscrits-1; i++) {
			a=new BigDecimal(i+1);
			ratio=a.divide(b, MathContext.DECIMAL128);
			if (ratio.compareTo(new BigDecimal(0.1))<=0) {
				if (noteMax.equals(new BigDecimal(0))) {
					noteMax = notes.get(i);
				}
				noteMin = notes.get(i);
			}
		}
		noteEctsA.setNoteMaxi(noteMax);
		noteEctsA.setNoteMini(noteMin);
		echelleNotation.add(noteEctsA);
		
		// 10% < Rang <= 35% = B
		NoteEctsPojo noteEctsB = new NoteEctsPojo();
		noteEctsB.setCodeDistribution(NoteEctsPojo.DistributionECTS.B);
		noteMin = new BigDecimal(0);
		noteMax = new BigDecimal(0);
		for (int i=0; i<=nbInscrits-1; i++) {
			a=new BigDecimal(i+1);
			ratio=a.divide(b, MathContext.DECIMAL128);
			if ((ratio.compareTo(new BigDecimal(0.35))<=0)
				&&(ratio.compareTo(new BigDecimal(0.10))>0)){
				if (noteMax.equals(new BigDecimal(0))) {
					noteMax = notes.get(i);
				}
				noteMin = notes.get(i);
			}
		}
		noteEctsB.setNoteMaxi(noteMax);
		noteEctsB.setNoteMini(noteMin);
		echelleNotation.add(noteEctsB);
		
		// 35% < Rang <= 65% = C
		NoteEctsPojo noteEctsC = new NoteEctsPojo();
		noteEctsC.setCodeDistribution(NoteEctsPojo.DistributionECTS.C);
		noteMin = new BigDecimal(0);
		noteMax = new BigDecimal(0);
		for (int i=0; i<=nbInscrits-1; i++) {
			a=new BigDecimal(i+1);
			ratio=a.divide(b, MathContext.DECIMAL128);
			if ((ratio.compareTo(new BigDecimal(0.65))<=0)
				&&(ratio.compareTo(new BigDecimal(0.35))>0)){
				if (noteMax.equals(new BigDecimal(0))) {
					noteMax = notes.get(i);
				}
				noteMin = notes.get(i);
			}
		}
		noteEctsC.setNoteMaxi(noteMax);
		noteEctsC.setNoteMini(noteMin);
		echelleNotation.add(noteEctsC);
		
		// 65% < Rang <= 90% = D
		NoteEctsPojo noteEctsD = new NoteEctsPojo();
		noteEctsD.setCodeDistribution(NoteEctsPojo.DistributionECTS.D);
		noteMin = new BigDecimal(0);
		noteMax = new BigDecimal(0);
		for (int i=0; i<=nbInscrits-1; i++) {
			a=new BigDecimal(i+1);
			ratio=a.divide(b, MathContext.DECIMAL128);
			if ((ratio.compareTo(new BigDecimal(0.90))<=0)
				&&(ratio.compareTo(new BigDecimal(0.65))>0)){
				if (noteMax.equals(new BigDecimal(0))) {
					noteMax = notes.get(i);
				}
				noteMin = notes.get(i);
			}
		}
		noteEctsD.setNoteMaxi(noteMax);
		noteEctsD.setNoteMini(noteMin);
		echelleNotation.add(noteEctsD);
		
		// 90% < Rang <= 100% = E
		NoteEctsPojo noteEctsE = new NoteEctsPojo();
		noteEctsE.setCodeDistribution(NoteEctsPojo.DistributionECTS.E);
		noteMin = new BigDecimal(0);
		noteMax = new BigDecimal(0);
		for (int i=0; i<=nbInscrits-1; i++) {
			a=new BigDecimal(i+1);
			ratio=a.divide(b, MathContext.DECIMAL128);
			if ((ratio.compareTo(new BigDecimal(1))<=0)
				&&(ratio.compareTo(new BigDecimal(0.90))>0)){
				if (noteMax.equals(new BigDecimal(0))) {
					noteMax = notes.get(i);
				}
				noteMin = notes.get(i);
			}
		}
		noteEctsE.setNoteMaxi(noteMax);
		noteEctsE.setNoteMini(noteMin);
		echelleNotation.add(noteEctsE);
		
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
	 * @return the notes
	 */
	public List<BigDecimal> getNotes() {
		return notes;
	}


	/**
	 * @param notes the notes to set
	 */
	public void setNotes(List<BigDecimal> notes) {
		this.notes = notes;
	}


	/**
	 * @return the nbInscrits
	 */
	public Integer getNbInscrits() {
		return nbInscrits;
	}


	/**
	 * @param nbInscrits the nbInscrits to set
	 */
	public void setNbInscrits(Integer nbInscrits) {
		this.nbInscrits = nbInscrits;
	}


	/**
	 * @return the echelleNotation
	 */
	public List<NoteEctsPojo> getEchelleNotation() {
		return echelleNotation;
	}


	/**
	 * @param echelleNotation the echelleNotation to set
	 */
	public void setEchelleNotation(List<NoteEctsPojo> echelleNotation) {
		this.echelleNotation = echelleNotation;
	}

	

}
