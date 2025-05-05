package com.example.sqlite_database_integration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private ArrayList<ContactModel> contacts;
    private OnContactItemClickListener listener;

    // Interface for handling item clicks
    public interface OnContactItemClickListener {
        void onContactItemClick(ContactModel contact);

        void onContactClick(ContactModel contact);
    }

    public ContactAdapter(Context context, ArrayList<ContactModel> contacts, OnContactItemClickListener listener) {
        this.context = context;
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactModel contact = contacts.get(position);
        holder.contactIdTextView.setText(String.valueOf(contact.getId()));
        holder.contactNameTextView.setText(contact.getName());
        holder.contactPhoneTextView.setText(contact.getPhoneNo());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactItemClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateContactList(ArrayList<ContactModel> newContacts) {
        this.contacts.clear();
        this.contacts.addAll(newContacts);
        notifyDataSetChanged();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactIdTextView, contactNameTextView, contactPhoneTextView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactIdTextView = itemView.findViewById(R.id.tvContactId);
            contactNameTextView = itemView.findViewById(R.id.tvContactName);
            contactPhoneTextView = itemView.findViewById(R.id.tvContactPhone);
        }
    }
}
