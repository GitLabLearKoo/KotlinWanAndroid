package app.itgungnir.kwa.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.MainActivity
import app.itgungnir.kwa.common.color
import app.itgungnir.kwa.common.http.HttpUtil
import app.itgungnir.kwa.common.simpleDialog
import app.itgungnir.kwa.common.widget.icon_font.IconFontView
import app.itgungnir.kwa.main.home.HomeFragment
import app.itgungnir.kwa.main.mine.MineFragment
import app.itgungnir.kwa.main.project.ProjectFragment
import app.itgungnir.kwa.main.tree.TreeFragment
import app.itgungnir.kwa.main.weixin.WeixinFragment
import kotlinx.android.synthetic.main.activity_main.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
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
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_action_main_search -> Unit // WZY
                R.id.menu_action_main_websites -> Unit // WZY
                R.id.menu_action_main_navigation -> Unit // WZY
            }
            true
        }
        // 设置DrawerLayout与Toolbar联动
        ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0).apply {
            drawerLayout.addDrawerListener(this)
            this.syncState()
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
        val selectedColor = this.color(R.color.clr_icon_selected)
        val unSelectedColor = this.color(R.color.clr_icon_unselected)
        bottomTab.init(
            targetFrameId = R.id.fragment,
            fragmentManager = supportFragmentManager,
            items = listOf(
                MainState.TabItem(
                    title = "首页",
                    unselectedIcon = getString(R.string.icon_home_normal),
                    selectedIcon = getString(R.string.icon_home_select)
                ) to HomeFragment(),
                MainState.TabItem(
                    title = "知识体系",
                    unselectedIcon = getString(R.string.icon_tree_normal),
                    selectedIcon = getString(R.string.icon_tree_select)
                ) to TreeFragment(),
                MainState.TabItem(
                    title = "公众号",
                    unselectedIcon = getString(R.string.icon_weixin_normal),
                    selectedIcon = getString(R.string.icon_weixin_select)
                ) to WeixinFragment(),
                MainState.TabItem(
                    title = "项目",
                    unselectedIcon = getString(R.string.icon_project_normal),
                    selectedIcon = getString(R.string.icon_project_select)
                ) to ProjectFragment(),
                MainState.TabItem(
                    title = "我的",
                    unselectedIcon = getString(R.string.icon_mine_normal),
                    selectedIcon = getString(R.string.icon_mine_select)
                ) to MineFragment()
            ),
            itemLayoutId = R.layout.list_item_main_bottom_tab,
            render = { view, data, selected ->
                val icon = view.findViewById<IconFontView>(R.id.iconView)
                val title = view.findViewById<TextView>(R.id.titleView)
                title.text = data.title
                when (selected) {
                    true -> {
                        icon.text = data.selectedIcon
                        icon.textColor = selectedColor
                        title.textColor = selectedColor
                    }
                    false -> {
                        icon.text = data.unselectedIcon
                        icon.textColor = unSelectedColor
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
