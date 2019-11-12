package app.itgungnir.kwa.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-12
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
    }

    abstract fun layoutId(): Int
}