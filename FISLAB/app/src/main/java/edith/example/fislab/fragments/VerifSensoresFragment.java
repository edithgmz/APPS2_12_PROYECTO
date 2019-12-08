package edith.example.fislab.fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edith.example.fislab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifSensoresFragment extends Fragment {
    private Context context;
    private Sensor sGameRotVec, sLinAcc, sRotVec, sProxi;

    public VerifSensoresFragment() {
        // Required empty public constructor
    }

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_verif_sensores, container, false);
        CheckBox chBxGameRotVec = linearLayout.findViewById(R.id.chBxGameRotVec);
        CheckBox chBxLinearAccel = linearLayout.findViewById(R.id.chBxLinearAccel);
        CheckBox chBxRotationVec = linearLayout.findViewById(R.id.chBzRotationVec);
        CheckBox chBxProximity = linearLayout.findViewById(R.id.chBxProximity);
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sGameRotVec = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
            sLinAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sRotVec = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sProxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        if (sGameRotVec != null) { chBxGameRotVec.setChecked(true); }
        if (sLinAcc != null) { chBxLinearAccel.setChecked(true); }
        if (sRotVec != null) { chBxRotationVec.setChecked(true); }
        if (sProxi != null) { chBxProximity.setChecked(true); }
        return linearLayout;
    }

}
