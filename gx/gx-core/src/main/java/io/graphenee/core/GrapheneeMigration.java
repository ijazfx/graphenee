package io.graphenee.core;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

import io.graphenee.util.DataSourceUtil;

@Service
public class GrapheneeMigration {

	public void migrate(Flyway flyway) {
		DataSource dataSource = flyway.getConfiguration().getDataSource();
		String dbVendor = DataSourceUtil.determineDbVendor(dataSource);
		Flyway fw = Flyway.configure().dataSource(dataSource).locations("classpath:db/graphenee/migration/" + dbVendor).table("graphenee_schema_version").baselineOnMigrate(true)
				.baselineVersion("0").load();
		fw.migrate();
	}

}
