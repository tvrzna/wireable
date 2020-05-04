package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
}
