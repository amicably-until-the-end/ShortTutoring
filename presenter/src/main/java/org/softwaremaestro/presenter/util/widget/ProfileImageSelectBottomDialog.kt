package org.softwaremaestro.presenter.util.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.softwaremaestro.presenter.databinding.DialogProfileImageSelectBinding
import org.softwaremaestro.presenter.login.Animal
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

class ProfileImageSelectBottomDialog(
    private val onImageChanged: (Int) -> Unit,
    private val onSelect: (Int) -> Unit,
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: DialogProfileImageSelectBinding
    private var selected: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogProfileImageSelectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgProfileImage.children.forEachIndexed { i, child ->
            child.setOnClickListener {
                binding.rgProfileImage.children.forEach { it.alpha = 0.3F }
                child.alpha = 1F
                selected = Animal.values()[i].resId.also {
                    onImageChanged(it)
                    binding.btnReturn.setEnabledAndChangeColor(true)
                }
            }
        }

        binding.btnReturn.setOnClickListener {
            selected?.let { onSelect(it) }
        }
    }
}