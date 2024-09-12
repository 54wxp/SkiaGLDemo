package com.wxp.skiaapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    protected ConstraintLayout mFrameLayout = null;
    private SkiaCanvasGLView mGLSurfaceView = null;
    private int[] mGLContextAttrs = {8, 8, 8, 8, 24, 8, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.init();
        setContentView(R.layout.activity_main);
        mGLSurfaceView = (SkiaCanvasGLView) findViewById(R.id.skGLView);
    }


//    public void init() {
//
//        // FrameLayout
//        ViewGroup.LayoutParams framelayout_params =
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//
//        mFrameLayout = new ConstraintLayout(this);
//
//        mFrameLayout.setLayoutParams(framelayout_params);
//
//        // Cocos2dxEditText layout
//        ViewGroup.LayoutParams edittext_layout_params =
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        // Cocos2dxGLSurfaceView
//        this.mGLSurfaceView = this.onCreateView();
//        this.mGLSurfaceView.setPreserveEGLContextOnPause(true);
//
//        // ...add to FrameLayout
//        mFrameLayout.addView(this.mGLSurfaceView);
//
//        // Switch to supported OpenGL (ARGB888) mode on emulator
//        // this line dows not needed on new emulators and also it breaks stencil buffer
//        //if (isAndroidEmulator())
//        //   this.mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
//
//        this.mGLSurfaceView.setRenderer(new SkiaRenderer());
//
//        // Set framelayout as the content view
//        setContentView(mFrameLayout);
//    }
//
//
//    public SkiaCanvasGLView onCreateView() {
//        SkiaCanvasGLView glSurfaceView = new SkiaCanvasGLView(this);
//        //this line is need on some device if we specify an alpha bits
//        if(this.mGLContextAttrs[3] > 0) glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//
//        // use custom EGLConfigureChooser
//        Cocos2dxEGLConfigChooser chooser = new Cocos2dxEGLConfigChooser(this.mGLContextAttrs);
//        glSurfaceView.setEGLConfigChooser(chooser);
//
//        return glSurfaceView;
//    }
//
//
//    private class Cocos2dxEGLConfigChooser implements GLSurfaceView.EGLConfigChooser
//    {
//        private int[] mConfigAttributes;
//        private  final int EGL_OPENGL_ES2_BIT = 0x04;
//        private  final int EGL_OPENGL_ES3_BIT = 0x40;
//        public Cocos2dxEGLConfigChooser(int redSize, int greenSize, int blueSize, int alphaSize, int depthSize, int stencilSize, int multisamplingCount)
//        {
//            mConfigAttributes = new int[] {redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize, multisamplingCount};
//        }
//        public Cocos2dxEGLConfigChooser(int[] attributes)
//        {
//            mConfigAttributes = attributes;
//        }
//
//        @Override
//        public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
//        {
//            int[][] EGLAttributes = {
//                    {
//                            // GL ES 2 with user set
//                            EGL10.EGL_RED_SIZE, mConfigAttributes[0],
//                            EGL10.EGL_GREEN_SIZE, mConfigAttributes[1],
//                            EGL10.EGL_BLUE_SIZE, mConfigAttributes[2],
//                            EGL10.EGL_ALPHA_SIZE, mConfigAttributes[3],
//                            EGL10.EGL_DEPTH_SIZE, mConfigAttributes[4],
//                            EGL10.EGL_STENCIL_SIZE, mConfigAttributes[5],
//                            EGL10.EGL_SAMPLE_BUFFERS, (mConfigAttributes[6] > 0) ? 1 : 0,
//                            EGL10.EGL_SAMPLES, mConfigAttributes[6],
//                            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                            EGL10.EGL_NONE
//                    },
//                    {
//                            // GL ES 2 with user set 16 bit depth buffer
//                            EGL10.EGL_RED_SIZE, mConfigAttributes[0],
//                            EGL10.EGL_GREEN_SIZE, mConfigAttributes[1],
//                            EGL10.EGL_BLUE_SIZE, mConfigAttributes[2],
//                            EGL10.EGL_ALPHA_SIZE, mConfigAttributes[3],
//                            EGL10.EGL_DEPTH_SIZE, mConfigAttributes[4] >= 24 ? 16 : mConfigAttributes[4],
//                            EGL10.EGL_STENCIL_SIZE, mConfigAttributes[5],
//                            EGL10.EGL_SAMPLE_BUFFERS, (mConfigAttributes[6] > 0) ? 1 : 0,
//                            EGL10.EGL_SAMPLES, mConfigAttributes[6],
//                            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                            EGL10.EGL_NONE
//                    },
//                    {
//                            // GL ES 2 with user set 16 bit depth buffer without multisampling
//                            EGL10.EGL_RED_SIZE, mConfigAttributes[0],
//                            EGL10.EGL_GREEN_SIZE, mConfigAttributes[1],
//                            EGL10.EGL_BLUE_SIZE, mConfigAttributes[2],
//                            EGL10.EGL_ALPHA_SIZE, mConfigAttributes[3],
//                            EGL10.EGL_DEPTH_SIZE, mConfigAttributes[4] >= 24 ? 16 : mConfigAttributes[4],
//                            EGL10.EGL_STENCIL_SIZE, mConfigAttributes[5],
//                            EGL10.EGL_SAMPLE_BUFFERS, 0,
//                            EGL10.EGL_SAMPLES, 0,
//                            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                            EGL10.EGL_NONE
//                    },
//                    {
//                            // GL ES 2 by default
//                            EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                            EGL10.EGL_NONE
//                    }
//            };
//
//            EGLConfig result = null;
//            for (int[] eglAtribute : EGLAttributes) {
//                result = this.doChooseConfig(egl, display, eglAtribute);
//                if (result != null)
//                    return result;
//            }
//
//            //Log.e(DEVICE_POLICY_SERVICE, "Can not select an EGLConfig for rendering.");
//            return null;
//        }
//
//        private EGLConfig doChooseConfig(EGL10 egl, EGLDisplay display, int[] attributes) {
//            EGLConfig[] configs = new EGLConfig[1];
//            int[] matchedConfigNum = new int[1];
//            boolean result = egl.eglChooseConfig(display, attributes, configs, 1, matchedConfigNum);
//            if (result && matchedConfigNum[0] > 0) {
//                return configs[0];
//            }
//            return null;
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
