package com.ac.austin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import android.app.*;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.pm.PackageManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.AutoFocusCallback;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnCompletionListener{
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private DrawerLayout mDrawerLayout;
    private TextView mDrawerText;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    public String message_delay;
    Thread audioThread=null;
    Thread audioSuperThread=null;
    Thread lightThread=null;
    Thread lightSuperThread=null;
    boolean stopThread;
    boolean aliveThread;
    ImageView audioOnIcon;
    ImageView lightOnIcon;
//    ImageView audioOffIcon;
    MediaPlayer beep_long;
    MediaPlayer beep_short;
    MediaPlayer beep_delay;
    Camera camera=null;
    LaunchpadSectionFragment launchFrag;
    AudioSectionFragment audioFrag;
    LightSectionFragment lightFrag;

    public static String MODE = "ENCODE";
    public static HashMap <Character, String> charToMorse = new HashMap<Character, String>();
    public static HashMap <String, Character> morseToChar = new HashMap<String, Character>();

    @Override
    public void onDestroy() {
        stopThread=true;
        Log.e("Light ","Destroy"+stopThread);
        super.onDestroy();
    }
    @Override
    public void onResume() {
        stopThread=false;
        Log.e("Light ","Resume"+stopThread);
        super.onResume();
    }

    @Override
    public void onStop() {
        stopThread=true;
        Log.e("Light ","Stop"+stopThread);
        super.onStop();
    }

    @Override
    public void onPause() {
        stopThread=true;
        Log.e("Light ","Pause"+stopThread);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopThread=false;
        aliveThread=false;
//        Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        launchFrag=new LaunchpadSectionFragment();
        audioFrag=new AudioSectionFragment();
        lightFrag=new LightSectionFragment();


         //snippet for mediaplayer
        beep_long = MediaPlayer.create(MainActivity.this, R.raw.long_beep);
        beep_short = MediaPlayer.create(MainActivity.this, R.raw.short_beep);
        beep_delay = MediaPlayer.create(MainActivity.this, R.raw.delay);



                Context context = this;
        PackageManager pm = context.getPackageManager();

        // if device support camera?
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            Toast errorToast = Toast.makeText(getApplicationContext(), "Cannot Convert to Light!", Toast.LENGTH_SHORT);
            errorToast.show();
            return;
        }



//        audioOnIcon=(ImageView)audioFrag.getView().findViewById(R.id.sound_on_icon);
//        audioOffIcon=(ImageView)audioFrag.getView().findViewById(R.id.sound_off_icon);



        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar=getActionBar();


        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        // Specify that tabs should be displayed in the action bar.

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


//        DrawerStuffs
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

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
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

    @Override
    public void onCompletion(MediaPlayer mp){
    }

    //more drawer stuff
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerText);
//        return super.onPrepareOptionsMenu(menu);
//    }

    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return launchFrag;
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return audioFrag;

                default:
                    return lightFrag;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


    }

    /**morse code functions**/
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
    public void toggleButtonPressed(View view){
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


    public void soundImageOn(){
        audioOnIcon.setVisibility(View.VISIBLE);
    }
    public void soundImageOff(){
        audioOnIcon.setVisibility(View.INVISIBLE);
    }


    public int playAudioMessage(final String message){
        final int[] delay_counter = new int[1];
            Runnable r=new Runnable(){
                int delay=0;
                @Override
                public void run(){
                    if (!stopThread){
                        try{
                            delay_counter[0]=0;
//                            audioFrag.setImageView();
//                            soundImageOn();
                            Log.e("SoundOn",""+audioOnIcon.getDrawable());
//                            Log.e("SoundOff", "" + audioOffIcon.getDrawable());
                            int count=0;
                            char[] characters=message.toCharArray();
                                for (char character:characters){
                                    count++;
                                if(character=='.'){
                                    delay=beep_short.getDuration()+250;
                                    beep_short.start();

                                }else if(character=='-'){
                                            delay=beep_long.getDuration()+250;
                                            beep_long.start();

                                }else if(character=='/'){
                                    delay=beep_delay.getDuration()+250;
                                    beep_delay.start();
                                }else{
                                    Toast errorToast = Toast.makeText(getApplicationContext(), ""+character, Toast.LENGTH_SHORT);
                                    errorToast.show();
                                }
                                    audioOnIcon.getHandler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            audioOnIcon.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    audioOnIcon.getHandler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            audioOnIcon.setVisibility(View.INVISIBLE);
                                        }
                                    }, delay-50);
                                    Thread.sleep(delay);

                                delay_counter[0]+=delay-250;
                                Log.e("delay"+message+count, ""+delay_counter[0]);
//                                soundImageOff();
                                }
                        }catch(Exception e){
                            Toast errorToast = Toast.makeText(getApplicationContext(), "goofed", Toast.LENGTH_SHORT);
                            errorToast.show();

                        }
                    }
                }
            };
            audioThread =new Thread(r);
            audioThread.start();
            try{
                audioThread.join();
            }catch(Exception e){
                Toast errorToast = Toast.makeText(getApplicationContext(), "can't join Thread", Toast.LENGTH_SHORT);
                errorToast.show();

            }
            return delay_counter[0];
    }
