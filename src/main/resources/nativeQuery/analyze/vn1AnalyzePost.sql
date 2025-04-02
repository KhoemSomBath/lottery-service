SELECT postCode,
       postGroup,
       rebateCode,
       number,
       betAmount
FROM (
         SELECT va.pc      postCode,
                va.pg      postGroup,
                va.rc      rebateCode,
                va.nu      number,
                SUM(va.ba) betAmount
         FROM vnone_analyzing va
         WHERE va.dc = :drawCode
           AND va.uc IN :userCodes
           AND va.pg <> 'LO'
         GROUP BY va.pc, va.pg, va.rc, va.nu
     ) q1
WHERE (q1.betAmount >= :amount2D AND q1.rebateCode = '2D')
   OR (q1.betAmount >= :amount3D AND q1.rebateCode = '3D')