package com.almitasoft.wowchat.UI.adaptors

import com.almitasoft.wowchat.UI.Fragments.ChatsFragment
import com.almitasoft.wowchat.UI.Fragments.UserFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPageAdapter (fm : FragmentManager)  : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {
    override fun getItem(position: Int): Fragment {
        when (position){
            0 ->{
                return UserFragment()
            }
            1->{
                return ChatsFragment()
            }

        }

        return UserFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position){
            0->{
                return "Friends"
            }
            1->{
                return "Chats"
            }
        }

        return null!!
        }


    override fun getCount() = 2
}



