package app.itgungnir.kwa.support.schedule.done

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.common.ScheduleDoneActivity
import app.itgungnir.kwa.common.renderFooter
import app.itgungnir.kwa.common.simpleDialog
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.common.widget.easy_adapter.bind
import app.itgungnir.kwa.common.widget.list_footer.ListFooter
import app.itgungnir.kwa.common.widget.status_view.StatusView
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.schedule.ScheduleDelegate
import kotlinx.android.synthetic.main.activity_schedule_done.*
import my.itgungnir.grouter.annotation.Route
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import org.jetbrains.anko.toast

@Route(ScheduleDoneActivity)
class ScheduleDoneActivity : AppCompatActivity() {

    private var listAdapter: EasyAdapter? = null

    private var footer: ListFooter? = null

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = this,
            viewModelClass = ScheduleDoneViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_done)
        initComponent()
        observeVM()
    }

    private fun initComponent() {

        headBar.title("已完成日程").back { finish() }

        scheduleDonePage.apply {
            // Refresh Layout
            refreshLayout().setOnRefreshListener {
                viewModel.getScheduleList()
            }
            // Status View
            statusView().addDelegate(StatusView.Status.SUCCEED, R.layout.view_status_list) {
                val list = it.findViewById<RecyclerView>(R.id.list)
                // Easy Adapter
                listAdapter = list.bind()
                    .addDelegate({ true }, ScheduleDelegate(
                        clickCallback = { _, _ -> },
                        longClickCallback = { position, id ->
                            context.simpleDialog(supportFragmentManager, "确定要删除该日程吗？") {
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
                it.findViewById<TextView>(R.id.tip).text = "还没有完成任何日程~"
            }
        }

        viewModel.getScheduleList()
    }

    private fun observeVM() {

        viewModel.pick(ScheduleDoneState::refreshing)
            .observe(this, Observer { refreshing ->
                refreshing?.a?.let {
                    scheduleDonePage.refreshLayout().isRefreshing = it
                }
            })

        viewModel.pick(ScheduleDoneState::items, ScheduleDoneState::hasMore)
            .observe(this, Observer { states ->
                states?.let {
                    when (it.a.isNotEmpty()) {
                        true -> scheduleDonePage.statusView().succeed { listAdapter?.update(states.a) }
                        else -> scheduleDonePage.statusView().empty { }
                    }
                    footer?.onLoadSucceed(it.b)
                }
            })

        viewModel.pick(ScheduleDoneState::loading)
            .observe(this, Observer { loading ->
                if (loading?.a == true) {
                    footer?.onLoading()
                }
            })

        viewModel.pick(ScheduleDoneState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    toast(it)
                    footer?.onLoadFailed()
                }
            })
    }
}