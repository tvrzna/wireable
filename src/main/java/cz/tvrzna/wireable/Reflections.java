package cz.tvrzna.wireable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The utility class that provides help methods for reflections.
 *
 * @since 0.1.0
 * @author michalt
 */
public final class Reflections
{

	/**
	 * Instantiates a new reflections.
	 */
	private Reflections()
	{
	}

	/**
	 * Find annotated methods.
	 *
	 * @param <T>
	 *          the generic type
	 * @param obj
	 *          the obj
	 * @param annoClazz
	 *          the anno clazz
	 * @return the method[]
	 */
	public static <T extends Annotation> Method[] findAnnotatedMethods(Object obj, Class<T> annoClazz)
	{
		List<Method> methods = new ArrayList<>();
		Class<?> clazz = obj.getClass();
		while (clazz != null && clazz != Object.class)
		{
			for (Method method : clazz.getDeclaredMethods())
			{
				if (method.isAnnotationPresent(annoClazz))
				{
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods.toArray(new Method[methods.size()]);
	}

	/**
	 * Find annotated fields.
	 *
	 * @param <T>
	 *          the generic type
	 * @param obj
	 *          the obj
	 * @param annoClazz
	 *          the anno clazz
	 * @return the field[]
	 */
	public static <T extends Annotation> Field[] findAnnotatedFields(Object obj, Class<T> annoClazz)
	{
		List<Field> fields = new ArrayList<>();
		Class<?> clazz = obj.getClass();
		while (clazz != null)
		{
			for (Field field : clazz.getDeclaredFields())
			{
				if (field.isAnnotationPresent(annoClazz))
				{
					fields.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * Scan package for all available classes in any level.
	 *
	 * @param packageName
	 *          the package name
	 * @return the class[]
	 * @throws ClassNotFoundException
	 *           the class not found exception
	 * @throws IOException
	 *           Signals that an I/O exception has occurred.
	 */
	public static Class<?>[] scanPackage(String packageName) throws ClassNotFoundException, IOException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace(".", "/");
		File baseDir = new File(URLDecoder.decode(classLoader.getResource(path).getPath(), StandardCharsets.UTF_8.name()));
		List<Class<?>> classes = new ArrayList<>();
		if (baseDir.isDirectory())
		{
			classes.addAll(findClasses(baseDir, packageName));
		}
		else
		{
			JarFile jarFile = new JarFile(baseDir.getPath().substring(5, baseDir.getPath().indexOf("!")));
			classes.addAll(findClasses(jarFile, packageName, path));
			jarFile.close();
		}
		classes.sort(Comparator.comparing(Class::getName, Comparator.naturalOrder()));
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Find classes in currently developed project.
	 *
	 * @param directory
	 *          the directory
	 * @param packageName
	 *          the package name
	 * @return the list
	 * @throws ClassNotFoundException
	 *           the class not found exception
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException
	{
		List<Class<?>> classes = new ArrayList<>();
		File[] files = directory.listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				if (!file.getName().contains("."))
				{
					classes.addAll(findClasses(file, packageName + "." + file.getName()));
				}
			}
			else if (file.getName().endsWith(".class"))
			{
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	/**
	 * Find classes in defined <code>jarFile</code>.
	 *
	 * @param jarFile
	 *          the jar file
	 * @param packageName
	 *          the package name
	 * @param path
	 *          the path
	 * @return the list
	 * @throws ClassNotFoundException
	 *           the class not found exception
	 */
	private static List<Class<?>> findClasses(JarFile jarFile, String packageName, String path) throws ClassNotFoundException
	{
		List<Class<?>> classes = new ArrayList<>();

		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements())
		{
			JarEntry entry = entries.nextElement();
			if (entry.getName().contains(path) && entry.getName().endsWith(".class"))
			{
				String className = entry.getName().replace("/", ".").substring(0, entry.getName().length() - 6);
				classes.add(Class.forName(className));
			}
		}

		return classes;
	}
}
