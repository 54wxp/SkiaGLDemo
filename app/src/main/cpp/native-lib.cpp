#include <jni.h>
#include <string>

#include <SkBitmap.h>

#include <include/core/SkFont.h>
#include "include/core/SkCanvas.h"
#include "include/core/SkSurface.h"
#include <include/core/SkPaint.h>
#include <include/core/SkRect.h>
#include <include/core/SkColor.h>
#include <include/core/SkGraphics.h>

#include <android/bitmap.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>

#include <android/log.h>
#include <GrContextOptions.h>
#include <GrContext.h>
#include <gl/GrGLInterface.h>
#include <GrBackendSurface.h>
#include <gl/GrGLTypes.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <GLES3/gl3.h>
#include <chrono>


#define TAG "JNI_TAG"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

bool isInit = false;
sk_sp<GrContext> grContext;

extern "C"
JNIEXPORT void JNICALL
native_init(JNIEnv *env, jobject thiz, jint width,jint height){
    sk_sp<const GrGLInterface> glInterface(GrGLCreateNativeInterface());
    GrContextOptions options;
    options.fDisableDistanceFieldPaths = true;
    options.fDisableCoverageCountingPaths = true;
    grContext = GrContext::MakeGL(std::move(glInterface), options);
    isInit = true;

//    SkImageInfo info = SkImageInfo:: MakeN32Premul(width, height);
//    sk_sp<SkSurface> gpuSurface(
//            SkSurface::MakeRenderTarget(grContext.get(), SkBudgeted::kNo, info));
//    if (!gpuSurface) {
//        SkDebugf("SkSurface::MakeRenderTarget returned null\n");
//        return;
//    }
//    background = gpuSurface->getCanvas();
}


extern "C" JNIEXPORT void
JNICALL
drawFrame(JNIEnv *env, jclass clazz, jint width, jint height) {
    if (!isInit) {
        return;
    }

    SkColorType colorType;
    // setup surface for fbo0
    GrGLFramebufferInfo fboInfo;
    fboInfo.fFBOID = 0;
    if (false) {
        fboInfo.fFormat = GL_RGBA16F;
        colorType = kRGBA_F16_SkColorType;
    } else {
        fboInfo.fFormat = GL_RGBA8;
        colorType = kN32_SkColorType;
    }
    GrBackendRenderTarget backendRT(width, height, 0, 8, fboInfo);

    SkSurfaceProps props(0, kUnknown_SkPixelGeometry);

    sk_sp<SkSurface> renderTarget(SkSurface::MakeFromBackendRenderTarget(
            grContext.get(), backendRT, kBottomLeft_GrSurfaceOrigin, colorType,
            nullptr, &props));

    auto canvas = renderTarget->getCanvas();
    canvas->clear(SK_ColorTRANSPARENT);

    SkPaint paint;

    paint.setColor(SK_ColorBLUE);
    SkRect rect;
    rect.set(SkIRect::MakeWH(width,height));

    canvas->drawRect(rect,paint);

    SkPaint paint2;
    paint2.setColor(SK_ColorWHITE);
    const char *str = "Hello Surface Skia";

    SkFont skfont(SkTypeface::MakeDefault(),100);

    for(int i = 0; i < 100; ++i){
        for(int j = 0; j < 100; ++j){
            canvas->drawString(str,100 + i,100 + j,skfont,paint2);
        }
    }
    canvas->flush();
}

