package com.accentsoftware.habitual;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

public class Feedback extends AppCompatActivity {

    private EditText message;
    private MultiStateToggleButton subject;
    private final String SUBJECT [] = {"Habitual:Bug","Habitual:Request", "Habitual:Comment"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        message = (EditText) findViewById(R.id.message);
        subject = (MultiStateToggleButton) findViewById(R.id.feedbackSubject);
        subject.setValue(0); //Default on bug subject
    }

    public void sendFeedbackClick(View view){
        if (message.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter a message to send.", Toast.LENGTH_SHORT).show();
        }
        else {
            int val = subject.getValue();
            new SendMail().execute( SUBJECT[val], message.getText().toString() );
        }
    }

    private class SendMail extends AsyncTask<String, Void, Integer>
    {
        ProgressDialog pd = null;
        String error = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(Feedback.this);
            pd.setTitle("Sending Mail");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub
            GMailSender sender = new GMailSender("habitual.application@gmail.com", "HabitualApplication");

            sender.setTo(new String[]{"zarif.shahriar@gmail.com"});
            sender.setFrom("habitual.application@gmail.com");
            sender.setSubject(params[0]);
            sender.setBody(params[1]);
            Log.i("doInBackground", params[0]);
            try {
                if(sender.send()) {
                    System.out.println("Message sent");
                    return 1;
                } else {
                    return 2;
                }
            } catch (Exception e) {
                error = e.getMessage();
                Log.e("SendMail", e.getMessage(), e);
            }

            return 3;
        }

        protected void onPostExecute(Integer result) {
            pd.dismiss();
            if(result==1) {
                Toast.makeText(Feedback.this,
                        "Feedback was sent successfully.", Toast.LENGTH_LONG)
                        .show();
            } else if(result==2) {
                Toast.makeText(Feedback.this,
                        "Feedback was not sent. Please try again later.", Toast.LENGTH_LONG).show();
            } else if(result==3) {
                Toast.makeText(Feedback.this,
                        "There was a problem sending the feedback, please try again later.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
