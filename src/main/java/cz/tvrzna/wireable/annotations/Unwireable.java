package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cz.tvrzna.wireable.enums.PriorityLevel;

/**
 * This annotation defines classes, that are loaded into
 * <code>WireableContext</code>. These classes could not be injected into
 * another <code>Wireable</code>.
 *
 * @since 0.2.0
 * @author michalt
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Unwireable
{
	/**
	 * Priority level defines order in which is classes loaded/handled.
	 *
	 * @since 0.4.0
	 * @return the priority level
	 */
	PriorityLevel priority() default PriorityLevel.NORMAL;
}
