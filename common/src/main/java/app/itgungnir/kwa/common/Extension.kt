package app.itgungnir.kwa.common

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import app.itgungnir.kwa.common.util.glide.GlideApp
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jakewharton.rxbinding2.view.RxView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.util.concurrent.TimeUnit

/**
 * 加载网络图片到ImageView中
 */
fun ImageView.load(url: String) = GlideApp.with(this.context)
    .load(url.replaceFirst("http://", "https://"))
    .placeholder(R.drawable.icon_developer)
    .error(R.drawable.icon_developer)
    .transform(CenterCrop(), RoundedCornersTransformation(20, 0))
    .into(this)

/**
 * 加载本地res文件夹中的图片到ImageView中（目前仅供无图模式下使用）
 */
fun ImageView.load(imgRes: Int) = GlideApp.with(this.context)
    .load(imgRes)
    .placeholder(R.drawable.icon_developer)
    .error(R.drawable.icon_developer)
    .transform(CenterCrop(), RoundedCornersTransformation(20, 0))
    .into(this)

/**
 * dp转px
 */
fun Context.dp2px(dp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

/**
 * 获取XML配置中的颜色
 */
fun Context.color(id: Int) = ContextCompat.getColor(this, id)

/**
 * 隐藏软键盘
 */
fun View.hideSoftInput() = (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
    ?.hideSoftInputFromWindow(windowToken, 0)

/**
 * 显示/隐藏控件
 */
fun View.ifShow(flag: Boolean) = apply {
    visibility = if (flag) View.VISIBLE else View.GONE
}

/**
 * 防抖动的点击事件
 */
fun View.onAntiShakeClick(milliSeconds: Long = 500L, block: (View) -> Unit) = RxView.clicks(this)
    .throttleFirst(milliSeconds, TimeUnit.MILLISECONDS)
    .subscribe { block.invoke(this) }!!
