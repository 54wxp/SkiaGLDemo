package com.wxp.skiaapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * <pre>
 *     author : weixiangpei
 * </pre>
 */
public class SkiaCanvasGLView extends GLSurfaceView {

    private SkiaRenderer mRenderer;

    public SkiaCanvasGLView(Context context) {
        super(context);
        initView();
    }

    public SkiaCanvasGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    protected void initView() {
        this.setEGLContextClientVersion(2);
        //this.setFocusableInTouchMode(true);
        this.mRenderer = new SkiaRenderer();
        super.setRenderer(this.mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    protected void onSizeChanged(final int pNewSurfaceWidth, final int pNewSurfaceHeight, final int pOldSurfaceWidth, final int pOldSurfaceHeight) {
        super.onSizeChanged(pNewSurfaceWidth, pNewSurfaceHeight, pOldSurfaceWidth, pOldSurfaceHeight);
        this.mRenderer.setScreenWidthAndHeight(pNewSurfaceWidth, pNewSurfaceHeight);

    }
}
