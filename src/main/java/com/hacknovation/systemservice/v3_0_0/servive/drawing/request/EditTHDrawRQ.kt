package com.hacknovation.systemservice.v3_0_0.servive.drawing.request

import javax.validation.constraints.NotNull

/**
 * @author Sombath
 * create at 9/5/23 1:11 AM
 */
data class EditTHDrawRQ(
    @NotNull
    val resultPostAt: String,
    @NotNull
    val stopPostAt: String,
)
