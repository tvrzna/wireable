package cz.tvrzna.wireable;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnEvent;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Unwireable;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.exceptions.WireableException;
import cz.tvrzna.wireable.helpers.WireableExceptionHandler;

/**
 * The core <code>Wireable</code> class, that handles preinitialization of all
 * {@link Wireable} classes, their dependency injection with {@link Wired}
 * annotation and runs their initialization methods. At first it runs
 * {@link OnCreate}, after them it runs {@link OnStartup}.<br>
 * Since 0.2.0 there are preloaded {@link OnEvent} methods, that could be fired
 * with {@link #fireEvent(String, Object...)} method.
 *
 * @author michalt
 * @since 0.2.0
 */
public final class WireableContext
{
	private static WireableContainer INSTANCE = null;

	/**
	 * Instantiates a new wireable context.
	 */
	private WireableContext()
	{
	}

	private static WireableContainer getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new WireableContainer();
		}
		return INSTANCE;
	}

	/**
	 * Creates new {@link WireableContainer}.
	 *
	 * @return the wireable container
	 * @since 0.4.0
	 */
	public static WireableContainer create()
	{
		return new WireableContainer();
	}

	/**
	 * Creates and initializes new {@link WireableContainer}.
	 *
	 * @param strPackage
	 *          the str package
	 * @return the wireable container
	 * @throws WireableException
	 *           the wireable exception
	 * @since 0.4.0
	 */
	public static WireableContainer createAndInit(String strPackage) throws WireableException
	{
		WireableContainer container = new WireableContainer();
		container.init(strPackage);
		return container;
	}

	/**
	 * Inits Wireable and Unwireable in all classes, that are found in defined
	 * <code>strPackage</code>. Those classes, are stored in
	 * <code>classContext</code>.<br>
	 * Since 0.3.0: If {@link Wireable} uses interface, these interfaces could be
	 * used for targeting via {@link Wired}. If multiple classes uses same
	 * interface, it is better to define {@link Wireable#priorityFor()} to define,
	 * which class should be wired as default by interface. <br>
	 * All {@link Wireable} and {@link Unwireable} are checked for members with
	 * {@link Wired} annotation. These members are linked to {@link Wireable} and
	 * {@link Unwireable} classes, that were initialized in previous step.<br>
	 * After classes initialization all methods with annotation {@link OnEvent}
	 * are preloaded into <code>eventContext</code>. These method could be fired
	 * by {@link #fireEvent(String, Object...)}<br>
	 * Last two steps is running of {@link OnCreate} and {@link OnStartup}
	 * annotated methods inside loaded classes. Since 0.2.0 priority level of
	 * these methods is supported.
	 *
	 * @param strPackage
	 *          the str package
	 * @throws WireableException
	 *           the wireable exception
	 * @see WireableContainer#init(String)
	 */
	public static void init(String strPackage) throws WireableException
	{
		getInstance().init(strPackage);
	}

	/**
	 * Gets the single instance of {@link Wireable} class, that is loaded in
	 * <code>classContext</code>. This methods access classes loaded in context to
	 * any class.
	 *
	 * @param <T>
	 *          the generic type
	 * @param clazz
	 *          the clazz
	 * @return single instance of Wireable from WireableContext
	 * @see WireableContainer#getInstance(Class)
	 */
	public static <T> T getInstance(Class<T> clazz)
	{
		return getInstance().getInstance(clazz);
	}

	/**
	 * Gets the single instance of {@link Wireable} or {@link Unwireable} class,
	 * that is loaded in <code>classContext</code>. This methods access classes
	 * loaded in context to any class. If <code>clazz</code> is interface, it
	 * looks for priority definition into <code>interfaceContext</code>, if
	 * nothing is found, it iterates all known classes in
	 * <code>classContext</code> to find suitable class.
	 *
	 * @param <T>
	 *          the generic type
	 * @param clazz
	 *          the clazz
	 * @param onlyWireable
	 *          the only wireable
	 * @return single instance of Wireable from WireableContext
	 * @see WireableContainer#getInstance(Class, boolean)
	 */
	public static <T> T getInstance(Class<T> clazz, boolean onlyWireable)
	{
		return getInstance().getInstance(clazz, onlyWireable);
	}

	/**
	 * Fire events, that are pre-loaded in <code>eventContext</code>. All possible
	 * <code>params</code> are passed, but there is no argument type check, if
	 * mismatch occurs, <code>WireableException</code> is thrown.
	 *
	 * @param eventName
	 *          the event name
	 * @param params
	 *          the params
	 * @throws WireableException
	 *           the wireable exception
	 * @see WireableContainer#fireEvent(String, Object...)
	 * @since 0.2.0
	 */
	public static void fireEvent(String eventName, Object... params) throws WireableException
	{
		getInstance().fireEvent(eventName, params);
	}

	/**
	 * (non-Javadoc).
	 *
	 * @param eventName
	 *          the event name
	 * @param params
	 *          the params
	 * @see WireableContext#fireEventAsync(String, WireableExceptionHandler,
	 *      Object...)
	 */
	public static void fireEventAsync(String eventName, Object... params)
	{
		getInstance().fireEventAsync(eventName, params);
	}

	/**
	 * Fire asynchronous events, that are pre-loaded in <code>eventContext</code>.
	 * All possible <code>params</code> are passed, but there is no argument type
	 * check, if mismatch occurs, it tries to handle exception with
	 * {@link WireableExceptionHandler} if is presented in params.
	 *
	 * @param eventName
	 *          the event name
	 * @param handler
	 *          the handler
	 * @param params
	 *          the params
	 * @see WireableContainer#fireEventAsync(String, WireableExceptionHandler,
	 *      Object...)
	 * @since 0.3.0
	 */
	public static void fireEventAsync(String eventName, WireableExceptionHandler handler, Object... params)
	{
		getInstance().fireEventAsync(eventName, handler, params);
	}

	/**
	 * Wire all {@link Wireable} fields defined by {@link Wired} annotation in
	 * object. Since 0.3.0 multiple objects could be wired.
	 *
	 * @param objects
	 *          the objects
	 * @throws WireableException
	 *           the wireable exception
	 * @see WireableContainer#wireObjects(Object...)
	 */
	public static void wireObjects(Object... objects) throws WireableException
	{
		getInstance().wireObjects(objects);
	}
}
