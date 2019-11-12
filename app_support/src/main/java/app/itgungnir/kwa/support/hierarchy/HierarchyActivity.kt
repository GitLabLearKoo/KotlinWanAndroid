package app.itgungnir.kwa.support.hierarchy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import app.itgungnir.kwa.common.HierarchyActivity
import app.itgungnir.kwa.common.html
import app.itgungnir.kwa.common.widget.easy_adapter.ListItem
import app.itgungnir.kwa.support.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_hierarchy.*
import my.itgungnir.grouter.annotation.Route

@Route(HierarchyActivity)
class HierarchyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hierarchy)
        initComponent()
    }

    private fun initComponent() {
        val json = intent.extras?.getString("json")
        val vo = Gson().fromJson(json, TreeVO::class.java)

        headBar.title(vo.name)
            .back(getString(R.string.icon_back)) { finish() }

        tabLayout.setupWithViewPager(viewPager)

        viewPager.adapter =
            object : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getItem(position: Int): Fragment =
                    HierarchyChildFragment.newInstance(vo.children[position].id)

                override fun getCount(): Int =
                    vo.children.size

                override fun getPageTitle(position: Int) =
                    html(vo.children[position].name)
            }
    }

    private data class TreeVO(
        val name: String,
        val children: List<TreeTagVO>
    ) : ListItem {

        data class TreeTagVO(
            val id: Int,
            val name: String
        ) : ListItem
    }
}