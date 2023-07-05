package org.softwaremaestro.data.mapper

import org.softwaremaestro.data.dto.RequestDto
import org.softwaremaestro.domain.model.vo.RequestVO

object RequestMapper {
    fun asDomain(requestDto: RequestDto): RequestVO {
        return RequestVO(requestDto.studentId)
    }
}

fun RequestDto.asDomain(): RequestVO {
    return RequestMapper.asDomain(this)
}