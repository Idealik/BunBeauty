package com.bunbeauty.ideal.myapplication.clean_architecture.data.api

import com.bunbeauty.ideal.myapplication.clean_architecture.callback.subscribers.message.*
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Dialog
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Message
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.Subscriber
import com.bunbeauty.ideal.myapplication.clean_architecture.data.db.models.entity.User
import com.google.firebase.database.*

class MessageFirebase {

    private val firebaseLinks = mutableListOf<DatabaseReference>()

    fun deleteLinks() {
        for (link in firebaseLinks) {
            link.removeValue()
        }
    }

    fun insert(message: Message) {
        val messageRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(message.dialogId)
            .child(message.userId)
            .child(message.id)

        val items = HashMap<String, Any>()
        items[Message.MESSAGE] = message.message
        items[Message.TIME] = ServerValue.TIMESTAMP
        items[Message.OWNER_ID] = message.ownerId

        if (message.orderId.isNotEmpty())
            items[Message.ORDER_ID] = message.orderId
        if (message.finishOrderTime != 0L)
            items[Message.FINISH_ORDER_TIME_ID] = message.finishOrderTime

        items[Message.TYPE] = message.type
        messageRef.updateChildren(items)
    }

    fun update(message: Message) {
        val messageRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(message.dialogId)
            .child(message.userId)
            .child(message.id)

        val items = HashMap<String, Any>()
        items[Message.MESSAGE] = message.message
        items[Message.TIME] = ServerValue.TIMESTAMP

        if (message.orderId.isNotEmpty())
            items[Message.ORDER_ID] = message.orderId
        if (message.finishOrderTime != 0L)
            items[Message.FINISH_ORDER_TIME_ID] = message.finishOrderTime

        items[Message.TYPE] = message.type
        messageRef.updateChildren(items)
    }

    fun getByDialogId(
        dialog: Dialog,
        loadingLimit: Int,
        messagesCallback: MessagesCallback,
        messageCallback: MessageCallback,
        updateMessageCallback: UpdateMessageCallback
    ) {

        val messageQuery = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(dialog.id)
            .child(dialog.user.id).limitToLast(loadingLimit)

        messageQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount == 0L) messagesCallback.returnList(emptyList())
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        messageQuery.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(messageSnapshot: DataSnapshot, p1: String?) {
                if (!messageSnapshot.hasChildren()) return
                updateMessageCallback.returnUpdatedCallback(
                    getMessageFromSnapshot(messageSnapshot)
                )
            }

            override fun onChildAdded(messageSnapshot: DataSnapshot, previousId: String?) {
                if (!messageSnapshot.hasChildren()) return
                val addedMessage =
                    getMessageFromSnapshot(messageSnapshot)
                addedMessage.dialogId = dialog.id
                addedMessage.userId = dialog.user.id
                messageCallback.returnGottenObject(addedMessage)
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    fun deleteByOrderId(
        dialog: Dialog,
        orderId: String,
        deleteAllMessageCallback: DeleteAllMessageCallback
    ) {
        val messageQuery = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(dialog.id)
            .child(dialog.user.id)
            .orderByChild(Message.ORDER_ID).equalTo(orderId)

        messageQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                if (snapshot.childrenCount > 0) {
                    for (messageSnapshot in snapshot.children) {
                        val message = getMessageFromSnapshot(messageSnapshot)
                        delete(dialog, message)
                        message.dialogId = dialog.id
                        message.userId = dialog.user.id
                        message.orderId = orderId
                        messages.add(message)
                    }
                    deleteAllMessageCallback.returnDeletedList(messages)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    fun delete(dialog: Dialog, message: Message) {
        val subscriberRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(dialog.id)
            .child(dialog.user.id)
            .child(message.id)

        subscriberRef.removeValue()
    }

    fun getLastMessage(
        myId: String,
        companionId: String,
        messageCallback: MessageCallback
    ) {
        val messageRef = FirebaseDatabase.getInstance()
            .getReference(Dialog.DIALOGS)
            .child(myId)
            .child(companionId)
            .orderByChild(Message.TIME).limitToLast(10)

        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(messagesSnapshot: DataSnapshot) {

                var lastMessage = Message()
                if (messagesSnapshot.childrenCount > 0) {
                    for (messageSnapshot in messagesSnapshot.children.reversed()) {
                        val message = getMessageFromSnapshot(messageSnapshot)
                        if (message.type == Message.TEXT_MESSAGE_STATUS || message.ownerId == User.getMyId()) {
                            lastMessage = message
                            break
                        }
                    }
                }

                lastMessage.dialogId = myId
                lastMessage.userId = companionId
                messageCallback.returnGottenObject(lastMessage)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getMessageFromSnapshot(messageSnapshot: DataSnapshot): Message {
        val message = Message()
        message.id = messageSnapshot.key!!
        message.message = messageSnapshot.child(Message.MESSAGE).value as? String ?: ""
        message.time = messageSnapshot.child(Message.TIME).value as? Long ?: 0
        message.finishOrderTime =
            messageSnapshot.child(Message.FINISH_ORDER_TIME_ID).value as? Long ?: 0
        message.type = messageSnapshot.child(Message.TYPE).getValue(Int::class.java) ?: 0
        message.ownerId = messageSnapshot.child(Message.OWNER_ID).value as? String ?: ""
        message.orderId = messageSnapshot.child(Message.ORDER_ID).value as? String ?: ""
        return message
    }

    fun getIdForNew(userId: String, dialogId: String) =
        FirebaseDatabase.getInstance().getReference(User.USERS)
            .child(userId)
            .child(Dialog.DIALOGS)
            .child(dialogId)
            .child(Message.MESSAGES).push().key!!

}