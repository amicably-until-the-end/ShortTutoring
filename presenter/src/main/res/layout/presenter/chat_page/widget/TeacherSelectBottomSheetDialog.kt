package layout.presenter.chat_page.widget

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.softwaremaestro.presenter.databinding.DialogTeacherSelectBinding

class TeacherSelectBottomSheetDialog(private val context: Context, private val questionId: String) :
    BottomSheetDialog(context) {

    private lateinit var binding: DialogTeacherSelectBinding


}