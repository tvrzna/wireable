package cz.tvrzna.wireable;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.tvrzna.wireable.exceptions.ApplicationContextException;
import cz.tvrzna.wireable.test.TestWireableClass;
import cz.tvrzna.wireable.test.TestWireableClass2;
import cz.tvrzna.wireable.test2.TestWireableClassWithException;
import cz.tvrzna.wireable.test3.TestOnCreatePriority;
import cz.tvrzna.wireable.test4.TestOnEvent;

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

	@Test
	public void testOnCreatePriority() throws ApplicationContextException
	{
		resetApplicationContext();

		ApplicationContext.init(TestOnCreatePriority.class.getPackage().getName());

		Assertions.assertEquals(6, ApplicationContext.getInstance(TestOnCreatePriority.class).getRunCount());
	}

	@Test
	public void testOnEvent() throws ApplicationContextException
	{
		resetApplicationContext();

		ApplicationContext.init(TestOnEvent.class.getPackageName());
		ApplicationContext.fireEvent(null);
		ApplicationContext.fireEvent("event1", "value");
		ApplicationContext.fireEvent("event2", "value", true);
		Assertions.assertThrows(ApplicationContextException.class, () -> ApplicationContext.fireEvent("event3", "value", true));

		Assertions.assertEquals(3, ApplicationContext.getInstance(TestOnEvent.class).getEvent1Count());
		Assertions.assertEquals(2, ApplicationContext.getInstance(TestOnEvent.class).getEvent2Count());

	}
}
