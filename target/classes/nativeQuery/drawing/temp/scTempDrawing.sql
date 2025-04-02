SELECT
       id,
       code               drawCode,
       postpone_number    postponeNumber,
       shift_code         shiftCode,
       is_recent          isRecent,
       is_set_win         isSetWin,

       is_released_lo     isReleasedLo,
       is_released_post   isReleasedPost,

       stoped_lo_at       stoppedLoAt,
       stoped_post_at     stoppedPostAt,

       resulted_lo_at     resultedLoAt,
       resulted_post_at   resultedPostAt,
       status
FROM sc_temp_drawing
WHERE code = :drawCode LIMIT 1