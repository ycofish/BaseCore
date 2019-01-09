package com.ryanyu.basecore.helper

import com.ryanyu.basecore.fragment.RYBaseFragment


object RYLibSetting {
    var headerBackBtnIcon: Int? = null
    var initRYFragmentModule : InitRYFragmentModule? = null
    interface InitRYFragmentModule {
        fun initFragment(fragmentType: Int): RYBaseFragment?
    }
}
