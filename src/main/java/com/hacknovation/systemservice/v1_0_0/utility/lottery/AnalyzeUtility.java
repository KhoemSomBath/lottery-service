package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.analyze.AnalyzeNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.analyze.AnalyzeTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.SellingLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyzeUtility {

    private final AnalyzeNQ analyzeNQ;

    public void analyseVn1(List<AnalyseRS> analyseRS, List<String> memberCodes, SellingLotteryRQ analyseRQ) {
        List<AnalyzeTO> analyzeLo = analyzeNQ.vn1AnalyzeLo(memberCodes, analyseRQ.getFilterByDrawCode(), analyseRQ.getFilterByAmount2D(), analyseRQ.getFilterByAmount3D());
        List<AnalyzeTO> analyzePost = analyzeNQ.vn1AnalyzePost(memberCodes, analyseRQ.getFilterByDrawCode(), analyseRQ.getFilterByAmount2D(), analyseRQ.getFilterByAmount3D());
        setAnalyze(analyzePost, analyzeLo, analyseRS);
    }

    public void analyseVn2(List<AnalyseRS> analyseRS, List<String> memberCodes, SellingLotteryRQ analyseRQ) {
        List<AnalyzeTO> analyzeLo = analyzeNQ.vn2AnalyzeLo(memberCodes, analyseRQ.getFilterByDrawCode(), analyseRQ.getFilterByAmount2D(), analyseRQ.getFilterByAmount3D());
        List<AnalyzeTO> analyzePost = analyzeNQ.vn2AnalyzePost(memberCodes, analyseRQ.getFilterByDrawCode(), analyseRQ.getFilterByAmount2D(), analyseRQ.getFilterByAmount3D());
        setAnalyze(analyzePost, analyzeLo, analyseRS);
    }

    public void analyseLeap(List<AnalyseRS> analyseRS, List<String> memberCodes, SellingLotteryRQ analyseRQ) {
        List<AnalyzeTO> analyzeLo = analyzeNQ.leapAnalyzeLo(memberCodes, analyseRQ.getFilterByDrawCode(), analyseRQ.getFilterByAmount2D(), analyseRQ.getFilterByAmount3D(), analyseRQ.getFilterByAmount4D());
        List<AnalyzeTO> analyzePost = analyzeNQ.leapAnalyzePost(memberCodes, analyseRQ.getFilterByDrawCode(), analyseRQ.getFilterByAmount2D(), analyseRQ.getFilterByAmount3D(), analyseRQ.getFilterByAmount4D());
        setAnalyze(analyzePost, analyzeLo, analyseRS);
    }

    private void setAnalyze(List<AnalyzeTO> analyzePost, List<AnalyzeTO> analyzeLo, List<AnalyseRS> analyseRS) {

        List<AnalyseItemsRS> analyseItemsA = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsB = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsC = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsD = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsF = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsI = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsN = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsK = new ArrayList<>();
        List<AnalyseItemsRS> analyseItemsLo = new ArrayList<>();
        for (AnalyzeTO item : analyzePost) {
            if (PostConstant.POST_A.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsA.add(analyseItemsRS1);
            }
            if (PostConstant.POST_B.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsB.add(analyseItemsRS1);
            }
            if (PostConstant.POST_C.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsC.add(analyseItemsRS1);
            }
            if (PostConstant.POST_D.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsD.add(analyseItemsRS1);
            }
            if (PostConstant.POST_F.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsF.add(analyseItemsRS1);
            }
            if (PostConstant.POST_I.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsI.add(analyseItemsRS1);
            }
            if (PostConstant.POST_N.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsN.add(analyseItemsRS1);
            }
            if (PostConstant.POST_K.equalsIgnoreCase(item.getPostCode())) {
                AnalyseItemsRS analyseItemsRS1 = new AnalyseItemsRS();
                BeanUtils.copyProperties(item, analyseItemsRS1);
                analyseItemsK.add(analyseItemsRS1);
            }
        }

        for (AnalyzeTO lo : analyzeLo ) {
            AnalyseItemsRS analyseItemsLo1 = new AnalyseItemsRS();
            BeanUtils.copyProperties(lo, analyseItemsLo1);
            analyseItemsLo.add(analyseItemsLo1);
        }

        AnalyseRS analyseA = new AnalyseRS();
        analyseA.setPostCode(PostConstant.POST_A);
        analyseA.setItems(analyseItemsA);
        analyseRS.add(analyseA);

        AnalyseRS analyseB = new AnalyseRS();
        analyseB.setPostCode(PostConstant.POST_B);
        analyseB.setItems(analyseItemsB);
        analyseRS.add(analyseB);

        AnalyseRS analyseC = new AnalyseRS();
        analyseC.setPostCode(PostConstant.POST_C);
        analyseC.setItems(analyseItemsC);
        analyseRS.add(analyseC);

        AnalyseRS analyseD = new AnalyseRS();
        analyseD.setPostCode(PostConstant.POST_D);
        analyseD.setItems(analyseItemsD);
        analyseRS.add(analyseD);

        AnalyseRS analyseF = new AnalyseRS();
        analyseF.setPostCode(PostConstant.POST_F);
        analyseF.setItems(analyseItemsF);
        analyseRS.add(analyseF);

        AnalyseRS analyseI = new AnalyseRS();
        analyseI.setPostCode(PostConstant.POST_I);
        analyseI.setItems(analyseItemsI);
        analyseRS.add(analyseI);

        AnalyseRS analyseN = new AnalyseRS();
        analyseN.setPostCode(PostConstant.POST_N);
        analyseN.setItems(analyseItemsN);
        analyseRS.add(analyseN);

        AnalyseRS analyseK = new AnalyseRS();
        analyseK.setPostCode(PostConstant.POST_K);
        analyseK.setItems(analyseItemsK);
        analyseRS.add(analyseK);

        AnalyseRS analyseLo = new AnalyseRS();
        analyseLo.setPostCode(PostConstant.LO_GROUP);
        analyseLo.setItems(analyseItemsLo);
        analyseRS.add(analyseLo);

    }

}
