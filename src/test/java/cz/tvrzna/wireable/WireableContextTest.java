package cz.tvrzna.wireable;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.tvrzna.wireable.exceptions.WireableException;
import cz.tvrzna.wireable.test.TestWireObjectsClass;
import cz.tvrzna.wireable.test.TestWireableClass;
import cz.tvrzna.wireable.test.TestWireableClass2;
import cz.tvrzna.wireable.test2.TestWireableClassWithException;
import cz.tvrzna.wireable.test3.TestOnCreatePriority;
import cz.tvrzna.wireable.test4.TestOnEvent;

public class WireableContextTest
{

	private void resetWireableContext()
	{
		try
		{
			Field fLoaded = WireableContext.class.getDeclaredField("loaded");
			fLoaded.setAccessible(true);
			fLoaded.set(null, false);
		}
		catch (Exception e)
		{
			Assertions.fail("Could not reset WireableContext!");
		}
	}

	@Test
	public void testInitNoAnnotation() throws WireableException
	{
		resetWireableContext();
		WireableContext.init(TestWireableClass.class.getPackage().getName().concat(".empty"));

		Assertions.assertNull(WireableContext.getInstance(TestWireableClass.class));
	}

	@Test
	public void testInit() throws WireableException
	{
		resetWireableContext();
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertNotNull(WireableContext.getInstance(TestWireableClass.class));
	}

	@Test
	public void testInitAndCallMethod() throws WireableException
	{
		resetWireableContext();
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertTrue(WireableContext.getInstance(TestWireableClass.class).testMethod());
	}

	@Test
	public void testDoubleInit() throws WireableException
	{
		resetWireableContext();
		WireableContext.init(TestWireableClass.class.getPackage().getName());
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertNotNull(WireableContext.getInstance(TestWireableClass.class));
	}

	@Test
	public void testInitSecondClassFromPackage() throws WireableException
	{
		resetWireableContext();
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		Assertions.assertNotNull(WireableContext.getInstance(TestWireableClass2.class));
	}

	@Test
	public void testInitWithException() throws WireableException
	{
		resetWireableContext();
		Assertions.assertThrows(WireableException.class, () -> WireableContext.init(TestWireableClassWithException.class.getPackage().getName()));
	}

	@Test
	public void testOnCreatePriority() throws WireableException
	{
		resetWireableContext();

		WireableContext.init(TestOnCreatePriority.class.getPackage().getName());

		Assertions.assertEquals(6, WireableContext.getInstance(TestOnCreatePriority.class).getRunCount());
	}

	@Test
	public void testOnEvent() throws WireableException
	{
		resetWireableContext();

		WireableContext.init(TestOnEvent.class.getPackageName());
		WireableContext.fireEvent(null);
		WireableContext.fireEvent("event1", "value");
		WireableContext.fireEvent("event2", "value", true);
		Assertions.assertThrows(WireableException.class, () -> WireableContext.fireEvent("event3", "value", true));

		Assertions.assertEquals(3, WireableContext.getInstance(TestOnEvent.class).getEvent1Count());
		Assertions.assertEquals(2, WireableContext.getInstance(TestOnEvent.class).getEvent2Count());
	}

	@Test
	@Deprecated
	public void testApplicationContext() throws WireableException
	{
		resetWireableContext();

		ApplicationContext.init(TestOnEvent.class.getPackageName());
		ApplicationContext.fireEvent(null);
		ApplicationContext.getInstance(TestOnEvent.class);
	}

	@Test
	public void testWireObjects() throws WireableException
	{
		resetWireableContext();

		TestWireObjectsClass test = new TestWireObjectsClass();
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		test.testBefore();
		WireableContext.wireObjects(test);
		test.testAfter();
	}
}
