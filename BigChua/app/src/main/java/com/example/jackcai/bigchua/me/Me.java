package com.example.jackcai.bigchua.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jackcai.bigchua.MainTabBar;
import com.example.jackcai.bigchua.R;
import com.example.jackcai.bigchua.UserVerify.LoginActivity;

import java.util.Random;

public class Me extends Fragment implements View.OnClickListener {

    private LinearLayout linearLayout ;
    private TextView tvHeaderName;
    private TextView tvUsername;
    private TextView tvUserEmail;
    private ImageView ivUserHead;

    private static int [] IMGs = {R.mipmap.h1,R.mipmap.h2,R.mipmap.h3,R.mipmap.h4,R.mipmap.h5,R.mipmap.h6,R.mipmap.h7,R.mipmap.h8,R.mipmap.h9,R.mipmap.h10,R.mipmap.h11,R.mipmap.h12,R.mipmap.h13,R.mipmap.h14,R.mipmap.h15,R.mipmap.h16,R.mipmap.h17,R.mipmap.h18,R.mipmap.h19,R.mipmap.h20,R.mipmap.h21,R.mipmap.h22};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_me, container, false);

        linearLayout = (LinearLayout)view.findViewById(R.id.me_user_zhuxiao_tv);
        linearLayout.setOnClickListener(this);

        tvHeaderName = (TextView)view.findViewById(R.id.me_user_name_header_tv);
        tvUsername = (TextView)view.findViewById(R.id.me_nike_name_tv);
        tvUserEmail = (TextView)view.findViewById(R.id.me_user_name_tv);
        ivUserHead = (ImageView)view.findViewById(R.id.me_user_header);
        Random random = new Random(System.currentTimeMillis());
        ivUserHead.setImageResource(IMGs[random.nextInt(IMGs.length)]);

        Intent intent = getActivity().getIntent();
        tvHeaderName.setText(intent.getStringExtra("username"));
        tvUsername.setText(intent.getStringExtra("username"));
        tvUserEmail.setText(intent.getStringExtra("useremail"));

        return view;
    }


    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定要注销登录?");
        builder.setCancelable(false);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case -2:
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("remeber_user",getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean("isRemeber",false);
                        editor.commit();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case -1:
                        break;
                }
            }
        };

        builder.setNegativeButton("确定",listener);
        builder.setPositiveButton("取消",listener );
        builder.show();


    }
}
