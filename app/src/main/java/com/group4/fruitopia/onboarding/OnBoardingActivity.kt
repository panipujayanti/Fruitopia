package com.group4.fruitopia.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.viewpager2.widget.ViewPager2
import com.group4.fruitopia.MainActivity
import com.group4.fruitopia.R
import com.group4.fruitopia.databinding.ActivityOnBoardingBinding
import com.group4.fruitopia.login.LoginActivity
import com.group4.fruitopia.onboarding.adapter.OnBoardingAdapter


class OnBoardingActivity : AppCompatActivity() {
    companion object {
        fun navigate(context: Context) {
            context.startActivity(
                Intent(context, OnBoardingActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
    }



    private val binding by lazy {
        ActivityOnBoardingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        setupWindowInsets()
        setupViewPager()
        createPageIndicator()

    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewPager() {
        val viewPager: ViewPager2 = binding.viewPager2
        val adapter = OnBoardingAdapter(this)
        viewPager.adapter = adapter

        binding.btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < (viewPager.adapter?.itemCount ?: 0) - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                navigateToLoginActivity()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updatePageIndicator(position)
            }
        })
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun createPageIndicator() {
        val indicatorLayout = binding.llIndicator
        val adapter = binding.viewPager2.adapter as OnBoardingAdapter

        for (i in 0 until adapter.itemCount) {
            val dot = ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(this@OnBoardingActivity, R.drawable.dot_indicator_inactive))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 0, 16, 0)
                }
            }
            indicatorLayout.addView(dot)
        }

        updatePageIndicator(0)
    }

    private fun updatePageIndicator(position: Int) {
        val indicatorLayout = binding.llIndicator

        for (i in 0 until indicatorLayout.childCount) {
            val dot = indicatorLayout.getChildAt(i) as ImageView
            dot.setImageDrawable(
                if (i == position) ContextCompat.getDrawable(this, R.drawable.dot_indicator_active)
                else ContextCompat.getDrawable(this, R.drawable.dot_indicator_inactive)
            )
        }
    }
}
