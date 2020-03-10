package cz.tvrzna.wireable.test2;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.Wireable;

@Wireable
public class TestWireableClassWithException
{

	@OnCreate
	private void init() throws Exception
	{
		throw new Exception();
	}

}
