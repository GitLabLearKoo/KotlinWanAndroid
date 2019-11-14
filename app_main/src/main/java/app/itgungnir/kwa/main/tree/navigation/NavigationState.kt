package app.itgungnir.kwa.main.tree.navigation

import app.itgungnir.kwa.common.widget.easy_adapter.ListItem
import my.itgungnir.rxmvvm.core.mvvm.State

data class NavigationState(
    val tabs: List<NavTabVO> = listOf(),
    val items: List<NavigationVO> = listOf(),
    val error: Throwable? = null
) : State {

    data class NavTabVO(
        val name: String,
        val selected: Boolean = false
    ) : ListItem

    data class NavigationVO(
        val title: String,
        val children: List<NavTagVO> = listOf()
    ) : ListItem {

        data class NavTagVO(
            val id: Int,
            val originId: Int,
            val name: String,
            val link: String
        ) : ListItem
    }
}