package cz.tvrzna.wireable.helpers;

import cz.tvrzna.wireable.exceptions.WireableException;

/**
 * The Interface WireableExceptionHandler defines functional interface for
 * exception handling.
 *
 * @author michalt
 * @since 0.3.0
 */
@FunctionalInterface
public interface WireableExceptionHandler
{

	/**
	 * Handle exception.
	 *
	 * @param e
	 *          the e
	 */
	public void handleException(WireableException e);
}
