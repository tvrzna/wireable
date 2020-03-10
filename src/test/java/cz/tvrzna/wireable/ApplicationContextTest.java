package cz.tvrzna.wireable;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.tvrzna.wireable.exceptions.ApplicationContextException;
import cz.tvrzna.wireable.test.TestWireableClass;
import cz.tvrzna.wireable.test.TestWireableClass2;
import cz.tvrzna.wireable.test2.TestWireableClassWithException;

public class ApplicationContextTest
{

	private void resetApplicationContext()
	{
		try
		{
			Field fLoaded = ApplicationContext.class.getDeclaredField("loaded");
			fLoaded.setAccessible(true);
			fLoaded.set(null, false);
		}
		catch (Exception e)
		{
			Assertions.fail("Could not reset ApplicationContext!");
		}
	}

	@Test
	public void testInitNoAnnotation() throws ApplicationContextException
	{
		resetApplicationContext();
		ApplicationContext.init(TestWireableClass.class.getPackage().getName().concat(".empty"));

		Assertions.assertNull(ApplicationContext.getInstance(TestWireableClass.class));
	}

	@Test
	public void testInit() throws ApplicationContextException
	{
		resetApplicationContext();
		ApplicationContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertNotNull(ApplicationContext.getInstance(TestWireableClass.class));
	}

	@Test
	public void testInitAndCallMethod() throws ApplicationContextException
	{
		resetApplicationContext();
		ApplicationContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertTrue(ApplicationContext.getInstance(TestWireableClass.class).testMethod());
	}

	@Test
	public void testDoubleInit() throws ApplicationContextException
	{
		resetApplicationContext();
		ApplicationContext.init(TestWireableClass.class.getPackage().getName());
		ApplicationContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertNotNull(ApplicationContext.getInstance(TestWireableClass.class));
	}

	@Test
	public void testInitSecondClassFromPackage() throws ApplicationContextException
	{
		resetApplicationContext();
		ApplicationContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertNotNull(ApplicationContext.getInstance(TestWireableClass2.class));
	}

	@Test
	public void testInitWithException() throws ApplicationContextException
	{
		resetApplicationContext();
		Assertions.assertThrows(ApplicationContextException.class, () -> ApplicationContext.init(TestWireableClassWithException.class.getPackage().getName()));
	}
}
