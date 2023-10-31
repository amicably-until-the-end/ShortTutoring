package org.softwaremaestro.presenter.util.widget

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.softwaremaestro.domain.socket.SocketManager

class STTFirebaseAnalytics {
    companion object {
        private var firebaseAnalytics = Firebase.analytics

        fun logEvent(event: EVENT, key: String? = null, value: String? = null) {
            SocketManager.userId ?: return
            val bundle = Bundle().apply {
                putString("user_id", SocketManager.userId)
                if (key != null && value != null) {
                    putString(key, value)
                }
            }
            firebaseAnalytics.logEvent(event.key, bundle)
        }
    }

    enum class EVENT(val key: String) {
        QUESTION_NORMAL("question_normal"),
        QUESTION_RESERVED("question_reserved"),
        TUTORING_DURATION("tutoring_duration"),
        LECTURE_DURATION("lecture_duration"),
        REGISTER("register")
    }
}