package cz.tvrzna.wireable.test;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;

@Wireable
public class TestWireableClass
{
	@Wired
	private TestWireableClass2 clazz2;

	@OnCreate
	private void onCreate()
	{
		System.out.println("onCreate in TestWireableClass");
	}

	public boolean testMethod() {
		try {
			clazz2.testMethod();
			return true;
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}