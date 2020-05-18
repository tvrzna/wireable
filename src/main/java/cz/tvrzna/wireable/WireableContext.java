package cz.tvrzna.wireable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnEvent;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Unwireable;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.enums.PriorityLevel;
import cz.tvrzna.wireable.exceptions.WireableException;
import cz.tvrzna.wireable.helpers.WireableWrapper;

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
	private static boolean loaded = false;
	private static Map<Class<?>, WireableWrapper> classContext;
	private static Map<String, List<Method>> eventContext;

	/**
	 * Instantiates a new wireable context.
	 */
	private WireableContext()
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
	 *           the wireable exception
	 */
	public static void init(String strPackage) throws WireableException
	{
		if (!loaded)
		{
			classContext = new HashMap<>();
			eventContext = new HashMap<>();
			try
			{
				for (Class<?> clazz : Reflections.scanPackage(strPackage))
				{
					if (clazz.isAnnotationPresent(Wireable.class) || clazz.isAnnotationPresent(Unwireable.class))
					{
						Constructor<?> constr = clazz.getDeclaredConstructor();
						constr.setAccessible(true);
						classContext.put(clazz, new WireableWrapper(constr.newInstance(), clazz.isAnnotationPresent(Wireable.class)));
					}
				}

				for (Object o : getInstances())
				{
					wireObjects(o);
				}

				for (Object o : getInstances())
				{
					for (Method method : Reflections.findAnnotatedMethods(o, OnEvent.class))
					{
						OnEvent onEvent = method.getAnnotation(OnEvent.class);
						if (onEvent.value() != null)
						{
							eventContext.computeIfAbsent(onEvent.value().toLowerCase(), k -> new ArrayList<>()).add(method);
						}
					}
				}

				for (Object o : getInstances())
				{
					invokeMethodsByPriority(o, OnCreate.class);
				}

				for (Object o : getInstances())
				{
					invokeMethodsByPriority(o, OnStartup.class);
				}

				if (!classContext.isEmpty())
				{
					loaded = true;
				}
			}
			catch (Exception e)
			{
				throw new WireableException("Could not initialize services", e);
			}
		}
	}

	/**
	 * Invoke methods of defined annotation class by predefined priority level.
	 *
	 * @param <T>
	 *          the generic type
	 * @param o
	 *          the object with annotated methods
	 * @param clazz
	 *          the class of annotation
	 * @throws Exception
	 *           the exception
	 */
	private static <T extends Annotation> void invokeMethodsByPriority(Object o, Class<T> clazz) throws Exception
	{
		for (PriorityLevel priority : PriorityLevel.values())
		{
			for (Method method : Reflections.findAnnotatedMethods(o, clazz))
			{
				T anno = method.getAnnotation(clazz);
				PriorityLevel annPriority = PriorityLevel.NORMAL;

				if (anno instanceof OnCreate)
				{
					annPriority = ((OnCreate) anno).priority();
				}
				else if (anno instanceof OnStartup)
				{
					annPriority = ((OnStartup) anno).priority();
				}

				if (priority.equals(annPriority))
				{
					method.setAccessible(true);
					method.invoke(o);
				}
			}
		}
	}

	/**
	 * Gets the instances.
	 *
	 * @return the instances
	 */
	private static List<Object> getInstances()
	{
		return classContext.values().stream().map(w -> w.getInstance()).collect(Collectors.toList());
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
	 */
	public static <T> T getInstance(Class<T> clazz)
	{
		return getInstance(clazz, true);
	}

	/**
	 * Gets the single instance of {@link Wireable} or {@link Unwireable} class,
	 * that is loaded in <code>classContext</code>. This methods access classes
	 * loaded in context to any class.
	 *
	 * @param <T>
	 *          the generic type
	 * @param clazz
	 *          the clazz
	 * @param onlyWireable
	 *          the only wireable
	 * @return single instance of Wireable from WireableContext
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz, boolean onlyWireable)
	{
		WireableWrapper wrapper = classContext.get(clazz);
		if (wrapper != null)
		{
			if (onlyWireable)
			{
				if (wrapper.isWireable())
				{
					return (T) wrapper.getInstance();
				}
			}
			else
			{
				return (T) wrapper.getInstance();
			}
		}
		return null;
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
	 * @since 0.2.0
	 */
	public static void fireEvent(String eventName, Object... params) throws WireableException
	{
		if (eventName != null)
		{
			for (Method m : eventContext.get(eventName.toLowerCase()))
			{
				Object[] args = new Object[m.getParameterCount()];
				for (int i = 0; i < (args.length > params.length ? params.length : args.length); i++)
				{
					args[i] = params[i];
				}
				try
				{
					m.setAccessible(true);
					m.invoke(getInstance(m.getDeclaringClass(), false), args);
				}
				catch (Exception e)
				{
					throw new WireableException("Could not fire method ".concat(m.getName()), e);
				}
			}
		}
	}

	/**
	 * Wire all {@link Wireable} fields defined by {@link Wired} annotation in
	 * object.
	 *
	 * @param o
	 *          the o
	 * @throws WireableException
	 *           the wireable exception
	 */
	public static void wireObjects(Object o) throws WireableException
	{
		try
		{
			for (Field field : Reflections.findAnnotatedFields(o, Wired.class))
			{
				field.setAccessible(true);
				field.set(o, getInstance(field.getType()));
			}
		}
		catch (Exception e)
		{
			throw new WireableException("Could not wire instance", e);
		}
	}
}
