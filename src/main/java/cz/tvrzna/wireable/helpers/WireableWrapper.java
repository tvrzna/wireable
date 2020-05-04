package cz.tvrzna.wireable.helpers;

/**
 * The Class WireableWrapper encapsulates instances of <code>Wireable</code>
 * classes.
 * 
 * @since 0.2.0
 * @author michalt
 */
public class WireableWrapper
{
	private final Object instance;
	private final boolean wireable;

	/**
	 * Instantiates a new wireable wrapper.
	 *
	 * @param instance
	 *          the instance
	 * @param wireable
	 *          the wireable
	 */
	public WireableWrapper(Object instance, boolean wireable)
	{
		this.instance = instance;
		this.wireable = wireable;
	}

	/**
	 * Gets the single instance of WireableWrapper.
	 *
	 * @return the instance
	 */
	public Object getInstance()
	{
		return instance;
	}

	/**
	 * Checks if is wireable.
	 *
	 * @return the wireable
	 */
	public boolean isWireable()
	{
		return wireable;
	}
}
