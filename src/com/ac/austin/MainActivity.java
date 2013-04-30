package com.ac.austin;

import java.util.*;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	public void switchChar(String symbol, String message){
		HashMap hm =new HashMap();
		hm.put(".-", "A");
		hm.put("-...", "B");
		hm.put("-.-.", "C");
		hm.put("-..", "D");
		hm.put(".", "E");
		hm.put("..-.", "F");
		hm.put("--.", "G");
		hm.put("....", "H");
		hm.put("..", "I");
		hm.put(".---", "J");
		hm.put("-.-", "K");
		hm.put(".-..", "L");
		hm.put("--", "M");
		hm.put("-.", "N");
		hm.put("---", "O");
		hm.put(".--.", "P");
		hm.put("--.-", "Q");
		hm.put(".-.", "R");
		hm.put("...", "S");
		hm.put("-", "T");
		hm.put("..-", "U");
		hm.put("...-", "V");
		hm.put(".--", "W");
		hm.put("-..-", "X");
		hm.put("-.--", "Y");
		hm.put("--..", "Z");
		Iterator iter = hm.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry mEntry = (Map.Entry) iter.next();
			if(mEntry.getKey()==symbol){
				message+=mEntry.getValue();
				//System.out.println(mEntry.getValue());
			}
		}
		
	}
	public String decodeMorse(String message){
		//System.out.println("Hello");
		String[]words=message.split(" ");
		String decoded=null;
		for(String word:words){
			switchChar(word,decoded);
		}
		return decoded;
				
	}
	public void sendMessage(View view){
		Intent intent=new Intent (this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, decodeMorse(message));
		startActivity(intent);
		//do shit
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
