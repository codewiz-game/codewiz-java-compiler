package com.github.crob1140.codewiz.database.dialects;

import java.util.Map;

public interface SQLDialect {
	String createConnectionUrl(String subprotocol, String host, Integer port, String schema, Map<String, Object> options);
}
