package com.lucrative.codingchallenge.controller;

import com.lucrative.codingchallenge.model.RewardSummaryResponseVo;
import com.lucrative.codingchallenge.model.Transaction;
import com.lucrative.codingchallenge.service.RewardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api("API to calculate reward points per month and total reward.")
public class RewardController {

  @Autowired
  private RewardService rewardService;


  @ApiOperation("Calculates Reward Points per month and total reward points for each customer "
    + "based on the provided transaction list in request body.")
  @PostMapping("/reward-summary")
  public ResponseEntity<RewardSummaryResponseVo> calculateRewardSummary(
    @RequestBody() List<Transaction> transactions) {
    return ResponseEntity.ok(rewardService.calculateCompleteRewardSummary(transactions));
  }


}

