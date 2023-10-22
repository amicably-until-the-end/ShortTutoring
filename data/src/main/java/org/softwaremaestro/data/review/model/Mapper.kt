package org.softwaremaestro.data.review.model

import org.softwaremaestro.domain.review.entity.ReviewReqVO
import org.softwaremaestro.domain.review.entity.ReviewResVO

object Mapper {

    fun asDomain(reviewGetResDto: ReviewGetResDto): List<ReviewResVO>? {
        return reviewGetResDto.history?.map {
            ReviewResVO(
                questionId = it.questionId,
                tutoringId = it.tutoringId,
                reviewRating = it.reviewRating,
                reviewComment = it.reviewComment,
                studentId = it.studentId,
                studentName = it.student?.name,
                endedAt = it.endedAt,
                profileImage = it.student?.profileImage,
                role = it.student?.role,
                schoolLevel = it.student?.school?.level,
                schoolGrade = it.student?.school?.grade,
                schoolName = it.student?.school?.schoolName
            )
        }
    }

    fun asDto(reviewReqVO: ReviewReqVO): ReviewCreateReqDto {
        return ReviewCreateReqDto(
            rating = reviewReqVO.rating,
            comment = reviewReqVO.comment
        )
    }
}


fun ReviewGetResDto.asDomain(): List<ReviewResVO>? {
    return Mapper.asDomain(this)
}

fun ReviewReqVO.asDto(): ReviewCreateReqDto {
    return Mapper.asDto(this)
}
