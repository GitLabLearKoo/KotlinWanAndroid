package app.itgungnir.kwa.support.login

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.LoginActivity
import app.itgungnir.kwa.common.RegisterActivity
import app.itgungnir.kwa.common.hideSoftInput
import app.itgungnir.kwa.common.onAntiShakeClick
import app.itgungnir.kwa.common.redux.AppRedux
import app.itgungnir.kwa.common.redux.LocalizeUserInfo
import app.itgungnir.kwa.support.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import org.jetbrains.anko.toast

@Route(LoginActivity)
class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = LoginViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponent()
        observeVM()
    }

    @SuppressLint("CheckResult")
    private fun initComponent() {

        headBar.title("用户登录").back { finish() }

        Observable.combineLatest(
            arrayOf(
                RxTextView.textChanges(userNameInput.getInput()),
                RxTextView.textChanges(passwordInput.getInput())
            )
        ) { items: Array<Any> -> items.all { item -> item.toString().trim().isNotEmpty() } }
            .subscribe { valid: Boolean ->
                when (valid) {
                    true ->
                        login.ready("登录")
                    else ->
                        login.disabled("登录")
                }
            }

        login.apply {
            disabled("登录")
            onAntiShakeClick(2000L) {
                hideSoftInput()
                loading()
                val userName = userNameInput.getInput().editableText.toString().trim()
                val password = passwordInput.getInput().editableText.toString().trim()
                viewModel.login(userName, password)
            }
        }

        toRegister.onAntiShakeClick(2000L) {
            it.hideSoftInput()
            Router.instance.with(this)
                .target(RegisterActivity)
                .go()
        }
    }

    private fun observeVM() {

        viewModel.pick(LoginState::userInfo)
            .observe(this, Observer { userInfo ->
                userInfo?.a?.let {
                    AppRedux.instance.dispatch(
                        LocalizeUserInfo(it.collectIds - -1, it.userName), listOf()
                    )
                    login.ready("登录")
                    finish()
                }
            })

        viewModel.pick(LoginState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    toast(it)
                    login.ready("登录")
                }
            })
    }
}