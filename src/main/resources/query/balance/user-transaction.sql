select t.id,
       lpad(t.id, 9, '0') as code,
       t.user_code           userCode,
       t.proceed_by          proceedBy,
       u.username            proceedByUsername,
       u.nickname            proceedByNickname,
       t.amount,
       t.currency_code       currencyCode,
       t.remark,
       t.`type`,
       t.status,
       t.created_at          createdAt,
       t.updated_at          updatedAt
from transactions t
         inner join users u on u.code = t.proceed_by
where case
          when
              :transactionType = 'deposit' then t.type = 'deposit'
          when
              :transactionType = 'withdraw' then t.type = 'withdraw'
          else
              t.type in ('deposit', 'withdraw')
    end
  and t.user_code = :userCode
  and if(:keyword <> 'all', concat(lpad(t.id, 9, '0')) like '%', true)
order by t.id desc;