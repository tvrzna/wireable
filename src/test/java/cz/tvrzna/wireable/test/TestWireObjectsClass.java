package cz.tvrzna.wireable.test;

import org.junit.jupiter.api.Assertions;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wired;

public class TestWireObjectsClass
{
	@Wired
	private TestWireableClass test;

	@OnCreate
	private void onCreate()
	{
		Assertions.fail("This method should not be invoked at all");
	}

	@OnStartup
	private void onStartup()
	{
		Assertions.fail("This method should not be invoked at all");
	}
	
	public void testBefore()
	{
		Assertions.assertNull(test);
	}

	public void testAfter()
	{
		Assertions.assertTrue(test.testMethod());
	}
}
