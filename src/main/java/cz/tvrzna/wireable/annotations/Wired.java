package cz.tvrzna.wireable.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation defines members of {@link Wireable} classes, that are
 * injected from <code>ApplicationContext</code>. If this annotation is used in
 * classes, that are not {@link Wireable}, they will not be injected.
 *
 * @since 0.1.0
 * @author michalt
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Wired
{
}
