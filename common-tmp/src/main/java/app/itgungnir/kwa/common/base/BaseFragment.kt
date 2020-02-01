package app.itgungnir.kwa.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-12
 */
abstract class BaseFragment : Fragment() {

    private var isInitialized: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    override fun onResume() {
        super.onResume()
        if (!isInitialized) {
            onLazyLoad()
            isInitialized = true
        }
    }

    fun onLazyLoad() {}

    abstract fun layoutId(): Int
}