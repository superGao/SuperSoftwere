package com.supergao.software.entity;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.view.ViewGroup;

import com.supergao.software.R;

/**
 *
 *@author superGao
 *creat at 2016/3/14
 */
public class TranslatePageTransformer implements PageTransformer {

	/**
	 * 当我们的ViewPager滑动的时候，每一个页面都会回调该方法
	 * position:当前第几个页面
	 * view:某个页面对应的视图 --- 布局的视图
	 */
	@Override
	public void transformPage(View view, float position) {
		// 渐变效果，判断区间（-1，1）
		if (position<1 && position >-1) {
			// 视差加速效果，让里面的所有子空间都给一个加速偏移量
			ViewGroup rl=(ViewGroup) view.findViewById(R.id.rl);
//			for (int i = 0; i < rl.getChildCount(); i++) {
//				View child=rl.getChildAt(i);
//				float factoe=(float) (Math.random()*2);
//				if (child.getTag() == null) {
//					child.setTag(factoe);
//				}else{
//					factoe=(Float) child.getTag();
//				}
//				// 加速偏移量(在child原来的位置再加一个偏移值)
//				child.setTranslationX(-position*200*factoe);
//				child.setTranslationY(position*100*factoe);
//			}
			// 缩放效果
			// 缩放的范围：0-1
			rl.setScaleX(Math.max(0.8f, 1 - Math.abs(position)));
			rl.setScaleY(Math.max(0.8f, 1 - Math.abs(position)));
			
			// 3D翻转动画 往外翻转
//			rl.setPivotX(position<0f?rl.getWidth():0f);
//			rl.setPivotY(rl.getHeight()*0.5f);
//			rl.setRotationY(position*90);
			
			// 3D翻转动画 往内翻转
//			rl.setPivotX(position<0f?rl.getWidth():0f);
//			rl.setPivotY(rl.getHeight()*0.5f);
//			rl.setRotationY(-position*90);
			
			// 羊肉串效果
			rl.setPivotX(rl.getHeight()*0.5f);
			rl.setPivotY(rl.getHeight()*0.5f);
			rl.setRotationY(-position*90);
		}
	}

}
