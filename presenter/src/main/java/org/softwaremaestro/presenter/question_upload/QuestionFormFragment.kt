package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding

@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    lateinit var binding: FragmentQuestionFormBinding

    private val viewModel : QuestionUploadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormBinding.inflate(inflater,container,false)

        binding.btnSubmit.setOnClickListener {
            viewModel.uploadQuestion(binding.etDetail.text.toString())
        }
        observe()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observe()
    }
    private fun observe(){
        viewModel.resultString.observe(viewLifecycleOwner, Observer {
            Log.d("mymym",viewModel.resultString.value!!);
        })
    }
}