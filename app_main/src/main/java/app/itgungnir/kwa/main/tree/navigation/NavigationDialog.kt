package app.itgungnir.kwa.main.tree.navigation

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.main.R
import kotlinx.android.synthetic.main.dialog_navigation.*
import my.itgungnir.rxmvvm.core.mvvm.buildFragmentViewModel
import app.itgungnir.kwa.common.widget.dialog.FullScreenDialog
import app.itgungnir.kwa.common.widget.easy_adapter.Differ
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem
import app.itgungnir.kwa.common.widget.easy_adapter.bind
import app.itgungnir.kwa.common.widget.easy_adapter.update
import org.jetbrains.anko.support.v4.toast

class NavigationDialog : FullScreenDialog() {

    private var currIndex = 0

    private lateinit var leftManager: LinearLayoutManager
    private lateinit var rightManager: LinearLayoutManager

    private val viewModel by lazy {
        buildFragmentViewModel(
            fragment = this,
            viewModelClass = NavigationViewModel::class.java
        )
    }

    override fun layoutId(): Int = R.layout.dialog_navigation

    override fun initComponent() {

        leftManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rightManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        headBar.title("导航")
            .back(getString(R.string.icon_back)) { this.dismiss() }

        sideBar.bind(
            manager = leftManager,
            diffAnalyzer = object : Differ {
                override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                    (oldItem as NavigationState.NavTabVO).name == (newItem as NavigationState.NavTabVO).name

                override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                    (oldItem as NavigationState.NavTabVO).selected == (newItem as NavigationState.NavTabVO).selected

                override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Bundle? {
                    oldItem as NavigationState.NavTabVO
                    newItem as NavigationState.NavTabVO
                    val bundle = Bundle()
                    if (oldItem.selected != newItem.selected) {
                        bundle.putBoolean("PL_SELECT", newItem.selected)
                    }
                    return if (bundle.isEmpty) null else bundle
                }
            }
        ).addDelegate({ true }, SideBarDelegate { position ->
            selectTabAt(position)
            rightManager.scrollToPositionWithOffset(position, 0)
        }).initialize()

        navList.apply {
            bind(manager = rightManager)
                .addDelegate({ true }, NavigationDelegate())
                .initialize()
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (rightManager.findLastVisibleItemPosition() != rightManager.itemCount - 1) {
                        var index = rightManager.findFirstCompletelyVisibleItemPosition()
                        if (index == -1) {
                            index = rightManager.findFirstVisibleItemPosition()
                        }
                        selectTabAt(index)
                    }
                }
            })
        }

        // Init data
        viewModel.getNavigationList()
    }

    override fun observeVM() {

        viewModel.pick(NavigationState::tabs)
            .observe(this, Observer { tabs ->
                tabs?.a?.let {
                    sideBar.update(it)
                }
            })

        viewModel.pick(NavigationState::items)
            .observe(this, Observer { items ->
                items?.a?.let {
                    navList.update(it)
                }
            })

        viewModel.pick(NavigationState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    toast(it)
                }
            })
    }

    private fun selectTabAt(position: Int) {
        if (position != currIndex) {
            viewModel.setState {
                copy(
                    tabs = tabs.mapIndexed { i, item ->
                        item.copy(selected = i == position)
                    }
                )
            }
            leftManager.scrollToPositionWithOffset(position, 0)
            currIndex = position
        }
    }
}