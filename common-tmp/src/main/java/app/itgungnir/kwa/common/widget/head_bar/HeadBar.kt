package app.itgungnir.kwa.common.widget.head_bar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import app.itgungnir.kwa.common.R
import app.itgungnir.kwa.common.ifShow
import app.itgungnir.kwa.common.onAntiShakeClick
import kotlinx.android.synthetic.main.view_head_bar.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.textColor

class HeadBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private var toolsLayout: LinearLayout? = null
    private var menuView: ImageView? = null

    private var textColor: Int = Color.WHITE
    private var showDivider: Boolean = false
    private var dividerColor: Int = Color.LTGRAY
    private var menuBackground: Int = Color.WHITE
    private var menuIconColor: Int = Color.BLACK
    private var menuTitleColor: Int = Color.BLACK

    private val menuList = mutableListOf<MenuItem>()

    init {
        View.inflate(context, R.layout.view_head_bar, this)

        context.obtainStyledAttributes(attrs, R.styleable.HeadBar).apply {
            textColor = getColor(R.styleable.HeadBar_hb_textColor, textColor)
            showDivider = getBoolean(R.styleable.HeadBar_hb_showDivider, showDivider)
            dividerColor = getColor(R.styleable.HeadBar_hb_dividerColor, dividerColor)
            menuBackground = getColor(R.styleable.HeadBar_hb_menuBackground, menuBackground)
            menuIconColor = getColor(R.styleable.HeadBar_hb_menuIconColor, menuIconColor)
            menuTitleColor = getColor(R.styleable.HeadBar_hb_menuTitleColor, menuTitleColor)
            recycle()
        }

        back.imageTintList = ColorStateList.valueOf(textColor)

        title.textColor = textColor

        if (showDivider) {
            divider.visibility = View.VISIBLE
            divider.backgroundColor = dividerColor
        } else {
            divider.visibility = View.GONE
        }

        menu.apply {
            this.imageTintList = ColorStateList.valueOf(textColor)
            setOnClickListener {
                HeadBarMenu(context, menuBackground, menuIconColor, menuTitleColor).apply {
                    setItems(menuList)
                }.showUnder(menu)
            }
        }

        toolsLayout = tools
        menuView = menu
    }

    fun back(onBackPressed: () -> Unit) = apply {
        back.apply {
            visibility = View.VISIBLE
            this.onAntiShakeClick(2000L) {
                onBackPressed.invoke()
            }
        }
        title.leftPadding = 0
    }

    fun title(titleStr: String) = apply {
        title.text = titleStr
    }

    fun addToolButton(iconRes: Int, callback: () -> Unit) = apply {
        val view = LayoutInflater.from(context).inflate(R.layout.view_head_bar_icon, toolsLayout, false)
        view.findViewById<ImageView>(R.id.icon).apply {
            this.imageResource = iconRes
            this.onAntiShakeClick(2000L) {
                callback.invoke()
            }
        }
        toolsLayout?.addView(view)
    }

    fun addMenuItem(iconRes: Int, title: String, callback: () -> Unit) = apply {
        menuView?.visibility = View.VISIBLE
        menuList.add(MenuItem(iconRes, title, callback))
    }

    fun updateToolButton(position: Int, loading: Boolean, iconRes: Int, tintColor: Int) {
        toolsLayout?.getChildAt(position)?.let {
            val icon = it.findViewById<ImageView>(R.id.icon)
            val progress = it.findViewById<ProgressBar>(R.id.progressBar)
            icon.ifShow(!loading)
            progress.ifShow(loading)
            if (!loading) {
                icon.apply {
                    imageResource = iconRes
                    imageTintList = ColorStateList.valueOf(tintColor)
                }
            }
        }
    }

    fun toolButtonCount() = toolsLayout?.childCount ?: 0
}