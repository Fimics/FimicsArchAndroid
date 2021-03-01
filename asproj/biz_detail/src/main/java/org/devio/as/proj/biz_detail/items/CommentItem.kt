package org.devio.`as`.proj.biz_detail.items

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.layout_detail_item_comment.*
import kotlinx.android.synthetic.main.layout_detail_item_comment_item.view.*
import org.devio.`as`.proj.biz_detail.R
import org.devio.`as`.proj.biz_detail.model.DetailModel
import org.devio.`as`.proj.common.ext.loadCircle

import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder
import kotlin.math.min

/**
 * 详情页--商品评论模块
 */
class CommentItem(val model: DetailModel) : HiDataItem<DetailModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        holder.comment_title.text = model.commentCountTitle
        val commentTag: String? = model.commentTags
        if (commentTag != null) {
            val tagsArray: List<String>? = commentTag.split(" ")
            if (tagsArray != null && tagsArray.isNotEmpty()) {
                for (index in tagsArray.indices) {
                    //肯定会存在滑动复用的问题 ，此时创建标签的时候，我们需要检查有没有可以复用的

                    val chipGroup = holder.chip_group
                    chipGroup.visibility = View.VISIBLE
                    val chipLabel = if (index < chipGroup.childCount) {
                        chipGroup.getChildAt(index) as Chip
                    } else {
                        val chipLabel = Chip(context)
                        chipLabel.chipCornerRadius = HiDisplayUtil.dp2px(4f).toFloat()
                        chipLabel.chipBackgroundColor =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_faf0
                                )
                            )
                        chipLabel.setTextColor(ContextCompat.getColor(context, R.color.color_999))
                        chipLabel.textSize = 14f
                        chipLabel.gravity = Gravity.CENTER
                        chipLabel.isCheckedIconVisible = false
                        chipLabel.isCheckable = false
                        chipLabel.isChipIconVisible = false

                        holder.chip_group.addView(chipLabel)
                        chipLabel
                    }
                    chipLabel.text = tagsArray[index]
                }
            }
        }

        model.commentModels?.let {
            val commentContainer = holder.comment_container
            commentContainer.visibility = View.VISIBLE
            for (index in 0..min(it.size - 1, 3)) {
                val comment = it[index]
                val itemView = if (index < commentContainer.childCount) {
                    commentContainer.getChildAt(index)
                } else {
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.layout_detail_item_comment_item, commentContainer, false)
                    commentContainer.addView(view)
                    view
                }
                itemView.user_avatar.loadCircle(comment.avatar)
                itemView.user_name.text = comment.nickName
                itemView.comment_content.text = comment.content
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }

}