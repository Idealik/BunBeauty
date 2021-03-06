package com.bunbeauty.ideal.myapplication.clean_architecture.data.api

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.dialog.DialogCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.dialog.DialogChangedCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.dialog.DialogsCallback
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Dialog
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.google.firebase.database.*

class DialogFirebase {

    private val referencesMap = hashMapOf<DatabaseReference, ChildEventListener>()

    fun removeObservers() {
        referencesMap.forEach {
            it.key.removeEventListener(it.value)
        }
    }

    fun insert(dialog: Dialog) {
        val dialogRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(dialog.id)
            .child(dialog.user.id)

        val dialogItems = HashMap<String, Any>()
        dialogItems[Dialog.IS_CHECKED] = dialog.isChecked
        dialogRef.updateChildren(dialogItems)

        val items = HashMap<String, Any>()
        items[Dialog.IS_CHECKED] = dialog.isChecked
        dialogRef.updateChildren(items)
    }

    fun update(dialog: Dialog) {
        val dialogRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(dialog.id) // user id
            .child(dialog.user.id)

        val items = HashMap<String, Any>()
        items[Dialog.IS_CHECKED] = dialog.isChecked
        dialogRef.updateChildren(items)
    }

    fun getDialogsByUserId(
        userId: String,
        dialogsCallback: DialogsCallback,
        dialogChangedCallback: DialogChangedCallback,
        dialogCallback: DialogCallback
    ) {

        val dialogsRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(userId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dialogsSnapshot: DataSnapshot) {

                val dialogs = arrayListOf<Dialog>()
                for (dialogSnapshot in dialogsSnapshot.children) {
                    dialogs.add(getDialogFromSnapshot(dialogSnapshot, userId))
                }

                dialogsCallback.returnList(dialogs)

                val childEventListener =
                    object : ChildEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onChildChanged(dialogSnapshot: DataSnapshot, p1: String?) {
                            val changedDialog = getDialogFromSnapshot(dialogSnapshot, userId)
                            dialogChangedCallback.returnChanged(changedDialog)
                        }

                        override fun onChildAdded(
                            dialogSnapshot: DataSnapshot,
                            previousId: String?
                        ) {
                            if (dialogs.isNotEmpty()) {
                                if (previousId == dialogs.last().id) {
                                    val addedDialog = getDialogFromSnapshot(dialogSnapshot, userId)
                                    dialogs.add(addedDialog)
                                    dialogCallback.returnGottenObject(addedDialog)
                                }
                            } else {
                                val addedDialog = getDialogFromSnapshot(dialogSnapshot, userId)
                                dialogs.add(addedDialog)
                                dialogCallback.returnGottenObject(addedDialog)
                            }

                        }

                        override fun onChildRemoved(p0: DataSnapshot) {
                        }
                    }
                dialogsRef.addChildEventListener(childEventListener)
                referencesMap[dialogsRef] = childEventListener
            }

            override fun onCancelled(error: DatabaseError) {
                // Some error
            }
        }

        dialogsRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun getById(dialog: Dialog, dialogCallback: DialogCallback) {
        val dialogsRef = FirebaseDatabase.getInstance()
            .getReference(User.USERS)
            .child(dialog.ownerId)
            .child(Dialog.DIALOGS)
            .child(dialog.id)

        dialogsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dialogSnapshot: DataSnapshot) {
                dialogCallback.returnGottenObject(
                    getDialogFromSnapshot(
                        dialogSnapshot,
                        dialog.ownerId
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Some error
            }
        })
    }

    private fun returnDialogList(
        dialogsSnapshot: DataSnapshot,
        userId: String
    ): ArrayList<Dialog> {
        val dialogs = arrayListOf<Dialog>()
        for (dialogSnapshot in dialogsSnapshot.children) {
            dialogs.add(getDialogFromSnapshot(dialogSnapshot, userId))
        }
        return dialogs
    }

    private fun getDialogFromSnapshot(dialogSnapshot: DataSnapshot, userId: String): Dialog {
        val dialog = Dialog()
        dialog.id = userId
        dialog.ownerId = userId
        dialog.isChecked = dialogSnapshot.child(Dialog.IS_CHECKED).value as? Boolean ?: true
        dialog.user.id = dialogSnapshot.key!!

        return dialog
    }

    fun getIdForNew(userId: String) = FirebaseDatabase.getInstance().getReference(User.USERS)
        .child(userId)
        .child(Dialog.DIALOGS).push().key!!


}