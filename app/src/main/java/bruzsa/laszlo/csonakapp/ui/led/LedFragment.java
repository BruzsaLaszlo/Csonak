package bruzsa.laszlo.csonakapp.ui.led;

import static bruzsa.laszlo.csonakapp.ui.SharedViewModel.LED_COUNT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import bruzsa.laszlo.csonakapp.databinding.FragmentLedBinding;
import bruzsa.laszlo.csonakapp.ui.SharedViewModel;

public class LedFragment extends Fragment {

    private List<SeekBar> seekBars;
    private List<SwitchMaterial> switches;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLedBinding binding = FragmentLedBinding.inflate(getLayoutInflater());
        seekBars = List.of(
                binding.seekBar0,
                binding.seekBar1,
                binding.seekBar2,
                binding.seekBar3,
                binding.seekBar4,
                binding.seekBar5);
        switches = List.of(
                binding.switchLed0,
                binding.switchLed1,
                binding.switchLed2,
                binding.switchLed3,
                binding.switchLed4,
                binding.switchLed5);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.isConnected().observe(getViewLifecycleOwner(), aBoolean -> onResume());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < LED_COUNT; i++) {
            int fi = i;
            seekBars.get(i).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        sharedViewModel.setLED(fi, progress);
                    else
                        switches.get(fi).setChecked(progress != 0);
                    switches.get(fi).setText(String.format(Locale.getDefault(), "%d", progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // not used
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // not used
                }
            });

            switches.get(i).setOnCheckedChangeListener((buttonView, isChecked) -> {
                seekBars.get(fi).setEnabled(isChecked);
                sharedViewModel.setSwitchIsChecked(fi, isChecked);
                if (isChecked)
                    sharedViewModel.setLED(fi, seekBars.get(fi).getProgress());
                else
                    sharedViewModel.setLED(fi, 0);
            });
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("LedFragment", "onPause()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("LedFragment", "onResume()");
        boolean isCon = sharedViewModel.isConnected().getValue();
        for (int i = 0; i < LED_COUNT; i++) {
            if (isCon) seekBars.get(i).setProgress(sharedViewModel.getLed(i));
            switches.get(i).setChecked(sharedViewModel.getSwitchIsChecked(i));
            seekBars.get(i).setEnabled(isCon && switches.get(i).isChecked());
            switches.get(i).setEnabled(isCon);
        }
    }
}