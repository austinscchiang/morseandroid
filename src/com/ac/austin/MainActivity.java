package com.ac.austin;

import java.util.*;
import java.io.*;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.content.res.AssetManager;
import android.content.ClipData;
import android.content.ClipboardManager;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public static String MODE = "ENCODE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	private boolean asciiRange(int asciiNum){
		if (asciiNum>47 && asciiNum<60) return true; //number
		else if (asciiNum>64 && asciiNum<91) return true; //alphabet
		else if (asciiNum>96 && asciiNum<123) return true; //alphabet
		switch (asciiNum){
		case 39: return true;
		case 44: return true;
		case 45: return true;
		case 46: return true;
		case 47: return true;
		case 63: return true;
		case 95: return true;
		}
		return false;
		
	}
	private char switchDecodeChar(String symbol){
		String line;
		int asciiCount;
		try{
			AssetManager am = getAssets();
			InputStream inputStream = am.open("morsesource.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader fileReader = new BufferedReader(inputStreamReader);
			for(asciiCount=0;asciiCount<96; asciiCount++){
				//if(asciiCount == 12) return '\n';
				if(asciiRange(asciiCount)){
					line=fileReader.readLine();
					if(symbol.equals(line)){
						fileReader.close();
						return(char)asciiCount;
					}
					
				}else if(symbol.equals("/")||symbol.equals("\n")){
					return ' ';
				}
			}/*
			for(asciiCount=48;asciiCount<48; asciiCount++){
				line=fileReader.readLine();
				if(symbol.equals(line)){
					fileReader.close();
					return(char)asciiCount;
				}
				else if(symbol.equals("/")||symbol.equals("\n")){
					return ' ';
				}
			}
			for(asciiCount=65;asciiCount<91 ;asciiCount++){
				line=fileReader.readLine();
				if(symbol.equals(line)){
					fileReader.close();
					return(char)asciiCount;
				}
				else if(symbol.equals("/")||symbol.equals("\n")){
					return ' ';
				}
			}*/
		}
		catch(Exception e){
			System.out.println("could not open");
			return '<';
		}
		return '?';
	}
	private String decodeMorse(String message){
		String[]words=message.split(" ");
		String decoded="";
		for(String word:words){
			decoded+=switchDecodeChar(word);
		}
		return decoded;
				
	}
	private String switchEncodeChar(char symbol){
		String line;
		int asciiCount=65;
		try{
			AssetManager am = getAssets();
			InputStream inputStream = am.open("morsesource.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader fileReader = new BufferedReader(inputStreamReader);
			for(asciiCount=0;asciiCount<123; asciiCount++){
//				if(asciiCount == 12) return "\n";
				if(asciiRange(asciiCount)){
					line=fileReader.readLine();
					if(symbol==(char)(asciiCount)){
						fileReader.close();
						return line+=" ";
					}
					
				}
			}
		}
		catch(Exception e){
			System.out.println("could not open");
			return "B";
		}
		return "C";
	}
	private String encodeMorse(String message){
		String output="";
		String[]words=message.split(" ");
		for(String word:words){
			char[] characters=word.toCharArray();
			for (char character:characters){
				output+=switchEncodeChar(character);
			}
			output+="/ ";
		}
		return output;
	}
	
	public void modeSelect(View view){
		EditText promptMessage=(EditText)findViewById(R.id.edit_message);
		if (view==findViewById(R.id.encodeButton)){
			MODE="ENCODE";
			promptMessage.setHint("Enter Morse Code");			
		}else if (view==findViewById(R.id.decodeButton)){
			MODE="DECODE";
			promptMessage.setHint("Enter Text");
		}
		promptMessage.setText(null);
	}
	public void copyMessage(View view){
		EditText copyString= (EditText)findViewById(R.id.outputText);
		ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("Copied", copyString.getText());
		clipBoard.setPrimaryClip(clip);
	}
	
	public void sendMessage(View view){
//		Intent intent=new Intent (this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		System.out.println(message);
		//startActivity(intent);
		EditText output = (EditText)findViewById(R.id.outputText);
		if(MODE.equals("ENCODE")){
			output.setText(decodeMorse(message));
		}else if (MODE.equals("DECODE")){
			output.setText(encodeMorse(message));
		}
		output.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
