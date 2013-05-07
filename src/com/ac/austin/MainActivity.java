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

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	public String switchChar(String symbol){
		String message=null;
		if(symbol.equals(".-"))message="A";
		else if(symbol.equals("-..."))message="B";
		else if(symbol.equals("-.-."))message="C";
		else if(symbol.equals("-.."))message="D";
		else if(symbol.equals("."))message="E";
		else if(symbol.equals("..-."))message="F";
		else if(symbol.equals("--.-"))message="G";
		else if(symbol.equals("...."))message="H";
		else if(symbol.equals(".."))message="I";
		else if(symbol.equals(".---"))message="J";
		else if(symbol.equals("-.-"))message="K";
		else if(symbol.equals(".-.."))message="L";
		else if(symbol.equals("--"))message="M";
		else if(symbol.equals("-."))message="N";
		else if(symbol.equals("---"))message="O";
		else if(symbol.equals(".--."))message="P";
		else if(symbol.equals("--.-"))message="Q";
		else if(symbol.equals(".-."))message="R";
		else if(symbol.equals("..."))message="S";
		else if(symbol.equals("-"))message="T";
		else if(symbol.equals("..-"))message="U";
		else if(symbol.equals("...-"))message="V";
		else if(symbol.equals(".--"))message="W";
		else if(symbol.equals("-..-"))message="X";
		else if(symbol.equals("-.--"))message="Y";
		else if(symbol.equals("--.."))message="Z";	
		return message;
	}
	public String decodeMorse(String message){
		String[]words=message.split(" ");
		String decoded="";
		for(String word:words){
			decoded+=switchChar(word);
		}
		return decoded;
				
	}
	/*public void openFile(String textFileName){
		Scanner fileReader;
		try{
			fileReader=new Scanner (new File(textFileName));
		}
		catch(Exception e){
			System.out.println("could not open");
		}
	}*/
	public String switchChar(char symbol){
		String line;
		int asciiCount=65;
		try{
			AssetManager am = getAssets();
			InputStream inputStream = am.open("morsesource.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader fileReader = new BufferedReader(inputStreamReader);
			for(asciiCount=65;asciiCount<90;asciiCount++){
				line=fileReader.readLine();
				if(symbol==(char)(asciiCount) || symbol==(char)(asciiCount+32)){
					fileReader.close();
					return line+=" ";
				}
			}
		}
		catch(Exception e){
			System.out.println("could not open");
			return "B";
		}
		return "C";
	}
	public String encodeMorse(String message){
		String output="";
		String[]words=message.split(" ");
		for(String word:words){
			char[] characters=word.toCharArray();
			for (char character:characters){
				output+=switchChar(character);
			}
			output+=" ";
		}
		return output;
	}
	public void sendMessage(View view){
		Intent intent=new Intent (this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, encodeMorse(message));
		System.out.println(message);
		//startActivity(intent);
		EditText output = (EditText)findViewById(R.id.editText1);
		output.setText(encodeMorse(message));
		output.setVisibility(View.VISIBLE);
		//do shit
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
