package br.com.appdev.serverapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Datastore {

    private static Datastore instance = null;
    private Context context;
    private List<Contact> contacts;
    private ContactsAdapter adapter;

    protected Datastore() {}

    public static Datastore sharedInstance() {
        if (instance == null)
            instance = new Datastore();
        return instance;
    }

    public void setContext(Context context, ContactsAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        contacts = new ArrayList<Contact>();
        new ReadJSON().execute(new String[] {
                "http://www.pherasdeveloper.com.br/news/news_json_lst.asp"
        });
    }

    public void addContact(Contact contact) {

    }

    private class ReadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String text = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(
                        connection.getInputStream());
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String read = reader.readLine();
                while (read != null) {
                    text += read;
                    read = reader.readLine();
                }
                reader.close();
                inputStreamReader.close();
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            try {
                JSONObject json = new JSONObject(jsonStr);
                JSONArray array = json.getJSONArray("newsletter");
                for (int i=0; i<array.length(); i++) {
                    JSONObject news = array.getJSONObject(i);
                    String name = news.getString("nome");
                    String email = news.getString("email");
                    contacts.add(new Contact(name, email));
                }
                adapter.setContacts(contacts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class AddItemJSON extends AsyncTask<String, Void, String> {

        private Contact contact;

        public AddItemJSON(Contact contact) {
            this.contact = contact;
        }

        @Override
        protected String doInBackground(String... urls) {

            String text = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                builder.appendQueryParameter("nome", contact.getName());
                builder.appendQueryParameter("email", contact.getEmail());
                String qry = builder.build().getEncodedQuery();

                OutputStream outputStream = connection.getOutputStream();
                OutputStreamWriter outputStreamWriter =
                        new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter writer = new BufferedWriter(outputStreamWriter);
                writer.write(qry);
                writer.flush();
                writer.close();
                outputStreamWriter.close();
                outputStream.close();

                connection.connect();

                InputStream inputStream = new BufferedInputStream(
                        connection.getInputStream()
                );
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String read = reader.readLine();
                while (read != null) {
                    text += read;
                    read = reader.readLine();
                }
                reader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return text;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);

            try {
                JSONObject json = new JSONObject(jsonStr);
                String result = json.getString("result");
                if (result.contentEquals("ok")) {
                    //chama um método do adapter para inserção do contato na lista
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
