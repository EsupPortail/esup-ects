package org.esupportail.ects.services.authentication;

import org.esupportail.ects.domain.beans.User;


/**
 * The interface of authenticators.
 */
public interface Authenticator {

	/**
	 * @return the authenticated user.
	 */
	User getUser();

}