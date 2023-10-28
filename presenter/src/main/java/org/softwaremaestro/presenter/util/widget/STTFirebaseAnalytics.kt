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
        NUM_QUESTION_NORMAL("num_question", "num_question_normal"),
        NUM_QUESTION_RESERVED("num_question", "num_question_reserved"),
        NUM_TUTORING("num_tutoring", "num_tutoring"),
        TOTAL_TUTORING_TIME("total_tutoring_duration", "total_tutoring_duration"),
        NUM_LECTURE_PLAYED("num_lecture_played", "num_lecture_played")
    }
}