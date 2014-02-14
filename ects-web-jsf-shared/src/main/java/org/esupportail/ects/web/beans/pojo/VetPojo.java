package org.esupportail.ects.web.beans.pojo;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * @author gmartel
 */
public class VetPojo implements Serializable {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -673452958567721467L;

	/**
	 * Code Etape.
	 */
	private String codEtp;
	
	/**
	 * Numero Version Etape.
	 */
	private Integer codVrsVet;
	
		/**
	 * libelle web.
	 */
	private String libWebVet;
	
	/**
	 * Libelle Court de l'Etape.
	 */
	private String licEtp;
	
	/**
	 * témoin Session Unique (O/N°.
	 */
	private String temoinSessionUnique;

	/**
	 * Nbre de crédits à l'étape
	 */
	private BigDecimal credits;

	
	/*
	 ******************* INIT ************************* */
	/**
	 * @param codEtp
	 * @param codVrsVet
	 * @param libWebVet
	 * @param licEtp
	 */
	public VetPojo(String codEtp, Integer codVrsVet, String libWebVet,
			String licEtp) {
		super();
		this.codEtp = codEtp;
		this.codVrsVet = codVrsVet;
		this.libWebVet = libWebVet;
		this.licEtp = licEtp;
	}
	
	/**
	 * 
	 */
	public VetPojo() {
		super();
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codEtp == null) ? 0 : codEtp.hashCode());
		result = prime * result
				+ ((codVrsVet == null) ? 0 : codVrsVet.hashCode());
		result = prime * result
				+ ((libWebVet == null) ? 0 : libWebVet.hashCode());
		result = prime * result + ((licEtp == null) ? 0 : licEtp.hashCode());
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
		VetPojo other = (VetPojo) obj;
		if (codEtp == null) {
			if (other.codEtp != null)
				return false;
		} else if (!codEtp.equals(other.codEtp))
			return false;
		if (codVrsVet == null) {
			if (other.codVrsVet != null)
				return false;
		} else if (!codVrsVet.equals(other.codVrsVet))
			return false;
		if (libWebVet == null) {
			if (other.libWebVet != null)
				return false;
		} else if (!libWebVet.equals(other.libWebVet))
			return false;
		if (licEtp == null) {
			if (other.licEtp != null)
				return false;
		} else if (!licEtp.equals(other.licEtp))
			return false;
		return true;
	}
	
	/*
	 ******************* METHODS ********************** */

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VetPojo [codEtp=" + codEtp + ", codVrsVet=" + codVrsVet
				+ ", libWebVet=" + libWebVet + ", licEtp=" + licEtp
				+ ", credits=" + credits + "]";
	}
	
	/*
	 ******************* ACCESSORS ******************** */
	
	/**
	 * @param codEtp
	 */
	public void setCodEtp(String codEtp) {
		this.codEtp = codEtp;
	}


	/**
	 * @return
	 */
	public String getCodEtp() {
		return codEtp;
	}

	/**
	 * @param codVrsVet
	 */
	public void setCodVrsVet(Integer codVrsVet) {
		this.codVrsVet = codVrsVet;
	}

	/**
	 * @return
	 */
	public Integer getCodVrsVet() {
		return codVrsVet;
	}

	/**
	 * @param libWebVet
	 */
	public void setLibWebVet(String libWebVet) {
		this.libWebVet = libWebVet;
	}

	/**
	 * @return
	 */
	public String getLibWebVet() {
		return libWebVet;
	}

	/**
	 * @param licEtp
	 */
	public void setLicEtp(String licEtp) {
		this.licEtp = licEtp;
	}

	/**
	 * @return
	 */
	public String getLicEtp() {
		return licEtp;
	}
	
	/**
	 * @return the temoinSessionUnique
	 */
	public String getTemoinSessionUnique() {
		return temoinSessionUnique;
	}

	/**
	 * @param temoinSessionUnique the temoinSessionUnique to set
	 */
	public void setTemoinSessionUnique(String temoinSessionUnique) {
		this.temoinSessionUnique = temoinSessionUnique;
	}

	/**
	 * @return the credits
	 */
	public BigDecimal getCredits() {
		return credits;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits(BigDecimal credits) {
		this.credits = credits;
	}
	

}
