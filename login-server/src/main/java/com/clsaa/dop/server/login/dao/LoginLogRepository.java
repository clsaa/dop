package com.clsaa.dop.server.login.dao;


import com.clsaa.dop.server.login.model.po.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joyren
 */
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {


}
