package com.hulk.delivery.adapter;

import com.hulk.delivery.ui.view.RangeSeekBar;

/**
 * Created by hulk-out on 2018/4/4.
 */

public class PriceRangeAdapter implements RangeSeekBar.RangeAdapter {
    private static final int RANGE_MIN_VALUE = 0;
    private static final int RANGE_MAX_VALUE = 100;

    private static final int[] RANGE_STEPS = new int[]{
            0, 10, 20, 30, 50, 100
    };

    @Override
    public int getMinRangeValue() {
        return RANGE_MIN_VALUE;
    }

    @Override
    public int getMaxRangeValue() {
        return RANGE_MAX_VALUE;
    }

    @Override
    public boolean hasRangeSteps() {
        return true;
    }

    @Override
    public int getRangeStepsCount() {
        return RANGE_STEPS.length;
    }

    @Override
    public int getRangeStep(int stepIndex) {
        return RANGE_STEPS[stepIndex];
    }
}
