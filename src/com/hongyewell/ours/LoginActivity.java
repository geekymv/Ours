package com.hongyewell.ours;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hongyewell.pojo.Author;
import com.hongyewell.pojo.User;
import com.hongyewell.util.WebUtil;


public class LoginActivity extends Activity {
	private Button btnLogin,btnRegister;
	private EditText edtUserName;
	private EditText edtPassword;
	private CheckBox ckRememberCB;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_toRegister);
        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        ckRememberCB = (CheckBox) findViewById(R.id.cb_rememberCheck);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			//���˺ź����붼���õ��ı�����
			String account = pref.getString("account", "");
			String password = pref.getString("password", "");
			edtUserName.setText(account);
			edtPassword.setText(password);
			ckRememberCB.setChecked(true);
			/*userLogin();*/
		}
        
        //�����¼
        btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				userLogin();
			}
		});
        
        //���ע��
        btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
       
        
    }
    
    
    //���ýӿڣ��û���¼
    private void userLogin(){
    	new AsyncTask<Void, Integer, Author>() {
			//��ȡ�û�������û���������
			String username = edtUserName.getText().toString();
        	String password = edtPassword.getText().toString();
        	

			@Override
			protected Author doInBackground(Void... arg0) {
				Author author = new Author();
				WebUtil webUtil = new WebUtil();
				author = webUtil.userLogin(username, password);
				return author;
			}
			
			@Override
			protected void onPostExecute(Author result) {
				if (result.getId() == -1) {
					Toast.makeText(LoginActivity.this, "�ף������û����������Ƿ���ȷ~", Toast.LENGTH_SHORT).show();
				}
				else {
					editor = pref.edit();
					if (ckRememberCB.isChecked()) {
						//��鸴ѡ���Ƿ�ѡ��
						editor.putBoolean("remember_password", true);
						editor.putString("account", username);
						editor.putString("password", password);
						
					}else {
						editor.clear();
					}
					editor.commit();//�����ύ
					String username = result.getUsername();
					Toast.makeText(LoginActivity.this, username+"��¼�ɹ�~", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					intent.putExtra("username", result.getUsername());
					intent.putExtra("id", result.getId());
				    startActivity(intent);
					
				}
			}
			
		}.execute();
    }
    
}
