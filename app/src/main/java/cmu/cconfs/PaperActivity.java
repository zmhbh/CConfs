package cmu.cconfs;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class PaperActivity extends AppCompatActivity {

    private String paperTitle;
    private String paperAbstract;
    private String paperAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_paper);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle("Paper");

        paperTitle = getIntent().getStringExtra("paperTitle");
        paperAbstract = getIntent().getStringExtra("paperAbstract");
        paperAuthor = getIntent().getStringExtra("paperAuthor");

        TextView textView_paperTitle = (TextView) findViewById(R.id.paperTitle);
        TextView textView_paperAbstract = (TextView) findViewById(R.id.abstractContent);
        TextView textView_paperAuthor = (TextView) findViewById(R.id.authorContent);

        textView_paperTitle.setText(paperTitle);
        textView_paperAbstract.setText(paperAbstract);
        textView_paperAuthor.setText(paperAuthor);
    }


}
