package cz.tvrzna.wireable.test5;

import cz.tvrzna.wireable.annotations.Wireable;

@Wireable
public class TestInterfaceA implements ITestInterface, ITestInterface2
{

	@Override
	public void hello()
	{
		System.out.println("Hello from class A");
	}

	@Override
	public void bye()
	{
		System.out.println("Bye from class A");
	}

}
