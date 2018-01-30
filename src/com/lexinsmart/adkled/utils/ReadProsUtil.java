package com.lexinsmart.adkled.utils;

import java.io.IOException;
import java.util.Properties;

public class ReadProsUtil {
	public static Properties pros = null;
	static {
		pros = new Properties();
		
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("remottxt.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
