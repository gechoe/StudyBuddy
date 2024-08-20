package com.example.studybuddy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.animation.ValueAnimator;

public class DisappearingRingView extends View {

    private Paint paint;
    private float startAngle;
    private float sweepAngle;
    private ValueAnimator animator;

    // (30 minutes * 60 seconds/minute * 1000 milliseconds/second) = 1800000 milliseconds
    private long totalTimeMillis = 1800000; // 30 minutes in milliseconds
    private long timeLeftInMillis;

    // Starts the disappearing ring timer set up
    public DisappearingRingView(Context context) {
        super(context);
        init();
    }

    // Starts the disappearing ring timer set up
    public DisappearingRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Starts the disappearing ring timer set up
    public DisappearingRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // initialize function
    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.indigo_600));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        startAngle = -90; // Start angle of the arc (top)
        sweepAngle = 360; // Sweep angle of the arc (full circle)

        // Set up animator to decrease the sweep angle gradually
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(totalTimeMillis); // Duration of animation matches the total time
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                sweepAngle = 360 * progress; // Calculate sweep angle based on progress
                invalidate(); // Redraw the view
            }
        });
    }

    // Slowly cuts down the ring based on the time left in the timer
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - (int) (paint.getStrokeWidth() / 2);

        // Calculate the progress based on remaining time
        float progress = (float) timeLeftInMillis / totalTimeMillis;

        // Calculate the sweep angle based on progress
        float angle = 360 * progress;

        // Draw the arc representing the ring
        canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, startAngle, angle, false, paint);
    }

    // Starts the animation of the timer ring being cut down
    public void startAnimation(long timeLeft) {
        timeLeftInMillis = timeLeft;

        if (animator != null && !animator.isRunning()) {
            animator.setDuration(timeLeft); // Set the animator duration to match the remaining time
            animator.start(); // Start the animation if not already running
        }
    }

    // Stops the animation of the timer ring being cut down
    public void stopAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel(); // Cancel the animation if running
        }
    }

    // Resets
    public void makeRingDisappear() {
        sweepAngle = 0; // Set sweep angle to 0 to make the ring disappear
        invalidate(); // Redraw the view
    }
}
