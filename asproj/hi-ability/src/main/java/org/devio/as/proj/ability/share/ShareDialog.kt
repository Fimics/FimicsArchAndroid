package org.devio.`as`.proj.ability.share

import android.content.Context
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ability_share_item.*
import kotlinx.android.synthetic.main.ability_share_item.view.*
import org.devio.`as`.proj.ability.R
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.library.util.HiDisplayUtil.*

internal class ShareDialog(context: Context) : AlertDialog(context) {
    private val channels = arrayListOf<ResolveInfo>()
    private var shareChannelClickListener: onShareChannelClickListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        val contentView = RecyclerView(context)
        contentView.layoutManager = GridLayoutManager(context, 4)
        contentView.adapter = ShareAdapter()
        contentView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        contentView.setBackgroundResource(R.drawable.ability_share_panel_bg)
        val dp2px = dp2px(20f)
        contentView.setPadding(dp2px, 0, dp2px, dp2px)
        setContentView(contentView)
        window?.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun setChannels(
        list: List<ResolveInfo>,
        shareChannelClickListener: onShareChannelClickListener
    ) {
        this.shareChannelClickListener = shareChannelClickListener
        channels.clear()
        channels.addAll(list)
    }

    internal inner class ShareAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val pm = context.packageManager
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(
                layoutInflater.inflate(
                    R.layout.ability_share_item,
                    parent,
                    false
                )
            ) {}
        }

        override fun getItemCount(): Int {
            return channels.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val resolveInfo = channels[position]
            holder.itemView.share_text.text = resolveInfo.loadLabel(pm)
            holder.itemView.share_icon.setImageDrawable(resolveInfo.loadIcon(pm))
            holder.itemView.setOnClickListener {
                shareChannelClickListener?.onClickChannel(resolveInfo)
                dismiss()
            }
        }
    }

    interface onShareChannelClickListener {
        fun onClickChannel(resolveInfo: ResolveInfo)
    }
}