package com.hacknovation.systemservice.v3_0_0.controller

import com.hacknovation.systemservice.v1_0_0.ui.controller.BaseController
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS
import com.hacknovation.systemservice.v3_0_0.servive.drawing.THDrawingService
import com.hacknovation.systemservice.v3_0_0.servive.drawing.request.CreateTHDrawRQ
import com.hacknovation.systemservice.v3_0_0.servive.drawing.request.EditTHDrawRQ
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author Sombath
 * create at 8/5/23 1:53 PM
 */

@Tag(name = "TH Drawing", description = "Drawing API for thia-lottery")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
@RestController
@RequestMapping("/api/v1.0.0/drawing/th")
class THDrawingController(private val thDrawingService: THDrawingService): BaseController() {

    @GetMapping
    fun getDraw(@PageableDefault(page = 0, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable, @RequestParam date: String): ResponseEntity<StructureRS> {
        return response(thDrawingService.getDraw(pageable, date))
    }

    @PostMapping
    fun createDraw(@Valid @RequestBody createTHDrawRQ: CreateTHDrawRQ): ResponseEntity<StructureRS> {
        return response(thDrawingService.createDraw(createTHDrawRQ))
    }

    @PutMapping("{id}")
    fun editDraw(@Valid @RequestBody editTHDrawRQ: EditTHDrawRQ, @PathVariable id: Long): ResponseEntity<StructureRS> {
        return response(thDrawingService.editDraw(editTHDrawRQ, id))
    }

    @DeleteMapping("{id}")
    fun deleteDraw(@PathVariable id: Long): ResponseEntity<StructureRS> {
        return response(thDrawingService.deleteDraw(id))
    }
}