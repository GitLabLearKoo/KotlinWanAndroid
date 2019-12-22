package app.itgungnir.kwa.main.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import app.itgungnir.kwa.common.html
import app.itgungnir.kwa.main.R
import app.itgungnir.kwa.main.project.child.ProjectChildFragment
import kotlinx.android.synthetic.main.fragment_project.*
import my.itgungnir.rxmvvm.core.mvvm.buildFragmentViewModel
import org.jetbrains.anko.support.v4.toast

class ProjectFragment : Fragment() {

    private val viewModel by lazy {
        buildFragmentViewModel(
            fragment = this,
            viewModelClass = ProjectViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_project, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        observeVM()
    }

    private fun initComponent() {

        tabLayout.setupWithViewPager(viewPager)

        // Init Data
        viewModel.getProjectTabs()
    }

    private fun observeVM() {

        viewModel.pick(ProjectState::tabs)
            .observe(this, Observer { tabs ->
                tabs?.a?.let {
                    viewPager.adapter = object :
                        FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                        override fun getItem(position: Int): Fragment =
                            ProjectChildFragment.newInstance(it[position].id)

                        override fun getCount(): Int =
                            it.size

                        override fun getPageTitle(position: Int) =
                            html(it[position].name)
                    }
                }
            })

        viewModel.pick(ProjectState::error)
            .observe(this, Observer { error ->
                error?.a?.message?.let {
                    toast(it)
                }
            })
    }
}