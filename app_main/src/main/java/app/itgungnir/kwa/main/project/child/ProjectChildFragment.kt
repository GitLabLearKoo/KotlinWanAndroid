package app.itgungnir.kwa.main.project.child

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.common.popToast
import app.itgungnir.kwa.common.renderFooter
import app.itgungnir.kwa.main.R
import kotlinx.android.synthetic.main.fragment_project_child.*
import my.itgungnir.rxmvvm.core.mvvm.LazyFragment
import my.itgungnir.rxmvvm.core.mvvm.buildFragmentViewModel
import my.itgungnir.ui.easy_adapter.Differ
import my.itgungnir.ui.easy_adapter.EasyAdapter
import my.itgungnir.ui.easy_adapter.ListItem
import my.itgungnir.ui.easy_adapter.bind
import my.itgungnir.ui.list_footer.ListFooter
import my.itgungnir.ui.status_view.StatusView

class ProjectChildFragment : LazyFragment() {

    private var listAdapter: EasyAdapter? = null

    private var footer: ListFooter? = null

    private val viewModel by lazy {
        buildFragmentViewModel(
            fragment = this,
            viewModelClass = ProjectChildViewModel::class.java
        )
    }

    companion object {
        fun newInstance(flag: Int) = ProjectChildFragment().apply {
            arguments = Bundle().apply { putInt("FLAG", flag) }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_project_child

    override fun initComponent() {
        projectPage.apply {
            // Refresh Layout
            refreshLayout().setOnRefreshListener {
                flag()?.let { viewModel.getProjects(it) }
            }
            // Status View
            statusView().addDelegate(StatusView.Status.SUCCEED, R.layout.view_status_list) {
                val list = it.findViewById<RecyclerView>(R.id.list)
                // Recycler View
                listAdapter = list.bind(
                    diffAnalyzer = object : Differ {
                        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                            oldItem as ProjectChildState.ProjectArticleVO
                            newItem as ProjectChildState.ProjectArticleVO
                            return oldItem.id == newItem.id && oldItem.originId == newItem.originId
                        }

                        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
                            oldItem as ProjectChildState.ProjectArticleVO
                            newItem as ProjectChildState.ProjectArticleVO
                            return oldItem.date == newItem.date
                        }

                        override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Bundle? {
                            oldItem as ProjectChildState.ProjectArticleVO
                            newItem as ProjectChildState.ProjectArticleVO
                            val bundle = Bundle()
                            if (oldItem.date != newItem.date) {
                                bundle.putString("PL_DATE", newItem.date)
                            }
                            return if (bundle.isEmpty) null else bundle
                        }
                    })
                    .addDelegate({ true }, ProjectChildDelegate())
                    .initialize()
                // Footer
                footer = ListFooter.Builder()
                    .bindTo(list)
                    .render(R.layout.view_list_footer) { view, status -> renderFooter(view, status) }
                    .doOnLoadMore {
                        if (!refreshLayout().isRefreshing) {
                            flag()?.let { flag -> viewModel.loadMoreProjects(flag) }
                        }
                    }
                    .build()
            }.addDelegate(StatusView.Status.EMPTY, R.layout.view_status_list_empty) {
                it.findViewById<TextView>(R.id.tip).text = "本栏目暂无项目~"
            }
        }
    }

    override fun onLazyLoad() {
        flag()?.let { viewModel.getProjects(it) }
    }

    override fun observeVM() {

        viewModel.pick(ProjectChildState::refreshing)
            .observe(this, Observer { refreshing ->
                refreshing?.a?.let {
                    projectPage.refreshLayout().isRefreshing = it
                }
            })

        viewModel.pick(ProjectChildState::items, ProjectChildState::hasMore)
            .observe(this, Observer { states ->
                states?.let {
                    when (it.a.isNotEmpty()) {
                        true -> projectPage.statusView().succeed { listAdapter?.update(states.a) }
                        else -> projectPage.statusView().empty { }
                    }
                    footer?.onLoadSucceed(it.b)
                }
            })

        viewModel.pick(ProjectChildState::loading)
            .observe(this, Observer { loading ->
                if (loading?.a == true) {
                    footer?.onLoading()
                }
            })

        viewModel.pick(ProjectChildState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    popToast(it)
                    footer?.onLoadFailed()
                }
            })
    }

    private fun flag() = arguments?.getInt("FLAG")
}