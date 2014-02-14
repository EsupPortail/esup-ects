package org.esupportail.ects.web.beans.pojo;

import java.io.Serializable;



/**
 * @author gmartel
 */
public class ElpPojo implements Serializable {

	/*
	 ******************* PROPERTIES ******************* */

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -673452958567721467L;

	/**
	 * Code Element pédagogique.
	 */
	private String codElp;
	
	
	/**
	 * libelle long ELP.
	 */
	private String libElp;
	

	
	/*
	 ******************* INIT ************************* */
	/**
	 * @param codElp
	 * @param libElp
	 */
	public ElpPojo(String codElp, String libElp) {
		super();
		this.codElp = codElp;
		this.libElp = libElp;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codElp == null) ? 0 : codElp.hashCode());
		result = prime * result + ((libElp == null) ? 0 : libElp.hashCode());
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
		ElpPojo other = (ElpPojo) obj;
		if (codElp == null) {
			if (other.codElp != null)
				return false;
		} else if (!codElp.equals(other.codElp))
			return false;
		if (libElp == null) {
			if (other.libElp != null)
				return false;
		} else if (!libElp.equals(other.libElp))
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
		return "ElpPojo [codElp=" + codElp + ", libElp=" + libElp + "]";
	}


	/*
	 ******************* ACCESSORS ******************** */
	

	/**
	 * @return the codElp
	 */
	public String getCodElp() {
		return codElp;
	}


	/**
	 * @param codElp the codElp to set
	 */
	public void setCodElp(String codElp) {
		this.codElp = codElp;
	}


	/**
	 * @return the libElp
	 */
	public String getLibElp() {
		return libElp;
	}


	/**
	 * @param libElp the libElp to set
	 */
	public void setLibElp(String libElp) {
		this.libElp = libElp;
	}


}
