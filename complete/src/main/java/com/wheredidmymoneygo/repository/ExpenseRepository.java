package com.wheredidmymoneygo.repository;

import com.wheredidmymoneygo.model.ExpenseModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<ExpenseModel, String> {
    List<ExpenseModel> findByTimestampBetween(Instant from, Instant till);
}
