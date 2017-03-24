package com.github.crob1140.codewiz.database.dialects;

import java.util.Map;
import java.util.stream.Collectors;

public class MySQLDialect implements SQLDialect {
	static {
		try {
		    Class.forName("com.mysql.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
	
	public MySQLDialect() {
		
	}

	@Override
	public String createConnectionUrl(String subprotocol, String host, Integer port, String schema, Map<String, Object> options) {
		return String.format("jdbc:%s://%s:%d/%s?%s", subprotocol, host, port, schema, options.entrySet().stream()
				.map(entry -> entry.getKey() + "=" + entry.getValue())
				.collect(Collectors.joining("&")));
	}
}
