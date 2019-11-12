package app.itgungnir.kwa.common.util

import android.app.Application
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-12
 */
class RefreshUtil private constructor() : Util {

    companion object {
        val instance by lazy { RefreshUtil() }
    }

    override fun init(application: Application) {
        // Default refresh initializer
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
            layout.setEnableAutoLoadMore(false)
            layout.setEnableLoadMoreWhenContentNotFull(false)
        }
        // Default refresh header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        // Default refresh footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}