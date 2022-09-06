package cz.tvrzna.wireable.helpers;

import cz.tvrzna.wireable.enums.PriorityLevel;

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
	private final PriorityLevel priorityLevel;

	/**
	 * Instantiates a new wireable wrapper.
	 *
	 * @param instance
	 *          the instance
	 * @param wireable
	 *          the wireable
	 * @param priorityLevel
	 *          the priority level
	 */
	public WireableWrapper(Object instance, boolean wireable, PriorityLevel priorityLevel)
	{
		this.instance = instance;
		this.wireable = wireable;
		this.priorityLevel = priorityLevel;
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

	/**
	 * Gets the priority level.
	 *
	 * @return the priority level
	 * @since 0.4.0
	 */
	public PriorityLevel getPriorityLevel()
	{
		return priorityLevel;
	}
}
