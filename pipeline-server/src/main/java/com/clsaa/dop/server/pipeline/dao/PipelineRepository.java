package com.clsaa.dop.server.pipeline.dao;

import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;



public interface PipelineRepository extends MongoRepository<Pipeline, ObjectId>{
    @Query(value = "{'isDeleted': false}")
    List<Pipeline> findAllNoDeleted();

    @Query(value = "{'_id': ?0}")
    Pipeline findById(String id);

    @Query(value = "{'isDeleted': 'false'}")
    List<Pipeline> findByCuser(Long cuser);

    @Query(value = "{'isDeleted': 'false'}")
    List<Pipeline> findByAppEnvId(Long envid);

    List<Pipeline> findByAppId(Long appid);
}
