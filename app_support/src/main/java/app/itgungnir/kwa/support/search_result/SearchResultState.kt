package app.itgungnir.kwa.support.search_result

import my.itgungnir.rxmvvm.core.mvvm.State
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem

data class SearchResultState(
    val refreshing: Boolean = false,
    val items: List<SearchResultArticleVO> = listOf(),
    val loading: Boolean = false,
    val hasMore: Boolean = false,
    val error: Throwable? = null
) : State {

    data class SearchResultArticleVO(
        val id: Int,
        val originId: Int,
        val author: String,
        val title: String,
        val date: String,
        val link: String
    ) : ListItem
}