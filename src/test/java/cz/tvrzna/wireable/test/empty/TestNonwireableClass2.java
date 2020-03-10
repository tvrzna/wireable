package cz.tvrzna.wireable.test.empty;

import cz.tvrzna.wireable.annotations.OnCreate;

public class TestNonwireableClass2
{
	@OnCreate
	private void onCreate()
	{
		System.out.println("This method should be never called");
	}
}
