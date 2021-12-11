package com.kushagrakurl.dierolling;

import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class ResultPopUp {

    Integer selectedDie;


    public ResultPopUp( String selectedDie){
        this.selectedDie = Integer.parseInt(selectedDie);
    }

    public void showPopupWindow(final View view) {

    //Create a View object yourself through inflater
    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
    View popupView = inflater.inflate(R.layout.result_popup, null);

    //Specify the length and width through constants
    int width = LinearLayout.LayoutParams.MATCH_PARENT;
    int height = LinearLayout.LayoutParams.MATCH_PARENT;

    //Make Inactive Items Outside Of PopupWindow
    boolean focusable = true;

    //Create a window with our parameters
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

    //Set the location of the window on the screen
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    //Initialize the elements of our window, install the handler

    TextView result = popupView.findViewById(R.id.result);

        for (int i = 1; i <= 15; i++) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int randomVal = (int)(Math.random() * selectedDie) + 1;
                    result.setText(String.valueOf(randomVal));
                    Random rnd = new Random();
                    result.setTextColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                }
            }, 500 * i/10);

            /*int randomVal = (int)(Math.random() * selectedDie) + 1;
            result.setText(String.valueOf(randomVal));*/
        }



    //Handler for clicking on the inactive zone of the window

    popupView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            updateLogs(result.getText().toString());
            //Close the window when clicked
            popupWindow.dismiss();
            return true;
        }
    });
}
    public void updateLogs(String addLog) {
        // Shuffling the data of ArrayList using system time
        ArrayList<String> logArr = MainActivity.logHistory;
        logArr.add(addLog);
        CustomAdapter adapter = new CustomAdapter(logArr);
        //recyclerView.setAdapter(adapter);
    }

}
