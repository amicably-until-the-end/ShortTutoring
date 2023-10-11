package org.softwaremaestro.data.event.model

import org.softwaremaestro.domain.event.entity.EventVO
import org.softwaremaestro.domain.event.entity.EventsVO

class Mapper {
    fun asDomain(eventsDto: EventsResDto): EventsVO {
        return EventsVO(
            count = eventsDto.count,
            events = eventsDto.events?.map {
                EventVO(
                    createdAt = it.createdAt,
                    id = it.id,
                    url = it.url,
                    image = it.image
                )
            }
        )
    }

    fun toDto(eventsVO: EventsVO): EventsResDto {
        return EventsResDto(
            count = eventsVO.count,
            events = eventsVO.events?.map {
                EventDto(
                    createdAt = it.createdAt,
                    id = it.id,
                    url = it.url,
                    image = it.image
                )
            }
        )
    }
}

fun EventsResDto.asDomain(): EventsVO {
    return Mapper().asDomain(this)
}

fun EventsVO.toDto(): EventsResDto {
    return Mapper().toDto(this)
}