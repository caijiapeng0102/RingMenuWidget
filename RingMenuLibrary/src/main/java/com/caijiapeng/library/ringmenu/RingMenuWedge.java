package com.caijiapeng.library.ringmenu;

import android.graphics.Path;
import android.graphics.RectF;

public class RingMenuWedge extends Path {
	private int x, y;
	private int InnerSize, OuterSize;
	private float StartArc;
	private float ArcWidth;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param InnerSize
	 * @param OuterSize
	 * @param StartArc
	 * @param ArcWidth
	 */
	protected RingMenuWedge(int x, int y, int InnerSize, int OuterSize,
							float StartArc, float ArcWidth) {
		super();
		if (StartArc >= 360) {
			StartArc = StartArc - 360;
		}
		this.x = x;
		this.y = y;
		this.InnerSize = InnerSize;
		this.OuterSize = OuterSize;
		this.StartArc = StartArc;
		this.ArcWidth = ArcWidth;
		this.buildPath();
	}

	/**
	 * 
	 */
	protected void buildPath() {
		final RectF rect = new RectF();
		final RectF rect2 = new RectF();
		// Rectangles values
		rect.set(this.x - this.InnerSize, this.y - this.InnerSize, this.x
				+ this.InnerSize, this.y + this.InnerSize);
		rect2.set(this.x - this.OuterSize, this.y - this.OuterSize, this.x
				+ this.OuterSize, this.y + this.OuterSize);
		this.reset();
		this.arcTo(rect2, StartArc, ArcWidth);
		this.arcTo(rect, StartArc + ArcWidth, -ArcWidth);
		this.close();
	}

	public float getStartArc() {
		return StartArc;
	}

}