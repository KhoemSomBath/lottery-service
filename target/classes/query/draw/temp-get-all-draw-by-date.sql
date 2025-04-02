select *
from ((select id,
              'VN1'              lottery,
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
       from vnone_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)

      union all

      (select id,
              'VN2'              lottery,
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
       from vntwo_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)

      union all

      (select id,
              'TN'               lottery,
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
       from tn_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)

      union all

      (select id,
              'KH'             lottery,
              code             drawCode,
              postpone_number  postponeNumber,
              shift_code       shiftCode,
              is_recent        isRecent,
              is_set_win       isSetWin,

              is_released_lo   isReleasedLo,
              is_released_post isReleasedPost,
              is_released_post isReleasedPostA,

              stoped_lo_at     stoppedLoAt,
              stoped_post_at   stoppedAAt,
              stoped_post_at   stoppedPostAt,

              resulted_lo_at   resultedLoAt,
              resulted_post_at resultedPostAt,
              resulted_post_at resultedAAt,
              is_night         isNight,
              status
       from khr_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)

      union all

      (select id,
              'LEAP'           lottery,
              code             drawCode,
              postpone_number  postponeNumber,
              shift_code       shiftCode,
              is_recent        isRecent,
              is_set_win       isSetWin,

              is_released_lo   isReleasedLo,
              is_released_post isReleasedPost,
              is_released_post isReleasedPostA,

              stoped_lo_at     stoppedLoAt,
              stoped_post_at   stoppedAAt,
              stoped_post_at   stoppedPostAt,

              resulted_lo_at   resultedLoAt,
              resulted_post_at resultedPostAt,
              resulted_post_at resultedAAt,
              0                isNight,
              status
       from leap_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)

      union all

      (select id,
              'SC'             lottery,
              code             drawCode,
              postpone_number  postponeNumber,
              shift_code       shiftCode,
              is_recent        isRecent,
              is_set_win       isSetWin,

              is_released_lo   isReleasedLo,
              is_released_post isReleasedPost,
              is_released_post isReleasedPostA,

              stoped_lo_at     stoppedLoAt,
              stoped_post_at   stoppedAAt,
              stoped_post_at   stoppedPostAt,

              resulted_lo_at   resultedLoAt,
              resulted_post_at resultedPostAt,
              resulted_post_at resultedAAt,
              0                isNight,
              status
       from sc_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)

      union all

      (select id,
              'TH'             lottery,
              code             drawCode,
              postpone_number  postponeNumber,
              shift_code       shiftCode,
              is_recent        isRecent,
              is_set_win       isSetWin,

              is_released_lo   isReleasedLo,
              is_released_post isReleasedPost,
              is_released_post isReleasedPostA,

              stoped_lo_at     stoppedLoAt,
              stoped_post_at   stoppedAAt,
              stoped_post_at   stoppedPostAt,

              resulted_lo_at   resultedLoAt,
              resulted_post_at resultedPostAt,
              resulted_post_at resultedAAt,
              0                isNight,
              status
       from th_temp_drawing
       where resulted_post_at between concat(:filterDate, ' 00:00:00') and concat(:filterDate, ' 23:59:59')
       order by resulted_post_at)) res

