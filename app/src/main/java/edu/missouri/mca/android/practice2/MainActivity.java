package edu.missouri.mca.android.practice2;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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
        binding.mainRepos.setOnItemClickListener((parent, view, position, id) -> {
            final String htmlUrl = model.getRepos().get(position).getHtmlUrl();
            if (htmlUrl != null)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl)));
        });
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
