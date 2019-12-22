package app.itgungnir.kwa

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import app.itgungnir.kwa.common.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.permission.GPermission
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

@Route(SplashActivity)
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 如果App已经启动了，则跳过这个页面，直接跳转到主页
        if (!App.isFirstRun) {
            navigate()
            return
        }

        // 使用Kotlin-anko绘制界面
        object : AnkoComponent<SplashActivity> {
            override fun createView(ui: AnkoContext<SplashActivity>): View = with(ui) {
                verticalLayout {
                    backgroundColor = this@SplashActivity.color(R.color.colorPure)
                    imageView {
                        imageResource = R.drawable.icon_developer
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                    }.lparams(matchParent, wrapContent) {
                        leftMargin = ui.ctx.dp2px(50F).toInt()
                        rightMargin = ui.ctx.dp2px(50F).toInt()
                        bottomMargin = ui.ctx.dp2px(50F).toInt()
                    }
                }.apply {
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                }
            }
        }.setContentView(this)

        initComponents()
    }

    /**
     * 界面初始化时，停留一段时间后调用GPermission的API请求系统权限
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
            .onGranted { navigate() }
            .onDenied { finish() }
            .request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE to "文件读写",
                Manifest.permission.READ_PHONE_STATE to "获取手机状态"
            )
    }

    /**
     * 跳转
     */
    private fun navigate() {
        // 标记应用已经启动
        App.isFirstRun = false
        // 跳转到MainActivity并结束当前页面
        Router.instance.with(this)
            .target(MainActivity)
            .go()
        finish()
    }

    /**
     * 重写返回键的点击事件，在该页点击返回键无效
     */
    override fun onBackPressed() = Unit
}