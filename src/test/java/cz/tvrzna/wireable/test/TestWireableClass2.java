package cz.tvrzna.wireable.test;

import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;

@Wireable
public class TestWireableClass2
{
	@Wired
	private TestWireableClass clazz1;

	@OnStartup
	private void onStartup()
	{
		System.out.println("onStartup in TestWireableClass2");
	}

	public void testMethod() {
		System.out.println("Test method of initialized class TestWireableClass2.");
	}
}
