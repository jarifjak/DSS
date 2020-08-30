package com.jarifjak.digitalsecuritysolution.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.jarifjak.digitalsecuritysolution.R;
import com.jarifjak.digitalsecuritysolution.model.DialogExtra;
import com.jarifjak.digitalsecuritysolution.utility.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomDialog extends DialogFragment {

    @BindView(R.id.alertTitleTV)
    AppCompatTextView alertTitleTV;
    @BindView(R.id.errorDescriptionTV)
    AppCompatTextView errorDescriptionTV;
    @BindView(R.id.circleIV)
    CircleImageView circleIV;
    @BindView(R.id.oneButtonLayout)
    LinearLayout oneButtonLayout;
    @BindView(R.id.twoButtonLayout)
    LinearLayout twoButtonLayout;
    @BindView(R.id.confirmButton)
    AppCompatButton confirmButton;
    @BindView(R.id.cancelButton)
    AppCompatButton cancelButton;

    private int alertFlag;
    private int dialogType;
    private String jsonObject;
    private DialogListener listener;

    public static CustomDialog newInstance(int dialogType, int flag, String jsonObject) {

        CustomDialog dialog = new CustomDialog();

        Bundle args = new Bundle();

        args.putInt("flag", flag);
        args.putInt("dialogType", dialogType);
        args.putString("jsonObject", jsonObject);

        dialog.setArguments(args);

        return dialog;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_custom, container, false);
        ButterKnife.bind(this, view);

        alertFlag = Objects.requireNonNull(getArguments()).getInt("flag");
        dialogType = getArguments().getInt("dialogType");
        jsonObject = getArguments().getString("jsonObject", null);


        initialize();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initialize() {

        DialogExtra dialogExtra = new Gson().fromJson(jsonObject, DialogExtra.class);

        alertTitleTV.setText(dialogExtra.getTitle());
        errorDescriptionTV.setText(dialogExtra.getDescription());

        if (dialogType == Constants.DIALOG_ONE_BUTTON) {

            twoButtonLayout.setVisibility(View.GONE);
            circleIV.setImageResource(alertFlag == 1 ? R.drawable.ic_success : R.drawable.ic_error);

        } else if (dialogType == Constants.DIALOG_TWO_BUTTON) {


            twoButtonLayout.setVisibility(View.VISIBLE);
            oneButtonLayout.setVisibility(View.GONE);

            if (alertFlag == 3) {

                //For logout

                confirmButton.setText("Logout");
                circleIV.setImageResource(R.drawable.ic_gear);

            } else if (alertFlag == 4) {

                //For delete

                circleIV.setImageResource(R.drawable.ic_gear);
                confirmButton.setText("Delete");
            }

        }
    }

    @Override
    public void onResume() {

        super.onResume();

        Window window = Objects.requireNonNull(getDialog()).getWindow();
        Display display = Objects.requireNonNull(window).getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        this.listener = (DialogListener) context;
    }


    @OnClick({R.id.okayButton, R.id.cancelButton, R.id.confirmButton})
    public void onViewClicked(View view) {

        int id = view.getId();

        if (id == R.id.okayButton) {

            dismiss();

        } else if (id == R.id.cancelButton) {


            dismiss();

        } else if (id == R.id.confirmButton) {

            listener.onConfirmClick(alertFlag);
            dismiss();
        }
    }

    public interface DialogListener {

        void onConfirmClick(int flag);
    }
}