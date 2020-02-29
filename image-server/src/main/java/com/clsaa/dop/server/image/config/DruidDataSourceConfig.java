package com.clsaa.dop.server.image.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * Druid数据源配置类
 *
 * @author 任贵杰
 */
@Getter
@Setter
@SpringBootConfiguration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidDataSourceConfig {

  private String url;
  private String username;  private String password;
  private String driverClassName;
  private int maxActive;
  private int maxIdle;
  private int minIdle;
  private int initialSize;
  private int maxWait;

  /**
   * 普通数据源(主数据源)必须配置，spring启动时会执行初始化数据操作（无论是否真的需要），选择查找DataSource class类型的数据源
   *
   * @return {@link DataSource}
   */
  @Primary
  @Bean(name = "datasource.druid")
  public DataSource createDataSource() {
    DruidDataSource datasource = new DruidDataSource();
    //base
    datasource.setUrl(this.url);
    datasource.setUsername(this.username);
    datasource.setPassword(this.password);
    datasource.setDriverClassName(this.driverClassName);
    //configuration
    datasource.setMaxActive(this.maxActive);
    datasource.setMinIdle(this.maxIdle);
    datasource.setMinIdle(this.minIdle);
    datasource.setInitialSize(this.initialSize);
    datasource.setMaxWait(this.maxWait);
    return datasource;
  }

}