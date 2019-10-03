package in.davise.multiplefilechooser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "FileChooser";
    private static final int FILE_PICKER_REQUEST = 1;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(chooseFile, "Choose File(s)") , FILE_PICKER_REQUEST);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == FILE_PICKER_REQUEST) {
                if (resultCode == Activity.RESULT_OK) {
                    JSONArray filePaths = new JSONArray();
                    if (data.getClipData() != null) {
                        int itemCount = data.getClipData().getItemCount();
                        for (int i = 0; i < itemCount; i++) {
                            JSONObject file = new JSONObject();
                            file.put("path", data.getClipData().getItemAt(i).getUri().toString());
                            filePaths.put(file);
                        }
                    } else if (data.getData() != null) {
                        JSONObject file = new JSONObject();
                        file.put("path", data.getData().toString());
                        filePaths.put(file);
                    }

                    Log.e(TAG, filePaths.toString());
                    text.setText(filePaths.toString());
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e(TAG, "Cancelled");
                }
            }
        } catch (JSONException je) {

        }
    }
}
