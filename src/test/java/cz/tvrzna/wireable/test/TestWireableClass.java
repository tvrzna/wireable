package cz.tvrzna.wireable.test;

import org.junit.jupiter.api.Assertions;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.enums.PriorityLevel;

@Wireable(priority = PriorityLevel.HIGH)
public class TestWireableClass
{
	@Wired
	private TestWireableClass2 clazz2;

	@Wired
	private TestUnwireableClass testUnwireableClass;

	@OnCreate
	private void onCreate()
	{
		System.out.println("onCreate in TestWireableClass");
	}

	@OnStartup(priority = PriorityLevel.LOW)
	private void onStartupLow()
	{
		Assertions.assertNull(testUnwireableClass);
	}

	public boolean testMethod()
	{
		try
		{
			clazz2.testMethod();
			return true;
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}