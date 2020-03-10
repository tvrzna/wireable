package cz.tvrzna.wireable.test;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.Wired;

public class TestNonwireableClass
{
	@Wired
	private TestWireableClass clazz1;

	@OnCreate
	private void onCreate()
	{
		System.out.println("This method should be never called");
	}
}
