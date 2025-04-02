SELECT id,
       code               drawCode,
       postpone_number    postponeNumber,
       shift_code         shiftCode,
       is_recent          isRecent,
       is_set_win         isSetWin,

       is_released_lo     isReleasedLo,
       is_released_post   isReleasedPost,

       stoped_lo_at       stoppedLoAt,
       stoped_a_at        stoppedAAt,
       stoped_post_at     stoppedPostAt,

       resulted_lo_at     resultedLoAt,
       resulted_post_at   resultedPostAt,
       is_night           isNight,
       status
FROM tn_temp_drawing
WHERE date (resulted_post_at) BETWEEN date(:filterByStartDate) AND date(:filterByEndDate)
ORDER BY resulted_post_at