package com.ac.austin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public static String MODE = "ENCODE";
	//public final ToggleButton buttonPressed=(ToggleButton)findViewById(R.id.encodeButton);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ToggleButton buttonPressed=(ToggleButton)findViewById(R.id.encodeButton);
		buttonPressed.setChecked(true);
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
			}
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
		String[]paragraphs=message.split("\n");
		for (String paragraph:paragraphs){
			String[]words=paragraph.split(" ");
			for(String word:words){
				char[] characters=word.toCharArray();
				for (char character:characters){
					output+=switchEncodeChar(character);
				}
				output+="/ ";
			}
			output=output.substring(0,output.length()-2);
			output+="\n";
		}
		output=output.substring(0,output.length()-1);
		return output;
	}
	private void toggleButtonPressed(View view){
		ToggleButton buttonPressed=(ToggleButton)view;
		ToggleButton buttonNotPressed=null;
		if (buttonPressed==findViewById(R.id.encodeButton)){
				buttonNotPressed=(ToggleButton)findViewById(R.id.decodeButton);
		}else{
				buttonNotPressed=(ToggleButton)findViewById(R.id.encodeButton);
		}
		boolean temp=buttonPressed.isChecked();
		buttonPressed.setChecked(temp);
		buttonNotPressed.setChecked(!temp);
	}
	public void modeSelect(View view){
		EditText promptMessage=(EditText)findViewById(R.id.edit_message);
		toggleButtonPressed(view);
		if (view==findViewById(R.id.encodeButton)){
			MODE="ENCODE";
			promptMessage.setHint("Enter Text");			
		}else if (view==findViewById(R.id.decodeButton)){
			MODE="DECODE";
			promptMessage.setHint("Enter Morse Code");
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
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		System.out.println(message);
		//startActivity(intent);
		EditText output = (EditText)findViewById(R.id.outputText);
		if(MODE.equals("ENCODE")){
			output.setText(encodeMorse(message));
		}else if (MODE.equals("DECODE")){
			output.setText(decodeMorse(message));
		}
		output.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
