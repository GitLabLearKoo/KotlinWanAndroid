package app.itgungnir.kwa.support.setting.delegate

import android.os.Bundle
import android.view.View
import app.itgungnir.kwa.common.widget.easy_adapter.BaseDelegate
import app.itgungnir.kwa.common.widget.easy_adapter.EasyAdapter
import app.itgungnir.kwa.support.R
import app.itgungnir.kwa.support.setting.SettingState
import kotlinx.android.synthetic.main.list_item_setting_button.view.*

class ButtonDelegate(
    private val callback: () -> Unit
) : BaseDelegate<SettingState.ButtonVO>() {

    override fun layoutId(): Int = R.layout.list_item_setting_button

    override fun onCreateVH(container: View) {
        container.apply {
            logout.apply {
                ready("退出登录")
                setOnClickListener {
                    this@ButtonDelegate.callback.invoke()
                }
            }
        }
    }

    override fun onBindVH(
        item: SettingState.ButtonVO,
        holder: EasyAdapter.VH,
        position: Int,
        payloads: MutableList<Bundle>
    ) {
    }
}