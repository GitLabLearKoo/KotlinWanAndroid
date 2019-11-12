package app.itgungnir.kwa.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.common.popToast
import app.itgungnir.kwa.common.renderFooter
import app.itgungnir.kwa.common.widget.easy_adapter.Differ
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem
import app.itgungnir.kwa.common.widget.easy_adapter.bind
import app.itgungnir.kwa.common.widget.list_footer.ListFooter
import app.itgungnir.kwa.common.widget.status_view.StatusView
import app.itgungnir.kwa.main.R
import app.itgungnir.kwa.main.home.delegate.BannerDelegate
import app.itgungnir.kwa.main.home.delegate.HomeArticleDelegate
import app.itgungnir.kwa.main.home.search.SearchDialog
import kotlinx.android.synthetic.main.fragment_home.*
import my.itgungnir.rxmvvm.core.mvvm.buildFragmentViewModel

class HomeFragment : Fragment() {

    private var listAdapter: EasyAdapter? = null

    private var footer: ListFooter? = null

    private val viewModel by lazy {
        buildFragmentViewModel(
            fragment = this,
            viewModelClass = HomeViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        observeVM()
    }

    private fun initComponent() {
        // Head Bar
        headBar.title("首页")
            .addToolButton(getString(R.string.icon_search)) {
                SearchDialog().show(childFragmentManager, SearchDialog::class.java.name)
            }
        // Common Page
        homePage.apply {
            // Swipe Refresh Layout
            refreshLayout().setOnRefreshListener {
                viewModel.getHomeData()
            }
            // Status View
            statusView().addDelegate(StatusView.Status.SUCCEED, R.layout.view_status_list) {
                val list = it.findViewById<RecyclerView>(R.id.list)
                // Recycler View
                listAdapter = list.bind(diffAnalyzer = object : Differ {
                    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                        if (oldItem is HomeState.HomeArticleVO && newItem is HomeState.HomeArticleVO) {
                            oldItem.id == newItem.id && oldItem.originId == newItem.originId
                        } else {
                            false
                        }

                    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                        if (oldItem is HomeState.HomeArticleVO && newItem is HomeState.HomeArticleVO) {
                            oldItem.date == newItem.date
                        } else {
                            false
                        }

                    override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Bundle? =
                        if (oldItem is HomeState.HomeArticleVO && newItem is HomeState.HomeArticleVO) {
                            val bundle = Bundle()
                            if (oldItem.date != newItem.date) {
                                bundle.putString("PL_DATE", newItem.date)
                            }
                            if (bundle.isEmpty) null else bundle
                        } else {
                            null
                        }
                }).addDelegate({ data -> data is HomeState.BannerVO }, BannerDelegate())
                    .addDelegate({ data -> data is HomeState.HomeArticleVO }, HomeArticleDelegate())
                    .initialize()
                // Recycler Footer
                footer = ListFooter.Builder()
                    .bindTo(list)
                    .render(R.layout.view_list_footer) { view, status -> renderFooter(view, status) }
                    .doOnLoadMore {
                        if (!homePage.refreshLayout().isRefreshing) {
                            viewModel.loadMoreHomeData()
                        }
                    }.build()
            }.addDelegate(StatusView.Status.EMPTY, R.layout.view_status_list_empty) {
                it.findViewById<TextView>(R.id.tip).text = "暂时没有文章~"
            }
        }
        // Init Data
        viewModel.getHomeData()
    }

    private fun observeVM() {

        viewModel.pick(HomeState::refreshing)
            .observe(this, Observer { refreshing ->
                refreshing?.a?.let {
                    homePage.refreshLayout().isRefreshing = it
                }
            })

        viewModel.pick(HomeState::dataList, HomeState::hasMore)
            .observe(this, Observer { states ->
                states?.let {
                    when (it.a.isNotEmpty()) {
                        true -> homePage.statusView().succeed { listAdapter?.update(states.a) }
                        else -> homePage.statusView().empty { }
                    }
                    footer?.onLoadSucceed(it.b)
                }
            })

        viewModel.pick(HomeState::loading)
            .observe(this, Observer { loading ->
                if (loading?.a == true) {
                    footer?.onLoading()
                }
            })

        viewModel.pick(HomeState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    popToast(it)
                    footer?.onLoadFailed()
                }
            })
    }
}