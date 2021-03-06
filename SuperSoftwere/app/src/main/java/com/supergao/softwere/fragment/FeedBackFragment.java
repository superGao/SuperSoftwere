package com.supergao.softwere.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.R;

import com.supergao.softwere.utils.ToastUtil;

/***
 * 意见反馈fragment
 */
public class FeedBackFragment extends ContentFragment implements View.OnClickListener{
    private EditText etFeedbackContent;
    private Button btFeedbackCommit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_feedback, container, false);
        initView(contentView) ;
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(View view) {
        etFeedbackContent = (EditText)view.findViewById(R.id.et_feedback_content);
        btFeedbackCommit = (Button)view.findViewById(R.id.bt_feedback_commit);
        btFeedbackCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_feedback_commit:
                String feedbackText = etFeedbackContent.getText().toString().trim();
                if(!feedbackText.isEmpty()){
                    uploadContent("", "", feedbackText);
                }else{
                    ToastUtil.showShort(getActivity(),"反馈内容不能为空");
                }
                break;

            default:
                break;
        }
    }

    /**
     * 提交反馈数据
     * @param id
     * @param key
     * @param content
     */
    public void uploadContent(String id, String key,String content) {
        showLoadingDialog("正在提交反馈，请稍候.");

    }
}
