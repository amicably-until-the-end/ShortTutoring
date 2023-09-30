package org.softwaremaestro.data.common.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun LocalDateTime.toStringWithTimeZone(): String {
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = ZonedDateTime.of(this, zoneId)
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    return zonedDateTime.format(formatter)
}

fun String.parseToLocalDateTime(): LocalDateTime? {
    try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val zonedDateTime = ZonedDateTime.parse(this, formatter)
        return zonedDateTime.toLocalDateTime()
    } catch (e: Exception) {
        return null
    }
}

