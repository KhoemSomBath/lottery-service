SELECT
       id,
       code               drawCode,
       postpone_number    postponeNumber,
       shift_code         shiftCode,
       is_recent          isRecent,
       is_set_win         isSetWin,

       is_released_lo     isReleasedLo,
       is_released_post   isReleasedPost,
       is_released_post_a isReleasedPostA,

       stoped_lo_at       stoppedLoAt,
       stoped_a_at        stoppedAAt,
       stoped_post_at     stoppedPostAt,

       resulted_lo_at     resultedLoAt,
       resulted_post_at   resultedPostAt,
       resulted_a_at      resultedAAt,
       is_night           isNight,
       status
FROM vntwo_temp_drawing
WHERE code = :drawCode LIMIT 1