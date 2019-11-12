package app.itgungnir.kwa.support.schedule

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.ScheduleActivity
import app.itgungnir.kwa.common.ScheduleDoneActivity
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.schedule.dialog.AddScheduleDialog
import app.itgungnir.kwa.support.schedule.menu.MenuContent
import app.itgungnir.kwa.support.schedule.menu.MenuView
import kotlinx.android.synthetic.main.activity_schedule.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.grouter.api.Router
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel

@Route(ScheduleActivity)
class ScheduleActivity : AppCompatActivity() {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = ScheduleViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        initComponent()
        observeVM()
    }

    private fun initComponent() {

        headBar.title("我的日程")
            .back(getString(R.string.icon_back)) { finish() }
            .addToolButton(getString(R.string.icon_add)) {
                viewModel.setState { copy(dismissFlag = null) }
                AddScheduleDialog().show(supportFragmentManager, AddScheduleDialog::class.java.name)
            }
            .addToolButton(getString(R.string.icon_schedule_done)) {
                Router.instance.with(this)
                    .target(ScheduleDoneActivity)
                    .go()
            }

        initDropDownMenu()
    }

    private fun initDropDownMenu() {
        val popUpViews = mutableListOf<View>()
        // 类型
        MenuView(this).apply {
            bind(
                ScheduleState.MenuTabVO(title = "不限", value = null, selected = true),
                ScheduleState.MenuTabVO(title = "工作", value = 1),
                ScheduleState.MenuTabVO(title = "学习", value = 2),
                ScheduleState.MenuTabVO(title = "生活", value = 3)
            )
            selectCallback = { position, title, value ->
                dropDownMenu.toggleTab(selected = position != 0, text = if (position == 0) null else title)
                viewModel.setState { copy(type = value) }
            }
            popUpViews.add(this)
        }
        // 优先级
        MenuView(this).apply {
            bind(
                ScheduleState.MenuTabVO(title = "不限", value = null, selected = true),
                ScheduleState.MenuTabVO(title = "重要", value = 1),
                ScheduleState.MenuTabVO(title = "一般", value = 2)
            )
            selectCallback = { position, title, value ->
                dropDownMenu.toggleTab(selected = position != 0, text = if (position == 0) null else title)
                viewModel.setState { copy(priority = value) }
            }
            popUpViews.add(this)
        }
        // 排序
        MenuView(this).apply {
            bind(
                ScheduleState.MenuTabVO(title = "默认排序", value = 4, selected = true),
                ScheduleState.MenuTabVO(title = "逆序排序", value = 3)
            )
            selectCallback = { position, _, value ->
                dropDownMenu.toggleTab(selected = position != 0)
                value?.let { viewModel.setState { copy(orderBy = it) } }
            }
            popUpViews.add(this)
        }
        // DropDownMenu
        dropDownMenu.setDropDownMenu(popUpViews, MenuContent(this))
    }

    private fun observeVM() {

        viewModel.pick(ScheduleState::type, ScheduleState::priority, ScheduleState::orderBy)
            .observe(this, Observer { states ->
                states?.let {
                    viewModel.getScheduleList(it.a, it.b, it.c)
                }
            })
    }

    override fun onBackPressed() {
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu()
        } else {
            super.onBackPressed()
        }
    }
}