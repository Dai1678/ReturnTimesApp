package org.dai1678.returntimesapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SetMailDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText contactEdit;
    TextInputEditText addressEdit;
    Button quoteAddressButton;

    private static int PICK_CONTACT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mail_detail);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("連絡先の設定");
        }

        contactEdit = findViewById(R.id.contactEdit);
        addressEdit = findViewById(R.id.addressEdit);
        quoteAddressButton = findViewById(R.id.quoteAddress);

        quoteAddressButton.setOnClickListener(this);

        //TODO メアド入力フォームの下に送信先LINEユーザー設定フォームをいれたい
    }

    //ListViewのチェック処理

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if(item.getItemId() == R.id.save_profile){
            String contact = contactEdit.getText().toString();
            String address = addressEdit.getText().toString();

            if(!contact.equals("") && !address.equals("")){
                Intent intent = new Intent();
                intent.putExtra("contact", contact);
                intent.putExtra("address", address);    //メールアドレスをSettingProfileActivityへ送る
                setResult(RESULT_OK, intent);

                finish();
            }else{
                if(contact.equals("")){
                    final int ALERT_CONTACT = 2;

                    FragmentManager fragmentManager = getFragmentManager();

                    AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_CONTACT);
                    alertDialogFragment.show(fragmentManager,"alertDialog");    //警告アラート表示
                }

                if(address.equals("")) {
                    int ALERT_ADDRESS = 3;
                    FragmentManager fragmentManager = getFragmentManager();

                    AlertDialogFragment alertDialogFragment = new AlertDialogFragment(ALERT_ADDRESS);
                    alertDialogFragment.show(fragmentManager, "alertDialog");    //警告アラート表示

                }
            }

            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.quoteAddress){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT){
            if (resultCode == Activity.RESULT_OK){
                Uri contactData = data.getData();
                CursorLoader cursorLoader = new CursorLoader(this, contactData, null, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor.moveToFirst()){
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String mailAddress = "";

                    Cursor mail = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id, null, null);

                    assert mail != null;
                    if (mail.moveToFirst()){
                        mailAddress = mail.getString(mail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }

                    mail.close();

                    contactEdit.setText(name);
                    addressEdit.setText(mailAddress);

                }

                cursor.close();
            }
        }
    }

}
