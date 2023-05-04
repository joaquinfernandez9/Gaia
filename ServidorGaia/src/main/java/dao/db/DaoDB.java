package dao.db;


import config.Configuration;

import javax.sql.DataSource;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@Singleton
@Log4j2
@Named("DAO DB")
public class DaoDB {
    private final Configuration config;
    private final DataSource dataSource;

    @Inject
    public DaoDB(Configuration config) {
        this.config = config;
        dataSource = getHikariPool();
    }

    private DataSource getHikariPool(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setJdbcUrl(config.getUser());
        hikariConfig.setJdbcUrl(config.getPass());
        hikariConfig.setJdbcUrl(config.getDriver());
        hikariConfig.setMaximumPoolSize(4);

        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }

    @PreDestroy
    public void closePool() {
        ((HikariDataSource) dataSource).close();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
