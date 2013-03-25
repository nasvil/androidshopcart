LOCAL_PATH := $(call my-dir)
PR_PATH := $(LOCAL_PATH)/../../../playready/android/playready/src/

include $(CLEAR_VARS)
LOCAL_MODULE    := drmmanager
LOCAL_SRC_FILES := drmmanager.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmsynclist
LOCAL_SRC_FILES := drmsynclist.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmxmrbuilder
LOCAL_SRC_FILES := drmxmrbuilder.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmcore
LOCAL_SRC_FILES := drmcore.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmblackbox
LOCAL_SRC_FILES := drmblackbox.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmcrypto
LOCAL_SRC_FILES := drmcrypto.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmsha256
LOCAL_SRC_FILES := drmsha256.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmpkcrypto
LOCAL_SRC_FILES := drmpkcrypto.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := oemrsa
LOCAL_SRC_FILES := oemrsa.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmnoncestore
LOCAL_SRC_FILES := drmnoncestore.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmbignum
LOCAL_SRC_FILES := drmbignum.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmmetering
LOCAL_SRC_FILES := drmmetering.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmdevcert
LOCAL_SRC_FILES := drmdevcert.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := wmdrmcert
LOCAL_SRC_FILES := wmdrmcert.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmsecureclock
LOCAL_SRC_FILES := drmsecureclock.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmdevicedevcert
LOCAL_SRC_FILES := drmdevicedevcert.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmxmrparser
LOCAL_SRC_FILES := drmxmrparser.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmxmrcommon
LOCAL_SRC_FILES := drmxmrcommon.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmdomainstore
LOCAL_SRC_FILES := drmdomainstore.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmdomain
LOCAL_SRC_FILES := drmdomain.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmcerts
LOCAL_SRC_FILES := drmcerts.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := oemaes
LOCAL_SRC_FILES := oemaes.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := oem_common
LOCAL_SRC_FILES := oem_common.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := oem_ansi
LOCAL_SRC_FILES := oem_ansi.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := drmconstants
LOCAL_SRC_FILES := drmconstants.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := playready-jni
LOCAL_SRC_FILES := playready-jni.c
LOCAL_C_INCLUDES := $(PR_PATH)/inc $(PR_PATH)/oem/common/inc $(PR_PATH)/certs/daytona $(PR_PATH)/crypto/bignum/daytona $(PR_PATH)/oem/ansi/inc $(PR_PATH)/tools/common $(PR_PATH)/xmr/commonlib $(PR_PATH)/xmr/parserlib
LOCAL_STATIC_LIBRARIES := drmmanager drmsynclist drmxmrbuilder drmcore drmblackbox drmcrypto drmsha256 drmpkcrypto oemrsa drmnoncestore drmbignum drmmetering drmdevcert wmdrmcert drmsecureclock drmdevicedevcert drmxmrparser drmxmrcommon drmdomainstore drmdomain drmcerts oemaes oem_common oem_ansi drmconstants

include $(BUILD_SHARED_LIBRARY)
