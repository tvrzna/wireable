# wireable
[![Release](https://img.shields.io/github/release/tvrzna/wireable.svg)](https://github.com/tvrzna/wireable/releases/latest)
[![javadoc](https://javadoc.io/badge2/cz.tvrzna/wireable/0.3.3/javadoc.svg)](https://javadoc.io/doc/cz.tvrzna/wireable/0.3.3)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/tvrzna/wireable/Build)](https://github.com/tvrzna/wireable/actions?query=workflow:Build)
[![GitHub](https://img.shields.io/github/license/tvrzna/wireable)](https://github.com/tvrzna/wireable)

Simple Java singleton dependency injection via Reflections.

## What is wireable good for?
Almost everyone, who is used to dependency injections, likes the idea of injecting of services via an annotation in a class. For some microprojects or microservices could be Deltaspike, Spring, etc. incredible large with many function, that will not be used.

## How does it works?
Wireable allows to create very simple application context, that scans defined package (and its subpackages). All classes with annotation `@Wireable` are added into the mentioned application context as an instance. These classes will have injected all members with annotation `@Wired` with reflections and then all methods (without arguments) with annotations `@OnCreate` and `@OnStartup` are executed.

## Interfaces as @Wired
Since `0.3.0` interfaces are supported as @Wired classes. If multiple classes points to similar interface, it it better to specify in your `@Wireable` annotation the `priorityFor` value by interface class.

## But why?
Wireable is dependency-free with minimal size in units of kilobytes.

## Installation
```xml
<dependency>
    <groupId>cz.tvrzna</groupId>
    <artifactId>wireable</artifactId>
    <version>0.3.3</version>
</dependency>
```

## Example
Try to image a simple HTTP server, that serves data from database.

__Main.java__
```java
package test.project;

import cz.tvrzna.wireable.WireableContext;
import cz.tvrzna.wireable.exceptions.WireableException;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			WireableContext.init("test.project");
		}
		catch (WireableException e)
		{
			e.printStackTrace();
		}
	}
}
```

__DatabaseService.java__
```java
package test.project;

import cz.tvrzna.wireable.annotations.OnCreate;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.enums.PriorityLevel;

@Wireable
public class DatabaseService
{
	private String[] data;

	@OnCreate
	private void init()
	{
		data = new String[]	{ "First", "Second", "Third" };
	}

	@OnCreate(priority = PriorityLevel.LOW)
	private void reinit()
	{
		data = new String[] { "First", "Second", "Third", "Fourth"};
	}

	public String[] getData()
	{
		return data;
	}

	public void removeUserSession(Long id)
	{
		// Remove token
	}
}

```

__WebserverService.java__
```java
package test.project;

import cz.tvrzna.wireable.WireableContext;
import cz.tvrzna.wireable.annotations.OnEvent;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;
import cz.tvrzna.wireable.exceptions.WireableException;

@Wireable
public class WebserverService
{
	@Wired
	private DatabaseService databaseService;

	@OnStartup
	private void start()
	{
		// Create endpoint, that points on URL and method handleRequest
		// Start server
	}

	public Response handleRequest(HttpRequest request) throws WireableException
	{
		WireableContext.fireEvent("doLogout", 1200l);
		return Response.ok(databaseService.getData()).build();
	}
}
```

__WatcherService.java__
```java
package test.project;

import cz.tvrzna.wireable.WireableContext;
import cz.tvrzna.wireable.annotations.OnEvent;
import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Unwireable;
import cz.tvrzna.wireable.exceptions.WireableException;

@Unwireable
public class WatcherService
{
	@OnStartup
	private void onStartup()
	{
		// Watch some data changes
	}

	@OnEvent("doLogout")
	public void logout(Long id) throws WireableException
	{
		LateClass watcher = new LateClass();
		WireableContext.wireObjects(watcher);
		watcher.handleId(id);
	}
}
```

__LateClass.java__
```java
package test.project;

import cz.tvrzna.wireable.annotations.Wired;

public class LateClass
{
	@Wired
	private DatabaseService databaseService;

	public void handleId(Long id)
	{
		databaseService.removeUserSession(id);
	}
}

```

### Explanation of example
 1. `ApplicationContext.init("test.project");` starts initialization of the ApplicationContext on the start of Main class and scans package `test.project`, which contains these classes.
 2. `DatabaseService` and `WebserverService` are initialized as instance, since they have `@Wireable` or `@Unwireable` annotation, and are put into `HashMap` in ApplicationContext. These classes needs to have the **constructor without arguments**.
 3. `DatabaseService` and `WebserverService` are listed for `@Wired` annotation members. `WebserverService` has one, so it adds reference of `DatabaseService` from ApplicationContext on member named `databaseService`. `@Unwireable` clases could not by `@Wired`.
 4. Both service classes are scanned for methods without arguments with annotation `@OnCreate`, it is aware of OnCreate priority level. `DatabaseService` has private method init(), so this method is invoked and variable `data` is set.
 5. As it was in step before, classes are scanned again, but for methods without arguments with `@OnStartup` annotation. `WebserverService` has private method start(), so this method is invoked and server is started.
 6. If handleRequest method is called, it calls event `doLogout`. That invokes method annotated as `@OnEvent("doLogout")` with all passed arguments.
 7. When logout method of `WatcherService` is called, it creates new instance of `LateClass`. This method is out of context, but all `@Wired` annotated members, becames accessable. This kind of classes could have any constructor, but `@OnCreate`, `@OrStartup` nor `@OnEvent` methods are not invoked.
```
