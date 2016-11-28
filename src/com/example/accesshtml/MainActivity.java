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
        			result_show.setText(result);//ʹ��Textview��ʾ���
        			show.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
        		}
        	}
        };
		
		new Thread(){
			public void run(){
					try {
						URL httpUrl=new URL("http://www.baidu.com/");
						HttpURLConnection coon=(HttpURLConnection) httpUrl.openConnection();
						coon.setConnectTimeout(5 * 1000);//�������ӳ�ʱ
						coon.setRequestMethod("GET");//��get��ʽ�������� ,GETһ��Ҫ��д
						if(coon.getResponseCode()!=200)
							throw new RuntimeException("����urlʧ��");
						InputStream iStream=coon.getInputStream();//�õ����緵�ص�������
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

	protected String readData(InputStream inSream, String charsetName) throws Exception{//��ȡ������Դ
		// TODO Auto-generated method stub
		
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=-1;
		while((len=inSream.read(buffer))!=-1){
			outStream.write(buffer, 0, len);
		}
		byte[] data=outStream.toByteArray();//���ֽ������תΪ�ֽ�����
		outStream.close();//�ر��ֽ������
		inSream.close();//�ر�������
		return new String(data, charsetName);//���ػ�ȡ�����ݣ���ҳ��Դ����
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
