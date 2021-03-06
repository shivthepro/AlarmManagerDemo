package com.shangeeth.mobiclock.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shangeeth.mobiclock.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    private TextView mHoursTv;
    private TextView mMinutesTv;
    private TextView mSecondsTv;
    private LinearLayout mStartLayout;
    private LinearLayout mPauseOrStopLayout;

    private Timer mTimer;

    private long mTotalTimerTimeInMs;

    private TextView mSelectedItem = null;

    private static final String TAG = "TimerFragment";
    private Handler mHandler;

    private boolean isTimerRunning = false;
    private Button lPauseOrResumeButton;
    private String mTempValue;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View lView = inflater.inflate(R.layout.fragment_timer, container, false);

        mStartLayout = (LinearLayout) lView.findViewById(R.id.controller_layout);
        mPauseOrStopLayout = (LinearLayout) lView.findViewById(R.id.pause_stop_layout);

        mHoursTv = (TextView) lView.findViewById(R.id.hours_tv);
        mMinutesTv = (TextView) lView.findViewById(R.id.minutes_tv);
        mSecondsTv = (TextView) lView.findViewById(R.id.seconds_tv);

        mSelectedItem = mSecondsTv;
        selectTV(mSelectedItem);

        setOnClickListeners(lView);

        return lView;
    }


    private void setOnClickListeners(View pView) {

        mHoursTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = mHoursTv;
                selectTV(mSelectedItem);
            }
        });

        mMinutesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = mMinutesTv;
                selectTV(mSelectedItem);
            }
        });

        mSecondsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = mSecondsTv;
                selectTV(mSelectedItem);
            }
        });

        ((Button) pView.findViewById(R.id.one_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("1");
            }
        });

        ((Button) pView.findViewById(R.id.two_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("2");
            }
        });

        ((Button) pView.findViewById(R.id.three_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("3");
            }
        });
        ((Button) pView.findViewById(R.id.four_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("4");
            }
        });
        ((Button) pView.findViewById(R.id.five_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("5");
            }
        });
        ((Button) pView.findViewById(R.id.six_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("6");
            }
        });
        ((Button) pView.findViewById(R.id.seven_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("7");
            }
        });
        ((Button) pView.findViewById(R.id.eight_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("8");
            }
        });
        ((Button) pView.findViewById(R.id.nine_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("9");
            }
        });
        ((Button) pView.findViewById(R.id.zero_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed("0");
            }
        });
        ((ImageButton) pView.findViewById(R.id.delete_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem.setText("00");
            }
        });

        ((Button) pView.findViewById(R.id.start_timer_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick:  Start Timer");
                if (!mHoursTv.getText().toString().equals("00") || !mMinutesTv.getText().toString().equals("00") || !mSecondsTv.getText().toString().equals("00")) {
                    mStartLayout.setVisibility(View.GONE);
                    mPauseOrStopLayout.setVisibility(View.VISIBLE);

                    setDefaultColor();
                    calculateTotalMs();

                    //Start Timer
                    startTimer();
                }
            }
        });

        lPauseOrResumeButton = (Button) pView.findViewById(R.id.pause_btn);
        lPauseOrResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    mTimer.cancel();
                    mTimer.cancel();
                    isTimerRunning = false;
                    lPauseOrResumeButton.setText("Resume");
                    lPauseOrResumeButton.setBackgroundResource(R.color.green);
                } else {
                    lPauseOrResumeButton.setText("Pause");
                    lPauseOrResumeButton.setBackgroundResource(R.color.red);
                    startTimer();
                }
            }
        });

        ((Button) pView.findViewById(R.id.reset_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

    }

    /**
     * Handling the button Pressed action
     *
     * @param pNumber the number pressed
     */
    private void buttonPressed(String pNumber) {
//        if (Integer.valueOf(mSelectedItem.getText().toString()) == 59 ) {
//            mSelectedItem.setText("0" + pNumber);
//        } else {
        mTempValue = mSelectedItem.getText().toString().substring(0, 1);
        mSelectedItem.setText(mSelectedItem.getText().toString().substring(1) + pNumber);
        checkForRange();
//        }
    }

    /**
     * Resets the Timer to initial state
     */
    private void resetTimer() {

        mTimer.cancel();

        lPauseOrResumeButton.setText("Pause");
        lPauseOrResumeButton.setBackgroundResource(R.color.red);

        isTimerRunning = false;
        setDefaultValues();

        mSelectedItem = mSecondsTv;
        selectTV(mSelectedItem);

        mPauseOrStopLayout.setVisibility(View.GONE);
        mStartLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Starts the timer
     */
    private void startTimer() {
        mHandler = new Handler();
        Log.e(TAG, "startTimer: ");
        mTimer = new Timer();

        isTimerRunning = true;

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (mTotalTimerTimeInMs - 1000 == 0) {
                            resetTimer();
                            startActivity(new Intent(getActivity(), AlarmDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(getString(R.string.is_timer), true));
                        }
                        mTotalTimerTimeInMs = mTotalTimerTimeInMs - 1000;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUi();
                            }
                        });
                    }
                });
            }
        }, 1000, 1000);


    }


    /**
     * Sets the default value 00
     */
    private void setDefaultValues() {
        mHoursTv.setText("00");
        mMinutesTv.setText("00");
        mSecondsTv.setText("00");
    }

    /**
     * Calculates the Total time in ms for the selected time in the timer.
     */
    private void calculateTotalMs() {
        Log.e(TAG, "calculateTotalMs: ");

        mTotalTimerTimeInMs = ((Integer.valueOf(mHoursTv.getText().toString()) * 60 +
                Integer.valueOf(mMinutesTv.getText().toString())) * 60 +
                Integer.valueOf(mSecondsTv.getText().toString())) * 1000;

        Log.e(TAG, "calculateTotalMs: " + mTotalTimerTimeInMs);
    }

    /**
     * Updates the Ui
     */
    private void updateUi() {
        mHoursTv.setText(String.format("%02d", TimeUnit.MILLISECONDS.toHours(mTotalTimerTimeInMs)));
        mMinutesTv.setText(String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(mTotalTimerTimeInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mTotalTimerTimeInMs))));
        mSecondsTv.setText(String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(mTotalTimerTimeInMs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTotalTimerTimeInMs))));
    }

    /**
     * Sets the color to default and changes the selected colour.
     *
     * @param pSelectedTV
     */
    private void selectTV(TextView pSelectedTV) {
        setDefaultColor();
        pSelectedTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    /**
     * Once the timer starts the default color is set
     */
    public void setDefaultColor() {

        mHoursTv.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_grey));
        mMinutesTv.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_grey));
        mSecondsTv.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_grey));

    }

    /**
     * Checking the range
     */
    private void checkForRange() {
        if (Integer.valueOf(mMinutesTv.getText().toString()) > 59) {
            mMinutesTv.setText(mTempValue + mMinutesTv.getText().toString().substring(0, 1));
            Toast.makeText(getActivity(), "The value cant be more than 59", Toast.LENGTH_SHORT).show();
        }
        if (Integer.valueOf(mSecondsTv.getText().toString()) > 59) {
            mSecondsTv.setText(mTempValue + mSecondsTv.getText().toString().substring(0, 1));
            Toast.makeText(getActivity(), "The value cant be more than 59", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
    }
}
