package com.example.jackcai.bigchua.me;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jackcai.bigchua.MainTabBar;
import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.UserVerify.LoginActivity;

public class Me extends Fragment implements View.OnClickListener {

    private LinearLayout linearLayout ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_me, container, false);

        linearLayout = (LinearLayout)view.findViewById(R.id.me_user_zhuxiao_tv);
        linearLayout.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
