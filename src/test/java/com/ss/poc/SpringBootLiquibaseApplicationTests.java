package com.ss.poc;

import static org.junit.Assert.*;

import java.net.ConnectException;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.OutputCapture;
import org.springframework.core.NestedCheckedException;

@Ignore
public class SpringBootLiquibaseApplicationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void testDefaultSettings() throws Exception {
		try {
			SpringBootLiquibaseApplication.main(new String[] { "--server.port=0" });
		}
		catch (IllegalStateException ex) {
			if (serverNotRunning(ex)) {
				return;
			}
		}
		String output = this.outputCapture.toString();
		assertTrue("Wrong output: " + output,
				output.contains("Successfully acquired change log lock")
						&& output.contains("Creating database history "
								+ "table with name: PUBLIC.DATABASECHANGELOG")
				&& output.contains("Table person created")
				&& output.contains("ChangeSet classpath:/db/"
						+ "changelog/db.changelog-master.yaml::1::"
						+ "marceloverdijk ran successfully")
				&& output.contains("New row inserted into apm_tag_details")
				&& output.contains("ChangeSet classpath:/db/changelog/"
						+ "db.changelog-master.yaml::2::"
						+ "sauravsingh ran successfully")
				&& output.contains("Successfully released change log lock"));
	}

	private boolean serverNotRunning(IllegalStateException ex) {
		@SuppressWarnings("serial")
		NestedCheckedException nested = new NestedCheckedException("failed", ex) {
		};
		if (nested.contains(ConnectException.class)) {
			Throwable root = nested.getRootCause();
			if (root.getMessage().contains("Connection refused")) {
				return true;
			}
		}
		return false;
	}

}
