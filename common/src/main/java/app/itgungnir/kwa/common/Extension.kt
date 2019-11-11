package app.itgungnir.kwa.common

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import app.itgungnir.kwa.common.util.GlideApp
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.contentView

/**
 * Activity - 弹出SnackBar
 */
fun AppCompatActivity.popToast(content: String) {
    Snackbar.make(contentView!!, content, Snackbar.LENGTH_SHORT).show()
}

/**
 * Fragment - 弹出SnackBar
 */
fun Fragment.popToast(content: String) {
    Snackbar.make(view!!, content, Snackbar.LENGTH_SHORT).show()
}

/**
 * 加载网络图片到ImageView中
 */
fun ImageView.load(url: String) =
    GlideApp.with(this.context)
        .load(url.replaceFirst("http://", "https://"))
        .placeholder(R.mipmap.img_placeholder)
        .error(R.mipmap.img_placeholder)
        .centerCrop()
        .into(this)

/**
 * 加载本地res文件夹中的图片到ImageView中（目前仅供无图模式下使用）
 */
fun ImageView.load(imgRes: Int) =
    GlideApp.with(this.context)
        .load(imgRes)
        .placeholder(R.mipmap.img_placeholder)
        .error(R.mipmap.img_placeholder)
        .centerCrop()
        .into(this)
