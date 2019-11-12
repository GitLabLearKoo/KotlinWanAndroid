package app.itgungnir.kwa.main.home.search

import android.os.Bundle
import android.view.View
import android.widget.TextView
import app.itgungnir.kwa.common.color
import app.itgungnir.kwa.common.onAntiShakeClick
import app.itgungnir.kwa.common.redux.AppRedux
import app.itgungnir.kwa.common.redux.ClearSearchHistory
import app.itgungnir.kwa.main.R
import kotlinx.android.synthetic.main.list_item_search_history.view.*
import app.itgungnir.kwa.common.widget.easy_adapter.BaseDelegate
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.common.widget.flex.FlexView
import app.itgungnir.kwa.common.widget.status_view.StatusView
import org.jetbrains.anko.textColor

class SearchHistoryDelegate(
    private val keyClickCallback: (String) -> Unit
) : BaseDelegate<SearchState.SearchHistoryVO>() {

    override fun layoutId(): Int = R.layout.list_item_search_history

    override fun onCreateVH(container: View) {
        container.apply {
            // Clear Button
            clearView.onAntiShakeClick(2000L) {
                AppRedux.instance.dispatch(ClearSearchHistory)
                statusView.empty { }
                clearView.apply {
                    isEnabled = false
                    textColor = context.color(R.color.clr_divider)
                }
            }
            // Status View
            statusView.addDelegate(StatusView.Status.SUCCEED, R.layout.view_status_flex) {
                it.findViewById<FlexView>(R.id.children).bind<SearchState.SearchTagVO>(
                    layoutId = R.layout.list_item_tag,
                    render = { view, data ->
                        view.findViewById<TextView>(R.id.tagView).apply {
                            text = data.name
                            textColor = context.color(R.color.text_color_level_1)
                            onAntiShakeClick(2000L) {
                                keyClickCallback.invoke(data.name)
                            }
                        }
                    }
                )
            }.addDelegate(StatusView.Status.EMPTY, R.layout.view_status_flex_empty) {
                it.findViewById<TextView>(R.id.tip).text = "快来搜点干货吧( •̀ ω •́ )✧"
            }
        }
    }

    override fun onBindVH(
        item: SearchState.SearchHistoryVO,
        holder: EasyAdapter.VH,
        position: Int,
        payloads: MutableList<Bundle>
    ) {

        holder.render(item) {

            if (item.data.isEmpty()) {
                statusView.empty { }
                clearView.apply {
                    isEnabled = false
                    textColor = context.color(R.color.clr_divider)
                }
            } else {
                statusView.succeed {
                    (it as FlexView).refresh(item.data)
                }
                clearView.apply {
                    isEnabled = true
                    textColor = context.color(R.color.colorAccent)
                }
            }
        }
    }
}