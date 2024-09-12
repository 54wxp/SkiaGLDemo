package com.yjy.skiaapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

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
