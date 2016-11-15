package com.example.tcpdemo02;


import java.util.ArrayList;
import java.util.List;

import com.example.tcpdemo02.ClientSocket.OnSocketRecieveCallBack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/*
 * TCP协议长链服务器发送并接收数据
 */

public class MainActivity extends Activity implements OnClickListener, OnSocketRecieveCallBack {

	public static final String SERVER_NAME = "192.168.0.114";
	public static final int PORT = 6600;
	
	Button btn_send;
	EditText ed_send;
	ListView mListView;
	ArrayAdapter<String> arryAdapter = null;
	List<String> mDatas = null;
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mDatas.add((String)msg.obj);
			arryAdapter.notifyDataSetChanged();
		}

	};
	ClientSocket mClientSocket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDatas = new ArrayList<String>();
		mDatas.add("Hello world");
		arryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas);

		ed_send = (EditText)this.findViewById(R.id.et_send);
		btn_send = (Button)this.findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		mListView = (ListView)this.findViewById(R.id.list);
		mListView.setAdapter(arryAdapter);

		mClientSocket = new ClientSocket(SERVER_NAME, PORT);
		mClientSocket.setOnSocketRecieveCallBack(this);
		mClientSocket.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String msg = ed_send.getText().toString();

		if(msg != null && mClientSocket.isSocketConnected()){
			mClientSocket.addSendMsgToQueue(msg);
			mDatas.add(msg);
			arryAdapter.notifyDataSetChanged();
			ed_send.setText("");
		}
	}

	@Override
	public void OnRecieveFromServerMsg(String msg) {
		// TODO Auto-generated method stub
		if(msg != null){
			Message message = Message.obtain();
			message.obj = msg;
			mHandler.sendMessage(message);
		}

	}
}

