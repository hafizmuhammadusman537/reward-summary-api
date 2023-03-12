package com.lucrative.codingchallenge.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class RewardSummaryResponseVo {
  private List<RewardSummary> perMonthRewardPoints;
  private Map<String, Integer>  totalRewardPoints;
}
