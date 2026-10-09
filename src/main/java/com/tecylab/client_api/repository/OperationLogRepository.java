package com.tecylab.client_api.repository;

import com.tecylab.client_api.model.OperationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends MongoRepository<OperationLog, String> {
}
