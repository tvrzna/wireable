# wireable
[![javadoc](https://javadoc.io/badge2/cz.tvrzna/wireable/0.1.0/javadoc.svg)](https://javadoc.io/doc/cz.tvrzna/wireable/0.1.0) 
Simple Java singleton dependency injection via Reflections.

## What is wireable good for?
Almost everyone, who is used to dependency injections, likes the idea of injecting of services via an annotation in a class. For some microprojects or microservices could be Deltaspike, Spring, etc. incredible large with many function, that will not be used.

## How does it works?
Wireable allows to create very simple application context, that scans defined package (and its subpackages). All classes with annotation `@Wireable` are added into the mentioned application context as an instance. These classes will have injected all members with annotation `@Wired` with reflections and then all methods (without arguments) with annotations `@OnCreate` and `@OnStartup` are executed.

## But why?
Wireable is dependency-free with minimal size in units of kilobytes.

## Installation
```xml
<dependency>
    <groupId>cz.tvrzna</groupId>
    <artifactId>wireable</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Example
Try to image a simple HTTP server, that serves data from database.

__Main.java__
```java
package test.project;

import cz.tvrzna.wireable.ApplicationContext;
import cz.tvrzna.wireable.exceptions.ApplicationContextException;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			ApplicationContext.init("test.project");
		}
		catch (ApplicationContextException e)
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

@Wireable
public class DatabaseService
{
	private String[] data;

	@OnCreate
	private void init()
	{
		data = new String[]	{ "First", "Second", "Third" };
	}

	public String[] getData()
	{
		return data;
	}
}

```

__WebserverService.java__
```java
package test.project;

import cz.tvrzna.wireable.annotations.OnStartup;
import cz.tvrzna.wireable.annotations.Wireable;
import cz.tvrzna.wireable.annotations.Wired;

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

	public Response handleRequest(HttpRequest request)
	{
		return Response.ok(databaseService.getData()).build();
	}
}
```

### Explanation of example
 1. `ApplicationContext.init("test.project");` starts initialization of the ApplicationContext on the start of Main class and scans package `test.project`, which contains these classes.
 2. `DatabaseService` and `WebserverService` are initialized as instance, since they have `@Wireable` annotation, and are put into `HashMap` in ApplicationContext. These classes needs to have the **constructor without arguments**.
 3. `DatabaseService` and `WebserverService` are listed for `@Wired` annotation members. `WebserverService` has one, so it adds reference of `DatabaseService` from ApplicationContext on member named `databaseService`.
 4. Both service classes are scanned for methods without arguments with annotation `@OnCreate`. `DatabaseService` has private method init(), so this method is invoked and variable `data` is set.
 5. As it was in step before, classes are scanned again, but for methods without arguments with `@OnStartup` annotation. `WebserverService` has private method start(), so this method is invoked and server is started.