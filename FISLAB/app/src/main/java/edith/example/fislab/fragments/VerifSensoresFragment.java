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
    //Sensores
    private Sensor sGameRotVec, sLinAcc, sRotVec, sProxi;

    public VerifSensoresFragment() {}

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //Obtener contexto cuando el fragmento se encuentra en la actividad
        this.context = context;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflar layout del fragmento
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_verif_sensores, container, false);
        //Vincular componentes
        CheckBox chBxGameRotVec = linearLayout.findViewById(R.id.chBxGameRotVec);
        CheckBox chBxLinearAccel = linearLayout.findViewById(R.id.chBxLinearAccel);
        CheckBox chBxRotationVec = linearLayout.findViewById(R.id.chBzRotationVec);
        CheckBox chBxProximity = linearLayout.findViewById(R.id.chBxProximity);
        //Obtener servicio del sistema
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //Verificar que el manager no esté vacío
        if (sensorManager != null) {
            sGameRotVec = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
            sLinAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sRotVec = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sProxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
        //Marcar checkbox si el sensor se encuentra disponible en el dispositivo
        if (sGameRotVec != null) { chBxGameRotVec.setChecked(true); }
        if (sLinAcc != null) { chBxLinearAccel.setChecked(true); }
        if (sRotVec != null) { chBxRotationVec.setChecked(true); }
        if (sProxi != null) { chBxProximity.setChecked(true); }
        //Devolver layout
        return linearLayout;
    }
}
