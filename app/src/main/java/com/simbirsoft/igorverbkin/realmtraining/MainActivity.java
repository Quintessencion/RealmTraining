package com.simbirsoft.igorverbkin.realmtraining;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edit_query) EditText editText;
    @BindView(R.id.add_person) Button addPerson;
    @BindView(R.id.refresh) Button refresh;
    @BindView(R.id.add_person_dog) Button addDog;
    @BindView(R.id.grid) GridView gv;

    private Realm realm;
    private List<Event> persons = new ArrayList<>();

    private View view;
    private TextView progressSeek;
    private SeekBar lightControl;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                openAndroidPermissionsMenu();
            }
        }

        ButterKnife.bind(this);
        view = getLayoutInflater().inflate(R.layout.dialog, null);
        progressSeek = view.findViewById(R.id.progress_seek);
        lightControl = view.findViewById(R.id.light_control);
        lightControl.setOnSeekBarChangeListener(listener);
        lightControl.setMax(getScreenBrightness());
        alert = buildDialog();

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        if (realm.where(Event.class).count() == 0) {
            fillRealmEvents();
        }

        refreshLayout();

        gv.setOnItemClickListener((parent, view, position, id) -> {
            alert.show();
        });
    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private AlertDialog.Builder buildDialog() {
        return new AlertDialog.Builder(this)
                .setTitle("Realm save:")
                .setView(view)
                .setPositiveButton("OK", (dialog1, which) -> {

                })
                .setNegativeButton("Cancel", null);
    }

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setScreenBrightness((int) (progress * 2.55));
            progressSeek.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    protected int getScreenBrightness() {
        return Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    public void setScreenBrightness(int brightness) {
        if (brightness >= 0 && brightness <= 255) {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    private void fillRealmEvents() {
        final JSONArray jsonArrayEvents = getJsonArray();
        realm.executeTransaction(realm -> realm.createAllFromJson(Event.class, jsonArrayEvents));
    }

    private JSONArray getJsonArray() {
        try (InputStream is = getAssets().open("events.json")) {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);

            return new JSONObject(new String(buffer)).getJSONArray("events");
        } catch (IOException | JSONException e) {
            Log.d("task", "Error reading json file: " + e.getMessage());
        }
        return null;
    }

    public void refresh(View view) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.delete(Event.class);
            }
        });
        refreshLayout();
    }

    public void addPerson(View view) {
        realm.executeTransaction(realm -> {
//            try {
//                long id = (long) realm.where(Person.class).max("id");
//                Person person = realm.createObject(Person.class, ++id);
//                person.setName("Name " + id);
//                person.setAge((int) (Math.random() * 41 + 10));
//                realm.insert(person);
//            } catch (NullPointerException e) {
//                Person person = realm.createObject(Person.class, 1);
//                person.setName("Name 1");
//            }
        });
        refreshLayout();
    }

    public void addDog(View view) {
        try {
            final long id = Long.parseLong(editText.getText().toString());
            realm.executeTransaction(realm -> {
//                Person person = realm.where(Person.class).equalTo("id", id).findFirst();
//                if (person != null) {
//                    person.getDogs().add(new Dog());
//                } else {
//                    Toast.makeText(MainActivity.this, "Персоны с id = '" + editText.getText().toString() + "' не существует", Toast.LENGTH_SHORT).show();
//                }
            });
            refreshLayout();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Некорректный  id '" + editText.getText().toString() + "'", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshLayout() {
        realm.where(Event.class).findAll().asFlowable()
                .subscribe(events -> {
                    persons.clear();
                    persons.addAll(events);
                    updateAdapter();
                });
    }

    private void updateAdapter() {
        gv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, persons));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
