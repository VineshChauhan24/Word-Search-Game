/*------------------------------------------
 * --- simple math helper (Java version) ---
 * @author abdul_aris_r
 * @date 04 Juni 2016
 *------------------------------------------
 */

package com.aar.app.wordsearch.commons.math;

public class Vec2 {

    public static final Vec2 Right = new Vec2(1f, 0f);

	public float x;
	public float y;

	public Vec2() {
		this(0.0f, 0.0f);
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2(Vec2 v) {
		x = v.x;
		y = v.y;
	}
	
	public void add(Vec2 v) {
		x += v.x;
		y += v.y;
	}
	
	public void add(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	public void sub(Vec2 v) {
		x -= v.x;
		y -= v.y;
	}
	
	public void sub(float x, float y) {
		this.x -= x;
		this.y -= y;
	}

	/*
	 * hasil dot product dari dua vector
	 */
	public float dot(Vec2 v) {
		return (x * v.x) + (y * v.y);
	}

	/*
	 * panjang vector
	 */
	public float length() {
		return (float)Math.sqrt((x * x) + (y * y));
	}

	/*
	 * normalize vector dan return vector baru
	 * (normal vector yaitu vector yang memiliki length = 1)
	 */
	public void normalize() {
		float len = length();
		x /= len;
		y /= len;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}


	/*
	 * static function
	 */
	public static Vec2 add(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vec2 add(Vec2 v1, float x, float y) {
		return new Vec2(v1.x + x, v1.y + y);
	}

	public static Vec2 sub(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vec2 sub(Vec2 v1, float x, float y) {
		return new Vec2(v1.x - x, v1.y - y);
	}
	
	public static Vec2 div(Vec2 v1, float div) {
		return new Vec2(v1.x / div, v1.y / div);
	}
	
	public static Vec2 div(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.x / v2.x, v1.y / v2.y);
	}
	
	public static Vec2 div(Vec2 v1, float x, float y) {
		return new Vec2(v1.x / x, v1.y / y);
	}
	
	/*
	 * kalkulasikan normal dari dua vector
	 * (normal yaitu vector yang tegak lurus terhadap bidang)
	 */
	public static Vec2 normal(Vec2 v1, Vec2 v2) {
		return new Vec2( -(v1.y-v2.y), (v1.x-v2.x));
	}
	
	public static Vec2 normalize(Vec2 v) {
		float len = v.length();
		return new Vec2(v.x/len, v.y/len);
	}

}
