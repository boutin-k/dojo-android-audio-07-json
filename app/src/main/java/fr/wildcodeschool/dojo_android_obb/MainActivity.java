package fr.wildcodeschool.dojo_android_obb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import fr.wildcodeschool.dojo_android_obb.json.JsonParser;
import fr.wildcodeschool.dojo_android_obb.obb.ObbManager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.storage.OnObbStateChangeListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static android.os.storage.OnObbStateChangeListener.*;

public class MainActivity extends AppCompatActivity {
  // TAG
  private static final String TAG = "MainActivity";

  // ObbStateChange values
  private static final SparseArray<String> OBB_STATE = new SparseArray<>();
  static {
    OBB_STATE.put(ERROR_ALREADY_MOUNTED,    "ERROR_ALREADY_MOUNTED");
    OBB_STATE.put(ERROR_COULD_NOT_MOUNT,    "ERROR_COULD_NOT_MOUNT");
    OBB_STATE.put(ERROR_COULD_NOT_UNMOUNT,  "ERROR_COULD_NOT_UNMOUNT");
    OBB_STATE.put(ERROR_INTERNAL,           "ERROR_INTERNAL");
    OBB_STATE.put(ERROR_NOT_MOUNTED,        "ERROR_NOT_MOUNTED");
    OBB_STATE.put(ERROR_PERMISSION_DENIED,  "ERROR_PERMISSION_DENIED");
    OBB_STATE.put(MOUNTED,                  "MOUNTED");
    OBB_STATE.put(UNMOUNTED,                "UNMOUNTED");
  }
  private static final String UNDEFINED_ERROR = "UNDEFINED_ERROR";

  // OBB
  private ObbManager mObbManager;
  private OnObbStateChangeListener mObbListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    TextView lStateTextView = findViewById(R.id.state_text_view);
    TextView lJsonTextView  = findViewById(R.id.json_text_view);
    lJsonTextView.setMovementMethod(new ScrollingMovementMethod());

    // ObbStateChangeListener
    mObbListener = new OnObbStateChangeListener() {
      @Override
      public void onObbStateChange(String path, int state) {
        super.onObbStateChange(path, state);

        lStateTextView.setText(OBB_STATE.get(state, UNDEFINED_ERROR));

        if (MOUNTED == state) {
          // Get the file from OBB mounted path
          File file = new File(mObbManager.getFilePath("data.json"));

          // try with statement works here because FileInputStream
          // implement Closeable interface.
          try (FileInputStream lFileInputStream = new FileInputStream(file)) {
            JsonParser.getInstance().readJsonStream(lFileInputStream);
            Toast.makeText(MainActivity.this, R.string.json_ok, Toast.LENGTH_LONG)
              .show();
          } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(MainActivity.this, R.string.json_ko, Toast.LENGTH_LONG)
              .show();
          }
          // Set the logger color to green
          lStateTextView.setTextColor(getResources().getColor(R.color.colorMounted));
          return;
        }
        if (UNMOUNTED == state) {
          // Set the logger color to orange
          lStateTextView.setTextColor(getResources().getColor(R.color.colorUnMounted));
          return;
        }
        // Set the logger color to red
        lStateTextView.setTextColor(getResources().getColor(R.color.colorError));
      }
    };

    // mount the OBB file
    mObbManager = new ObbManager(this);
    if (RESULT_OK == mObbManager.requestReadObbPermission()) {
      // Permission has been granted
      if (!mObbManager.isObbMounted()) {
        mObbManager.mountMainObb(mObbListener);
      }
    }
  }

  /**
   * Callback for the result from requesting permissions.
   * This method is invoked for every call on requestPermissions
   * @param requestCode int: The request code passed in requestPermissions
   * @param permissions String: The requested permissions. Never null.
   * @param grantResults int: The grant results for the corresponding permissions.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String permissions[],
                                         @NonNull int[] grantResults) {
    if (requestCode == ObbManager.PERMISSIONS_REQUEST_READ_OBB) {
      if (grantResults.length > 0
        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission has been granted
        mObbManager.mountMainObb(mObbListener);
        Log.i(TAG, "OBB_PERMISSION GRANTED");
      } else {
        Log.e(TAG, "OBB_PERMISSION REFUSED");
        finish();
      }
    }
  }

  /**
   * Perform any final cleanup before an activity is destroyed
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();
    // If obbManager always exists and is always mounted
    if (null != mObbManager && mObbManager.isObbMounted()) {
      mObbManager.unmountMainObb(mObbListener);
    }
  }
}
