package com.example.androidplatform.util

import android.text.InputFilter
import android.text.Spanned

class MinMaxFilter(private val min: Int, private val max: Int) : InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length)).toInt()
            if (isInRange(min, max, input))
                return null
        } catch (_: NumberFormatException) { }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int) = c in a..b
}