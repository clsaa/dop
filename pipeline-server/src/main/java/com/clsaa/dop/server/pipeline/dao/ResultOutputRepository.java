package com.clsaa.dop.server.pipeline.dao;
import com.clsaa.dop.server.pipeline.model.po.Pipeline;
import com.clsaa.dop.server.pipeline.model.po.ResultOutput;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResultOutputRepository extends MongoRepository<ResultOutput, ObjectId> {
    List<ResultOutput> findByPipelineId(String pipelineId);
}
