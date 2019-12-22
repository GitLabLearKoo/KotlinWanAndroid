package app.itgungnir.kwa.main.mine.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.hideSoftInput
import app.itgungnir.kwa.common.onAntiShakeClick
import app.itgungnir.kwa.common.widget.dialog.NoTitleDialogFragment
import app.itgungnir.kwa.main.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.dialog_add_article.*
import my.itgungnir.rxmvvm.core.mvvm.buildFragmentViewModel
import org.jetbrains.anko.support.v4.toast

class AddArticleDialog : NoTitleDialogFragment() {

    private val viewModel by lazy {
        buildFragmentViewModel(
            fragment = this,
            viewModelClass = AddArticleViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_add_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        observeVM()
    }

    @SuppressLint("CheckResult")
    private fun initComponent() {

        headBar.title("新增站外文章").back { this.dismiss() }

        Observable.combineLatest(
            arrayOf(
                RxTextView.textChanges(titleInput.getInput()),
                RxTextView.textChanges(linkInput.getInput())
            )
        ) { items: Array<Any> -> items.all { item -> item.toString().trim().isNotEmpty() } }
            .subscribe { valid: Boolean ->
                when (valid) {
                    true ->
                        addButton.ready("确定新增")
                    else ->
                        addButton.disabled("确定新增")
                }
            }

        addButton.apply {
            disabled("确定新增")
            onAntiShakeClick(2000L) {
                loading()
                it.hideSoftInput()
                val titleStr = titleInput.getInput().editableText.toString().trim()
                val linkStr = linkInput.getInput().editableText.toString().trim()
                viewModel.addArticle(titleStr, linkStr)
            }
        }
    }

    private fun observeVM() {

        viewModel.pick(AddArticleState::succeed)
            .observe(this, Observer { succeed ->
                succeed?.a?.let {
                    addButton.ready("确定新增")
                    this.dismiss()
                }
            })

        viewModel.pick(AddArticleState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    addButton.ready("确定新增")
                    toast(it)
                }
            })
    }
}