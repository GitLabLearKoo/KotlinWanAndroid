package app.itgungnir.kwa.support.register

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.RegisterActivity
import app.itgungnir.kwa.common.popToast
import app.itgungnir.kwa.support.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.rxmvvm.core.mvvm.BaseActivity
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import my.itgungnir.ui.hideSoftInput
import my.itgungnir.ui.onAntiShakeClick

@Route(RegisterActivity)
class RegisterActivity : BaseActivity() {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = RegisterViewModel::class.java
        )
    }

    override fun layoutId(): Int = R.layout.activity_register

    @SuppressLint("CheckResult")
    override fun initComponent() {

        headBar.title("用户注册")
            .back(getString(R.string.icon_back)) { finish() }

        Observable.combineLatest(
            arrayOf(
                RxTextView.textChanges(userNameInput.getInput()),
                RxTextView.textChanges(passwordInput.getInput()),
                RxTextView.textChanges(confirmPwdInput.getInput())
            )
        ) { items: Array<Any> -> items.all { item -> item.toString().trim().isNotEmpty() } }
            .subscribe { valid: Boolean ->
                when (valid) {
                    true ->
                        register.ready("注册")
                    else ->
                        register.disabled("注册")
                }
            }

        register.apply {
            disabled("注册")
            onAntiShakeClick(2000L) {
                hideSoftInput()
                loading()
                val userName = userNameInput.getInput().editableText.toString().trim()
                val password = passwordInput.getInput().editableText.toString().trim()
                val confirmPwd = confirmPwdInput.getInput().editableText.toString().trim()
                viewModel.register(userName, password, confirmPwd)
            }
        }
    }

    override fun observeVM() {

        viewModel.pick(RegisterState::succeed)
            .observe(this, Observer { succeed ->
                succeed?.a?.let {
                    register.ready("注册")
                    finish()
                }
            })

        viewModel.pick(RegisterState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    popToast(it)
                    register.ready("注册")
                }
            })
    }
}