package com.example.anton.testwork.ui.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.anton.testwork.ui.base.BaseTabFragment

class TabFragmentAdapter(mgr: FragmentManager, val listFragment: List<BaseTabFragment>)
    : FragmentStatePagerAdapter(mgr) {

    override fun getCount() = listFragment.size

    override fun getItem(position: Int) = listFragment[position]

    override fun getPageTitle(position: Int) = listFragment[position].title

    override fun getItemPosition(`object`: Any): Int = FragmentStatePagerAdapter.POSITION_NONE
}