LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := hqx
LOCAL_SRC_FILES := hq4x.c hq2x.c wrapper.c

include $(BUILD_SHARED_LIBRARY)
