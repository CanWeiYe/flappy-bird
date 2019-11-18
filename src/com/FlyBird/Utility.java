package com.FlyBird;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
/**
 * 随机函数的实现
 * 其他函数的实现
 * @author yeweican
 *{@code}
 */
public class Utility {
	public static final int EASY =10;
	public static final int HARD =20; 
	private static Random rand = new Random();
	/**
	 * @return 随机背景图片
	 */
	public static Image getRandomImage() {		// 随机背景
		if(rand.nextInt(2)>0)
			return new ImageIcon("images/bg_day.png").getImage();
		else
			return new ImageIcon("images/bg_night.png").getImage();
	}
	/**
	 * 随机管子
	 * 绿色 5/6  棕色 1/6
	 * @return 随机管子图片
	 */
	public static ColumnImage getRandomColumnImage() {
			if(rand.nextInt(6)<5)
				return new ColumnImage(new ImageIcon("images/pipe_down.png")
				,new ImageIcon("images/pipe_up.png")	);
			else
				return new ColumnImage(new ImageIcon("images/pipe2_down.png")
						,new ImageIcon("images/pipe2_up.png")	);
	}
	/**
	 * 上方管子y坐标
	 * 范围-200~-100
	 */
	public static int getRandomY() {			
		return rand.nextInt(101)-200;
	}
	 /**
	  * 生成上下管子间隔
	  * 范围100~150
	  */
	public static int getRandomYspace(int level) {	
		if(level == EASY)
		return rand.nextInt(51)+150;
		else return rand.nextInt(51)+100;
	}
	/**
	 * 生成前后两个管子间隔
	 * 范围150~200
	 */
	public static int getRandomXspace(int level) {	
		if(level == EASY)
		return rand.nextInt(51)+200;
		else return rand.nextInt(51)+150;
	}
	/**
	 * 
	 * @param choice ：1--黄 2--蓝 3--红
	 * @return 随机小鸟图片
	 */
	public static ImageIcon getBirdImage(int choice) {
		if(choice==1)return new ImageIcon("images/bird_fly.gif");
		else if(choice==2)return new ImageIcon("images/bird_fly2.gif");
		else return new ImageIcon("images/bird_fly3.gif");
	}
	/**
	 * 包括上下两根管子
	 * @author yeweican
	 *
	 */
	static class ColumnImage{		
		public ImageIcon down;
		public ImageIcon up;
		public ColumnImage(ImageIcon down,ImageIcon up) {
			this.down = down;
			this.up = up;
		}
	}
	
	/**
	 * 
	 * @param score ：本局所得分数
	 * @param ts ：存储所有分数的集合
	 * @return 奖牌图片
	 */
	public static ImageIcon getMedal(int score,Set<Integer> ts) {
		int rank = 1;
		for(Integer i:ts) {
			if(i == score)break;
			rank++;
		}
		rank = ts.size()-rank+1;
		if(rank==1)return new ImageIcon("images/medals_1.png");
		else if(rank==2)return new ImageIcon("images/medals_2.png");
		else if(rank==3)return new ImageIcon("images/medals_3.png");
		else return new ImageIcon("images/medals_0.png");
	}
	/**
	 * 
	 * @param score :分数
	 * @return 按序存储数字图片的List
	 */
	public static List<ImageIcon> getScoreImage(int score) {
		String s = String.valueOf(score);
		List<ImageIcon> list = new ArrayList<ImageIcon>();
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)=='0')list.add(new ImageIcon("images/font_048.png"));
			else if(s.charAt(i)=='1')list.add(new ImageIcon("images/font_049.png"));
			else if(s.charAt(i)=='2')list.add(new ImageIcon("images/font_050.png"));
			else if(s.charAt(i)=='3')list.add(new ImageIcon("images/font_051.png"));
			else if(s.charAt(i)=='4')list.add(new ImageIcon("images/font_052.png"));
			else if(s.charAt(i)=='5')list.add(new ImageIcon("images/font_053.png"));
			else if(s.charAt(i)=='6')list.add(new ImageIcon("images/font_054.png"));
			else if(s.charAt(i)=='7')list.add(new ImageIcon("images/font_055.png"));
			else if(s.charAt(i)=='8')list.add(new ImageIcon("images/font_056.png"));
			else list.add(new ImageIcon("images/font_057.png"));
		}
		return list;
	}
}
