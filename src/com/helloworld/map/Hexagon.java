package com.helloworld.map;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Hexagon extends Sprite{
	
	public int xDim; // Vi tri theo x trong mang 2 chieu
	public int yDim; // Vi tri theo y trong mang 2 chieu
	
	int type;
	// 0 : none
	// 1 : normal
	// 2 : with_a
	// 3 : with_b
	public Hexagon(float pX, float pY, TextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion);
		// TODO Auto-generated constructor stub
	}
	
}
