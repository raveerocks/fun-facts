package io.raveerocks.funfacts.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import io.raveerocks.funfacts.ActivityViewModel
import io.raveerocks.funfacts.R
import kotlin.random.Random

class MainViewModel:ViewModel() {

    companion object {
        val androidFacts = arrayOf(
            "The first commercial Android device was launched in September 2008",
            "The Android operating system has over 2 billion monthly active users",
            "The first Android version (1.0) was released on September 23, 2008",
            "The first smart phone running Android was the HTC Dream called the T-Mobile G1 in " + "some countries"
        )

        val californiaFacts = arrayOf(
            "The most populated state in the United States is California",
            "Three out of the ten largest U. S. cities are in California",
            "The largest tree in the world can be found in California",
            "California became a state in 1850"
        )
    }

    fun getFactToDisplay(authenticationState:ActivityViewModel.AuthenticationState,context: Context): String {

        val funFactType = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getString(context.getString(R.string.preference_fact_type_key),
                context.resources.getStringArray(R.array.fact_type)[0])

        return if (authenticationState == ActivityViewModel.AuthenticationState.UNAUTHENTICATED || funFactType.equals(
                context.getString(R.string.fact_type_android))) {
            androidFacts[Random.nextInt(0, androidFacts.size)]
        } else {
            californiaFacts[Random.nextInt(0, californiaFacts.size)]
        }
    }
}