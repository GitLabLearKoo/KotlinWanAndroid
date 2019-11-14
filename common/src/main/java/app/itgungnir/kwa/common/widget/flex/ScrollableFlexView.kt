package app.itgungnir.kwa.common.widget.flex

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.itgungnir.kwa.common.R
import app.itgungnir.kwa.common.dp2px
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem
import app.itgungnir.kwa.common.widget.easy_adapter.bind
import app.itgungnir.kwa.common.widget.easy_adapter.update
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager

class ScrollableFlexView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var horizontalSpacing: Float = context.dp2px(10.0F)
    private var verticalSpacing: Float = context.dp2px(10.0F)

    private var manager: FlexboxLayoutManager = FlexboxLayoutManager(context).apply {
        flexDirection = FlexDirection.ROW
        flexWrap = FlexWrap.WRAP
    }

    init {
        layoutManager = manager

        context.obtainStyledAttributes(attrs, R.styleable.ScrollableFlexView).apply {
            horizontalSpacing = getDimension(R.styleable.ScrollableFlexView_sfv_horizontalSpacing, horizontalSpacing)
            verticalSpacing = getDimension(R.styleable.ScrollableFlexView_sfv_verticalSpacing, verticalSpacing)
            recycle()
        }

        addItemDecoration(FlexboxItemDecoration(context).apply {
            this.setDrawable(GradientDrawable().apply {
                setSize(horizontalSpacing.toInt(), verticalSpacing.toInt())
            })
            this.setOrientation(FlexboxItemDecoration.BOTH)
        })
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ListItem> bind(layoutId: Int, render: (view: View, data: T) -> Unit) {
        bind(manager = manager).addDelegate(isForViewType = { true }, delegate = FlexDelegate(layoutId, render))
    }

    fun <T : ListItem> refresh(items: List<T>) {
        update(items)
    }
}