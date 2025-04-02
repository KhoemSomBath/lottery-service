package com.hacknovation.systemservice.v1_0_0.utility.number;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoDrawingItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingItemsEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 19/10/2022
 * time: 14:13
 */
@Component
@RequiredArgsConstructor
public class MapResultItemUtility {

    public void mappingVN1Result(List<VNOneTempDrawingItemsEntity> drawingItemsEntities, Date drawAt, boolean isNight) {
        List<VNOneTempDrawingItemsEntity> columnOneList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());
        // update post BCD
        columnOneList.stream().filter(it -> it.getPostCode().contains("B")).findFirst().ifPresent(postB -> {
            if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                NumberB numberB = new NumberB(postB.getFiveDigits());
                postB.setTwoDigits(numberB.getNumberB2D());
                postB.setThreeDigits(numberB.getNumberB3D());
                postB.setFourDigits(StringUtils.right(postB.getFiveDigits(), 4));
                columnOneList.stream().filter(c -> c.getPostCode().equals("C")).findFirst().ifPresent(postC -> {
                    postC.setTwoDigits(numberB.getNumberC2D());
                    postC.setThreeDigits(numberB.getNumberC3D());
                    postC.setFourDigits(postB.getFourDigits());
                    postC.setFiveDigits(postB.getFiveDigits());
                    postC.setSixDigits(postB.getSixDigits());
                });
                columnOneList.stream().filter(d -> d.getPostCode().equals("D")).findFirst().ifPresent(postD -> {
                    postD.setTwoDigits(numberB.getNumberD2D());
                    postD.setThreeDigits(numberB.getNumberD3D());
                    postD.setFourDigits(postB.getFourDigits());
                    postD.setFiveDigits(postB.getFiveDigits());
                    postD.setSixDigits(postB.getSixDigits());
                });
            }
        });

        if (!isNight) {

            String dayOfWeek = getDayOfWeek(drawAt);
            int columnFKN = getColumnNumberFKN(dayOfWeek);
            List<VNOneTempDrawingItemsEntity> columnFKNList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == columnFKN).collect(Collectors.toList());

            int columnI = getColumnNumberI(dayOfWeek);
            List<VNOneTempDrawingItemsEntity> columnIList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == columnI).collect(Collectors.toList());
            // update post A columnFKN to F 1
            columnFKNList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setTwoDigits(postA.getTwoDigits());
                        postF.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post A columnI to I column 1
            columnIList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                        postI.setTwoDigits(postA.getTwoDigits());
                        postI.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post LO1 columnFKN to K 1
            columnFKNList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("K")).findFirst().ifPresent(postK -> setDefaultDigit(postK, sixDigit));
                }
            });

            // update post B columnFKN to N 1
            columnFKNList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("N")).findFirst().ifPresent(postN -> {
                        NumberB numberB = new NumberB(postB.getFiveDigits());
                        postN.setTwoDigits(numberB.getNumberB2D());
                        postN.setThreeDigits(numberB.getNumberB3D());
                        postN.setFiveDigits(postN.getTwoDigits() + postN.getThreeDigits());
                        postN.setFourDigits(StringUtils.right(postN.getFiveDigits(), 4));
                        postN.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postN.getFiveDigits());
                    });
                }
            });
        } else {
            columnOneList.stream().filter(it -> it.getPostCode().equals("A4")).findFirst().ifPresent(postA4 -> postA4.setThreeDigits(null));
        }
    }

    public void mappingVN1OriginalResult(List<VNOneDrawingItemsEntity> drawingItemsEntities, Date drawAt, boolean isNight) {
        List<VNOneDrawingItemsEntity> columnOneList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());
        // update post BCD
        columnOneList.stream().filter(it -> it.getPostCode().contains("B")).findFirst().ifPresent(postB -> {
            if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                NumberB numberB = new NumberB(postB.getFiveDigits());
                postB.setTwoDigits(numberB.getNumberB2D());
                postB.setThreeDigits(numberB.getNumberB3D());
                postB.setFourDigits(StringUtils.right(postB.getFiveDigits(), 4));
                columnOneList.stream().filter(c -> c.getPostCode().equals("C")).findFirst().ifPresent(postC -> {
                    postC.setTwoDigits(numberB.getNumberC2D());
                    postC.setThreeDigits(numberB.getNumberC3D());
                    postC.setFourDigits(postB.getFourDigits());
                    postC.setFiveDigits(postB.getFiveDigits());
                    postC.setSixDigits(postB.getSixDigits());
                });
                columnOneList.stream().filter(d -> d.getPostCode().equals("D")).findFirst().ifPresent(postD -> {
                    postD.setTwoDigits(numberB.getNumberD2D());
                    postD.setThreeDigits(numberB.getNumberD3D());
                    postD.setFourDigits(postB.getFourDigits());
                    postD.setFiveDigits(postB.getFiveDigits());
                    postD.setSixDigits(postB.getSixDigits());
                });
            }
        });

        if (!isNight) {
            String dayOfWeek = getDayOfWeek(drawAt);
            int columnFKN = getColumnNumberFKN(dayOfWeek);
            List<VNOneDrawingItemsEntity> columnFKNList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == columnFKN).collect(Collectors.toList());

            int columnI = getColumnNumberI(dayOfWeek);
            List<VNOneDrawingItemsEntity> columnIList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == columnI).collect(Collectors.toList());

            // update post A columnFKN to F 1
            columnFKNList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setTwoDigits(postA.getTwoDigits());
                        postF.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post A columnI 3 to I column 1
            columnIList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                        postI.setTwoDigits(postA.getTwoDigits());
                        postI.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post LO1 columnFKN to K 1
            columnFKNList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("K")).findFirst().ifPresent(postK -> setDefaultDigit(postK, sixDigit));
                }
            });

            // update post B columnFKN to N 1
            columnFKNList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("N")).findFirst().ifPresent(postN -> {
                        NumberB numberB = new NumberB(postB.getFiveDigits());
                        postN.setTwoDigits(numberB.getNumberB2D());
                        postN.setThreeDigits(numberB.getNumberB3D());
                        postN.setFiveDigits(postN.getTwoDigits() + postN.getThreeDigits());
                        postN.setFourDigits(StringUtils.right(postN.getFiveDigits(), 4));
                        postN.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postN.getFiveDigits());
                    });
                }
            });
        } else {
            columnOneList.stream().filter(it -> it.getPostCode().equals("A4")).findFirst().ifPresent(postA4 -> postA4.setThreeDigits(null));
        }
    }

    public void mappingVN2Result(List<VNTwoTempDrawingItemsEntity> drawingItemsEntities, boolean isNight) {
        List<VNTwoTempDrawingItemsEntity> columnOneList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());
        // update post BCD
        columnOneList.stream().filter(it -> it.getPostCode().contains("B")).findFirst().ifPresent(postB -> {
            if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                NumberB numberB = new NumberB(postB.getFiveDigits());
                postB.setTwoDigits(numberB.getNumberB2D());
                postB.setThreeDigits(numberB.getNumberB3D());
                postB.setFourDigits(StringUtils.right(postB.getFiveDigits(), 4));
                columnOneList.stream().filter(c -> c.getPostCode().equals("C")).findFirst().ifPresent(postC -> {
                    postC.setTwoDigits(numberB.getNumberC2D());
                    postC.setThreeDigits(numberB.getNumberC3D());
                    postC.setFourDigits(postB.getFourDigits());
                    postC.setFiveDigits(postB.getFiveDigits());
                    postC.setSixDigits(postB.getSixDigits());
                });
                columnOneList.stream().filter(d -> d.getPostCode().equals("D")).findFirst().ifPresent(postD -> {
                    postD.setTwoDigits(numberB.getNumberD2D());
                    postD.setThreeDigits(numberB.getNumberD3D());
                    postD.setFourDigits(postB.getFourDigits());
                    postD.setFiveDigits(postB.getFiveDigits());
                    postD.setSixDigits(postB.getSixDigits());
                });
            }
        });

        if (!isNight) {
            List<VNTwoTempDrawingItemsEntity> columnTwoList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 2).collect(Collectors.toList());
            List<VNTwoTempDrawingItemsEntity> columnThreeList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 3).collect(Collectors.toList());
            // update post A column 2 to F 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setTwoDigits(postA.getTwoDigits());
                        postF.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post A column 3 to I column 1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                        postI.setTwoDigits(postA.getTwoDigits());
                        postI.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post LO1 column 2 to K 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("K")).findFirst().ifPresent(postK -> setDefaultDigit(postK, sixDigit));
                }
            });

            // update post B column 2 to N 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("N")).findFirst().ifPresent(postN -> {
                        NumberB numberB = new NumberB(postB.getFiveDigits());
                        postN.setTwoDigits(numberB.getNumberB2D());
                        postN.setThreeDigits(numberB.getNumberB3D());
                        postN.setFiveDigits(postN.getTwoDigits() + postN.getThreeDigits());
                        postN.setFourDigits(StringUtils.right(postN.getFiveDigits(), 4));
                        postN.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postN.getFiveDigits());
                    });
                }
            });
        } else {
            columnOneList.stream().filter(it -> it.getPostCode().equals("A4")).findFirst().ifPresent(postA4 -> postA4.setThreeDigits(null));
        }
    }

    public void mappingVN2OriginalResult(List<VNTwoDrawingItemsEntity> drawingItemsEntities, boolean isNight) {
        List<VNTwoDrawingItemsEntity> columnOneList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());
        // update post BCD
        columnOneList.stream().filter(it -> it.getPostCode().contains("B")).findFirst().ifPresent(postB -> {
            if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                NumberB numberB = new NumberB(postB.getFiveDigits());
                postB.setTwoDigits(numberB.getNumberB2D());
                postB.setThreeDigits(numberB.getNumberB3D());
                postB.setFourDigits(StringUtils.right(postB.getFiveDigits(), 4));
                columnOneList.stream().filter(c -> c.getPostCode().equals("C")).findFirst().ifPresent(postC -> {
                    postC.setTwoDigits(numberB.getNumberC2D());
                    postC.setThreeDigits(numberB.getNumberC3D());
                    postC.setFourDigits(postB.getFourDigits());
                    postC.setFiveDigits(postB.getFiveDigits());
                    postC.setSixDigits(postB.getSixDigits());
                });
                columnOneList.stream().filter(d -> d.getPostCode().equals("D")).findFirst().ifPresent(postD -> {
                    postD.setTwoDigits(numberB.getNumberD2D());
                    postD.setThreeDigits(numberB.getNumberD3D());
                    postD.setFourDigits(postB.getFourDigits());
                    postD.setFiveDigits(postB.getFiveDigits());
                    postD.setSixDigits(postB.getSixDigits());
                });
            }
        });

        if (!isNight) {
            List<VNTwoDrawingItemsEntity> columnTwoList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 2).collect(Collectors.toList());
            List<VNTwoDrawingItemsEntity> columnThreeList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 3).collect(Collectors.toList());
            // update post A column 2 to F 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setTwoDigits(postA.getTwoDigits());
                        postF.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post A column 3 to I column 1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                        postI.setTwoDigits(postA.getTwoDigits());
                        postI.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post LO1 column 2 to K 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("K")).findFirst().ifPresent(postK -> setDefaultDigit(postK, sixDigit));
                }
            });

            // update post B column 2 to N 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("N")).findFirst().ifPresent(postN -> {
                        NumberB numberB = new NumberB(postB.getFiveDigits());
                        postN.setTwoDigits(numberB.getNumberB2D());
                        postN.setThreeDigits(numberB.getNumberB3D());
                        postN.setFiveDigits(postN.getTwoDigits() + postN.getThreeDigits());
                        postN.setFourDigits(StringUtils.right(postN.getFiveDigits(), 4));
                        postN.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postN.getFiveDigits());
                    });
                }
            });
        } else {
            columnOneList.stream().filter(it -> it.getPostCode().equals("A4")).findFirst().ifPresent(postA4 -> postA4.setThreeDigits(null));
        }
    }

    public void mappingTNResult(List<TNTempDrawingItemsEntity> drawingItemsEntities, boolean isNight) {
        List<TNTempDrawingItemsEntity> columnOneList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());
        // update post BCD
        columnOneList.stream().filter(it -> it.getPostCode().contains("B")).findFirst().ifPresent(postB -> {
            if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                NumberB numberB = new NumberB(postB.getFiveDigits());
                postB.setTwoDigits(numberB.getNumberB2D());
                postB.setThreeDigits(numberB.getNumberB3D());
                postB.setFourDigits(StringUtils.right(postB.getFiveDigits(), 4));
                columnOneList.stream().filter(c -> c.getPostCode().equals("C")).findFirst().ifPresent(postC -> {
                    postC.setTwoDigits(numberB.getNumberC2D());
                    postC.setThreeDigits(numberB.getNumberC3D());
                    postC.setFourDigits(postB.getFourDigits());
                    postC.setFiveDigits(postB.getFiveDigits());
                    postC.setSixDigits(postB.getSixDigits());
                });
                columnOneList.stream().filter(d -> d.getPostCode().equals("D")).findFirst().ifPresent(postD -> {
                    postD.setTwoDigits(numberB.getNumberD2D());
                    postD.setThreeDigits(numberB.getNumberD3D());
                    postD.setFourDigits(postB.getFourDigits());
                    postD.setFiveDigits(postB.getFiveDigits());
                    postD.setSixDigits(postB.getSixDigits());
                });
            }
        });

        if (!isNight) {
            List<TNTempDrawingItemsEntity> columnTwoList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 2).collect(Collectors.toList());
            List<TNTempDrawingItemsEntity> columnThreeList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 3).collect(Collectors.toList());
            // update post A column 2 to F 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setTwoDigits(postA.getTwoDigits());
                        postF.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post A column 3 to I 1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                        postI.setTwoDigits(postA.getTwoDigits());
                        postI.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post LO1 column 2 to K 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("K")).findFirst().ifPresent(postK -> setDefaultDigit(postK, sixDigit));
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setFourDigits(postLO1.getFourDigits());
                    });
                }
            });

            // update post A_3D to post O
            columnOneList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getThreeDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("O")).findFirst().ifPresent(postO -> {
                        postO.setThreeDigits(postA.getThreeDigits());
                        postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                    });
                }
            });

            // update post LO1 to Post Z
            columnOneList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("Z")).findFirst().ifPresent(postZ -> setDefaultDigit(postZ, sixDigit));
                    columnOneList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                        postA.setFourDigits(postLO1.getFourDigits());
                    });
                }
            });

            // update post B column 2 to N 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    NumberB numberB = new NumberB(postB.getFiveDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("N")).findFirst().ifPresent(postN -> {
                        postN.setTwoDigits(numberB.getNumberB2D());
                        postN.setThreeDigits(numberB.getNumberB3D());
                        postN.setFiveDigits(postN.getTwoDigits() + postN.getThreeDigits());
                        postN.setFourDigits(StringUtils.right(postN.getFiveDigits(), 4));
                        postN.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postN.getFiveDigits());
                    });
                }
            });

            // update post  B column 3 to P 1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    NumberB numberB = new NumberB(postB.getFiveDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("P")).findFirst().ifPresent(postP -> {
                        postP.setTwoDigits(numberB.getNumberB2D());
                        postP.setThreeDigits(numberB.getNumberB3D());
                        postP.setFiveDigits(postP.getTwoDigits() + postP.getThreeDigits());
                        postP.setFourDigits(StringUtils.right(postP.getFiveDigits(), 4));
                        postP.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postP.getFiveDigits());
                    });
                }
            });

            // update fourD LO1 C3 to I 4D C1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                    postI.setFourDigits(postLO1.getFourDigits());
                });
            });

        } else {
            columnOneList.stream().filter(it -> it.getPostCode().equals("A4")).findFirst().ifPresent(postA4 -> postA4.setThreeDigits(null));

            columnOneList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                columnOneList.stream().filter( it-> it.getPostCode().equals("O")).findFirst().ifPresent(postO -> {
                    postO.setThreeDigits(postA.getThreeDigits());
                    postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                });
            });
            columnOneList.stream().filter(it -> it.getPostCode().equals("A2")).findFirst().ifPresent(postA -> {
                columnOneList.stream().filter( it-> it.getPostCode().equals("O2")).findFirst().ifPresent(postO -> {
                    postO.setThreeDigits(postA.getThreeDigits());
                    postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                });
            });
            columnOneList.stream().filter(it -> it.getPostCode().equals("A3")).findFirst().ifPresent(postA -> {
                columnOneList.stream().filter( it-> it.getPostCode().equals("O3")).findFirst().ifPresent(postO -> {
                    postO.setThreeDigits(postA.getThreeDigits());
                    postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                });
            });
        }
    }

    public void mappingTNOriginalResult(List<TNDrawingItemsEntity> drawingItemsEntities, boolean isNight) {
        List<TNDrawingItemsEntity> columnOneList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 1).collect(Collectors.toList());
        // update post BCD
        columnOneList.stream().filter(it -> it.getPostCode().contains("B")).findFirst().ifPresent(postB -> {
            if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                NumberB numberB = new NumberB(postB.getFiveDigits());
                postB.setTwoDigits(numberB.getNumberB2D());
                postB.setThreeDigits(numberB.getNumberB3D());
                postB.setFourDigits(StringUtils.right(postB.getFiveDigits(), 4));
                columnOneList.stream().filter(c -> c.getPostCode().equals("C")).findFirst().ifPresent(postC -> {
                    postC.setTwoDigits(numberB.getNumberC2D());
                    postC.setThreeDigits(numberB.getNumberC3D());
                    postC.setFourDigits(postB.getFourDigits());
                    postC.setFiveDigits(postB.getFiveDigits());
                    postC.setSixDigits(postB.getSixDigits());
                });
                columnOneList.stream().filter(d -> d.getPostCode().equals("D")).findFirst().ifPresent(postD -> {
                    postD.setTwoDigits(numberB.getNumberD2D());
                    postD.setThreeDigits(numberB.getNumberD3D());
                    postD.setFourDigits(postB.getFourDigits());
                    postD.setFiveDigits(postB.getFiveDigits());
                    postD.setSixDigits(postB.getSixDigits());
                });
            }
        });

        if (!isNight) {
            List<TNDrawingItemsEntity> columnTwoList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 2).collect(Collectors.toList());
            List<TNDrawingItemsEntity> columnThreeList = drawingItemsEntities.stream().filter(it -> it.getColumnNumber() == 3).collect(Collectors.toList());
            // update post A column 2 to F 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setTwoDigits(postA.getTwoDigits());
                        postF.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post A column 3 to I 1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getTwoDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                        postI.setTwoDigits(postA.getTwoDigits());
                        postI.setThreeDigits(postA.getThreeDigits());
                    });
                }
            });

            // update post LO1 column 2 to K 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("K")).findFirst().ifPresent(postK -> setDefaultDigit(postK, sixDigit));
                    columnOneList.stream().filter(it -> it.getPostCode().equals("F")).findFirst().ifPresent(postF -> {
                        postF.setFourDigits(postLO1.getFourDigits());
                    });
                }
            });

            // update post A_3D to post O
            columnOneList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                if (isNotNullAndNotBlank(postA.getThreeDigits())) {
                    columnOneList.stream().filter(it -> it.getPostCode().equals("O")).findFirst().ifPresent(postO -> {
                        postO.setThreeDigits(postA.getThreeDigits());
                        postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                    });
                }
            });

            // update post LO1 to Post Z
            columnOneList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                if (isNotNullAndNotBlank(postLO1.getTwoDigits())) {
                    String sixDigit = "**".concat(postLO1.getFourDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("Z")).findFirst().ifPresent(postZ -> setDefaultDigit(postZ, sixDigit));
                    columnOneList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                        postA.setFourDigits(postLO1.getFourDigits());
                    });
                }
            });

            // update post B column 2 to N 1
            columnTwoList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    NumberB numberB = new NumberB(postB.getFiveDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("N")).findFirst().ifPresent(postN -> {
                        postN.setTwoDigits(numberB.getNumberB2D());
                        postN.setThreeDigits(numberB.getNumberB3D());
                        postN.setFiveDigits(postN.getTwoDigits() + postN.getThreeDigits());
                        postN.setFourDigits(StringUtils.right(postN.getFiveDigits(), 4));
                        postN.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postN.getFiveDigits());
                    });
                }
            });

            // update post  B column 3 to P 1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("B")).findFirst().ifPresent(postB -> {
                if (isNotNullAndNotBlank(postB.getTwoDigits())) {
                    NumberB numberB = new NumberB(postB.getFiveDigits());
                    columnOneList.stream().filter(it -> it.getPostCode().equals("P")).findFirst().ifPresent(postP -> {
                        postP.setTwoDigits(numberB.getNumberB2D());
                        postP.setThreeDigits(numberB.getNumberB3D());
                        postP.setFiveDigits(postP.getTwoDigits() + postP.getThreeDigits());
                        postP.setFourDigits(StringUtils.right(postP.getFiveDigits(), 4));
                        postP.setSixDigits(StringUtils.left(postB.getSixDigits(), 1) + postP.getFiveDigits());
                    });
                }
            });

            // update fourD LO1 C3 to I 4D C1
            columnThreeList.stream().filter(it -> it.getPostCode().equals("LO1")).findFirst().ifPresent(postLO1 -> {
                columnOneList.stream().filter(it -> it.getPostCode().equals("I")).findFirst().ifPresent(postI -> {
                    postI.setFourDigits(postLO1.getFourDigits());
                });
            });

        } else {
            columnOneList.stream().filter(it -> it.getPostCode().equals("A4")).findFirst().ifPresent(postA4 -> postA4.setThreeDigits(null));

            columnOneList.stream().filter(it -> it.getPostCode().equals("A")).findFirst().ifPresent(postA -> {
                columnOneList.stream().filter( it-> it.getPostCode().equals("O")).findFirst().ifPresent(postO -> {
                    postO.setThreeDigits(postA.getThreeDigits());
                    postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                });
            });
            columnOneList.stream().filter(it -> it.getPostCode().equals("A2")).findFirst().ifPresent(postA -> {
                columnOneList.stream().filter( it-> it.getPostCode().equals("O2")).findFirst().ifPresent(postO -> {
                    postO.setThreeDigits(postA.getThreeDigits());
                    postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                });
            });
            columnOneList.stream().filter(it -> it.getPostCode().equals("A3")).findFirst().ifPresent(postA -> {
                columnOneList.stream().filter( it-> it.getPostCode().equals("O3")).findFirst().ifPresent(postO -> {
                    postO.setThreeDigits(postA.getThreeDigits());
                    postO.setTwoDigits(StringUtils.right(postO.getThreeDigits(), 2));
                });
            });
        }
    }

    private boolean isNotNullAndNotBlank(String text) {
        return text != null && !text.isBlank();
    }

    private void setDefaultDigit(VNOneTempDrawingItemsEntity item, String number) {
        if (number != null && number.length() > 4) {
            item.setTwoDigits(StringUtils.right(number, 2));
            item.setThreeDigits(StringUtils.right(number, 3));
            item.setFourDigits(StringUtils.right(number, 4));
            item.setFiveDigits(StringUtils.right(number, 5));
            item.setSixDigits("*"+item.getFiveDigits());
        }
    }

    private void setDefaultDigit(VNOneDrawingItemsEntity item, String number) {
        if (number != null && number.length() > 4) {
            item.setTwoDigits(StringUtils.right(number, 2));
            item.setThreeDigits(StringUtils.right(number, 3));
            item.setFourDigits(StringUtils.right(number, 4));
            item.setFiveDigits(StringUtils.right(number, 5));
            item.setSixDigits("*"+item.getFiveDigits());
        }
    }

    private void setDefaultDigit(VNTwoTempDrawingItemsEntity item, String number) {
        if (number != null && number.length() > 4) {
            item.setTwoDigits(StringUtils.right(number, 2));
            item.setThreeDigits(StringUtils.right(number, 3));
            item.setFourDigits(StringUtils.right(number, 4));
            item.setFiveDigits(StringUtils.right(number, 5));
            item.setSixDigits("*"+item.getFiveDigits());
        }
    }

    private void setDefaultDigit(VNTwoDrawingItemsEntity item, String number) {
        if (number != null && number.length() > 4) {
            item.setTwoDigits(StringUtils.right(number, 2));
            item.setThreeDigits(StringUtils.right(number, 3));
            item.setFourDigits(StringUtils.right(number, 4));
            item.setFiveDigits(StringUtils.right(number, 5));
            item.setSixDigits("*"+item.getFiveDigits());
        }
    }

    private void setDefaultDigit(TNTempDrawingItemsEntity item, String number) {
        if (number != null && number.length() > 4) {
            item.setTwoDigits(StringUtils.right(number, 2));
            item.setThreeDigits(StringUtils.right(number, 3));
            item.setFourDigits(StringUtils.right(number, 4));
            item.setFiveDigits(StringUtils.right(number, 5));
            item.setSixDigits("*"+item.getFiveDigits());
        }
    }

    private void setDefaultDigit(TNDrawingItemsEntity item, String number) {
        if (number != null && number.length() > 4) {
            item.setTwoDigits(StringUtils.right(number, 2));
            item.setThreeDigits(StringUtils.right(number, 3));
            item.setFourDigits(StringUtils.right(number, 4));
            item.setFiveDigits(StringUtils.right(number, 5));
            item.setSixDigits("*"+item.getFiveDigits());
        }
    }

    public Integer getColumnNumberFKN(String dayOfWeek) {
        switch (dayOfWeek) {
            case LotteryConstant.MONDAY:
            case LotteryConstant.TUESDAY:
            case LotteryConstant.SATURDAY:
            case LotteryConstant.SUNDAY:
                return 2;
            case LotteryConstant.WEDNESDAY:
            case LotteryConstant.THURSDAY:
                return 3;
            case LotteryConstant.FRIDAY:
                return 1;

        }
        return 0;
    }

    public Integer getColumnNumberI(String dayOfWeek) {
        switch (dayOfWeek) {
            case LotteryConstant.MONDAY:
            case LotteryConstant.WEDNESDAY:
            case LotteryConstant.THURSDAY:
            case LotteryConstant.SATURDAY:
            case LotteryConstant.SUNDAY:
                return 2;
            case LotteryConstant.TUESDAY:
                return 3;
            case LotteryConstant.FRIDAY:
                return 1;

        }
        return 0;
    }

    public String getDayOfWeek(Date date) {
        return new SimpleDateFormat("EEEE").format(date);
    }
}
