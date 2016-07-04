package com.cooksys.ftd.week3.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
	public static Map<String, Class<? extends AbstractCommand>> registry = new HashMap<>();
	static {
		registry.put(RegisterUser.class.getSimpleName(), RegisterUser.class);
		registry.put(Login.class.getSimpleName(), Login.class);
		registry.put(Upload.class.getSimpleName(), Upload.class);
		registry.put(Download.class.getSimpleName(), Download.class);
		registry.put(ListFiles.class.getSimpleName(), ListFiles.class);

	}
}
