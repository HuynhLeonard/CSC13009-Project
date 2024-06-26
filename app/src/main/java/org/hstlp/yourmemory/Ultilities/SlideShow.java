package org.hstlp.yourmemory.Ultilities;
import android.content.Intent;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import org.hstlp.yourmemory.R;
import org.hstlp.yourmemory.SelectedViewPagerItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
public class SlideShow extends AppCompatActivity{

    ViewPager2 viewPager2;
    ArrayList<SelectedViewPagerItem> listItem;
    MediaPlayer mediaPlayer;
    Runnable run;
    Handler handler;
    int rawMusic;
    ArrayList<String> listImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        viewPager2=(ViewPager2)findViewById(R.id.main_slider) ;

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            String[] images= intent.getStringArrayExtra("images");
            String nameMusic=intent.getStringExtra("music");

            switch (nameMusic){
                case "default 01":
                    rawMusic=R.raw.musicedm;
                    break;
                case "default 02":
                    rawMusic=R.raw.mono;
                    break;
                case "default 03":
                    rawMusic=R.raw.sontung;
                    break;
            }

//            for(int i=0;i<images.length;i++){
//                Toast.makeText(this, images[i], Toast.LENGTH_SHORT).show();
//            }

            //=====================================

            //get date
            ArrayList<Date> listDate= new ArrayList<Date>();
            for(int i=0;i<images.length;i++){
                File file = new File(images[i]);
                if(file.exists()) //Extra check, Just to validate the given path
                {
                    ExifInterface intf = null;
                    try
                    {
                        intf = new ExifInterface(images[i]);
                        if(intf != null)
                        {
                            String dateString = intf.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
                            Date lastModDate = new Date(file.lastModified());
                            listDate.add(lastModDate);
                            Log.i("PHOTO DATE", "Dated : "+ dateString); //Display dateString. You can do/use it your own way
                        }
                    }
                    catch (IOException e)
                    {
                    }
                    if(intf == null)
                    {
                        Date lastModDate = new Date(file.lastModified());
                        Log.i("PHOTO DATE", "Dated : "+ lastModDate);//Dispaly lastModDate. You can do/use it your own way
                    }
                }
            }

            listItem=new  ArrayList<SelectedViewPagerItem> ();
            for(int i=0;i<images.length;i++){
                SelectedViewPagerItem item = new SelectedViewPagerItem(images[i]);
                listItem.add(item);
            }

            viewPagerAdapterSlider aa=new viewPagerAdapterSlider(listItem,this);
            viewPager2.setAdapter(aa);
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(3);

            handler= new Handler();
            run= new Runnable() {
                @Override
                public void run() {
                    if(viewPager2.getCurrentItem()==listItem.size()-1){
                        viewPager2.setCurrentItem(0);
                    }else{
                        viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
                    }
                }
            };

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    if(position==viewPager2.getScrollBarSize()){
                        viewPager2.setCurrentItem(0);
                    }
                    super.onPageSelected(position);
                    handler.removeCallbacks(run);
                    handler.postDelayed(run,2000);
                }
            });

            viewPager2.getChildAt(0);
            mediaPlayer= MediaPlayer.create(getApplicationContext(),rawMusic);
            mediaPlayer.start();

        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();
    }
}
