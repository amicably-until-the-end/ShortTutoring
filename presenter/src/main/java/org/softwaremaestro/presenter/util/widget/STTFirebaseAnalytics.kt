package org.softwaremaestro.presenter.util.widget

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class STTFirebaseAnalytics {
    companion object {
        private var firebaseAnalytics = Firebase.analytics

        fun logEvent(event: EVENT, value: Any) {
            val bundle = Bundle().apply {
                when (value) {
                    is String -> putString(event.key, value)
                    is Long -> putLong(event.key, value)
                    else -> return
                }
            }
            firebaseAnalytics.logEvent(event.mName, bundle)
        }
    }

    enum class EVENT(val mName: String, val key: String) {
        NUM_QUESTION_NORMAL("num_question_normal", "num_question_normal"),
        NUM_QUESTION_RESERVED("num_question_reserved", "num_question_reserved"),
        NUM_TUTORING("num_tutoring", "num_tutoring"),
        TOTAL_TUTORING_TIME("total_tutoring_duration", "total_tutoring_duration"),
        TOTAL_LECTURE_TIME("total_lecture_time", "total_lecture_time")
    }
}