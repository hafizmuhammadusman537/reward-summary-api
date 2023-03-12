package com.lucrative.codingchallenge.service;

import com.lucrative.codingchallenge.model.RewardSummary;
import com.lucrative.codingchallenge.model.RewardSummaryResponseVo;
import com.lucrative.codingchallenge.model.Transaction;
import java.util.List;
import java.util.Map;

public interface RewardService {
  List<RewardSummary> calculateRewardsPerMonth(List<Transaction> transactions);

  Map<String, Integer> calculateRewardSummary(List<Transaction> transactions);

  RewardSummaryResponseVo calculateCompleteRewardSummary(List<Transaction> transactions);
}