extern "C"
JNIEXPORT void JNICALL
native_render(JNIEnv *env, jobject thiz, jobject jSurface,jint width,jint height){
    ANativeWindow *nativeWindow = ANativeWindow_fromSurface(env,jSurface);


    ANativeWindow_setBuffersGeometry(nativeWindow,  width, height, WINDOW_FORMAT_RGBA_8888);

    ANativeWindow_Buffer *buffer = new ANativeWindow_Buffer();

    ANativeWindow_lock(nativeWindow,buffer,0);


    int bpr = buffer->stride * 4;


    SkBitmap bitmap;
    SkImageInfo image_info = SkImageInfo
    ::MakeS32(buffer->width,buffer->height,SkAlphaType::kPremul_SkAlphaType);


    bitmap.setInfo(image_info,bpr);

    bitmap.setPixels(buffer->bits);

    SkCanvas *background = new SkCanvas(bitmap);
    SkPaint paint;

    paint.setColor(SK_ColorBLUE);
    SkRect rect;
    rect.set(SkIRect::MakeWH(width,height));

    background->drawRect(rect,paint);

    SkPaint paint2;
    paint2.setColor(SK_ColorWHITE);
    const char *str = "Hello Surface Skia";

    SkFont skfont(SkTypeface::MakeDefault(),100);

    for(int i = 0; i < 100; ++i){
        for(int j = 0; j < 100; ++j){
            background->drawString(str,100 + i,100 + j,skfont,paint2);
        }
    }

    SkImageInfo imageInfo = background->imageInfo();


    LOGE("row size:%d,buffer stride:%d",imageInfo.minRowBytes(),bpr);

    LOGE("before native_window stride:%d,width:%d,height:%d,format:%d",
            buffer->stride,buffer->width,buffer->height,buffer->format);

    int rowSize = imageInfo.minRowBytes();




    bool isCopy =  background->readPixels(imageInfo,buffer->bits,bpr,0,0);


    LOGE("after native_window stride:%d,width:%d,height:%d,format:%d",
         buffer->stride,buffer->width,buffer->height,buffer->format);

    ANativeWindow_unlockAndPost(nativeWindow);

}


extern "C"
JNIEXPORT void JNICALL
native_renderCanvas(JNIEnv *env, jobject thiz, jobject bitmap) {
    // TODO: implement native_renderCanvas()
    LOGE("native render");

    AndroidBitmapInfo info;
    int *pixel;
    int ret;

    ret = AndroidBitmap_getInfo(env,bitmap,&info);
    ret = AndroidBitmap_lockPixels(env,bitmap,(void**)&pixel);

    int width = info.width;
    int height = info.height;

    SkBitmap bm = SkBitmap();
    SkImageInfo image_info = SkImageInfo
            ::MakeS32(width,height,SkAlphaType::kOpaque_SkAlphaType);
    bm.setInfo(image_info,image_info.minRowBytes());
    bm.setPixels(pixel);

    SkCanvas background(bm);
    SkPaint paint;

    paint.setColor(SK_ColorBLACK);
    SkRect rect;
    rect.set(SkIRect::MakeWH(width,height));

    background.drawRect(rect,paint);

    SkPaint paint2;
    paint2.setColor(SK_ColorBLUE);
    const char *str = "Hello Skia";

    SkFont skfont(SkTypeface::MakeDefault(),100);

    LOGE("row size:%d",image_info.minRowBytes());

    background.drawString(str,100,100,skfont,paint2);

    AndroidBitmap_unlockPixels(env,bitmap);





}


static const char* const className = "com/yjy/skiaapplication/SkiaUtils";
static const JNINativeMethod gMethods[] = {
        {"native_init","(II)V",(void *) native_init},
        {"drawFrame","(II)V",(void *) drawFrame},
        {"native_renderCanvas","(Landroid/graphics/Bitmap;)V",(void *)native_renderCanvas},
        {"native_render","(Landroid/view/Surface;II)V",(void *)native_render}
};


jint JNI_OnLoad(JavaVM *vm,void* reserved){
    JNIEnv *env = NULL;
    jint result;

    if(vm->GetEnv((void**)&env,JNI_VERSION_1_4)!=JNI_OK){
        return -1;
    }

    jclass clazz = env->FindClass(className);
    if(!clazz){
        LOGE("can not find class");
        return -1;
    }

    if(env->RegisterNatives(clazz,gMethods, sizeof(gMethods)/sizeof(gMethods[0])) < 0){
        LOGE("can not register method");
        return -1;
    }

    return JNI_VERSION_1_4;

}
