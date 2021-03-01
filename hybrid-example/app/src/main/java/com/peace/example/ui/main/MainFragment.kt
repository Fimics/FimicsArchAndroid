package com.peace.example.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.Navigation
import com.peace.example.R
import com.peace.hybrid.route.RouteKeys

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val rootView = inflater.inflate(R.layout.main_fragment, container, false)

        val btn = rootView.findViewById<AppCompatButton>(R.id.btn_to_first_web)
        btn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(RouteKeys.URL.name, "file:///android_asset/index.html")
            Navigation.findNavController(rootView)
                .navigate(R.id.action_main_fragment_to_web_fragment, bundle)
        }
        return rootView
    }
}