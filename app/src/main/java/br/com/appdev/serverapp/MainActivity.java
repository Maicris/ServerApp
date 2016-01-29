package br.com.appdev.serverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView lstContacts;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstContacts = (ListView) findViewById(R.id.lstContacts);
        adapter = new ContactsAdapter(this);
    }

    public void updateAdapter() {
        lstContacts.setAdapter(adapter);
    }
}
