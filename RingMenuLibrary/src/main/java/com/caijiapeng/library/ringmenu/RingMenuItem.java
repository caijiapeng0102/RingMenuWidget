package com.caijiapeng.library.ringmenu;

public class RingMenuItem {

	private String menuLabel = null;
	private int menuIcon = 0;
	private boolean isEnabled = true;
	private RingMenuItemClickListener menuListener = null;
	
	/**
	 * 新建一个按钮实例
	 * @param displayName - 按钮文本，可为null
	 */
	public RingMenuItem(String displayName) {
		this.menuLabel = displayName;
	}
	
	/**
	 * 设置按钮的图标
	 * @param displayIcon - (int) Icon resource ID.
	 */
	public void setDisplayIcon(int displayIcon) {
		this.menuIcon = displayIcon;
	}
	
	/**
	 *设置按钮的单击事件
	 * @param listener
	 */
	public void setOnMenuItemPressed(RingMenuItemClickListener listener) {
		menuListener = listener;
	}



	public String getLabel() {
		return menuLabel;
	}

	public int getIcon() {
		return menuIcon;
	}


	public void menuActiviated() {
		if(menuListener != null)
			menuListener.execute();
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public interface RingMenuItemClickListener {
		public void execute();
	}


}