//

    public void sendAudioMessage(View view){
        audioOnIcon.setVisibility(View.VISIBLE);
        setHash(morseToChar, charToMorse);
        EditText editText = (EditText) findViewById(R.id.edit_message_audio);
        message_delay = editText.getText().toString();
        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        final String encoded=encodeMorse(message_delay, charToMorse);
            Runnable r=new Runnable(){
                int delay=0;
                @Override
                public void run(){
                    aliveThread=true;
                    try{
                        String[]words=encoded.split(" ");
                        for(String word:words){
                            if(!stopThread){
                            delay=playAudioMessage(word);
                            Thread.sleep(delay);
                            }
                        }
                    }catch(Exception e){
                        Toast errorToast = Toast.makeText(getApplicationContext(), "wut", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                    aliveThread=false;

                }

            };
            audioSuperThread=new Thread(r);
            if(!stopThread && !aliveThread){
                audioSuperThread.start();

            }


    }
    //flash functions

    public void flashON(){
        if(camera==null){
        camera = Camera.open();
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
        }

    }

    public void flashOFF(){
        if(camera!=null){
        camera.stopPreview();
        camera.release();
        camera=null;
        }
    }

    public int stringToLight(final String message){
        try{
            final int[] delay_counter = new int[1];
            Runnable r=new Runnable(){
                SeekBar frequencyBar=(SeekBar)findViewById(R.id.seekbar_light);
                int frequencyFraction=frequencyBar.getProgress()+1;
                int delay=0;
                @Override
                public void run(){
                    if(!stopThread){
                        Log.e("Light","Inside"+stopThread);
                        try{
                            delay_counter[0]=0;
                            int count=0;
                            char[] characters=message.toCharArray();
                            for (char character:characters){
                                count++;
                                if(character=='.'){
                                    flashON();
                                    delay=200;

                                }else if(character=='-'){
                                    flashON();
                                    delay=900;

                                }else if(character=='/'){
                                    flashOFF();
                                    delay=900;
                                }else{
                                    Toast errorToast = Toast.makeText(getApplicationContext(), ""+character, Toast.LENGTH_SHORT);
                                    errorToast.show();
                                }
                                delay=delay*frequencyFraction/10;
                                Thread.sleep(delay);
                                delay_counter[0]+=delay+400*frequencyFraction/10;
                                flashOFF();
                                Thread.sleep(400*frequencyFraction/10);
                                Log.e("delay"+message+count, ""+delay_counter[0]);
                            }
                        }catch(Exception e){
                            Toast errorToast = Toast.makeText(getApplicationContext(), "goofed", Toast.LENGTH_SHORT);
                            errorToast.show();
                        }
                    }
                }
            };
            lightThread =new Thread(r);
            lightThread.start();
            lightThread.join();
            return delay_counter[0];

        }catch(Exception e){
            Toast errorToast = Toast.makeText(getApplicationContext(), "Cannot Convert to Light!", Toast.LENGTH_SHORT);
            errorToast.show();
            flashOFF();
            return 0;
        }
    }

    public void sendLightMessage(View view){
        setHash(morseToChar, charToMorse);
        EditText editText = (EditText) findViewById(R.id.edit_message_light);
        String message = editText.getText().toString();
        System.out.println(message);
        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);

        final String encoded=encodeMorse(message, charToMorse);
            Runnable r =new Runnable(){
                int delay=0;
                @Override
                public void run(){
                    aliveThread=true;
                    try{
                        String[]words=encoded.split(" ");
                        for(String word:words){
                            if(!stopThread){
                                Log.e("Light",""+stopThread);
                            delay=stringToLight(word);
                            Thread.sleep(delay);
                            }
                        }
                    }catch(Exception e){
                        Toast errorToast = Toast.makeText(getApplicationContext(), "wut", Toast.LENGTH_SHORT);
                        errorToast.show();
                    }
                    aliveThread=false;
                }

            };
        lightSuperThread=new Thread(r);
        if(!stopThread &&!aliveThread){
            lightSuperThread.start();
        }
    }
    public class AudioSectionFragment extends Fragment {
        View rootAudioView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootAudioView = inflater.inflate(R.layout.fragment_section_audio, container, false);
            audioOnIcon=((ImageView)rootAudioView.findViewById(android.R.id.icon));
            audioOnIcon.setVisibility(View.INVISIBLE);
            return rootAudioView;
        }
    }
    public class LightSectionFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootLightView = inflater.inflate(R.layout.fragment_section_light, container, false);
//            lightOnIcon=((ImageView)rootLightView.findViewById(android.R.id.icon));
//            lightOnIcon.setVisibility(View.INVISIBLE);
            return rootLightView;
        }

    }
}
