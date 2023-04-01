package com.example.foodstache.Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AddTabPageAdapter(activity: FragmentActivity, private val Tabcount:Int):FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int =Tabcount

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0->AddRecetteFragment()
            1->AddImageFragment()
            2->AddVideoFragment()
            else->AddRecetteFragment()
        }
    }
}