package com.cooksys.ftd.week3.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
	public static Map<String, Class<? extends AbstractCommand>> registry = new HashMap<>();
	static {
		registry.put(RegisterUser.class.getSimpleName(), RegisterUser.class);
	}
}
