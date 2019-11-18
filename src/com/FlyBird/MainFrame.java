package com.FlyBird;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.FlyBird.Utility.ColumnImage;

/**
   *本渣渣第一个游戏项目</BR>
 *<strong>HOME():主界面  
 *</BR>
 *START():游戏开始
 *</BR>
 *</strong>
   *每单击一次小鸟向上跳，<strong>每一tick(40ms)检测小鸟isJumping,isCrashed,isRemoved属性并作出不同动作
 *</strong>
 * @see <a href="https://www.nowcoder.com/project/index/65">项目页面
 * </a>
 * @author yeweican
 *
 */
public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int tick = 30; // 游戏时刻(ms)
	private static int VectorSize = 0; // 管子数量
	private boolean isColumnRemoved = false;	// 管子是否被移除
	private List<Column> vec = new Vector<Column>(6); // 管子容器
	private JLabel title; // 主界面flappybird标题
	private JLabel playbutton; // 开始按钮
	private JLabel land; // 地面
	private JLabel bird; // 主界面鸟
	private JLabel currentScore_label;		// 本局分数
	private JLabel topScore_label;			// 最高分
	private JLabel medal_label;				// 奖牌
	private JLabel score_label;				// 得分面板
	private JLayeredPane layeredPane = this.getLayeredPane();// 中间容器
	private Image BackgroundIm; // 背景的图片
	private ScheduledExecutorService service ;// 计时器
	private Bird b;		// 鸟
	private int score;  // 分数
	private Set<Integer> ts = new TreeSet<Integer>();// 存放每局分数的集合
	/**
	 * 管子图片 pipe1绿色 pipe2棕色
	 */
	private ImageIcon pipe1_up = new ImageIcon("images/pipe_up.png");
	private ImageIcon pipe1_down = new ImageIcon("images/pipe_down.png");

	public MainFrame() {
		this.setIconImage(new ImageIcon("images/bird0_0.png").getImage());// 设置图标
		layeredPane.setBounds(0, 0, 1000, 700);
		HOME();
		setTitle("FlyBird");
		this.setLayout(null);
		setSize(1000, 700);
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(450, 200); // 窗口属性

	}

	public void HOME() { // 主界面
		
		BackgroundIm = Utility.getRandomImage();
		BackgroundPanel bgl = new BackgroundPanel(); // 背景
		layeredPane.add(bgl, JLayeredPane.DEFAULT_LAYER);
		ImageIcon titleImage = new ImageIcon("images/title.png");

		title = new JLabel(titleImage);
		title.setOpaque(false); // 标题
		title.setBounds(405, 150, titleImage.getIconWidth(), titleImage.getIconHeight());
		layeredPane.add(title, JLayeredPane.DRAG_LAYER);

		ImageIcon playImage = new ImageIcon("images/button_play.png");
		playbutton = new JLabel(playImage); // 开始按钮
		playbutton.setOpaque(false);
		playbutton.setBounds(435, 300, playImage.getIconWidth(), playImage.getIconHeight());
		playbutton.addMouseListener(new MouseAdapter() { // 按钮事件
			@Override
			public void mouseEntered(MouseEvent e) {
				playbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				playbutton.setCursor(Cursor.getDefaultCursor());
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				playbutton.setCursor(Cursor.getDefaultCursor());
				layeredPane.removeAll();
				layeredPane.repaint();
				service.shutdown();
				if(!service.isShutdown())
				service.shutdownNow();
				service = null;
				START();	 // 游戏开始
			}
		});
		layeredPane.add(playbutton, JLayeredPane.DRAG_LAYER);

		ImageIcon landIm = new ImageIcon("images/land.png"); // 地面
		land = new JLabel(landIm);
		land.setOpaque(false);
		land.setBounds(0, 560, landIm.getIconWidth(), landIm.getIconHeight());
		layeredPane.add(land, JLayeredPane.PALETTE_LAYER);
		land = new JLabel(landIm);
		land.setOpaque(false);
		land.setBounds(landIm.getIconWidth(), 560, landIm.getIconWidth(), landIm.getIconHeight());
		layeredPane.add(land, JLayeredPane.PALETTE_LAYER);
		land = new JLabel(landIm);
		land.setOpaque(false);
		land.setBounds(landIm.getIconWidth() * 2, 560, landIm.getIconWidth(), landIm.getIconHeight());
		layeredPane.add(land, JLayeredPane.PALETTE_LAYER);

		ImageIcon mybird = new ImageIcon("images/mybird_1.png"); // 小鸟
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(180, 290, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);
		mybird = new ImageIcon("images/mybird_2.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(270, 230, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);
		mybird = new ImageIcon("images/bird0_1.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(390, 200, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);

		mybird = new ImageIcon("images/mybird_3.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(500, 240, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);
		mybird = new ImageIcon("images/mybird_4.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(590, 360, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);
		mybird = new ImageIcon("images/mybird_5.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(700, 315, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);
		mybird = new ImageIcon("images/bird0_2.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(830, 315, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);
		mybird = new ImageIcon("images/bird0_2.png");
		bird = new JLabel(mybird);
		bird.setOpaque(false);
		bird.setBounds(950, 315, mybird.getIconWidth(), mybird.getIconHeight());
		layeredPane.add(bird, JLayeredPane.DRAG_LAYER);

		/**
		 * 下方管子
		 */
		JLabel pipe_up = new JLabel(pipe1_up);
		pipe_up.setOpaque(false);
		pipe_up.setBounds(700, 400, pipe1_up.getIconWidth(), pipe1_up.getIconHeight());
		layeredPane.add(pipe_up, JLayeredPane.PALETTE_LAYER);

		/**
		 * 上方管子
		 */
		JLabel pipe_down = new JLabel(pipe1_down);
		pipe_down = new JLabel(pipe1_down);
		pipe_down.setOpaque(false);
		pipe_down.setBounds(700, -60, pipe1_down.getIconWidth(), pipe1_down.getIconHeight());
		layeredPane.add(pipe_down, JLayeredPane.PALETTE_LAYER);

		ImageIcon mypipe = new ImageIcon("images/mypipe_up.png");
		JLabel pipe = new JLabel(mypipe);
		pipe.setOpaque(false);
		pipe.setBounds(-220, 200, mypipe.getIconHeight(), mypipe.getIconHeight());
		layeredPane.add(pipe, JLayeredPane.PALETTE_LAYER);

	//	ImageIcon demo = new ImageIcon("images/播放器.png"); // 播放器面板
	//	JLabel Demo = new JLabel(demo);
	//	Demo.setOpaque(false);
	//	Demo.setBounds(-22, -145, demo.getIconHeight(), demo.getIconHeight());
	//	layeredPane.add(Demo, JLayeredPane.PALETTE_LAYER);

	//	ImageIcon musicgif = new ImageIcon(); // 碟片
	//	JLabel MusicGif = new JLabel(musicgif);
	//	MusicGif.setOpaque(false);
	//	MusicGif.setBounds(-182, -186, musicgif.getIconWidth(), musicgif.getIconHeight());
	//	layeredPane.add(MusicGif, JLayeredPane.MODAL_LAYER);
		
		service = Executors.newSingleThreadScheduledExecutor();
	//	homeTimerTask tt = new homeTimerTask(pipe_down, pipe_up, new StringBuffer(), MusicGif);
	//	service.scheduleAtFixedRate(tt, 0, 100,TimeUnit.MILLISECONDS);
	}

	public void START()  { // 游戏开始
		score = 0;
	
		service = Executors.newScheduledThreadPool(3);
		BackgroundPanel bgl = new BackgroundPanel();
		layeredPane.add(bgl, JLayeredPane.DEFAULT_LAYER);// 背景
		/**
		 * 鼠标点击
		 * 鸟跳
		 */
		//TODO 鼠标点击
		bgl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					if(b.isCrashed) {
						bgl.removeMouseListener(this);
						return;
					}
					b.isJumping = true;
					b.yspeed = b.initJumpSpeed;
			}
		});

		ImageIcon landIm = new ImageIcon("images/land.png"); // 地面
		land = new JLabel(landIm);
		land.setOpaque(false);
		land.setBounds(0, 560, landIm.getIconWidth(), landIm.getIconHeight());
		layeredPane.add(land, JLayeredPane.POPUP_LAYER);
		land = new JLabel(landIm);
		land.setOpaque(false);
		land.setBounds(landIm.getIconWidth(), 560, landIm.getIconWidth(), landIm.getIconHeight());
		layeredPane.add(land, JLayeredPane.POPUP_LAYER);
		land = new JLabel(landIm);
		land.setOpaque(false);
		land.setBounds(landIm.getIconWidth() * 2, 560, landIm.getIconWidth(), landIm.getIconHeight());
		layeredPane.add(land, JLayeredPane.POPUP_LAYER);
		
		/**
		 * 
		 * @author 叶玮灿
		 *
		 *         游戏正式开始
		 */
		isColumnRemoved = false;
		b = new Bird(Utility.getBirdImage(1));
		
		ImageIcon ready = new ImageIcon("images/text_ready.png");
		ImageIcon tutorial = new ImageIcon("images/tutorial.png");
		JLabel ready_label = new JLabel(ready);
		JLabel tutorial_label = new JLabel(tutorial);
		ready_label.setOpaque(false);
		tutorial_label.setOpaque(false);
		ready_label.setBounds(420, 200, ready.getIconWidth(), ready.getIconHeight());
		tutorial_label.setBounds(467, 400, tutorial.getIconWidth(), tutorial.getIconHeight());
		layeredPane.add(ready_label,JLayeredPane.PALETTE_LAYER);
		layeredPane.add(tutorial_label,JLayeredPane.PALETTE_LAYER);
		/**
		 * 提示
		 */
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				ready_label.setVisible(false);
				tutorial_label.setVisible(false);
				layeredPane.remove(ready_label);
				layeredPane.remove(tutorial_label);
			}
		}, 1000);

		BirdTask bt =new BirdTask(b);
		service.scheduleWithFixedDelay(bt, 0, tick,TimeUnit.MILLISECONDS);
		ColumnTask ct = new ColumnTask();
		service.scheduleWithFixedDelay(ct, 0, tick,TimeUnit.MILLISECONDS);

	}
	
	class BirdTask implements Runnable{	// 每一游戏时刻鸟任务
		private Bird b;
		public BirdTask(Bird b) {
			this.b = b;
		}
		@Override
		public void run() {			
			if(!b.isCrashed) {
					b.move();
			}
			else {					   // 鸟已撞击管子
				for(Column c:vec) {				// 屏幕内已经过的管子
					if(c.x+c.Pipe_down.getIconWidth()>=b.x+b.currentImage.getIconWidth())
						break;
					score++;
				}
				if(!b.isRemoved) {				// 鸟未移出屏幕
					b.fall();
					if(!isColumnRemoved) {
						if(vec.size()>0)vec.get(0).remove();
						else;
					}
					else;
				}
				else {							// 鸟已移出屏幕
					/**
					 * home按钮
					 */
					ImageIcon home = new ImageIcon("images/button_home.png");
					JLabel home_label = new JLabel(home);
					home_label.setOpaque(false);
					home_label.setBounds(595, 235, home.getIconWidth(), home.getIconHeight());
					layeredPane.add(home_label, JLayeredPane.MODAL_LAYER);
					/**
					 * restart按钮
					 */
					ImageIcon restart = new ImageIcon("images/button_restart.png");
					JLabel restart_label = new JLabel(restart);
					restart_label.setOpaque(false);
					restart_label.setBounds(580, 284, restart.getIconWidth(), restart.getIconHeight());
					layeredPane.add(restart_label, JLayeredPane.MODAL_LAYER);
					/**
					 * home按钮事件
					 */
					home_label.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							home_label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}

						@Override
						public void mouseExited(MouseEvent e) {
							home_label.setCursor(Cursor.getDefaultCursor());
						}

						@Override
						public void mouseClicked(MouseEvent e) {
							home_label.setCursor(Cursor.getDefaultCursor());
							home_label.setVisible(false);
							currentScore_label.setVisible(false);
							currentScore_label = null;
							topScore_label.setVisible(false);
							topScore_label = null;
							medal_label.setVisible(false);
							medal_label = null;
							score_label.setVisible(false);
							score_label = null;
							b = null;
							layeredPane.removeAll();
							service.shutdownNow();
							System.gc();
							HOME();
						}	
					});
					/**
					 * restart按钮事件 
					 */
					restart_label.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							restart_label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}

						@Override
						public void mouseExited(MouseEvent e) {
							restart_label.setCursor(Cursor.getDefaultCursor());
						}

						@Override
						public void mouseClicked(MouseEvent e) {
							restart_label.setCursor(Cursor.getDefaultCursor());
							restart_label.setVisible(false);
							currentScore_label.setVisible(false);
							currentScore_label = null;
							topScore_label.setVisible(false);
							topScore_label = null;
							medal_label.setVisible(false);
							medal_label = null;
							score_label.setVisible(false);
							score_label = null;
							b = null;
							layeredPane.removeAll();
							service.shutdownNow();
							System.gc();
							START();
						}	
					});
					/**
					 * 得分面板
					 */
					ImageIcon score_panel = new ImageIcon("images/score_panel.png");
				    score_label = new JLabel(score_panel);
					score_label.setOpaque(false);
					score_label.setBounds(350, 230, score_panel.getIconWidth(), score_panel.getIconHeight());
					layeredPane.add(score_label, JLayeredPane.MODAL_LAYER);
					/**
					 * 奖牌
					 */
					ts.add(score);
					ImageIcon medal = Utility.getMedal(score, ts);
				    medal_label = new JLabel(medal);
					medal_label.setOpaque(false);
					medal_label.setBounds(380, 274, medal.getIconWidth(), medal.getIconHeight());
					layeredPane.add(medal_label, JLayeredPane.DRAG_LAYER);
					/**
					 * 本局分数显示
					 */
					List<ImageIcon> currentL = Utility.getScoreImage(score);
					for(int i=0;i<currentL.size();i++) {
					    currentScore_label = new JLabel(currentL.get(i));
						currentScore_label.setOpaque(false);
						currentScore_label.setBounds(515+16*i, 253, currentL.get(i).getIconWidth(), currentL.get(i).getIconHeight());
						layeredPane.add(currentScore_label, JLayeredPane.POPUP_LAYER);
					}
					/**
					 * 最高分数显示
					 */
					int top=0;			//找到最高分数
					int j = 1;			
					for(Integer i:ts) {
						if(j==ts.size())top=i;
						j++;
					}
					List<ImageIcon> topL = Utility.getScoreImage(top);
					for(int i=0;i<topL.size();i++) {
						topScore_label = new JLabel(topL.get(i));
						topScore_label.setOpaque(false);
						topScore_label.setBounds(515+16*i, 298, topL.get(i).getIconWidth(), topL.get(i).getIconHeight());
						layeredPane.add(topScore_label, JLayeredPane.POPUP_LAYER);
					}
				}
			}
		}
	}

	class ColumnTask implements Runnable { // 每一游戏时刻管子任务
		@Override
		public void run() {
			/**
			 * 柱子移动
			 */
			if(!b.isCrashed) {
			if(VectorSize<6) {
				Column col = new Column(Utility.getRandomColumnImage());
				vec.add(col);
			}
				for(int i =0;i<VectorSize;i++)
					vec.get(i).move(Bird.xspeed);
			}
		}
	}

	class BackgroundPanel extends JPanel { // 背景
		private static final long serialVersionUID = 1L;

		public BackgroundPanel() {
			setSize(1000, 700);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(BackgroundIm, 0, 0, this.getWidth(), this.getHeight(), this);
		}

	}

	class homeTimerTask implements Runnable{ // 主界面计时任务
		private boolean isRight = true;
		private JLabel j_down;
		private JLabel j_up;

		public homeTimerTask(JLabel j_down, JLabel j_up) {
			this.j_down = j_down;
			this.j_up = j_up;
		}

		/**
		 * 管子持续向右，到达临界点转向 向左同理 每次只移动上方管子，下方管子主动与上方管子同步
		 */

		@Override
		public void run() {
			if (isRight) {
				j_down.setBounds(j_down.getBounds().x + 5, j_down.getBounds().y, pipe1_up.getIconWidth(),
						pipe1_up.getIconHeight());
				j_up.setBounds(j_down.getBounds().x, j_up.getBounds().y, pipe1_down.getIconWidth(),
						pipe1_down.getIconHeight());
				if (j_down.getBounds().x >= 850)
					isRight = false;
			}
			if (!isRight) {
				j_down.setBounds(j_down.getBounds().x - 5, j_down.getBounds().y, pipe1_up.getIconWidth(),
						pipe1_up.getIconHeight());
				j_up.setBounds(j_down.getBounds().x, j_up.getBounds().y, pipe1_down.getIconWidth(),
						pipe1_down.getIconHeight());
				if (j_down.getBounds().x <= 700)
					isRight = true;
			}

			/**
			 * 如果当前播放歌曲与碟片不符则更新碟片
			 * AudioPlayer.getCurrentMusic():返回StringBuffer类,格式:"images/xx.gif"
			 * this.currentMusic:StringBuffer类,格式：
			 */

		}
	}

	class Column { // 管子类
		private int x; // 上方管子左上角x坐标
		private int y; // 上方管子左上角y坐标
		private int ySpace; // 上下管子间隔
		private int xSpace; // 前后管子间隔
		private JLabel down_label;
		private JLabel up_label;
		private ImageIcon Pipe_down;
		private ImageIcon Pipe_up;

		
		public Column(ColumnImage columnIm) {
			VectorSize++;
			if (vec.isEmpty())  // 第一根管子
				this.x = 1050;
			else
				this.x = vec.get(VectorSize-2).x+vec.get(VectorSize-2).xSpace+vec.get(VectorSize-2).Pipe_down.getIconWidth();
				this.y = Utility.getRandomY();
				if(score<=10) {
					this.ySpace = Utility.getRandomYspace(Utility.EASY);
					this.xSpace = Utility.getRandomXspace(Utility.EASY);
				}
				else {
					this.ySpace = Utility.getRandomYspace(Utility.HARD);
					this.xSpace = Utility.getRandomXspace(Utility.HARD);
				}
				Pipe_down = columnIm.down;
				down_label = new JLabel(Pipe_down);
				down_label.setOpaque(false);
				down_label.setBounds(x, y, Pipe_down.getIconWidth(), Pipe_down.getIconWidth());

				Pipe_up = columnIm.up;
				up_label = new JLabel(Pipe_up);
				up_label.setOpaque(false);
				up_label.setBounds(x, y + Pipe_down.getIconHeight() + this.ySpace, Pipe_up.getIconWidth(),
						Pipe_up.getIconHeight());

				layeredPane.add(down_label, JLayeredPane.POPUP_LAYER);
				layeredPane.add(up_label, JLayeredPane.POPUP_LAYER);
			
		}

		public void move(int speed) {
			this.down_label.setBounds(this.x - speed, y, Pipe_down.getIconWidth(), Pipe_down.getIconHeight());
			this.up_label.setBounds(this.x - speed, y + Pipe_down.getIconHeight() + this.ySpace, Pipe_up.getIconWidth(),
					Pipe_up.getIconHeight());
			this.x = this.x - speed;
			

			if (this.x + this.Pipe_down.getIconWidth() < 0) {// 管子移出屏幕则清理对象
				vec.remove(this);
				VectorSize--;
				vec.get(0).move(speed);  			// 防止由于删除0元素，原1元素向前移为0元素而漏移
				score++;
				layeredPane.remove(this.down_label);
				layeredPane.remove(this.up_label); 
				this.down_label = null;
				this.up_label = null;
				this.Pipe_up = null;
				this.Pipe_down = null;	
				System.gc();
			}
		}
		public void remove() {
			for(int i=0;i<vec.size();i++) {
				layeredPane.remove(vec.get(i).down_label);
				layeredPane.remove(vec.get(i).up_label); 
				vec.get(i).down_label = null;
				vec.get(i).up_label = null;
				vec.get(i).Pipe_up = null;
				vec.get(i).Pipe_down = null;	
			}
			System.gc();
			vec.clear();
			VectorSize = 0;
			isColumnRemoved = true;
		}
	}
	
	class Bird{			// 鸟类
		public boolean isJumping = false;		// 是否要跳
		public boolean isCrashed = false;		// 是否已撞击
		public boolean isRemoved = false;		// 是否已被移除
		/**
		 * 纵向加速度 
		 */
		public static final int g = 1;          
		public static final int xspeed = 4;		// 横向速度
		public int yspeed = 0;					// 纵向速度
		public int initJumpSpeed = -12;			// 起跳初速度
		
		private ImageIcon currentImage; 		// 当前贴图
		private ImageIcon bird_fly;
		private JLabel bird_label;
		private int x;							// 当前x坐标
		private int y;							// 当前y坐标
		
		public Bird(ImageIcon bird_fly) {
			this.x = 500;
			this.y = 280;
			this.bird_fly = bird_fly;
			this.currentImage = this.bird_fly;
			bird_label = new JLabel(this.bird_fly);
			bird_label.setOpaque(false);
			bird_label.setBounds(x, y, bird_fly.getIconWidth(), bird_fly.getIconHeight());
			layeredPane.add(bird_label,JLayeredPane.DRAG_LAYER);
		}
		/**
		 * 鸟每帧的移动 	
		 */
		public void move() {
			//FIXME 每帧的移动
			if(this.isJumping) {			// 跳
				if(y+yspeed<0) {			// 鸟要跳出屏幕上方
					bird_label.setBounds(x, 0, this.bird_fly.getIconWidth(), this.bird_fly.getIconHeight());
					y = 0;
				}
				else {
					bird_label.setBounds(x, y+yspeed, this.bird_fly.getIconWidth(), this.bird_fly.getIconHeight());
					y = y+yspeed;
				}		
				yspeed+=g;
				this.isJumping = false;
			}
			else {							// 不跳
				bird_label.setBounds(x, y+yspeed, this.bird_fly.getIconWidth(), this.bird_fly.getIconHeight());
				yspeed+=g;
				y = y+yspeed;
			}
			/**
			 * 撞到地面
			 */
			if(this.y+this.currentImage.getIconHeight()>=560) {
				this.isCrashed = true;
				return;
			}
			/**
			 * 撞到柱子	
			 */
			int i;
			for(i=0;i<vec.size();i++)
				if((vec.get(i).x<=this.x+this.currentImage.getIconWidth())&&(vec.get(i).x+vec.get(i).Pipe_down.getIconWidth()>=this.x+this.currentImage.getIconWidth()))
					break;	// i代表鸟通过中的管子，如未找到则不做操作
			if(i<vec.size()) {
				Column passing = vec.get(i);
				if(!(this.y>passing.Pipe_down.getIconHeight()+passing.y
						&&this.y+this.currentImage.getIconHeight()<
						passing.Pipe_down.getIconHeight()+passing.y+passing.ySpace))
				{
					this.isCrashed = true;			
					for(Column c:vec) {
						c.up_label.setVisible(false);
						c.down_label.setVisible(false);
					}
				}
				
			}
			
		}
		
		/**
		 * 鸟已撞击管子
		 * 鸟每帧不断下落
		 * 鸟移出屏幕后显示结束面板
		 */
		public void fall() {
			if(b.y<=1000) {				// 鸟未移出屏幕
				yspeed += g; 
				bird_label.setLocation(x, y+yspeed);
				this.y = y+yspeed;
			}
			else {
				layeredPane.remove(bird_label);
				this.bird_fly = null;
				System.gc();
				this.isRemoved = true;
			}
		}
	}
}
