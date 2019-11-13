package app.itgungnir.kwa.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.itgungnir.kwa.App
import app.itgungnir.kwa.BuildConfig
import app.itgungnir.kwa.R
import app.itgungnir.kwa.common.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.permission.GPermission
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

@Route(SplashActivity)
class SplashActivity : AppCompatActivity() {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = SplashViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用Kotlin-anko绘制界面
        object : AnkoComponent<SplashActivity> {
            override fun createView(ui: AnkoContext<SplashActivity>): View = with(ui) {
                verticalLayout {
                    backgroundColor = this@SplashActivity.color(R.color.colorPure)
                    imageView {
                        imageResource = R.mipmap.img_placeholder
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                    }.lparams(ui.ctx.dp2px(140F).toInt(), ui.ctx.dp2px(100F).toInt()) {
                        bottomMargin = ui.ctx.dp2px(50F).toInt()
                    }
                }.apply {
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                }
            }
        }.setContentView(this)
    }

    override fun onStart() {
        super.onStart()
        initComponents()
        observeVM()
    }

    /**
     * 界面初始化时，流程如下：
     * 1. 停留一段时间；
     * 2. 调用GPermission的API请求系统权限；
     * 3. 调用网络接口验证版本更新；
     * 4. 携带版本信息跳转到首页
     */
    @SuppressLint("CheckResult")
    private fun initComponents() {
        Observable.timer(1L, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { requestPermissions() }
    }

    /**
     * 请求权限，成功后开始验证版本更新，失败时直接退出此页
     */
    private fun requestPermissions() {
        GPermission.with(this)
            .showDialogAtPermissionRejection()
            .onGranted { viewModel.checkForUpdates() }
            .onDenied { finish() }
            .request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE to "文件读写",
                Manifest.permission.READ_PHONE_STATE to "获取手机状态"
            )
    }

    /**
     * 监听ViewModel处理的事件
     */
    private fun observeVM() {
        // 处理验证版本信息的事件
        viewModel.pick(SplashState::versionInfo, SplashState::error).observe(this, Observer {
            // 标记应用已经启动
            App.isFirstRun = false
            // 通过GRouter跳转到MainActivity
            val tmpRoute = Router.instance.with(this)
                .target(MainActivity)
            it.b?.let {
                popToast("获取版本信息失败")
            }
            it.a?.let { info ->
                if (info.upgradeVersionCode > BuildConfig.VERSION_CODE) {
                    tmpRoute.addParam("upgradeUrl", info.upgradeUrl)
                        .addParam("upgradeVersionName", info.upgradeVersionName)
                        .addParam("upgradeDesc", info.upgradeDesc)
                }
            }
            tmpRoute.go()
            // 退出当前页
            finish()
        })
    }

    /**
     * 重写返回键的点击事件，在该页点击返回键无效
     */
    override fun onBackPressed() = Unit
}