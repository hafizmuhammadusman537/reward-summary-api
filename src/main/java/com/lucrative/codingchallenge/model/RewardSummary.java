package com.lucrative.codingchallenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RewardSummary {
  private String customer;
  private String month;
  private int rewardPoints;
}
