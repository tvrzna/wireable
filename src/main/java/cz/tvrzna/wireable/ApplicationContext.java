package cz.tvrzna.wireable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.enums.PriorityLevel;
import cz.tvrzna.wireable.exceptions.ApplicationContextException;

/**
 * The core <code>Wireable</code> class, that handles preinitialization all
 * {@link Wireable} classes, their dependency injection with {@link Wired}
 * annotation and runs their initialization methods. At first it runs
 * {@link OnCreate}, after them it runs {@link OnStartup}.
 *
 * @since 0.1.0
 * @author michalt
 */
public final class ApplicationContext
{
	private static boolean loaded = false;
	private static Map<Class<?>, Object> classContext;

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
	 * Last two steps is running of {@link OnCreate} and {@link OnStartup}
	 * annotated methods inside loaded classes. Since 0.2.0 priority level of these
	 * methods is supported.
	 *
	 * @param strPackage
	 *          the str package
	 * @throws ApplicationContextException
	 *           the application context exception
	 */
	public static void init(String strPackage) throws ApplicationContextException
	{
		if (!loaded)
		{
			classContext = new HashMap<>();
			try
			{
				for (Class<?> clazz : Reflections.scanPackage(strPackage))
				{
					if (clazz.isAnnotationPresent(Wireable.class))
					{
						Constructor<?> constr = clazz.getDeclaredConstructor();
						constr.setAccessible(true);
						classContext.put(clazz, constr.newInstance());
					}
				}

				for (Object o : classContext.values())
				{
					for (Field field : Reflections.findAnnotatedFields(o, Wired.class))
					{
						field.setAccessible(true);
						field.set(o, classContext.get(field.getType()));
					}
				}

				for (Object o : classContext.values())
				{
					invokeMethodsByPriority(o, OnCreate.class);
				}

				for (Object o : classContext.values())
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
				throw new ApplicationContextException("Could not initialize services", e);
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
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> clazz)
	{
		return (T) classContext.get(clazz);
	}
}
