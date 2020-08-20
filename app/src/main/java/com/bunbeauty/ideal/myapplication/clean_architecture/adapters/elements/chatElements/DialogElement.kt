package com.bunbeauty.ideal.myapplication.clean_architecture.adapters.elements.chatElements

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ideal.myapplication.R
import com.bunbeauty.ideal.myapplication.clean_architecture.business.CircularTransformation
import com.bunbeauty.ideal.myapplication.clean_architecture.business.WorkWithStringsApi
import com.bunbeauty.ideal.myapplication.clean_architecture.business.WorkWithTimeApi
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Dialog
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.bunbeauty.ideal.myapplication.clean_architecture.mvp.activities.chat.MessagesActivity
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import java.util.*

class DialogElement(
    private val view: View,
    private val context: Context
) {
    private lateinit var avatarDialogElementImage: ImageView
    private lateinit var nameDialogElementText: TextView
    private lateinit var lastMessageDialogElementText: TextView
    private lateinit var messageTimeDialogElementText: TextView
    private lateinit var isCheckedDialogElementText: TextView
    private lateinit var dialogElementLayout: MaterialCardView
    private lateinit var dialog: Dialog

    fun createElement(dialog: Dialog) {
        this.dialog = dialog
        onViewCreated(view)
        setData(dialog)
    }

    private fun onViewCreated(view: View) {
        avatarDialogElementImage = view.findViewById(R.id.avatarDialogElementImage)
        nameDialogElementText = view.findViewById(R.id.nameDialogElementText)
        lastMessageDialogElementText = view.findViewById(R.id.lastMessageDialogElementText)
        messageTimeDialogElementText = view.findViewById(R.id.messageTimeDialogElementText)
        isCheckedDialogElementText = view.findViewById(R.id.isCheckedDialogElementText)
        dialogElementLayout = view.findViewById(R.id.dialogElementLayout)
        dialogElementLayout.setOnClickListener {
            goToDialog()
        }
    }

    private fun setData(dialog: Dialog) {
        showAvatar(dialog.user)
        nameDialogElementText.text = "${dialog.user.name} ${dialog.user.surname}"
        lastMessageDialogElementText.text =
            WorkWithStringsApi.cutString(dialog.lastMessage.message, 23)
        messageTimeDialogElementText.text =
            WorkWithTimeApi.getDateInFormatYMDHMS(Date(dialog.lastMessage.time)).substring(11, 16)

        if (!dialog.isChecked) {
            isCheckedDialogElementText.visibility = View.VISIBLE
        } else {
            isCheckedDialogElementText.visibility = View.GONE
        }
    }

    private fun showAvatar(user: User) {
        Picasso.get()
            .load(user.photoLink)
            .resize(
                context.resources.getDimensionPixelSize(R.dimen.photo_avatar_width),
                context.resources.getDimensionPixelSize(R.dimen.photo_avatar_width)
            )
            .centerCrop()
            .transform(CircularTransformation())
            .into(avatarDialogElementImage)
    }

    private fun goToDialog() {
        val intent = Intent(context, MessagesActivity::class.java)
        intent.putExtra(Dialog.DIALOG, dialog)
        intent.putExtra(User.USER, dialog.user)

        val myDialog = Dialog()
        myDialog.ownerId = dialog.user.id
        myDialog.user.id = dialog.ownerId
        myDialog.id = dialog.user.id

        intent.putExtra(Dialog.COMPANION_DIALOG, myDialog)
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(0, 0)
    }

    companion object {
        private const val TAG = "DBInf"
    }

}