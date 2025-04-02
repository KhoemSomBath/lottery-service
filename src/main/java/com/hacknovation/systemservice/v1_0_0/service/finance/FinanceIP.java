package com.hacknovation.systemservice.v1_0_0.service.finance;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.FinanceConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.TransactionBalanceRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.finance.FinanceListingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.finance.FinanceRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.PagingRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.finance.BalanceRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.finance.TransactionRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.SqlUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.finance.BalanceUtility;
import com.hacknovation.systemservice.v1_0_0.utility.finance.TransactionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceIP extends BaseServiceIP implements FinanceSV {

    private final HttpServletRequest request;
    private final TransactionUtility transactionUtility;
    private final BalanceUtility balanceUtility;
    private final TransactionBalanceRP balanceRP;
    private final SqlUtility sqlUtility;
    private final JwtToken jwtToken;
    private final UserRP userRP;
    private final ActivityLogUtility activityLogUtility;

    @Value("classpath:query/balance/user-transaction.sql")
    Resource userTransaction;

    @Override
    public StructureRS balance() {
        String userCode = jwtToken.getUserToken().getUserCode();
        TransactionBalanceEntity balanceEntity = balanceRP.userBalanceByCode(userCode);
        BalanceRS balanceKhrRS = new BalanceRS();
        BalanceRS balanceUsdRS = new BalanceRS();

        balanceKhrRS.setUserCode(userCode);
        balanceUsdRS.setUserCode(userCode);
        balanceUsdRS.setCurrency("USD");
        balanceUsdRS.setCurrencySignal("$");
        if (balanceEntity != null) {
            BeanUtils.copyProperties(balanceEntity, balanceKhrRS);
            balanceKhrRS.setBalanceAmount(balanceEntity.getBalanceKhr());
            balanceUsdRS.setBalanceAmount(balanceEntity.getBalanceUsd());
        }

        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, List.of(balanceKhrRS, balanceUsdRS));
    }

    @Override
    public StructureRS transaction(FinanceRQ financeRQ) {

        try {

            // Verification user
            var userToken = jwtToken.getUserToken();
            UserEntity userEntity = userRP.findByCode(financeRQ.getProceedForUserCode());
            String holdByUserCode = userEntity.getSuperSeniorCode();
            if (userEntity.getRoleCode().equalsIgnoreCase("senior")) {
                holdByUserCode = userEntity.getSuperSeniorCode();
            }

            if (userEntity.getRoleCode().equalsIgnoreCase("master")) {
                holdByUserCode = userEntity.getSeniorCode();
            }

            if (userEntity.getRoleCode().equalsIgnoreCase("agent")) {
                holdByUserCode = userEntity.getMasterCode();
            }

            if (userEntity.getRoleCode().equalsIgnoreCase("member")) {
                holdByUserCode = userEntity.getAgentCode();
            }

            // Set value
            financeRQ.setProceedByUserCode(userToken.getUserCode());
            financeRQ.setProceederCode(userToken.getUserCode());

//            financeRQ.setPlatformType(jwtToken.getPlatformName());
//            financeRQ.setIp(jwtToken.getIpAddress());
//            financeRQ.setUserAgent(jwtToken.getUserAgent());
//            financeRQ.setDeviceInfo(jwtToken.getDeviceName());

            if (FinanceConstant.DEPOSIT.equalsIgnoreCase(financeRQ.getTransactionType())) {

                TransactionBalanceEntity balanceEntity = balanceRP.userBalanceByCode(holdByUserCode);
                // check user referral balance before deposit
                if (!userEntity.getRoleCode().equalsIgnoreCase("super-senior")) {
                    BigDecimal balanceHolder = balanceEntity.getBalanceKhr();
                    if (LotteryConstant.CURRENCY_USD.equals(financeRQ.getCurrencyCode()))
                        balanceHolder = balanceEntity.getBalanceUsd();
                    if (balanceHolder.compareTo(BigDecimal.ZERO) <= 0) {
                        return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.NOT_ENOUGH_BALANCE);
                    }
                }

                // Deduce amount from top level
                if (!userEntity.getRoleCode().equalsIgnoreCase("super-senior")) {

                    FinanceRQ depositBack = new FinanceRQ();
                    depositBack.setCurrencyCode(financeRQ.getCurrencyCode());
                    depositBack.setType(FinanceConstant.WITHDRAW);
                    depositBack.setTransactionType(FinanceConstant.WITHDRAW);
                    depositBack.setProceedForUserCode(holdByUserCode);
                    depositBack.setRemark(financeRQ.getRemark());
                    depositBack.setAmount(financeRQ.getAmount());
                    depositBack.setProceederCode(userToken.getUserCode());

                    // check user balance before withdraw
                    if (balanceUtility.isNotEnoughBalance(balanceEntity, financeRQ.getAmount(), financeRQ.getCurrencyCode()))
                        return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.NOT_ENOUGH_BALANCE);

                    mapRequest(depositBack);
                }

                // deposit for user
                financeRQ.setType(FinanceConstant.DEPOSIT);
                financeRQ.setTransactionType(FinanceConstant.DEPOSIT);
                mapRequest(financeRQ);

                activityLogUtility.addActivityLog(
                        LotteryConstant.ALL,
                        ActivityLogConstant.MODULE_FINANCE,
                        jwtToken.getUserToken().getUserType(),
                        ActivityLogConstant.ACTION_UPDATE,
                        jwtToken.getUserToken().getUserType(),
                        balanceEntity
                );

                return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
            }

            if (FinanceConstant.WITHDRAW.equalsIgnoreCase(financeRQ.getTransactionType())) {

                // withdraw for user
                TransactionBalanceEntity checkUserBalance = balanceRP.userBalanceByCode(financeRQ.getProceedForUserCode());
                if (balanceUtility.isNotEnoughBalance(checkUserBalance, financeRQ.getAmount(), financeRQ.getCurrencyCode()))
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.NOT_ENOUGH_BALANCE);

                // Deposit back to top level
                if (!userEntity.getRoleCode().equalsIgnoreCase("super-senior")) {
                    FinanceRQ depositBack = new FinanceRQ();
                    depositBack.setCurrencyCode(financeRQ.getCurrencyCode());
                    depositBack.setType(FinanceConstant.DEPOSIT);
                    depositBack.setTransactionType(FinanceConstant.DEPOSIT);
                    depositBack.setProceedForUserCode(holdByUserCode);
                    depositBack.setRemark(financeRQ.getRemark());
                    depositBack.setAmount(financeRQ.getAmount());
                    depositBack.setProceederCode(userToken.getUserCode());
                    mapRequest(depositBack);
                }

                financeRQ.setType(FinanceConstant.WITHDRAW);
                financeRQ.setTransactionType(FinanceConstant.WITHDRAW);
                mapRequest(financeRQ);


                activityLogUtility.addActivityLog(
                        LotteryConstant.ALL,
                        ActivityLogConstant.MODULE_FINANCE,
                        jwtToken.getUserToken().getUserType(),
                        ActivityLogConstant.ACTION_UPDATE,
                        jwtToken.getUserToken().getUserType(),
                        checkUserBalance
                );


                return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY);
            }

            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.TRANSACTION_NOT_ALLOW);

        } catch (Exception e) {
            e.printStackTrace();
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.TRANSACTION_NOT_ALLOW);
        }

    }

    public void mapRequest(FinanceRQ financeRQ) {
        // Create transaction for deposit
        TransactionEntity transactionEntity = transactionUtility.transaction(financeRQ);

        // Mapping response
        TransactionRS transactionRS = new TransactionRS();
        BeanUtils.copyProperties(transactionEntity, transactionRS);

        // Update user balance
        financeRQ.setProceedForUserCode(transactionEntity.getUserCode());
        balanceUtility.depositWithdraw(financeRQ, transactionEntity);
    }

    @Override
    public StructureRS listing(String userCode) {
        return bodyListing(userCode);
    }

    @Override
    public StructureRS listMobile() {
        return bodyListing(jwtToken.getUserToken().getUserCode());
    }

    private StructureRS bodyListing(String userCode) {
        FinanceListingRQ financeListingRQ = new FinanceListingRQ(request);

        Map<String, Object> param = Map.of(
                "transactionType", financeListingRQ.getTransactionType(),
                "userCode", userCode,
                "keyword", financeListingRQ.getKeyword()
        );
        Page<TransactionRS> financeListTOS = sqlUtility.executeQueryForPage(userTransaction, PageRequest.of(financeListingRQ.getPage(), financeListingRQ.getSize()), param, TransactionRS.class);


        return responseBody(HttpStatus.OK, MessageConstant.SUCCESSFULLY, financeListTOS.getContent(),
                new PagingRS(financeListTOS.getNumber(), financeListTOS.getSize(), financeListTOS.getTotalElements()));
    }

}
