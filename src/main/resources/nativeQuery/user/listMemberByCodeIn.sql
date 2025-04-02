SELECT code,
       super_senior_code superSeniorCode,
       senior_code       seniorCode,
       master_code       masterCode,
       agent_code        agentCode
FROM users
WHERE role_code = 'member'
  AND code IN :userCodes