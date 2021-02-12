package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation defines repeatable annotations on methods, that are invoked
 * on <code>WireableContext.fireEvent()</code>.
 *
 * @author michalt
 * @since 0.3.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface OnEvents
{
	OnEvent[] value();
}
