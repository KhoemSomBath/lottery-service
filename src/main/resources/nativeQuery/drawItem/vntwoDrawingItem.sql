SELECT ld.`code` drawCode,
       ldi.post_code postCode,
       ldi.two_digits twoDigits,
       ldi.three_digits threeDigits,
       ldi.four_digits fourDigits,
       ld.is_night isNight
FROM vntwo_drawing_items ldi
         INNER JOIN vntwo_drawing ld
                    ON ldi.drawing_id = ld.id
WHERE date (ld.resulted_lo_at)= :filterDate AND ldi.column_number=1
ORDER BY ldi.id