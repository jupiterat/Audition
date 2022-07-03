package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.domain.usecase.LinkDataExtractionUseCase
import com.example.myapplication.domain.usecase.MentionDataExtractionUseCase
import com.example.myapplication.infra.repository.DataRepository
import com.example.myapplication.presentation.presenter.DataPresenter
import com.example.myapplication.presentation.view.DataFragmentView

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), DataFragmentView {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val KEY_MODE: String = "mode"
    private val mentionStr = "@billgates do you know where is @elonmuskzzz?"
    private val linkStr = "Olympics 2020 is happening; https://olympics.com/tokyo-2020/en/"

    private lateinit var presenter: DataPresenter
    private var mode: ExtractionMode = ExtractionMode.MENTION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = DataRepository(context)
        val mentionUseCase = MentionDataExtractionUseCase(repo)
        val linkRUseCase = LinkDataExtractionUseCase(repo)
        presenter = DataPresenter(this, mentionUseCase, linkRUseCase)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(KEY_MODE)) {
                val previous = ExtractionMode.fromValue(it.getInt(KEY_MODE, -1))
                if (previous == ExtractionMode.MENTION) binding.radioGroup.check(R.id.radioButton) else binding.radioGroup.check(
                    R.id.radioButton2
                )
            }
        }

        binding.buttonGetData.setOnClickListener {
            val original = binding.textviewFirst.text.toString()
            if (mode == ExtractionMode.MENTION) {
                presenter.handleMentionData(original)
            } else if (mode == ExtractionMode.LINK) {
                presenter.handleLinkData(original)
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioButton) {
                mode = ExtractionMode.MENTION
                binding.textviewFirst.setText(mentionStr)
            } else if (checkedId == R.id.radioButton2) {
                mode = ExtractionMode.LINK
                binding.textviewFirst.setText(linkStr)
            }
        }
        binding.radioGroup.check(R.id.radioButton)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MODE, mode.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.destroy()
    }

    override fun displayResult(result: String?) {
        binding.textviewResult.text = result
    }

    override fun displayError() {
        binding.textviewResult.text = "No data"
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    enum class ExtractionMode(val value: Int) {
        MENTION(1), LINK(2);

        companion object {
            fun fromValue(value: Int): ExtractionMode {
                return values()
                    .firstOrNull { it.value == value }
                    ?: MENTION
            }
        }
    }
}