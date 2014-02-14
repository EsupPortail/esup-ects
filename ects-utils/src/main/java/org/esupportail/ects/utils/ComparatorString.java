package org.esupportail.ects.utils;

import gouv.education.apogee.commun.transverse.dto.WSReferentiel.recupererSignataire.SignataireWSSignataireDTO;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Classe utilisée pour le tri de listes d'objets.
 * @author gmartel
 *
 */public class ComparatorString implements Comparator<Object>, Serializable {
	/**
	 * The serialization id. 
	 */
	private static final long serialVersionUID = -3564847013776612709L;
	
	
	/*
	 * PROPERTIES
	 */
	
	/**
	 * Permet d'identifier la classe afin de récupérer le bonne attribut.
	 */
	private Class< ? > className;

	/*
	 * INIT
	 */
	/**
	 * Constructor.
	 */
	public ComparatorString() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param c
	 *            Class
	 */
	public ComparatorString(final Class< ? > c) {
		super();
		className = c;
	}

	/*
	 * METHODS
	 */

	/**
	 * @param o1
	 * @param o2
	 * @return int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(final Object o1, final Object o2) {

		if (className.equals(SignataireWSSignataireDTO.class)) {
			return sortStr(((SignataireWSSignataireDTO) o1).getNomSig(), 
							((SignataireWSSignataireDTO) o2).getNomSig());
		}
		
		return 0;
	}

	
	/**
	 * Sort the given string.
	 * 
	 * @param lib1
	 * @param lib2
	 * @return int
	 */
	private int sortStr(final String lib1, final String lib2) {
		if (!lib1.equals(lib2)) {
			return lib1.compareTo(lib2);
		}
		return 0;		
	}
	
	
	/*
	 * ACCESSORS
	 */

	/**
	 * @return the className
	 */
	public Class< ? > getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(final Class< ? > className) {
		this.className = className;
	}
}
