package org.softwaremaestro.data.answer_upload.model

import org.softwaremaestro.domain.answer_upload.entity.StudentPickResultVO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Mapper {
    fun asDomain(studentPickResultDto: StudentPickResultDto): StudentPickResultVO {
        with(studentPickResultDto) {
            return StudentPickResultVO(
                startTime = startTime?.let { parseISO(it) },
                endTime = endTime?.let { parseISO(it) }
            )
        }
    }

    companion object {
        fun parseISO(str: String): LocalDateTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            return LocalDateTime.parse(str, formatter).plusHours(9)
        }
    }
}

fun StudentPickResultDto.asDomain(): StudentPickResultVO {
    return Mapper().asDomain(this)
}
