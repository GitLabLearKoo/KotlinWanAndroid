package app.itgungnir.kwa.support.setting.delegate

import android.os.Bundle
import android.view.View
import app.itgungnir.kwa.common.onAntiShakeClick
import app.itgungnir.kwa.common.redux.AppRedux
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.setting.SettingState
import kotlinx.android.synthetic.main.list_item_setting_digital.view.*
import app.itgungnir.kwa.common.widget.easy_adapter.BaseDelegate
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter

class DigitalDelegate(
    private val digitalClickCallback: (Int) -> Unit
) : BaseDelegate<SettingState.DigitalVO>() {

    override fun layoutId(): Int = R.layout.list_item_setting_digital

    override fun onCreateVH(container: View) {
    }

    override fun onBindVH(
        item: SettingState.DigitalVO,
        holder: EasyAdapter.VH,
        position: Int,
        payloads: MutableList<Bundle>
    ) {

        holder.render(item) {

            this.onAntiShakeClick(2000L) {
                digitalClickCallback.invoke(item.id)
            }

            iconView.text = item.iconFont

            titleView.text = item.title

            digitView.text = when {
                !AppRedux.instance.isAutoCache() -> "0KB"
                payloads.isNotEmpty() -> payloads[0].getString("PL_DIGIT") ?: item.digit
                else -> item.digit
            }
        }
    }
}