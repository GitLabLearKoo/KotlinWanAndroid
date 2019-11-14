package app.itgungnir.kwa.support.register

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.RegisterActivity
import app.itgungnir.kwa.common.hideSoftInput
import app.itgungnir.kwa.common.onAntiShakeClick
import app.itgungnir.kwa.support.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import org.jetbrains.anko.toast

@Route(RegisterActivity)
class RegisterActivity : AppCompatActivity() {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = RegisterViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initComponent()
        observeVM()
    }

    @SuppressLint("CheckResult")
    private fun initComponent() {

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

    private fun observeVM() {

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
                    toast(it)
                    register.ready("注册")
                }
            })
    }
}