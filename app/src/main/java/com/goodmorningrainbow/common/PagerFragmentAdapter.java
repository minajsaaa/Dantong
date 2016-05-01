package com.goodmorningrainbow.common;

import com.goodmorningrainbow.dantongapp.MainActivity;
import com.goodmorningrainbow.fragment.AdviceDetailFragment;
import com.goodmorningrainbow.fragment.AdviceFragment;
import com.goodmorningrainbow.fragment.MainLeftFragment;
import com.goodmorningrainbow.fragment.MainRightFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerFragmentAdapter extends FragmentPagerAdapter {
	
	private int mCount = 4;
	
	public PagerFragmentAdapter(FragmentManager fm, MainActivity activity) {
		super(fm);
		
		MainLeftFragment.getInstance().setActivity(activity);		
		MainRightFragment.getInstance().setActivity(activity);
		AdviceFragment.getInstance().setActivity(activity);
		AdviceDetailFragment.getInstance().setActivity(activity);
	}

	@Override
	public Fragment getItem(int position) {
		
		Fragment fragment = null;
		
		switch (position) {
		case Const.PAGE_LEFT:
			fragment = MainLeftFragment.getInstance();
			break;

		case Const.PAGE_RIGHT:
			fragment = MainRightFragment.getInstance();
			break;
			
		case Const.PAGE_ADVICE:
			fragment = AdviceFragment.getInstance();
			break;
			
		case Const.PAGE_ADVICE_DETAIL:
			fragment = AdviceDetailFragment.getInstance();
			break;						
		}
		
		return fragment;
	}

	@Override
	public int getCount() {
		return mCount;
	}

}
