package com.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.Adapter.HistoryAdapter;
import com.uidemo.R;
import com.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.iv_fanhui_search)
    ImageView ivFanhuiSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_history)
    RecyclerView rlHistory;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    private String keywords;
    private List<String> keywordList;
    private HistoryAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void findView() {
        ButterKnife.bind(this);
        rlHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initView() {


    }

    @Override
    public void initListener() {
        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtils.clearDate(getApplicationContext(),"keywords");
                keywordList.clear();
                tvHistory.setVisibility(View.GONE);
                rlHistory.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initData() {
        keywordList = new ArrayList<String>();
        keywords = PrefUtils.getString(getApplicationContext(), "keywords", "");
        Log.i("zw1103", keywords);
        if (keywords!=""){
            String[] keywordShuZu = keywords.split(" ");
            for (int i = 0; i < keywordShuZu.length; i++) {
                keywordList.add(keywordShuZu[i]);
            }
        }
        if (keywordList.size()==0){
            tvHistory.setVisibility(View.GONE);
        }
        Log.i("zw1103",keywordList.size()+" MMMMMMMMMMM");
        adapter = new HistoryAdapter(keywordList, getApplicationContext());
        rlHistory.setAdapter(adapter);
        adapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String keyword = keywordList.get(keywordList.size()-position-1);
                Log.i("zw1103", keyword + " SSSSSSSSSSSS");
          /*      for (int i = 0; i < keywordList.size(); i++) {
                    if (i < keywordList.size() - 1) {
                        keywordList.set(i, keywordList.get(i + 1));
                        keywordList.set(keywordList.size() - 1, keyword);
                    }
                }
                String keywords1 = "";
                for (int i = 0; i < keywordList.size(); i++) {
                    if (i != 0) {
                        keywords1 = keywords1 + " " + keywordList.get(i);
                    } else {
                        keywords1 = keywordList.get(0);
                    }
                }*/
                //PrefUtils.putString(getApplicationContext(), "keywords", keywords1);

                Intent intent = getIntent();
                intent.putExtra("keyword", keyword);
                setResult(6, intent);
                SearchActivity.this.finish();
            }
        });


        etSearch.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("多行监听", actionId + "\t" + KeyEvent.KEYCODE_ENTER);
                String keyword = etSearch.getText().toString().trim();
               /* for (int i = 0; i < keywordList.size(); i++) {
                    if (keywordList.get(i).equals(keyword)) {
                        if (i < keywordList.size() - 1) {
                            keywordList.set(i, keywordList.get(i + 1));
                            keywordList.set(keywordList.size() - 1, keyword);
                        }
                    } else {
                        keywordList.add(keyword);
                    }
                }*/
                /*String keywords1 = "";
                if (keywordList.size() == 0) {
                    keywords1 = keyword;
                } else {
                    for (int i = 0; i < keywordList.size(); i++) {
                        if (i == 0) {
                            keywords1 = keywordList.get(0);
                        } else {
                            keywords1 = keywords1 + " " + keywordList.get(i);
                        }
                    }
                }*/
                if (keywordList.size()==0){
                    if (!"".equals(keyword)){
                        keywords = keyword;
                    }
                }else {
                    if (!keywordList.contains(keyword)&&!"".equals(keyword)){
                        keywords = keywords + " " + keyword;
                    }
                }
                PrefUtils.putString(getApplicationContext(), "keywords", keywords);
                Log.i("zw1103", keywords + " SSSSSSSSSSSS");
                Intent intent = getIntent();
                intent.putExtra("keyword", keyword);
                setResult(6, intent);
                SearchActivity.this.finish();
                return false;
            }
        });
    }

    @OnClick(R.id.iv_fanhui_search)
    public void onViewClicked() {
        finish();
    }


}
