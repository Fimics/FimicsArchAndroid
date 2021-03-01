package org.devio.`as`.proj.biz_home.profile

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.common.route.HiRoute
import org.devio.hi.library.util.AppGlobals

internal object ProfileUtil {
    fun spannableTabItem(topText: Int, bottomText: String): CharSequence? {
        val spanStr = topText.toString()
        val ssb = SpannableStringBuilder()
        val ssTop = SpannableString(spanStr)

        val spanFlag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        ssTop.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(AppGlobals.get()!!, R.color.color_000)),
            0,
            ssTop.length,
            spanFlag
        )
        ssTop.setSpan(AbsoluteSizeSpan(18, true), 0, ssTop.length, spanFlag)
        ssTop.setSpan(StyleSpan(Typeface.BOLD), 0, ssTop.length, spanFlag)

        ssb.append(ssTop)
        ssb.append(bottomText)

        return ssb
    }
}