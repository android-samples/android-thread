package com.example.threadtest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {
	TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextView = (TextView)findViewById(R.id.textView1);
	}
	
	// -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //
	// 普通スレッド
	// -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //
	class MyThread extends Thread{
		String mName;
		public MyThread(String name){
			mName = name;
		}
		
		@Override
		public void run() {
			// 通常バックグランドをここに記述します
			try{
				for(int i = 0; i < 10; i++){
					Log.i("test", mName + ":" + i);
					Thread.sleep(10);
				}
			}
			catch(Exception ex){
			}

			// 結果
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					//取得したイメージをImageViewに設定
					mTextView.setText("Thread Test2 " + mName);
				}
			});
		}
	}

	// ボタン
	Handler	 mHandler = new Handler();		//Handlerのインスタンス生成
	public void buttonMethodThread1(View button){
		// スレッド起動
		new MyThread("A").start();
		new MyThread("B").start();
		//MyThread t;
		//t.join();
	}
	
	// -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //
	// AsyncTask
	// -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //
	// 引数、プログレス、結果
	class HttpTask extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(
				String... params) {
			if(params.length == 0)return null;
			String url = params[0];
			try{
				HttpGet req = new HttpGet(url);
				DefaultHttpClient client =
					new DefaultHttpClient();
				HttpResponse res =
					client.execute(req);
				String result =
					EntityUtils.toString(
						res.getEntity());
				return result;
			}
			catch(Exception ex){
				return "error: " + ex.toString();
			}
		}
		@Override
		protected void onPostExecute(String result) {
			mTextView.setText(result);
			super.onPostExecute(result);
		}
	}
	
	// ボタン
	public void buttonMethodThread2(View button){
		HttpTask task = new HttpTask();
		task.execute("http://www.yahoo.co.jp/");
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
