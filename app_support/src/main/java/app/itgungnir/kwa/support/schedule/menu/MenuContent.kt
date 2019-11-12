package app.itgungnir.kwa.support.schedule.menu

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.common.popToast
import app.itgungnir.kwa.common.renderFooter
import app.itgungnir.kwa.common.simpleDialog
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.schedule.ScheduleActivity
import app.itgungnir.kwa.support.schedule.ScheduleDelegate
import app.itgungnir.kwa.support.schedule.ScheduleState
import app.itgungnir.kwa.support.schedule.ScheduleViewModel
import app.itgungnir.kwa.support.schedule.dialog.EditScheduleDialog
import kotlinx.android.synthetic.main.view_schedule_menu_content.view.*
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import app.itgungnir.kwa.common.widget.easy_adapter.Differ
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem
import app.itgungnir.kwa.common.widget.easy_adapter.bind
import app.itgungnir.kwa.common.widget.list_footer.ListFooter
import app.itgungnir.kwa.common.widget.status_view.StatusView

class MenuContent @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = context as ScheduleActivity,
            viewModelClass = ScheduleViewModel::class.java
        )
    }

    private val fragmentManager by lazy { (context as ScheduleActivity).supportFragmentManager }

    private var listAdapter: EasyAdapter? = null

    private var footer: ListFooter? = null

    init {

        View.inflate(context, R.layout.view_schedule_menu_content, this)

        schedulePage.apply {
            // Refresh Layout
            refreshLayout().setOnRefreshListener {
                viewModel.getScheduleList()
            }
            // Status View
            statusView().addDelegate(StatusView.Status.SUCCEED, R.layout.view_status_list) {
                val list = it.findViewById<RecyclerView>(R.id.list)
                // Easy Adapter
                listAdapter = list.bind(
                    diffAnalyzer = object : Differ {
                        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                            (oldItem as ScheduleState.ScheduleVO).id == (newItem as ScheduleState.ScheduleVO).id

                        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                            (oldItem as ScheduleState.ScheduleVO).title == (newItem as ScheduleState.ScheduleVO).title &&
                                    oldItem.content == newItem.content && oldItem.targetDate == newItem.targetDate &&
                                    oldItem.type == newItem.type && oldItem.priority == newItem.priority

                        override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Bundle? {
                            oldItem as ScheduleState.ScheduleVO
                            newItem as ScheduleState.ScheduleVO
                            val bundle = Bundle()
                            if (oldItem.title != newItem.title || oldItem.content != newItem.content ||
                                oldItem.targetDate != newItem.targetDate
                            ) {
                                bundle.putBoolean("PL_DESC", true)
                            }
                            if (oldItem.type != newItem.type) {
                                bundle.putInt("PL_TYPE", newItem.type)
                            }
                            if (oldItem.priority != newItem.priority) {
                                bundle.putInt("PL_PRIORITY", newItem.priority)
                            }
                            return if (bundle.isEmpty) null else bundle
                        }
                    })
                    .addDelegate({ true }, ScheduleDelegate(
                        clickCallback = { position, data ->
                            viewModel.setState { copy(dismissFlag = null) }
                            EditScheduleDialog(position, data).show(
                                fragmentManager,
                                EditScheduleDialog::class.java.name
                            )
                        },
                        longClickCallback = { position, id ->
                            context.simpleDialog(fragmentManager, "确定要删除该日程吗？") {
                                viewModel.deleteSchedule(position, id)
                            }
                        }
                    ))
                    .initialize()
                // List Footer
                footer = ListFooter.Builder()
                    .bindTo(list)
                    .render(R.layout.view_list_footer) { view, status -> renderFooter(view, status) }
                    .doOnLoadMore {
                        if (!refreshLayout().isRefreshing) {
                            viewModel.loadMoreScheduleList()
                        }
                    }
                    .build()
            }.addDelegate(StatusView.Status.EMPTY, R.layout.view_status_list_empty) {
                it.findViewById<TextView>(R.id.tip).text = "没有符合条件的日程~"
            }
        }

        observeVM()
    }

    private fun observeVM() {

        viewModel.pick(ScheduleState::refreshing)
            .observe(context as ScheduleActivity, Observer { refreshing ->
                refreshing?.a?.let {
                    schedulePage.refreshLayout().isRefreshing = it
                }
            })

        viewModel.pick(ScheduleState::items, ScheduleState::hasMore)
            .observe(context as ScheduleActivity, Observer { states ->
                states?.let {
                    when (it.a.isNotEmpty()) {
                        true -> schedulePage.statusView().succeed { listAdapter?.update(states.a) }
                        else -> schedulePage.statusView().empty { }
                    }
                    footer?.onLoadSucceed(it.b)
                }
            })

        viewModel.pick(ScheduleState::loading)
            .observe(context as ScheduleActivity, Observer { loading ->
                if (loading?.a == true) {
                    footer?.onLoading()
                }
            })

        viewModel.pick(ScheduleState::error)
            .observe(context as ScheduleActivity, Observer { error ->
                error?.a?.message?.let {
                    (context as ScheduleActivity).popToast(it)
                    footer?.onLoadFailed()
                }
            })
    }
}