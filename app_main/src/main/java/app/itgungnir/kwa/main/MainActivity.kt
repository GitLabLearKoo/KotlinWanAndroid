package app.itgungnir.kwa.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.MainActivity
import app.itgungnir.kwa.common.color
import app.itgungnir.kwa.common.http.HttpUtil
import app.itgungnir.kwa.common.simpleDialog
import app.itgungnir.kwa.main.home.HomeFragment
import app.itgungnir.kwa.main.project.ProjectFragment
import app.itgungnir.kwa.main.tree.TreeFragment
import app.itgungnir.kwa.main.weixin.WeixinFragment
import kotlinx.android.synthetic.main.activity_main.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor

@Route(MainActivity)
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = MainViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
        observeVM()
    }

    private fun initComponents() {
        initMenuDrawer()
        initBottomNavigation()
        // Check for updates
        viewModel.checkForUpdates()
    }

    /**
     * 初始化DrawerLayout，设置其中菜单的点击事件，并与Toolbar联动
     */
    private fun initMenuDrawer() {
        // 点击菜单按钮弹出菜单
        main_menu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        // 设置NavigationView中菜单项的点击事件
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_nav_collection -> Unit // WZY
                R.id.menu_nav_todo -> Unit // WZY
                R.id.menu_nav_setting -> Unit // WZY
                R.id.menu_nav_feedback -> Unit // WZY
                R.id.menu_nav_about -> Unit // WZY
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    /**
     * 初始化底部导航栏
     */
    private fun initBottomNavigation() {
        val selectedColor = this.color(R.color.colorNavActive)
        val unSelectedColor = this.color(R.color.colorNavNormal)
        bottomTab.init(
            targetFrameId = R.id.fragment,
            fragmentManager = supportFragmentManager,
            items = listOf(
                MainState.TabItem(
                    iconRes = R.drawable.icon_home,
                    title = "首页"
                ) to HomeFragment(),
                MainState.TabItem(
                    iconRes = R.drawable.icon_hierarchy,
                    title = "知识体系"
                ) to TreeFragment(),
                MainState.TabItem(
                    iconRes = R.drawable.icon_subscription,
                    title = "公众号"
                ) to WeixinFragment(),
                MainState.TabItem(
                    iconRes = R.drawable.icon_project,
                    title = "项目"
                ) to ProjectFragment()
            ),
            itemLayoutId = R.layout.list_item_main_bottom_tab,
            render = { view, data, selected ->
                val icon = view.findViewById<ImageView>(R.id.iconView)
                val title = view.findViewById<TextView>(R.id.titleView)
                icon.imageResource = data.iconRes
                title.text = data.title
                when (selected) {
                    true -> {
                        icon.imageTintList = ColorStateList.valueOf(selectedColor)
                        title.textColor = selectedColor
                    }
                    false -> {
                        icon.imageTintList = ColorStateList.valueOf(unSelectedColor)
                        title.textColor = unSelectedColor
                    }
                }
            }
        )
    }

    /**
     * 监听VM状态
     */
    private fun observeVM() {
        // 监听检验软件更新的状态
        viewModel.pick(MainState::versionInfo).observe(this, Observer {
            it.a?.let { info ->
                simpleDialog(
                    manager = supportFragmentManager,
                    msg = "发现新版本：\r\nV${info.upgradeVersionName}\r\n\r\n${info.upgradeDesc}",
                    onConfirm = { confirmUpdate(info.upgradeUrl, info.upgradeVersionName) }
                )
            }
        })
    }

    /**
     * 检查更新：检查网络状态
     */
    private fun confirmUpdate(upgradeUrl: String, upgradeVersionName: String) {
        if (!HttpUtil.instance.isNetworkConnected(this)) {
            simpleDialog(supportFragmentManager, "当前没有网络！")
            return
        }
        if (!HttpUtil.instance.isWiFiConnected(this)) {
            simpleDialog(supportFragmentManager, "当前处于非WIFI环境，确定要继续下载吗？") {
                startDownloadApk(upgradeUrl, upgradeVersionName)
            }
            return
        }
        startDownloadApk(upgradeUrl, upgradeVersionName)
    }

    /**
     * 检查更新：开始下载应用
     */
    private fun startDownloadApk(upgradeUrl: String, upgradeVersionName: String) {
        val fileName = "KWA_${upgradeVersionName.replace(".", "_")}.apk"
        HttpUtil.instance.downloadApk(this, upgradeUrl, fileName)
    }
}
