package com.clsaa.dop.server.application.dao;

import com.clsaa.dop.server.application.model.po.KubeCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KubeCredentialRepository extends JpaRepository<KubeCredential, Long> {

}
