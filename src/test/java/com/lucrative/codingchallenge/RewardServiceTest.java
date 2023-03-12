package com.lucrative.codingchallenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.lucrative.codingchallenge.model.RewardSummary;
import com.lucrative.codingchallenge.model.RewardSummaryResponseVo;
import com.lucrative.codingchallenge.model.Transaction;
import com.lucrative.codingchallenge.service.RewardService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RewardServiceTest {

  @Autowired
  private RewardService rewardService;

  @Test
  public void testCalculateRewardPointsPerMonth() {
    List<Transaction> transactions = Arrays.asList(
      new Transaction( "John", LocalDate.now().toString(), 100.0),
      new Transaction( "John",  LocalDate.now().minusDays(1).toString(), 200.0),
      new Transaction( "John", LocalDate.now().minusDays(2).toString(), 300.0)
    );

    List<RewardSummary> rewardSummaries = rewardService.calculateRewardsPerMonth(transactions);

    assertNotNull(rewardSummaries);
    assertEquals(1, rewardSummaries.size());

    RewardSummary rewardSummary = rewardSummaries.get(0);
    assertEquals("John", rewardSummary.getCustomer());
    assertEquals(YearMonth.now(), YearMonth.parse(rewardSummary.getMonth()));
    assertEquals(750, rewardSummary.getRewardPoints());
  }


  @Test
  void testCalculateTotalRewardPoints() {
    List<Transaction> transactions = Arrays.asList(
      new Transaction( "John", LocalDate.now().toString(), 100.0),
      new Transaction( "Mary",  LocalDate.now().minusDays(10).toString(), 200.0),
      new Transaction( "Bob", LocalDate.now().minusDays(20).toString(), 300.0)
    );

    Map<String, Integer> rewardSummary = rewardService.calculateRewardSummary(transactions);

    assertNotNull(rewardSummary);
    assertEquals(3, rewardSummary.size());
    assertEquals(50, rewardSummary.get("John"));
    assertEquals(250, rewardSummary.get("Mary"));
    assertEquals(450, rewardSummary.get("Bob"));
  }

  @Test
  public void testCalculateCompleteRewardSummary() {
    List<Transaction> transactions = Arrays.asList(
      new Transaction( "John", LocalDate.now().toString(), 100.0),
      new Transaction( "John",  LocalDate.now().minusDays(1).toString(), 200.0),
      new Transaction( "John", LocalDate.now().minusDays(2).toString(), 300.0)
    );

    RewardSummaryResponseVo rewardSummaryResponse = rewardService.calculateCompleteRewardSummary(transactions);

    assertNotNull(rewardSummaryResponse);

    assertEquals(750, rewardSummaryResponse.getTotalRewardPoints().get("John"));
    RewardSummary rewardSummary = rewardSummaryResponse.getPerMonthRewardPoints().get(0);
    assertEquals("John", rewardSummary.getCustomer());
    assertEquals(YearMonth.now(), YearMonth.parse(rewardSummary.getMonth()));
    assertEquals(750, rewardSummary.getRewardPoints());
  }
}

