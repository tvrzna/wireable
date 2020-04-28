package cz.tvrzna.wireable.test3;

import org.junit.jupiter.api.Assertions;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.enums.PriorityLevel;

@Wireable
public class TestOnCreatePriority
{
	private int runCount = 0;


	@OnCreate(priority = PriorityLevel.HIGH)
	private void createHighPriority()
	{
		runCount++;
		Assertions.assertEquals(1, runCount);
	}

	@OnCreate
	private void createNormalPriority()
	{
		runCount++;
		Assertions.assertEquals(2, runCount);
	}

	@OnCreate(priority = PriorityLevel.LOW)
	private void createLowPriority()
	{
		runCount++;
		Assertions.assertEquals(3, runCount);
	}


	@OnStartup(priority = PriorityLevel.LOW)
	private void startupLowPriority()
	{
		runCount++;
		Assertions.assertEquals(6, runCount);
	}

	@OnStartup(priority = PriorityLevel.HIGH)
	private void startupHighPriority()
	{
		runCount++;
		Assertions.assertEquals(4, runCount);
	}

	@OnStartup
	private void startupNormalPriority()
	{
		runCount++;
		Assertions.assertEquals(5, runCount);
	}

	public int getRunCount()
	{
		return runCount;
	}

}
