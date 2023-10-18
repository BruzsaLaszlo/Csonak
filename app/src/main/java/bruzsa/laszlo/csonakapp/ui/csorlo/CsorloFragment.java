package bruzsa.laszlo.csonakapp.ui.csorlo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import bruzsa.laszlo.csonakapp.databinding.FragmentCsorloBinding;
import bruzsa.laszlo.csonakapp.ui.SharedViewModel;

public class CsorloFragment extends Fragment {

    SharedViewModel viewModel;
    FragmentCsorloBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        binding = FragmentCsorloBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonElore.setOnClickListener(b -> viewModel.setCsorlo(1));
        binding.buttonHatra.setOnClickListener(b -> viewModel.setCsorlo(2));
        binding.buttonStop.setOnClickListener(b -> viewModel.setCsorlo(0));

        viewModel.getCsorlo().observe(getViewLifecycleOwner(), csorlo -> {
            if (csorlo == 0) {
                binding.buttonStop.setEnabled(false);
                binding.buttonElore.setEnabled(true);
                binding.buttonHatra.setEnabled(true);
            } else if (csorlo == 1) {
                binding.buttonHatra.setEnabled(true);
                binding.buttonElore.setEnabled(false);
                binding.buttonStop.setEnabled(true);
            } else if (csorlo == 2) {
                binding.buttonHatra.setEnabled(false);
                binding.buttonElore.setEnabled(true);
                binding.buttonStop.setEnabled(true);
            }
        });
        viewModel.getLog().observe(getViewLifecycleOwner(), binding.logTextView::setText);

        binding.lockSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (!isChecked) viewModel.setCsorlo(0);
            binding.buttonElore.setEnabled(isChecked);
            binding.buttonHatra.setEnabled(isChecked);
            binding.buttonStop.setEnabled(false);
        }));
        binding.lockSwitch.setChecked(true);

        viewModel.isConnected().observe(getViewLifecycleOwner(), isConnected ->
                binding.progressBar.setVisibility(Boolean.TRUE.equals(isConnected) ? View.INVISIBLE : View.VISIBLE)
        );

    }
}