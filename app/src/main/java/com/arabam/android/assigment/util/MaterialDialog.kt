package com.arabam.android.assigment.util

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.arabam.android.assigment.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialDialog {
    companion object {
        inline fun createDialog(
            context: Context,
            dialogBuilder: AlertDialog.Builder.() -> Unit): AlertDialog {
            val builder = MaterialAlertDialogBuilder(
                context,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered
            ).setTitle(" ")
            builder.dialogBuilder()
            return builder.create()
        }

        fun AlertDialog.Builder.positiveButton(
            @StringRes resId: Int,
            handleClick: (which: Int) -> Unit = {}) {
            this.setPositiveButton(resId) { _, which -> handleClick(which) }
        }

        fun AlertDialog.Builder.singleChoiceItems(
            items: Array<String>,
            checkedItem: Int,
            handleClick: (which: Int) -> Unit = {}) {
            this.setSingleChoiceItems(items, checkedItem) { _, which -> handleClick(which)
            }
        }

        fun AlertDialog.Builder.title(@StringRes titleId: Int) {
            this.setTitle(titleId)
        }

        fun AlertDialog.Builder.view(view: View) {
            this.setView(view)
        }
    }
}
