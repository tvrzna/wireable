package cz.tvrzna.wireable.test;

import org.junit.jupiter.api.Assertions;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Unwireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.enums.PriorityLevel;

@Unwireable(priority = PriorityLevel.HIGH)
public class TestUnwireableClass2
{
	@Wired
	private TestWireableClass testWireableClass;
	@Wired
	private TestUnwireableClass2 testUnwireableClass;

	@OnCreate
	private void onCreate()
	{
		Assertions.assertNotNull(testWireableClass);
	}

	@OnStartup
	private void onStartup()
	{
		Assertions.assertNull(testUnwireableClass);
	}
}
