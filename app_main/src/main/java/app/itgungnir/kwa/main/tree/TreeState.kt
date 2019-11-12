package app.itgungnir.kwa.main.tree

import my.itgungnir.rxmvvm.core.mvvm.State
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem

data class TreeState(
    val refreshing: Boolean = false,
    val items: List<TreeVO> = listOf(),
    val error: Throwable? = null
) : State {

    data class TreeVO(
        val name: String,
        val children: List<TreeTagVO>
    ) : ListItem {

        data class TreeTagVO(
            val id: Int,
            val name: String
        ) : ListItem
    }
}