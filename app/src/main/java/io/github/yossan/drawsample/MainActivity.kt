package io.github.yossan.drawsample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.yossan.drawsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel() as T
            }
        }).get(MainViewModel::class.java)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)?.apply {
            findViewById<Button>(R.id.pen).apply {
                setOnClickListener { l -> onChangeMode(DrawView.Mode.pen) }
            }
            findViewById<Button>(R.id.eraser).apply {
                setOnClickListener { l -> onChangeMode(DrawView.Mode.eraser) }
            }
        }
    }

    private val drawView: DrawView by lazy {
        findViewById(R.id.draw_view)
    }

    private fun onChangeMode(mode: DrawView.Mode) {
    }
}