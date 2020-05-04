package cz.tvrzna.wireable.test4;

import org.junit.jupiter.api.Assertions;

import cz.tvrzna.wireable.annotations.OnEvent;
import cz.tvrzna.wireable.annotations.Wireable;

@Wireable
public class TestOnEvent
{
	private int event1Count = 0;
	private int event2Count = 0;

	@OnEvent("event1")
	private void event1_method1()
	{
		event1Count++;
	}

	@OnEvent("event1")
	private void event1_method2(String name)
	{
		Assertions.assertNotNull(name);
		event1Count++;
	}

	@OnEvent("event1")
	private void event1_method3(String name, Boolean bool)
	{
		Assertions.assertNotNull(name);
		Assertions.assertNull(bool);
		event1Count++;
	}

	@OnEvent("event2")
	private void event2_method1(String name, Boolean bool)
	{
		Assertions.assertNotNull(name);
		Assertions.assertTrue(bool);
		event2Count++;
	}

	@OnEvent("event2")
	private void event2_method2(String name)
	{
		Assertions.assertNotNull(name);
		event2Count++;
	}

	@OnEvent("event3")
	private void event3_method1(String name, String bool)
	{
		Assertions.fail();		
	}

	public int getEvent1Count()
	{
		return event1Count;
	}

	public int getEvent2Count()
	{
		return event2Count;
	}

}
