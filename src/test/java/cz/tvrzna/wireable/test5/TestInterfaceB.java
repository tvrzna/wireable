package cz.tvrzna.wireable.test5;

import cz.tvrzna.wireable.annotations.Wireable;

@Wireable(priorityFor = ITestInterface2.class)
public class TestInterfaceB implements ITestInterface, ITestInterface2
{

	@Override
	public void hello()
	{
		System.out.println("Hello from class B");
	}

	@Override
	public void bye()
	{
		System.out.println("Bye from class B");
	}

}
