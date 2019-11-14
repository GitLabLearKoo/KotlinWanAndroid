package app.itgungnir.kwa.support.setting.delegate

import android.os.Bundle
import android.view.View
import app.itgungnir.kwa.common.onAntiShakeClick
import app.itgungnir.kwa.common.widget.easy_adapter.BaseDelegate
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.setting.SettingState
import kotlinx.android.synthetic.main.list_item_setting_navigable.view.*

class NavigableDelegate(
    private val navigateCallback: (Int) -> Unit
) : BaseDelegate<SettingState.NavigableVO>() {

    override fun layoutId(): Int = R.layout.list_item_setting_navigable

    override fun onCreateVH(container: View) {
    }

    override fun onBindVH(
        item: SettingState.NavigableVO,
        holder: EasyAdapter.VH,
        position: Int,
        payloads: MutableList<Bundle>
    ) {

        holder.render(item) {

            this.onAntiShakeClick(2000L) {
                navigateCallback.invoke(item.id)
            }

            iconView.text = item.iconFont

            titleView.text = item.title
        }
    }
}