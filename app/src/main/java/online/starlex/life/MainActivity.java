package online.starlex.life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();

    Runnable getQuote = new Runnable() {
        @Override
        public void run() {
            String quote = "null";
            String path = "https://v1.hitokoto.cn/?c=d&encode=text&chatset=utf-8";
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    StringBuilder stringBuffer = new StringBuilder();
                    int c;
                    while ((c = isr.read()) != -1) {
                        stringBuffer.append((char) c);
                    }
                    quote = stringBuffer.toString();
                    isr.close();
                    is.close();
                    Log.i("格言", quote);
                } else {
                    //TODO:日志记录
                    Toast.makeText(getApplicationContext(), "一言拉取失败", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                //TODO:异常处理+日志记录
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "一言拉取失败", Toast.LENGTH_SHORT).show();
            }

            final String finalQuote = quote;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TextView quoteView = (TextView) findViewById(R.id.quote);
                    quoteView.setText(finalQuote);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button refreshQuote = (Button) findViewById(R.id.refresh_quote);
        refreshQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(getQuote).start();
            }
        });

        new Thread(getQuote).start();
    }


}