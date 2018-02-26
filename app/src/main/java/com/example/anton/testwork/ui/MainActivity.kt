package com.example.anton.testwork.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.anton.testwork.R
import com.example.anton.testwork.presentation.main.MainPresenter
import com.example.anton.testwork.presentation.main.MainView
import com.example.anton.testwork.ui.adapter.TabFragmentAdapter
import com.example.anton.testwork.ui.base.BaseTabFragment
import com.example.anton.testwork.ui.tabFragments.CardTypeFragment
import com.example.anton.testwork.ui.tabFragments.CardsFragment
import com.example.anton.testwork.ui.tabFragments.CustomersFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun showTabFragments() {
        val tabFragments = listOf<BaseTabFragment>(CustomersFragment(), CardsFragment(), CardTypeFragment())
        (this as? AppCompatActivity)?.supportFragmentManager?.let {
            pager.adapter = TabFragmentAdapter(it, tabFragments)
        }
        tabs.setupWithViewPager(pager)
    }
}
