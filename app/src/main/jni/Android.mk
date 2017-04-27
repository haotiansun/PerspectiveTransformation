LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


OpenCV_INSTALL_MODULES := on
OpenCV_CAMERA_MODULES := off

OPENCV_LIB_TYPE :=SHARED

ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
include D:\OpenCV-2.4.9-android-sdk\sdk\native\jni\OpenCV.mk
else
include $(OPENCV_MK_PATH)
endif

LOCAL_MODULE := OpenCV

LOCAL_SRC_FILES := com_example_lenovo_camera_2_OpenCVHelper.cpp

LOCAL_LDLIBS +=  -lm -llog

LOCAL_LDLIBS += -latomic


include $(BUILD_SHARED_LIBRARY)

