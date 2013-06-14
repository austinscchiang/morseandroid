package com.ac.austin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.*;
import android.text.method.ScrollingMovementMethod;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private DrawerLayout mDrawerLayout;
    private TextView mDrawerText;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton soundButton;
    private ViewPager mViewPager;



	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public static String MODE = "ENCODE";
	public static HashMap <Character, String> charToMorse = new HashMap<Character, String>();
	public static HashMap <String, Character> morseToChar = new HashMap<String, Character>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar=getActionBar();


        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        ToggleButton buttonPressed=(ToggleButton)findViewById(R.id.encodeButton);
        soundButton=(ImageButton)findViewById(R.id.soundButton);
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
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        // Add 3 tabs, specifying the tab's text and TabListener
        actionBar.addTab(actionBar.newTab().setText("Text").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Audio").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Flashlight").setTabListener(this));
    }

        // Create a tab listener that is calle
    // Create a tab listener that is called when the user changes tabs.d when the user changes tabs.
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    public AppSectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        // The first section of the app is the most interesting -- it offers
                        // a launchpad into the other demonstrations in this example application.
                        return new LaunchpadSectionFragment();

                    default:
                        // The other sections of the app are dummy placeholders.
                        Fragment fragment = new DummySectionFragment();
                        Bundle args = new Bundle();
                        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                        fragment.setArguments(args);
                        return fragment;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "Section " + (position + 1);
            }
    }
        /**
         * A fragment that launches other parts of the demo application.
         */
    public static class LaunchpadSectionFragment extends Fragment {
            //IMPLEMENT THIS SHIT
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

                // Demonstration of a collection-browsing activity.
                rootView.findViewById(R.id.demo_collection_button)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), com.example.android.effectivenavigation.CollectionDemoActivity.class);
                                startActivity(intent);
                            }
                        });

                // Demonstration of navigating to external activities.
                rootView.findViewById(R.id.demo_external_activity)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Create an intent that asks the user to pick a photo, but using
                                // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                                // the application from the device home screen does not return
                                // to the external activity.
                                Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                                externalActivityIntent.setType("image/*");
                                externalActivityIntent.addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                startActivity(externalActivityIntent);
                            }
                        });

                return rootView;
            }

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
        buttonPressed.setChecked(true);
        buttonNotPressed.setChecked(false);
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
