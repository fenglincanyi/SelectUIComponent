package com.gjr.selectuicomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SelectMultiCheckGroup checkGroup;
    private SelectMultiCheckGroup checkGroup1;
    private TextView rg_result;
    private TextView rg1_result;

    private List<String> list = Arrays.asList("不限", "当天", "<3天", "<1周", "<2周", "<1个月", "<2个月");
    private List<String> list1 = Arrays.asList("不限", "当天", "<3天", "<1周", "<2周", "<1个月", "<2个月", "<3个月", "<5个月", "<6个月", "<10个月");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_result = (TextView) findViewById(R.id.rg_result);
        rg1_result = (TextView) findViewById(R.id.rg1_result);

        checkGroup = (SelectMultiCheckGroup) findViewById(R.id.checkGroup);
        checkGroup.initData(list);// 初始化完了，才设置的监听，所以第一次设置默认第一个，不显示数据，同下
        checkGroup.setOnItemSelectedListener(new SelectMultiCheckGroup.OnItemSelectedListener() {
            @Override
            public void checked(View view, int position, boolean isChecked) {
                Toast.makeText(MainActivity.this, list.get(position)+" == "+ position +" ==  "+isChecked,Toast.LENGTH_SHORT).show();
                rg_result.setText("选择结果："+checkGroup.getSelectedAll().toString());
            }
        });

        checkGroup1 = (SelectMultiCheckGroup) findViewById(R.id.checkGroup1);
        checkGroup1.initData(list1);
        checkGroup1.setOnItemSelectedListener(new SelectMultiCheckGroup.OnItemSelectedListener() {
            @Override
            public void checked(View view, int position, boolean isChecked) {
                Toast.makeText(MainActivity.this, list1.get(position)+"  == "+ position +" ==  "+isChecked,Toast.LENGTH_SHORT).show();
                rg1_result.setText("选择结果："+checkGroup1.getSelectedOne());
            }
        });

    }
}
