package android.example.DressShop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Canvas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CanvasActivity paintView = new CanvasActivity(this);
        setContentView(paintView);
    }
}
