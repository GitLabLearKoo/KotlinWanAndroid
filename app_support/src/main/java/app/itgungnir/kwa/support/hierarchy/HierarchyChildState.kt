package app.itgungnir.kwa.support.hierarchy

import my.itgungnir.rxmvvm.core.mvvm.State
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem

data class HierarchyChildState(
    val refreshing: Boolean = false,
    val items: List<HierarchyArticleVO> = listOf(),
    val loading: Boolean = false,
    val hasMore: Boolean = false,
    val error: Throwable? = null
) : State {

    data class HierarchyArticleVO(
        val id: Int,
        val originId: Int,
        val author: String,
        val title: String,
        val date: String,
        val link: String
    ) : ListItem
}