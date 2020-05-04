package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation defines methods, that are invoked on
 * <code>WireableContext.fireEvent()</code>.
 *
 * @since 0.2.0
 * @author michalt
 * 
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnEvent
{
	String value();
}
