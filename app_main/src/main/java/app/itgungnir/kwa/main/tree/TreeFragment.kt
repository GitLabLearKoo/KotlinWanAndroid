package app.itgungnir.kwa.main.tree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.common.dp2px
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.common.widget.easy_adapter.bind
import app.itgungnir.kwa.common.widget.status_view.StatusView
import app.itgungnir.kwa.main.R
import app.itgungnir.kwa.main.tree.navigation.NavigationDialog
import app.itgungnir.kwa.main.tree.tools.ToolsDialog
import kotlinx.android.synthetic.main.fragment_tree.*
import my.itgungnir.rxmvvm.core.mvvm.buildActivityViewModel
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.support.v4.toast

class TreeFragment : Fragment() {

    private var listAdapter: EasyAdapter? = null

    private val viewModel by lazy {
        buildActivityViewModel(
            activity = activity!!,
            viewModelClass = TreeViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_tree, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        observeVM()
    }

    private fun initComponent() {
        // Head Bar
        headBar.title("知识体系")
            .addToolButton(R.drawable.icon_hot) {
                ToolsDialog().show(childFragmentManager, ToolsDialog::class.java.name)
            }
            .addToolButton(R.drawable.icon_navigate) {
                NavigationDialog().show(childFragmentManager, NavigationDialog::class.java.name)
            }
        // Common Page
        treePage.apply {
            // Swipe Refresh Layout
            refreshLayout().setOnRefreshListener {
                viewModel.getTreeList()
            }
            // Status View
            statusView().addDelegate(StatusView.Status.SUCCEED, R.layout.view_status_list) {
                it.findViewById<RecyclerView>(R.id.list).apply {
                    bottomPadding = context.dp2px(10.0F).toInt()
                    listAdapter = this.bind()
                        .addDelegate({ true }, TreeDelegate())
                        .initialize()
                }
            }.addDelegate(StatusView.Status.EMPTY, R.layout.view_status_list_empty) {
                it.findViewById<TextView>(R.id.tip).text = "还没有知识体系~"
            }
        }
        // Init Data
        viewModel.getTreeList()
    }

    private fun observeVM() {

        viewModel.pick(TreeState::refreshing)
            .observe(this, Observer { refreshing ->
                refreshing?.a?.let {
                    treePage.refreshLayout().isRefreshing = it
                }
            })

        viewModel.pick(TreeState::items)
            .observe(this, Observer { items ->
                items?.a?.let { data ->
                    treePage.statusView().succeed { listAdapter?.update(data) }
                }
            })

        viewModel.pick(TreeState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    toast(it)
                    treePage.statusView().empty { }
                }
            })
    }
}