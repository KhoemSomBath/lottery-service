package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.LoanEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.loan.LoanTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRP extends JpaRepository<LoanEntity, Long> {

    @Query(value = "SELECT * FROM loans l WHERE l.uc = :userCode AND l.lottery_type = :lottery AND DATE(l.issued_at) = DATE(:date) - INTERVAL 1 DAY LIMIT 1", nativeQuery = true)
    LoanEntity getYesterdayLoan(String userCode, String lottery, String date);

    @Query(value = "select l.id, " +
            "       u.code, " +
            "       u.username, " +
            "       u.nickname, " +
            "       ifnull(l.borrow_khr, 0) borrowKhr, " +
            "       ifnull(l.borrow_usd, 0) borrowUsd, " +
            "       ifnull(l.payback_khr, 0) paybackKhr, " +
            "       ifnull(l.payback_usd, 0) paybackUsd, " +
            "       ifnull(l.installment_payment_khr, l_yesterday.installment_payment_khr) installmentPaymentKhr, " +
            "       ifnull(l.installment_payment_usd, l_yesterday.installment_payment_usd) installmentPaymentUsd, " +
            "       l.next_payment nextPayment " +
            "from users u " +
            "         left join loans l on u.code = l.uc and l.lottery_type = :lottery and date(l.issued_at) = :date " +
            "         left join loans l_yesterday on u.code = l_yesterday.uc and l_yesterday.lottery_type = :lottery and date(l_yesterday.issued_at) = DATE(:date) - INTERVAL 1 DAY " +
            "where u.agent_code = :agent group by u.code", nativeQuery = true)
    List<LoanTO> getLoan(String lottery, String agent, String date);

}