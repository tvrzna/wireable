package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tvrzna.wireable.enums.PriorityLevel;

/**
 * This annotation defines methods, that are started after all {@link Wireable}
 * objects are initialized and all {@link OnCreate} methods are finished. These
 * methods could be <code>private</code>, but could not have any parameter.
 *
 * @since 0.1.0
 * @author michalt
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnStartup
{

	/**
	 * Priority.
	 *
	 * @since 0.2.0
	 * @return the on create priority
	 */
	PriorityLevel priority() default PriorityLevel.NORMAL;

}
