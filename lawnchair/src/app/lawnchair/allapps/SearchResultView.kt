package app.lawnchair.allapps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import app.lawnchair.search.CONTACT
import app.lawnchair.search.FILES
import app.lawnchair.search.MARKET_STORE
import app.lawnchair.search.START_PAGE
import app.lawnchair.search.SUGGESTION
import app.lawnchair.search.SearchTargetCompat
import app.lawnchair.search.data.SearchResultActionCallBack
import com.android.app.search.LayoutType

sealed interface SearchResultView {

    val isQuickLaunch: Boolean
    val titleText: CharSequence? get() = null

    fun launch(): Boolean

    fun bind(target: SearchTargetCompat, shortcuts: List<SearchTargetCompat>, callBack: SearchResultActionCallBack?)

    fun getFlags(extras: Bundle): Int {
        var flags = 0
        if (extras.getBoolean(EXTRA_HIDE_SUBTITLE, false)) {
            flags = flags or FLAG_HIDE_SUBTITLE
        }
        if (extras.getBoolean(EXTRA_HIDE_ICON, false)) {
            flags = flags or FLAG_HIDE_ICON
        }
        if (extras.getBoolean(EXTRA_QUICK_LAUNCH, false)) {
            flags = flags or FLAG_QUICK_LAUNCH
        }
        return flags
    }

    fun hasFlag(flags: Int, flag: Int): Boolean {
        return (flags and flag) != 0
    }

    fun shouldHandleClick(targetCompat: SearchTargetCompat): Boolean {
        val packageName = targetCompat.packageName
        return (packageName in listOf(MARKET_STORE, SUGGESTION, CONTACT, FILES)) &&
            targetCompat.layoutType != LayoutType.SMALL_ICON_HORIZONTAL_TEXT &&
            targetCompat.resultType != SearchTargetCompat.RESULT_TYPE_SHORTCUT
    }

    fun handleSearchTargetClick(context: Context, searchTargetIntent: Intent) {
        searchTargetIntent.resolveActivity(context.packageManager)?.let {
            context.startActivity(searchTargetIntent)
        } ?: run {
            Toast.makeText(context, "No app found to handle this action", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val FLAG_HIDE_SUBTITLE = 1 shl 0
        const val FLAG_HIDE_ICON = 1 shl 1
        const val FLAG_QUICK_LAUNCH = 1 shl 2

        const val EXTRA_HIDE_SUBTITLE = "hide_subtitle"
        const val EXTRA_HIDE_ICON = "hide_icon"
        const val EXTRA_QUICK_LAUNCH = "quick_launch"
        const val EXTRA_ICON_COMPONENT_KEY = "icon_component_key"
    }
}
