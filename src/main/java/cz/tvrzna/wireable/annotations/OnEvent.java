package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation defines methods, that are invoked on
 * <code>WireableContext.fireEvent()</code>.
 *
 * @author michalt
 * @since 0.2.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(OnEvents.class)
public @interface OnEvent
{
	String value();
}
