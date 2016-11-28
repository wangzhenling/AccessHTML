package com.example.accesshtml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private WebView show;
	private TextView result_show;
	private Handler myHandler;
	private String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		show = (WebView) findViewById(R.id.show);
		result_show=(TextView)findViewById(R.id.result);
		show.loadUrl("http://www.baidu.com/");
		
        myHandler=new Handler(){
        	public void handleMessage(Message msg){
        		if(msg.what==0x1122){
        			result_show.setText(result);//使用Textview显示结果
        			show.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
        		}
        	}
        };
		
		new Thread(){
			public void run(){
					try {
						URL httpUrl=new URL("http://www.baidu.com/");
						HttpURLConnection coon=(HttpURLConnection) httpUrl.openConnection();
						coon.setConnectTimeout(5 * 1000);//设置连接超时
						coon.setRequestMethod("GET");//以get方式发起请求 ,GET一定要大写
						if(coon.getResponseCode()!=200)
							throw new RuntimeException("请求url失败");
						InputStream iStream=coon.getInputStream();//得到网络返回的输入流
						result=readData(iStream, "utf-8");
						coon.disconnect();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}.start();
	}

	protected String readData(InputStream inSream, String charsetName) throws Exception{//获取网络资源
		// TODO Auto-generated method stub
		
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		while((len=inSream.read(buffer))!=-1){
			outStream.write(buffer, 0, len);
		}
		byte[] data=outStream.toByteArray();//将字节输出流转为字节数组
		outStream.close();//关闭字节输出流
		inSream.close();//关闭输入流
		return new String(data, charsetName);//返回获取的内容，网页的源代码
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
