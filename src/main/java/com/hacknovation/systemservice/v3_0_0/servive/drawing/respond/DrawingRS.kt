package com.hacknovation.systemservice.v3_0_0.servive.drawing.respond

import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * @author Sombath
 * create at 8/5/23 3:19 PM
 */
internal data class DrawingRS(
    val id: Long,
    val code: String?,
    val title: String?,
    val shiftCode: String?,
    val status: String?,
    val postponeNumber: String?,
    val isRecent: Boolean?,

    val stoppedLoAt: Instant?,
    val stoppedPostAt: Instant?,

    val resultedLoAt: Instant?,
    val resultedPostAt: Instant?,

    var stopAt: String?,
    var resultAt: String?,
    var date: String?,

    ) {
    constructor(entity: THTempDrawingEntity) : this(
        entity.id,
        entity.code,
        entity.title,
        entity.shiftCode,
        entity.status?.toString(),
        entity.postponeNumber,
        entity.recent,
        entity.stopedLoAt?.toInstant(),
        entity.stopedPostAt?.toInstant(),
        entity.resultedLoAt?.toInstant(),
        entity.resultedPostAt?.toInstant(),

        null,
        null,
        null
    )

    init {

        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.of("Asia/Phnom_Penh"))

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYY-MM-dd")
            .withZone(ZoneId.of("Asia/Phnom_Penh"))

        this.stopAt = timeFormatter.format(this.stoppedPostAt)
        this.resultAt = timeFormatter.format(this.resultedPostAt)
        this.date = dateFormatter.format(this.resultedPostAt)

    }
}