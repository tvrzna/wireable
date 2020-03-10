package cz.tvrzna.wireable.exceptions;

/**
 * This exception defines throwables, that could occur during
 * <code>ApplicationContext</code> initialization.
 *
 * @since 0.1.0
 * @author michalt
 */
public class ApplicationContextException extends Exception
{
	private static final long serialVersionUID = 3944223992814378400L;

	/**
	 * Instantiates a new application context exception.
	 */
	public ApplicationContextException()
	{
		super();
	}

	/**
	 * Instantiates a new application context exception.
	 *
	 * @param message
	 *          the message
	 */
	public ApplicationContextException(String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new application context exception.
	 *
	 * @param message
	 *          the message
	 * @param cause
	 *          the cause
	 */
	public ApplicationContextException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
