package ch.heigvd.digiback;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ch.heigvd.digiback.ui.activity.login.LoginActivity;
import ch.heigvd.digiback.ui.data.LoginDataSource;
import ch.heigvd.digiback.ui.data.LoginRepository;
import lombok.Getter;

// TODO comments
public class MainActivity extends AppCompatActivity {
    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
    private static final String TAG = "MainActivity";
    // private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    private AppBarConfiguration mAppBarConfiguration;
    private PopupWindow logoutPopup;

    @Getter
    private Toolbar toolbar;
    //private PopupWindow settingsPopup;

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logoutPopup = new PopupWindow(getLayoutInflater().inflate(R.layout.popup_deconnexion, null, false),100,100, true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View v =  navigationView.getHeaderView(0);
        ImageView imageView = v.findViewById(R.id.nav_image);
        imageView.setImageDrawable(getDrawable(R.drawable.logo_digiback_tiny));
        TextView email = v.findViewById(R.id.nav_email);
        email.setText("");
        TextView username = v.findViewById(R.id.nav_username);
        username.setText(loginRepository.getUsername());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_accueil, R.id.nav_calendar, R.id.nav_conseils, R.id.nav_exercice,
                R.id.nav_quiz, R.id.nav_etat, R.id.nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askPermission() {
        if (ContextCompat.checkSelfPermission(
                this, "ch.heigvd.digiback.permission.USE_CONDITIONS") ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Log.d(TAG, "Permission is granted");
        } else if (shouldShowRequestPermissionRationale("...")) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            Log.d(TAG, "Permission explanation");
        } else if (ContextCompat.checkSelfPermission(
                this, "ch.heigvd.digiback.permission.USE_CONDITIONS") ==
                PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, getString(R.string.permission_denied_explenation), Toast.LENGTH_LONG).show();
            logout();
            requestPermissionLauncher.launch("ch.heigvd.digiback.permission.USE_CONDITIONS");
            Log.d(TAG, "Permission denied");
        } else {
            requestPermissionLauncher.launch("ch.heigvd.digiback.permission.USE_CONDITIONS");
            Log.d(TAG, "Ask for permission");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem logout = menu.findItem(R.id.action_logout);
        logout.setOnMenuItemClickListener(item -> {
            logout();
            return true;
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout() {
        //todo logout current user
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
