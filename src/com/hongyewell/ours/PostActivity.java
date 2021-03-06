package com.hongyewell.ours;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hongyewell.pojo.User;
import com.hongyewell.util.ActivityCollector;
import com.hongyewell.util.WebUtil;

public class PostActivity extends Activity {
	private Button postInfoButton;
	private EditText editTitle;
	private EditText editContent;
	private String inputTitle,inputContent,username;
	private User user;
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		setContentView(R.layout.activity_post);
		
		postInfoButton = (Button) findViewById(R.id.btn_postInfo);
		editTitle = (EditText) findViewById(R.id.edit_title);
		editContent = (EditText) findViewById(R.id.edit_content);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		user = (User) bundle.getSerializable("user");
		username = user.getUsername();
	    id =String.valueOf(user.getId()) ;
		
		postInfoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputTitle = editTitle.getText().toString();
				inputContent = editContent.getText().toString();
				
				if (inputTitle == null || inputTitle.length() <= 0 || inputContent == null || inputContent.length() <= 0) {
					Toast.makeText(PostActivity.this, "您可能还没输入标题或内容o(>﹏<)o",Toast.LENGTH_SHORT ).show();
				}
				else 
				{
					new AsyncTask<Void, Void, Void>(){

						@Override
						protected Void doInBackground(Void... arg0) {
							
								WebUtil webUtil = new WebUtil();
								webUtil.postInfo(id, inputTitle,inputContent);
							return null;
						}
						
					}.execute();
					//跳转至主页...
					Intent intent = new Intent(PostActivity.this,MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
