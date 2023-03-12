package com.lucrative.codingchallenge.service.impl;

import com.lucrative.codingchallenge.model.RewardSummary;
import com.lucrative.codingchallenge.model.RewardSummaryResponseVo;
import com.lucrative.codingchallenge.model.Transaction;
import com.lucrative.codingchallenge.service.RewardService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RewardServiceImpl implements RewardService {

  private static final int POINTS_THRESHOLD_1 = 50;
  private static final int POINTS_THRESHOLD_2 = 100;

  @Override
  public List<RewardSummary> calculateRewardsPerMonth(List<Transaction> transactions) {
    log.info("calculating reward points.");
    // Group transactions by customer and month
    Map<String, Map<YearMonth, List<Transaction>>> transactionsByCustomerAndMonth = transactions.stream()
      .collect(Collectors.groupingBy(Transaction::getCustomer,
        Collectors.groupingBy(transaction -> YearMonth.from(LocalDate.parse(transaction.getDate())))));
    // Calculate reward points for each customer and month
    List<RewardSummary> rewardSummaries = new ArrayList<>();
    for (Map.Entry<String, Map<YearMonth, List<Transaction>>> entry : transactionsByCustomerAndMonth.entrySet()) {
      String customerId = entry.getKey();
      Map<YearMonth, List<Transaction>> transactionsByMonth = entry.getValue();
      for (Map.Entry<YearMonth, List<Transaction>> monthEntry : transactionsByMonth.entrySet()) {
        YearMonth month = monthEntry.getKey();
        List<Transaction> monthTransactions = monthEntry.getValue();
        int rewardPoints = calculateRewardPoints(monthTransactions);
        rewardSummaries.add(new RewardSummary(customerId, month.toString(), rewardPoints));
      }
    }
    return rewardSummaries;
  }

  @Override
  public Map<String, Integer> calculateRewardSummary(List<Transaction> transactions) {
    log.info("calculating reward summary.");
    return calculateRewardsPerMonth(transactions).stream().collect(
      Collectors.groupingBy(RewardSummary::getCustomer,
        Collectors.reducing(0, RewardSummary::getRewardPoints, Integer::sum)));
  }

  @Override
  public RewardSummaryResponseVo calculateCompleteRewardSummary(List<Transaction> transactions) {
    log.info("calculating complete reward summary.");
    RewardSummaryResponseVo vo = new RewardSummaryResponseVo();
    List<RewardSummary> rewardSummaries = calculateRewardsPerMonth(transactions);
    vo.setPerMonthRewardPoints(rewardSummaries);
    vo.setTotalRewardPoints(rewardSummaries.stream().collect(
      Collectors.groupingBy(RewardSummary::getCustomer,
        Collectors.reducing(0, RewardSummary::getRewardPoints, Integer::sum))));

    return vo;
  }

  private int calculateRewardPoints(List<Transaction> transactions) {
    int rewardPoints = 0;
    for (Transaction transaction : transactions) {
      double amount = transaction.getAmount();
      if (amount > POINTS_THRESHOLD_2) {
        rewardPoints += (int) ((amount - POINTS_THRESHOLD_2) * 2);
        amount = POINTS_THRESHOLD_2;
      }
      if (amount > POINTS_THRESHOLD_1) {
        rewardPoints += (int) (amount - POINTS_THRESHOLD_1);
      }
    }
    return rewardPoints;
  }

}

