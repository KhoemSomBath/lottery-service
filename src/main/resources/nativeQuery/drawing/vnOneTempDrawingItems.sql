SELECT drawing_id         drawingId,
       post_code          postCode,
       post_group         postGroup,
       two_digits         twoDigits,
       three_digits       threeDigits,
       four_digits        fourDigits,
       five_digits        fiveDigits,
       six_digits         sixDigits,
       column_number      columnNumber,
       province_code      provinceCode,
       reward_code        rewardCode,
       reward_group     rewardGroup

FROM vnone_temp_drawing_items
WHERE drawing_id IN (:drawingId)
AND column_number = 1