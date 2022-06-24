package com.varun.grogu.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.varun.grogu.R
import com.varun.grogu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry activity of the app.
 *
 * The app [R.navigation.nav_graph] is build upon this activity.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}