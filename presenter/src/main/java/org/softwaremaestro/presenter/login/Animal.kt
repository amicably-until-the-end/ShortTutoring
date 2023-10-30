package org.softwaremaestro.presenter.login

import org.softwaremaestro.presenter.R

enum class Animal(val resId: Int, val mName: String) {
    DOG(R.drawable.ic_profile_dog, "dog"),
    FOX(R.drawable.ic_profile_fox, "fox"),
    DUCK(R.drawable.ic_profile_duck, "duck"),
    LION(R.drawable.ic_profile_lion, "lion"),
    PENGUIN(R.drawable.ic_profile_penguin, "penguin"),
    POLAR_BEAR(R.drawable.ic_profile_polar_bear, "polar_bear"),
    TIGER(R.drawable.ic_profile_tiger, "tiger");

    companion object {
        fun toName(resId: Int) = values().find { it.resId == resId }!!.mName
        fun toResId(mName: String) = values().find { it.mName == mName }!!.resId

        fun getRandom(): Animal {
            val seed = (Math.random() * values().size).toInt()
            return Animal.values()[seed]
        }
    }
}