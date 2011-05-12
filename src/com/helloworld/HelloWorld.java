
package com.helloworld;

import com.helloworld.controls.Gameplay;
import com.helloworld.map.Hexagon;
import com.helloworld.map.Map;
import com.helloworld.utils.HexxagonConstant;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.Log;
import android.widget.Toast;

public class HelloWorld extends BaseGameActivity implements IOnSceneTouchListener {
    public static int CAMERA_WIDTH = 720;

    public static int CAMERA_HEIGHT = 480;

    private Camera camera;

    private Texture mTexture;

    private Texture mTextureCopyable;

    private Texture mTextureJumpable;

    private TextureRegion mHexaNormal;

    private TextureRegion mHexaCopyable;

    private TextureRegion mHexaJumpable;

    public float width_hex;

    public float height_hex;

    private int BACKGROUND = 1;

    private int SPRITE = BACKGROUND + 1;

    @Override
    public void onLoadComplete() {
        // TODO Auto-generated method stubget

    }

    @Override
    public Engine onLoadEngine() {
        // TODO Auto-generated method stub
        CAMERA_WIDTH = this.getWindowManager().getDefaultDisplay().getWidth();
        CAMERA_HEIGHT = this.getWindowManager().getDefaultDisplay().getHeight();

        this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.camera));
    }

    @Override
    public void onLoadResources() {
        // TODO Auto-generated method stub
        this.mTexture = new Texture(256, 256, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        this.mTextureCopyable = new Texture(256, 256, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        this.mTextureJumpable = new Texture(256, 256, TextureOptions.NEAREST_PREMULTIPLYALPHA);

        this.mHexaNormal = TextureRegionFactory.createFromAsset(this.mTexture, this,
                "map/hexa_normal.png", 0, 0);
        this.mHexaCopyable = TextureRegionFactory.createFromAsset(this.mTextureCopyable, this,
                "map/hexa_copyable.png", 0, 0);
        this.mHexaJumpable = TextureRegionFactory.createFromAsset(this.mTextureJumpable, this,
                "map/hexa_jumpable.png", 0, 0);

        this.mEngine.getTextureManager().loadTexture(this.mTexture);
        Map.initMap(this.getResources().openRawResource(R.raw.map_demo));
        Map.calsSizePerItem();
    }

    @Override
    public Scene onLoadScene() {
        // TODO Auto-generated method stub
        this.mEngine.registerUpdateHandler(new FPSLogger());
        Scene scene = new Scene(2);
        // scene.
        scene = drawMap(scene);
        scene.setBackground(new ColorBackground(0, 0, 0.5f));

        scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
                final Hexagon face = (Hexagon)pTouchArea;
                Gameplay.doAction(Map.maps[face.xDim][face.yDim]);
                Toast.makeText(HelloWorld.this, "x = " + face.xDim + "| y = " + face.yDim, 200)
                        .show();
                return true;
            }
        });
        scene.setTouchAreaBindingEnabled(true);

        return scene;
    }

    private Scene drawMap(Scene scene) {

        float offset_y = CAMERA_HEIGHT / 2 - Map.HEIGHT_HEX / 2;
        float offset_x = Map.X_AXIAL;

        float y = 0;
        float x = 0;

        float offset_i_x;
        float offset_i_y;

        int index = 0;
        int type;
        final IEntity background = scene.getChild(BACKGROUND);
        {
            for (int i = 0; i < 9; i++) {
                offset_i_x = offset_x + i * (Map.WIDTH_HEX * 3 / 4);
                offset_i_y = offset_y - i * (Map.HEIGHT_HEX / 2);
                for (int j = 0; j < 9; j++) {
                    y = offset_i_y + j * (Map.HEIGHT_HEX / 2);
                    x = offset_i_x + j * (Map.WIDTH_HEX * 3 / 4);
                    type = Map.maps[i][j].getType();
                    if (type == HexxagonConstant.CELL_DEFAULT
                            || type == HexxagonConstant.CELL_PLAYER1
                            || type == HexxagonConstant.CELL_PLAYER2) {
                        final Hexagon hexagon = new Hexagon(x, y, mHexaNormal);
                        hexagon.xDim = i;
                        hexagon.yDim = j;
                        hexagon.setSize(Map.WIDTH_HEX, Map.HEIGHT_HEX);

                        Map.maps[i][j].setSprite(hexagon);
                        Map.maps[i][j].setIndex(index);

                        background.getLastChild().attachChild(hexagon);

                        scene.registerTouchArea(hexagon);
                        index++;
                    } else if (Map.maps[i][j].getType() == HexxagonConstant.COPYABLE) {
                        final Hexagon hexagon = new Hexagon(x, y, mHexaCopyable);
                        hexagon.xDim = i;
                        hexagon.yDim = j;
                        hexagon.setSize(Map.WIDTH_HEX, Map.HEIGHT_HEX);

                        Map.maps[i][j].setSprite(hexagon);
                        Map.maps[i][j].setIndex(index);

                        background.getLastChild().attachChild(hexagon);
                        scene.registerTouchArea(hexagon);
                        index++;
                    } else if (Map.maps[i][j].getType() == HexxagonConstant.JUMPABLE) {
                        final Hexagon hexagon = new Hexagon(x, y, mHexaJumpable);
                        hexagon.xDim = i;
                        hexagon.yDim = j;
                        hexagon.setSize(Map.WIDTH_HEX, Map.HEIGHT_HEX);

                        Map.maps[i][j].setSprite(hexagon);
                        Map.maps[i][j].setIndex(index);

                        background.getLastChild().attachChild(hexagon);
                        scene.registerTouchArea(hexagon);
                        index++;
                    }
                }
            }
        }
        return scene;
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        // TODO Auto-generated method stub
        return false;
    }
}
