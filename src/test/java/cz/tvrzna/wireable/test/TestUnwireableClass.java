package cz.tvrzna.wireable.test;

import org.junit.jupiter.api.Assertions;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Unwireable;
import cz.tvrzna.wireable.annotations.Wired;

@Unwireable
public class TestUnwireableClass
{
	@Wired
	private TestWireableClass testWireableClass;
	@Wired
	private TestUnwireableClass testUnwireableClass;

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
