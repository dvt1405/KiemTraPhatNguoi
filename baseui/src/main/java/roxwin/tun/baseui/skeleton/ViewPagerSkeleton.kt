package roxwin.tun.baseui.skeleton

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import roxwin.tun.baseui.R

class ViewPagerSkeleton(val viewpager: ViewPager,
                        val tabLayout: TabLayout,
                        val actualAdapter: FragmentStatePagerAdapter?,
                        val fragmentManager: FragmentManager,
                        val tabLayoutRes: Int,
                        val itemLayoutRes: Int) : KunSkeleton.SkeletonScreen {
    private var itemCount: Int = 4
    private val skeletonAdapter by lazy {
        object : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment = DefFragment(itemLayoutRes)

            override fun getCount(): Int = itemCount
        }
    }

    private val skeletonTabLayout by lazy {
        KunSkeleton.bind(tabLayout)
                .layout(tabLayoutRes)
                .build()
    }

    init {

    }

    class Builder(val viewpager: ViewPager, val tabLayout: TabLayout, val fragmentManager: FragmentManager) {
        private var adapter: FragmentStatePagerAdapter? = null
        private var tabBarLayoutRes: Int = R.layout.skeleton_tab_layout
        private var viewPagerItemRes: Int = R.layout.skeleton_minibar_item
        fun adapter(adapter: FragmentStatePagerAdapter?): Builder {
            this.adapter = adapter
            return this
        }

        fun tabLayout(@LayoutRes layout: Int?) {
            layout?.let {
                this.tabBarLayoutRes = it
            }
        }

        fun fragmentItemLayout(@LayoutRes layout: Int?) {
            layout?.let {
                this.viewPagerItemRes = it
            }
        }

        fun build(): ViewPagerSkeleton = ViewPagerSkeleton(viewpager, tabLayout, adapter, fragmentManager, tabBarLayoutRes, viewPagerItemRes)
        fun run() = build().run()
        fun run(onRunListener: OnSkeletonRunListener?) = build().run(onRunListener)
    }


    class DefFragment(val layoutRes: Int) : Fragment(R.layout.skeleton_viewpager_fragment) {
        private val recycler_view by lazy {
            requireView().findViewById<RecyclerView>(R.id.recycler_view)
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            recycler_view.layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean {
                    return false
                }

                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            recycler_view.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
            KunSkeleton.bind(recycler_view)
                    .adapter(null)
                    .layoutItem(layoutRes)
                    .run()
        }
    }


    override fun run(onRunListener: OnSkeletonRunListener?) {
        viewpager.adapter = skeletonAdapter
        skeletonTabLayout.run(onRunListener)
        onRunListener?.onRun()
    }

    override fun hide(onRunListener: OnSkeletonRunListener?) {
        skeletonTabLayout.hide()
        actualAdapter?.let {
            viewpager.adapter = it
        }
        onRunListener?.onRun()
    }
}