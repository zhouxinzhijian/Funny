LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := gif
LOCAL_SRC_FILES := \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\Android.mk \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\Application.mk \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\gif.c \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\giflib\dgif_lib.c \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\giflib\gifalloc.c \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\giflib\gif_err.c \
	C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni\giflib\gif_hash.c \

LOCAL_C_INCLUDES += C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\main\jni
LOCAL_C_INCLUDES += C:\Users\zhouxinzhijian\Documents\GitHubFunny\Funny\Funny\image_loader_lib\src\debug\jni

include $(BUILD_SHARED_LIBRARY)
