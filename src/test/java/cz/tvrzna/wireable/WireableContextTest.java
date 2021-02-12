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
import cz.tvrzna.wireable.test5.ITestInterface;
import cz.tvrzna.wireable.test5.ITestInterface2;
import cz.tvrzna.wireable.test5.TestInterfaceA;

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

		WireableContext.init(TestOnEvent.class.getPackage().getName());
		WireableContext.fireEvent(null);
		WireableContext.fireEvent("unknown-event");
		WireableContext.fireEvent("event1", "value");
		WireableContext.fireEvent("event2", "value", true);
		Assertions.assertThrows(WireableException.class, () -> WireableContext.fireEvent("event3", "value", true));
		WireableContext.fireEvent("event4a");
		WireableContext.fireEvent("event4b");

		Assertions.assertEquals(3, WireableContext.getInstance(TestOnEvent.class).getEvent1Count());
		Assertions.assertEquals(2, WireableContext.getInstance(TestOnEvent.class).getEvent2Count());
		Assertions.assertEquals(2, WireableContext.getInstance(TestOnEvent.class).getEvent4Count());
	}

	@Test
	public void testOnEventAsync() throws WireableException, InterruptedException
	{
		resetWireableContext();

		WireableContext.init(TestOnEvent.class.getPackage().getName());

		WireableContext.fireEventAsync("event1", "value");
		WireableContext.fireEventAsync("event2", (e) -> {
			Assertions.fail("Unexpected error!");
		}, "value", true);
		WireableContext.fireEventAsync("event3", "value", true);
		WireableContext.fireEventAsync("event3", (e) -> {
			System.out.println(e.getMessage());
		}, "value", true);
	}

	@Test
	public void testWireObjects() throws WireableException
	{
		resetWireableContext();

		TestWireObjectsClass test = new TestWireObjectsClass();
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		test.testBefore();
		WireableContext.wireObjects(test, test, test);
		test.testAfter();
	}

	@Test()
	public void testWireNoObjects() throws WireableException
	{
		resetWireableContext();

		TestWireObjectsClass test = new TestWireObjectsClass();
		WireableContext.init(TestWireableClass.class.getPackage().getName());

		try
		{
			WireableContext.wireObjects(test, null, null);
			Assertions.fail("Exception should have been thrown.");
		}
		catch (WireableException e)
		{
			// Expected behaviour
		}
	}

	@Test
	public void testWireInterface() throws WireableException
	{
		resetWireableContext();
		WireableContext.init(TestInterfaceA.class.getPackage().getName());

		ITestInterface first = WireableContext.getInstance(ITestInterface.class);
		first.hello();

		ITestInterface2 second = WireableContext.getInstance(ITestInterface2.class);
		second.bye();
	}
}
