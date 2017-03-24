package com.github.crob1140.codewiz.database;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

public class DatabaseUpdater {
	
	private static final Contexts DEFAULT_CONTEXTS = null;
	private static final String MASTER_CHANGELOG_FILE_NAME = "changelog-master.yaml";
	
	private Connection connection;
	
	public DatabaseUpdater(Connection connection) {
		this.connection = connection;
	}
	
	public void update() throws LiquibaseException {		
		try {
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
			File changelogsDirectory = new File(this.getClass().getResource("changelogs").toURI());
			Liquibase liquibase = new Liquibase(MASTER_CHANGELOG_FILE_NAME, new FileSystemResourceAccessor(changelogsDirectory.getAbsolutePath()), database);
			liquibase.update(DEFAULT_CONTEXTS);
		} catch (URISyntaxException e) {
			// Do nothing - the URI is defined statically so this won't occur
		}
	}
}
