package com.nexters.winepick.survey.repository;

import com.nexters.winepick.survey.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Integer> {
}
