package br.com.appdev.serverapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ContactsAdapter extends BaseAdapter {

    private List<Contact> contacts;
    private Context context;

    public ContactsAdapter(Context context) {
        this.context = context;
        Datastore.sharedInstance().setContext(context, this);
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        MainActivity delegate = (MainActivity) context;
        delegate.updateAdapter();
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contact contact = contacts.get(position);

        View view = LayoutInflater.from(context).inflate(
                R.layout.contacts_adapter,
                parent,
                false
        );

        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtEmail = (TextView) view.findViewById(R.id.txtEmail);

        txtName.setText(contact.getName());
        txtEmail.setText(contact.getEmail());

        return view;
    }
}
