package cz.tvrzna.wireable.exceptions;

/**
 * This exception defines throwables, that could occur during
 * <code>WireableContext</code> initialization.
 *
 * @since 0.2.0
 * @author michalt
 */
public class WireableException extends Exception
{
	private static final long serialVersionUID = 3944223992814378400L;

	/**
	 * Instantiates a new wireable exception.
	 *
	 * @param message
	 *          the message
	 * @param cause
	 *          the cause
	 */
	public WireableException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
