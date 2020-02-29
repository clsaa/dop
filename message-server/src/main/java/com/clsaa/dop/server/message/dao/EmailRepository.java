package com.clsaa.dop.server.message.dao;

import com.clsaa.dop.server.message.model.po.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Email持久层
 *
 * @author joyren
 */
public interface EmailRepository extends MongoRepository<Email, String> {
}
