package app.itgungnir.kwa.common.base

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-12
 */
abstract class BaseLazyFragment : BaseFragment() {

    private var isInitialized: Boolean = false

    override fun onResume() {
        super.onResume()
        if (isInitialized) {
            return
        }
        onLazyLoad()
        isInitialized = true
    }

    abstract fun onLazyLoad()
}