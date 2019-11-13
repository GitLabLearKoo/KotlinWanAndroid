package app.itgungnir.kwa.main.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import app.itgungnir.kwa.common.MainActivity
import app.itgungnir.kwa.common.color
import app.itgungnir.kwa.common.http.HttpUtil
import app.itgungnir.kwa.common.simpleDialog
import app.itgungnir.kwa.common.widget.icon_font.IconFontView
import app.itgungnir.kwa.main.R
import app.itgungnir.kwa.main.home.HomeFragment
import app.itgungnir.kwa.main.mine.MineFragment
import app.itgungnir.kwa.main.project.ProjectFragment
import app.itgungnir.kwa.main.tree.TreeFragment
import app.itgungnir.kwa.main.weixin.WeixinFragment
import kotlinx.android.synthetic.main.activity_main.*
import my.itgungnir.grouter.annotation.Route
import org.jetbrains.anko.textColor

data class TabItem(
    val title: String,
    val unselectedIcon: String,
    val selectedIcon: String
)

@Route(MainActivity)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        initMenuDrawer()
        initBottomNavigation()
        checkForUpdate()
    }

    /**
     * 初始化DrawerLayout，设置其中菜单的点击事件，并与Toolbar联动
     */
    private fun initMenuDrawer() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_action_main_search -> Unit
                R.id.menu_action_main_websites -> Unit
                R.id.menu_action_main_navigation -> Unit
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
                R.id.menu_nav_collection -> Unit
                R.id.menu_nav_todo -> Unit
                R.id.menu_nav_setting -> Unit
                R.id.menu_nav_feedback -> Unit
                R.id.menu_nav_about -> Unit
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
                TabItem(
                    title = "首页",
                    unselectedIcon = getString(R.string.icon_home_normal),
                    selectedIcon = getString(R.string.icon_home_select)
                ) to HomeFragment(),
                TabItem(
                    title = "知识体系",
                    unselectedIcon = getString(R.string.icon_tree_normal),
                    selectedIcon = getString(R.string.icon_tree_select)
                ) to TreeFragment(),
                TabItem(
                    title = "公众号",
                    unselectedIcon = getString(R.string.icon_weixin_normal),
                    selectedIcon = getString(R.string.icon_weixin_select)
                ) to WeixinFragment(),
                TabItem(
                    title = "项目",
                    unselectedIcon = getString(R.string.icon_project_normal),
                    selectedIcon = getString(R.string.icon_project_select)
                ) to ProjectFragment(),
                TabItem(
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
     * 验证版本更新
     */
    private fun checkForUpdate() {
        intent.getStringExtra("upgradeUrl")?.let {
            val upgradeUrl = intent.getStringExtra("upgradeUrl")
            val upgradeVersionName = intent.getStringExtra("upgradeVersionName")
            val upgradeDesc = intent.getStringExtra("upgradeDesc")
            simpleDialog(
                manager = supportFragmentManager,
                msg = "发现新版本：\r\nV$upgradeVersionName\r\n\r\n$upgradeDesc",
                onConfirm = { confirmUpdate(upgradeUrl, upgradeVersionName) }
            )
        }
    }

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

    private fun startDownloadApk(upgradeUrl: String, upgradeVersionName: String) {
        val fileName = "KWA_${upgradeVersionName.replace(".", "_")}.apk"
        HttpUtil.instance.downloadApk(this, upgradeUrl, fileName)
    }
}
