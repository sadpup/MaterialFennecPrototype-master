package org.mozilla.materialfennec;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hongliang.li.ans.AutoTextAdapter;
import com.hongliang.li.ans.FFApplication;
import com.hongliang.li.ans.FFwebView;

public class MainActivity extends AppCompatActivity {
    private EditText urlView;
    private ImageView menuView;
    private ImageView switchView;
    private CardView cardView;
    private ViewPager pagerView;
    private LinearLayout containerView;
    private ImageView clearView;
    private TabLayout tabsView;
    private FrameLayout framelayoutView;//add
    private FFwebView ffwebView;//add
    private RecyclerView mRecyclerView;//add

    private int containerPadding;
    private String webUrl ;//add
    private Toast mToast;//add

    private AutoTextAdapter mAutoTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        containerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        containerView = (LinearLayout) findViewById(R.id.container);
        cardView = (CardView) findViewById(R.id.card);
        urlView = (EditText) findViewById(R.id.url);
        menuView = (ImageView) findViewById(R.id.menu);
        switchView = (ImageView) findViewById(R.id.switcher);
        pagerView = (ViewPager) findViewById(R.id.pager);
        clearView = (ImageView) findViewById(R.id.clear);
        tabsView = (TabLayout) findViewById(R.id.tabs);
        framelayoutView = (FrameLayout) findViewById(R.id.framelayout);//add
        ffwebView = (FFwebView) findViewById(R.id.ffwebView);//add
        mRecyclerView = (RecyclerView) findViewById(R.id.autotext_list_recyclerview);//add

        clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlView.clearFocus();
            }
        });

        urlView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                switchView.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
                menuView.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
            }
        });
        urlView.setOnEditorActionListener(myOnEditorActionListener);//add
        urlView.addTextChangedListener(mTextWatcher);//add

        LayoutTransition transition = containerView.getLayoutTransition();

        transition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (!urlView.hasFocus()) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(urlView.getWindowToken(), 0);

                    clearView.setVisibility(View.GONE);
                    showWebview();

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                    params.setMargins(containerPadding, containerPadding, containerPadding, containerPadding);
                    cardView.setLayoutParams(params);
                }
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (urlView.hasFocus()) {
                    clearView.setVisibility(View.VISIBLE);
                    showHomeFrame();
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    cardView.setLayoutParams(params);
                }
            }
        });

        pagerView.setAdapter(new HomeAdapter(getSupportFragmentManager()));

        tabsView.setupWithViewPager(pagerView);

        transition.setDuration(100);

        //add
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        webUrl = "http://www.baidu.com";
        ffwebView.loadUrl(webUrl);
        showWebview();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAutoTextAdapter = new AutoTextAdapter() ;//TODO
        mAutoTextAdapter.setData(FFApplication.mAutoTestDatas);
        mRecyclerView.setAdapter(mAutoTextAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));

    }

    @Override
    public void onBackPressed() {

        if (urlView.hasFocus()) {
            urlView.clearFocus();
        } else if (ffwebView!=null && ffwebView.canGoBack()){
            if(ffwebView.getUrl().equals(webUrl)){
                super.onBackPressed();
            }else{
                ffwebView.goBack();
            }
        }else {
            super.onBackPressed();
        }
    }

    public static class HomeAdapter extends FragmentPagerAdapter {
        private static HomePanel[] panels = new HomePanel[] {
            new TopSitesFragment(),
            DummyFragment.create("Bookmarks"),
            DummyFragment.create("History"),
            DummyFragment.create("Reading List"),
            DummyFragment.create("Recent Tabs"),
        };

        public HomeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) panels[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return panels[position].getTitle();
        }

        @Override
        public int getCount() {
            return panels.length;
        }
    }

    ///  addd
    private void showWebview(){
        mRecyclerView.setVisibility(View.GONE);
        if (framelayoutView.getVisibility() == View.VISIBLE)
            framelayoutView.setVisibility(View.GONE);
        if (pagerView.getVisibility() == View.VISIBLE)
            pagerView.setVisibility(View.GONE);
        if (ffwebView.getVisibility() == View.GONE)
            ffwebView.setVisibility(View.VISIBLE);
//        DisplayMetrics dm =getResources().getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.widthPixels;
//        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
//        rp.addRule(RelativeLayout.BELOW, urlView.getId());
//        rp.addRule(RelativeLayout.CENTER_HORIZONTAL);//在父中的位置
//        addContentView(ffwebView,rp);
    }
    private void showHomeFrame(){
        mRecyclerView.setVisibility(View.GONE);
        if (framelayoutView.getVisibility() == View.GONE)
            framelayoutView.setVisibility(View.VISIBLE);
        if (pagerView.getVisibility() == View.GONE)
            pagerView.setVisibility(View.VISIBLE);
        if (ffwebView.getVisibility() == View.VISIBLE)
            ffwebView.setVisibility(View.GONE);
    }

    private void showAutoTextView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        framelayoutView.setVisibility(View.GONE);
        pagerView.setVisibility(View.GONE);
        ffwebView.setVisibility(View.GONE);
    }

    TextView.OnEditorActionListener myOnEditorActionListener  = new TextView.OnEditorActionListener() {//add
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(v!=null){
                if (Patterns.WEB_URL.matcher(v.getText().toString()).matches()) {
                    showWebview();
                    ffwebView.loadUrl(v.getText().toString());
                }else{
                    showTips(getResources().getString(R.string.URL_is_illegal));
                }
            }
            return false;
        }
    };
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            SparseArray<String> datas = new SparseArray<String>();//可用listview代替
            int total = FFApplication.mAutoTestDatas.size();
            for (int i = 0,j=0;i<total;i++){
                String str = FFApplication.mAutoTestDatas.get(i);
                if(str.startsWith(s.toString())) {//TODO 好像s +"" 比较有效率
                    datas.put(j,str);
                    j++;
                }
            }
            mAutoTextAdapter.setData(datas);
            mAutoTextAdapter.notifyDataSetChanged();
            showAutoTextView();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onDestroy() {//add
        if(urlView!=null){
            urlView=null;
        }
        super.onDestroy();
    }

    private void showTips(final String msg){//add
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(msg);
                mToast.show();
            }
        });
    }
}
