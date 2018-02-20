package edu.missouri.mca.android.practice2;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import edu.missouri.mca.android.practice2.databinding.MainActivityBinding;
import edu.missouri.mca.android.practice2.model.Model;

public class MainActivity extends Activity {
    private final Model model = new Model();
    private MainActivityBinding binding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setModel(model);
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
