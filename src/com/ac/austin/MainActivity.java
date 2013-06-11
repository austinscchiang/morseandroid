package com.ac.austin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuInflater;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private TextView mDrawerText;
    private ActionBarDrawerToggle mDrawerToggle;
    
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public static String MODE = "ENCODE";
	public static HashMap <Character, String> charToMorse = new HashMap<Character, String>();
	public static HashMap <String, Character> morseToChar = new HashMap<String, Character>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ToggleButton buttonPressed=(ToggleButton)findViewById(R.id.encodeButton);
		buttonPressed.setChecked(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerText = (TextView) findViewById(R.id.left_drawer);
        mDrawerText.setText(R.string.drawer_content);
        mDrawerText.setTextColor(Color.WHITE);
        mDrawerText.setTextSize(20);
        mDrawerText.setMovementMethod(new ScrollingMovementMethod());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.mcticon, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle("Morse Code Translator");
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Morse Code Conversion Sheet");
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerText);
        return super.onPrepareOptionsMenu(menu);
    }
	
	
	private boolean asciiRange(int asciiNum){
		if (asciiNum>47 && asciiNum<60) return true; //number
		else if (asciiNum>64 && asciiNum<91) return true; //alphabet
		else if (asciiNum>96 && asciiNum<123) return true; //alphabet
		switch (asciiNum){
            case 33: return true;
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
	
	
	
	private void setHash(HashMap mToC,HashMap cToM){
		int asciiCount;
		try{
			String line;
			AssetManager am = getAssets();
			InputStream inputStream = am.open("morsesource.txt");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader fileReader = new BufferedReader(inputStreamReader);
			for(asciiCount=0;asciiCount<130; asciiCount++){
				if(asciiRange(asciiCount)){
					line=fileReader.readLine();
					cToM.put((char)asciiCount, line);
					mToC.put(line, (char)asciiCount);
				}
			}
			cToM.put('\n', "//");
			mToC.put("//", '\n');
			cToM.put(' ', "//");
			mToC.put("/", ' ');
			
		}
		catch(Exception e){
			System.out.println("could not open");
		}
	}
	
	private char switchDecodeChar(String symbol, HashMap<String, Character> hm){
		try{
			return Character.toUpperCase(hm.get(symbol));
		}
		catch(Exception e){
            Toast errorToast = Toast.makeText(getApplicationContext(), "Cannot Convert!", Toast.LENGTH_SHORT);
            errorToast.show();
            return '*';
		}
	}
	
	private String decodeMorse(String message, HashMap morseToChar){
		String[]words=message.split(" ");
		String decoded="";
		for(String word:words){
            char token=switchDecodeChar(word, morseToChar);
            if(token!='*'){
                decoded+=token;
            }
            else return "";
		}
		return decoded;				
	}
	
	
	
	
	
	private String switchEncodeChar(char symbol, HashMap<Character, String> hm){
        String single;
		try{
			single= hm.get(symbol);
            return single;
		}
		catch(Exception e){
            return "Error";
		}

	}

	private String encodeMorse(String message, HashMap charToMorse){
        if(message.equals("")){
            Toast errorToast = Toast.makeText(getApplicationContext(), "Cannot Convert!", Toast.LENGTH_SHORT);
            errorToast.show();
            return " ";
        }
        else{
            String output="";
            try{
                String[]paragraphs=message.split("\n");
                for (String paragraph:paragraphs){
                    String[]words=paragraph.split(" ");
                    for(String word:words){
                        char[] characters=word.toCharArray();
                        for (char character:characters){
                            String token=switchEncodeChar(character, charToMorse);
                            if(!token.equals("Error")){
                                output+=token;
                                output+=" ";
                            } else{
                                Toast errorToast = Toast.makeText(getApplicationContext(), "Cannot Convert!", Toast.LENGTH_SHORT);
                                errorToast.show();
                                return "  ";
                            }
                        }
                        output+="/ ";
                    }
                    output=output.substring(0,output.length()-2);
                    output+="// ";
                }
                output=output.substring(0,output.length()-4);
                return output;
            }
            catch(Exception e){
                Toast errorToast = Toast.makeText(getApplicationContext(), "Cannot Convert!", 1);
                errorToast.show();
                return "  ";
            }
        }
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
		setHash(morseToChar, charToMorse);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		System.out.println(message);
        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
		//startActivity(intent);
		EditText output = (EditText)findViewById(R.id.outputText);
		if(MODE.equals("ENCODE")){
			output.setText(encodeMorse(message, charToMorse));
		}else if (MODE.equals("DECODE")){
			output.setText(decodeMorse(message, morseToChar));
		}
		output.setVisibility(View.VISIBLE);
	}

}
