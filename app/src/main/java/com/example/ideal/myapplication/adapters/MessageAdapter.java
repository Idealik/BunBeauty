package com.example.ideal.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ideal.myapplication.R;
import com.example.ideal.myapplication.adapters.chatElements.MessageOrderElement;
import com.example.ideal.myapplication.adapters.chatElements.MessageReviewElement;
import com.example.ideal.myapplication.fragments.objects.Message;
import com.example.ideal.myapplication.other.DBHelper;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private int numberItems;
    private  ArrayList<Message> messageList;
    private Context context;
    private DBHelper dbHelper;
    private static final String ORDER_STATUS = "order status";

    public MessageAdapter(int numberItems, ArrayList<Message> messages, DBHelper dbHelper){
        this.numberItems = numberItems;
        messageList = messages;
        this.dbHelper = dbHelper;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // в этом методе создаем ViewHolder
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.message_element;
        //Класс, который позволяет создавать представления из xml файла
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // откуда, куда, необходимо ли помещать в родителя
        View view = layoutInflater.inflate(layoutIdForListItem,viewGroup,false);

        MessageViewHolder viewHolder = new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int index) {
        messageViewHolder.bind(messageList.get(index));
    }

    @Override
    public int getItemCount() {
        //общее количество элементов в нашем списке
        return numberItems;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        View view;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void bind(Message message){
            if(message.getStatus().equals(ORDER_STATUS)){
                MessageOrderElement messageOrderElement = new MessageOrderElement(message, view, context, dbHelper);
            }else {
                MessageReviewElement messageReviewElement = new MessageReviewElement(message,view,context);
            }
        }
    }

}
