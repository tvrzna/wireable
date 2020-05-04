package cz.tvrzna.wireable;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnEvent;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.exceptions.WireableException;

/**
 * The core <code>Wireable</code> class, that handles preinitialization all
 * {@link Wireable} classes, their dependency injection with {@link Wired}
 * annotation and runs their initialization methods. At first it runs
 * {@link OnCreate}, after them it runs {@link OnStartup}.<br>
 * Since 0.2.0 there are preloaded {@link OnEvent} methods, that could be fired
 * with {@link #fireEvent(String, Object...)} method.
 *
 * @deprecated since 0.2.0, use {@link WireableContext} instead
 * @since 0.1.0
 * @author michalt
 */
@Deprecated
public final class ApplicationContext
{
	/**
	 * Instantiates a new application context.
	 */
	private ApplicationContext()
	{
	}

	/**
	 * Inits Wireable in all classes, that are found in defined
	 * <code>strPackage</code>. Those classes, are stored in
	 * <code>classContext</code>.<br>
	 * All {@link Wireable} are checked for members with {@link Wired} annotation.
	 * These members are linked to {@link Wireable} classes, that were initialized
	 * in previous step.<br>
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
	 *           the application context exception
	 */
	@Deprecated
	public static void init(String strPackage) throws WireableException
	{
		WireableContext.init(strPackage);
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
	 * @return single instance of ApplicationContext
	 */
	@Deprecated
	public static <T> T getInstance(Class<T> clazz)
	{
		return WireableContext.getInstance(clazz);
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
	 *           the application context exception
	 * @since 0.2.0
	 */
	@Deprecated
	public static void fireEvent(String eventName, Object... params) throws WireableException
	{
		WireableContext.fireEvent(eventName, params);
	}
}
