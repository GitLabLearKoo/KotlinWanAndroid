package app.itgungnir.kwa.support.setting.delegate

import android.os.Bundle
import android.view.View
import app.itgungnir.kwa.common.widget.easy_adapter.BaseDelegate
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.setting.SettingState

class DividerDelegate : BaseDelegate<SettingState.DividerVO>() {

    override fun layoutId(): Int = R.layout.list_item_setting_divider

    override fun onCreateVH(container: View) {}

    override fun onBindVH(
        item: SettingState.DividerVO,
        holder: EasyAdapter.VH,
        position: Int,
        payloads: MutableList<Bundle>
    ) {
    }
}