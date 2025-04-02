package com.hacknovation.systemservice.v3_0_0.servive.drawing

import com.hacknovation.systemservice.constant.MessageConstant
import com.hacknovation.systemservice.constant.PostConstant
import com.hacknovation.systemservice.enums.DrawingStatus
import com.hacknovation.systemservice.exception.httpstatus.BadRequestException
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingEntity
import com.hacknovation.systemservice.v1_0_0.io.entity.th.THTempDrawingItemsEntity
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THDrawingRP
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingItemsRP
import com.hacknovation.systemservice.v1_0_0.io.repo.th.THTempDrawingRP
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP
import com.hacknovation.systemservice.v1_0_0.ui.model.response.PagingRS
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS
import com.hacknovation.systemservice.v3_0_0.servive.drawing.request.CreateTHDrawRQ
import com.hacknovation.systemservice.v3_0_0.servive.drawing.request.EditTHDrawRQ
import com.hacknovation.systemservice.v3_0_0.servive.drawing.respond.DrawingRS
import com.hazelcast.core.HazelcastInstance
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


/**
 * @author Sombath
 * create at 8/5/23 2:52 PM
 */

@Service
class THDrawingService(
    private val thTempDrawingRP: THTempDrawingRP,
    private val tHTempDrawingItemsRP: THTempDrawingItemsRP,
    private val hazelcastInstance: HazelcastInstance
) : BaseServiceIP() {

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYY-MM-dd")
        .withZone(ZoneId.systemDefault())

    fun getDraw(pageable: Pageable, date: String): StructureRS {
        val drawingEntities = thTempDrawingRP.getDrawByDate(date, pageable)

        return responseBody(
            HttpStatus.OK,
            MessageConstant.SUCCESSFULLY,
            drawingEntities.content.map { DrawingRS(it) },
            PagingRS(
                drawingEntities.number,
                drawingEntities.size,
                drawingEntities.totalElements
            )
        )
    }

    fun createDraw(request: CreateTHDrawRQ): StructureRS {
        try {
            val lastDraw = thTempDrawingRP.lastDraw()

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")
            val stopAt = ZonedDateTime.parse("${request.stopPostAt} Asia/Phnom_Penh", formatter).toInstant()
            val resultAt = ZonedDateTime.parse("${request.resultPostAt} Asia/Phnom_Penh", formatter).toInstant()

            val drawingEntity = THTempDrawingEntity()

            drawingEntity.code = getUniqueCode(lastDraw?.code)
            drawingEntity.status = DrawingStatus.WAITING
            drawingEntity.recent = true

            drawingEntity.stopedLoAt = Date.from(stopAt)
            drawingEntity.stopedPostAt = Date.from(stopAt)

            drawingEntity.resultedLoAt = Date.from(resultAt)
            drawingEntity.resultedPostAt = Date.from(resultAt)
            thTempDrawingRP.save(drawingEntity)

            val drawingItemEntities = PostConstant.POST_TH.map {
                val item = THTempDrawingItemsEntity()
                item.drawingId = drawingEntity.id.toInt()
                item.postCode = it
                item
            }

            tHTempDrawingItemsRP.saveAll(drawingItemEntities)

            this.removeCacheDraw()

            return responseBodyWithSuccessMessage()
        } catch (dateTimeParseException: DateTimeParseException) {
            throw BadRequestException("Unable to format date")
        }
    }

    fun editDraw(request: EditTHDrawRQ, id: Long): StructureRS {
        try {

            if(!thTempDrawingRP.existsById(id))
                return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.DRAW_COULD_NOT_FOUND)

            val draw = thTempDrawingRP.getById(id)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")
            val stopAt = ZonedDateTime.parse("${request.stopPostAt} Asia/Phnom_Penh", formatter).toInstant()
            val resultAt = ZonedDateTime.parse("${request.resultPostAt} Asia/Phnom_Penh", formatter).toInstant()

            draw.stopedLoAt = Date.from(stopAt)
            draw.stopedPostAt = Date.from(stopAt)

            draw.resultedLoAt = Date.from(resultAt)
            draw.resultedPostAt = Date.from(resultAt)
            thTempDrawingRP.save(draw)

            this.removeCacheDraw()

            return responseBodyWithSuccessMessage()
        } catch (dateTimeParseException: DateTimeParseException) {
            throw BadRequestException("Unable to format date")
        }
    }

    fun deleteDraw(id: Long): StructureRS{
        if(!thTempDrawingRP.existsById(id))
            return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.DRAW_COULD_NOT_FOUND)

        this.removeCacheDraw()

        thTempDrawingRP.deleteById(id)
        tHTempDrawingItemsRP.deleteAllByDrawingId(id.toInt())


        return responseBodyWithSuccessMessage()
    }

    private fun getUniqueCode(lastCode: String?): String {
        val calendar = Calendar.getInstance()
        val newYear = calendar[Calendar.YEAR].toString().substring(2, 4)
        if (lastCode == null) {
            return newYear + "0000001"
        }
        val currentYear = lastCode.substring(0, 2)
        if (currentYear != newYear) {
            return newYear + "0000001"
        }
        val numIncr = lastCode.substring(2, 3)
        var num7Digit = lastCode.substring(3, 9)
        return if (lastCode.substring(2, 9) == numIncr + "999999") {
            num7Digit = "000001"
            if (numIncr == "9") {
                return currentYear + "A" + num7Digit
            }
            if (numIncr.toDoubleOrNull() != null) {
                currentYear + (numIncr[0].code + 1).toChar() + num7Digit
            } else currentYear + (numIncr.toInt() + 1) + num7Digit
        } else {
            val increase = (num7Digit.toInt() + 1).toString()
            currentYear + numIncr + "000000".substring(increase.length) + increase
        }
    }

    private fun removeCacheDraw() {
        hazelcastInstance.getMap<String, Objects>("draws").delete("TH_${dateFormatter.format(Instant.now().atZone(ZoneId.of("Asia/Phnom_Penh")))}")
    }
}