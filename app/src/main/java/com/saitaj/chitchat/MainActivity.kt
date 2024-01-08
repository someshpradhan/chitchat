package com.saitaj.chitchat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.saitaj.chitchat.activity.NumberActivity
import com.saitaj.chitchat.adapter.ViewPagerAdapter
import com.saitaj.chitchat.databinding.ActivityMainBinding
import com.saitaj.chitchat.ui.CallFragment
import com.saitaj.chitchat.ui.ChatFragment
import com.saitaj.chitchat.ui.StoriesFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StoriesFragment())
        fragmentArrayList.add(CallFragment())
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser == null){
            startActivity(Intent(this,NumberActivity::class.java))
            finish()
        }
        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
        binding!!.viewpager.adapter = adapter
        binding!!.tabs.setupWithViewPager(binding!!.viewpager)

    }
}