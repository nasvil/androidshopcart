#define DRM_BUILD_PROFILE DRM_BUILD_PROFILE_OEM
#define NO_DRM_CRT 1

#include <jni.h>

#include <drmcommon.h>
#include <drmutf.h>
#include <drmutilities.h>
#include <drmcrt.h>
#include <drmcontextsizes.h>
#include <drmmanager.h>
#include <drmtoolsutils.h>

/* Globals */
DRM_APP_CONTEXT *g_poAppContext = NULL;
DRM_DWORD g_cchUrl = 0;
DRM_DWORD g_cbChallenge = 0;
DRM_CHAR *g_pchUrl = NULL;
DRM_BYTE *g_pbChallenge = NULL;
DRM_CONST_STRING g_dstrStoreName = EMPTY_DRM_STRING;
DRM_CONST_STRING g_dstrDrmPath = EMPTY_DRM_STRING;
DRM_BYTE *g_inBuf = NULL;
DRM_DWORD g_inSize = 0;
DRM_BYTE *g_outBuf = NULL;
DRM_DWORD g_outSize = 0;

const DRM_CONST_STRING CharToConstDRMString(const char *ansiString) {
  DRM_CONST_STRING retval = EMPTY_DRM_STRING;
  DRM_STRING tempString = EMPTY_DRM_STRING;
  tempString.cchString = (DRM_DWORD)DRMCRT_strlen((const DRM_CHAR *)ansiString);
  tempString.pwszString = (DRM_WCHAR *)Oem_MemAlloc(((DRM_DWORD)DRMCRT_strlen((const DRM_CHAR *)ansiString) + 1) * SIZEOF(DRM_WCHAR));
  DRM_STR_UTF8toDSTR((const DRM_CHAR *)ansiString, tempString.cchString, &tempString );
  // NULL terminate
  tempString.pwszString[tempString.cchString] = '\0';
  retval.pwszString = tempString.pwszString;
  retval.cchString = tempString.cchString;
  return (const DRM_CONST_STRING)retval;
}

DRM_RESULT DRM_CALL ProcessEnvelope(DRM_BYTE *headerBuf,
				    DRM_DWORD headerSize, 
				    DRM_ENVELOPED_FILE_CONTEXT *f_pEnvFile) {
  DRM_RESULT dr = DRM_SUCCESS;
  DRM_APP_CONTEXT_INTERNAL *poAppContextInternal = (DRM_APP_CONTEXT_INTERNAL *)g_poAppContext;
  DRM_DWORD cbRead = 0;
  DRM_BYTE *rmOffset = NULL;
  ZEROMEM(f_pEnvFile, SIZEOF(DRM_ENVELOPED_FILE_CONTEXT));

  /*
  ** Read the file header.
  */
  LITTLEENDIAN_BYTES_TO_DWORD(f_pEnvFile->oEnvHeader.dwFileSignature, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_DWORD);
  LITTLEENDIAN_BYTES_TO_DWORD(f_pEnvFile->oEnvHeader.cbHeaderSize, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_DWORD);
  LITTLEENDIAN_BYTES_TO_DWORD(f_pEnvFile->oEnvHeader.dwFileDataOffset, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_DWORD);
  LITTLEENDIAN_BYTES_TO_WORD(f_pEnvFile->oEnvHeader.wFormatVersion, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_WORD);
  LITTLEENDIAN_BYTES_TO_WORD(f_pEnvFile->oEnvHeader.wCompatibleVersion, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_WORD);
  LITTLEENDIAN_BYTES_TO_DWORD(f_pEnvFile->oEnvHeader.dwCipherType, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_DWORD);
  DRM_BYT_CopyBytes(f_pEnvFile->oEnvHeader.rgbCipherData, 0, headerBuf, cbRead, DRM_ENVELOPE_CIPHER_DATA_SIZE);
  cbRead += DRM_ENVELOPE_CIPHER_DATA_SIZE;
  LITTLEENDIAN_BYTES_TO_WORD(f_pEnvFile->oEnvHeader.cbOriginalFilename, headerBuf, cbRead);
  cbRead += SIZEOF(DRM_WORD);
  LITTLEENDIAN_BYTES_TO_DWORD(f_pEnvFile->oEnvHeader.cbDrmHeaderLen, headerBuf, cbRead);

  /*
  ** Check the file signature, make sure reported header size is sane, and check that the version is OK.
  */
  ChkBOOL(f_pEnvFile->oEnvHeader.dwFileSignature == DRM_ENVELOPE_FILE_SIGNATURE, DRM_E_ENVELOPE_CORRUPT);
  ChkBOOL(f_pEnvFile->oEnvHeader.cbHeaderSize >= DRM_ENVELOPE_MINIMUM_HEADER_SIZE, DRM_E_ENVELOPE_CORRUPT);
  ChkBOOL(f_pEnvFile->oEnvHeader.wCompatibleVersion <= DRM_ENVELOPE_CURRENT_FORMAT_VERSION, DRM_E_ENVELOPE_FILE_NOT_COMPATIBLE);
  ChkBOOL(f_pEnvFile->oEnvHeader.dwFileDataOffset >= f_pEnvFile->oEnvHeader.cbHeaderSize, DRM_E_ENVELOPE_CORRUPT);

  /*
  ** Check that the header length is valid.
  */
  ChkBOOL(f_pEnvFile->oEnvHeader.cbDrmHeaderLen <= DRM_MAX_PLAYREADYOBJSIZE, DRM_E_BUFFERTOOSMALL);
  ChkBOOL(f_pEnvFile->oEnvHeader.cbDrmHeaderLen % SIZEOF( DRM_WCHAR ) == 0, DRM_E_ENVELOPE_CORRUPT);
  
  /*
  ** Skip over the original filename and copy the RM content header itself.
  */
  rmOffset = headerBuf + DRM_ENVELOPE_MINIMUM_HEADER_SIZE + f_pEnvFile->oEnvHeader.cbOriginalFilename;
  MEMCPY((DRM_BYTE *)poAppContextInternal->oDRMBuffer.oBuffers.rgbDRMHeaderData, rmOffset, 
	 f_pEnvFile->oEnvHeader.cbDrmHeaderLen );

  poAppContextInternal->eHeaderInContext = DRM_CSP_HEADER_NOT_SET;
  ChkDRMap(Drm_Content_SetProperty(g_poAppContext, DRM_CSP_AUTODETECT_HEADER,
				   poAppContextInternal->oDRMBuffer.oBuffers.rgbDRMHeaderData,
				   f_pEnvFile->oEnvHeader.cbDrmHeaderLen ), DRM_E_INVALIDARG,
	   DRM_E_CH_INVALID_HEADER);

  /*
  ** Parse the cipher intiailization values.
  */
  switch (f_pEnvFile->oEnvHeader.dwCipherType) {
  case eDRM_AES_COUNTER_CIPHER:
    /* Copy the initial AES counter */
    MEMCPY((DRM_BYTE *)&f_pEnvFile->qwInitialCipherCounter, f_pEnvFile->oEnvHeader.rgbCipherData, SIZEOF(DRM_UINT64));
    FIX_ENDIAN_QWORD(f_pEnvFile->qwInitialCipherCounter);
    break;

  case eDRM_RC4_CIPHER:
    break;
    
  default:
    ChkDR(DRM_E_ENVELOPE_FILE_NOT_COMPATIBLE);
  }

  f_pEnvFile->dwFileDataStart = f_pEnvFile->oEnvHeader.dwFileDataOffset;

ErrorExit:
  return dr;
}

DRM_RESULT DRM_CALL BindCallback(IN const DRM_VOID *f_pvPolicyCallbackData,
				 IN DRM_POLICY_CALLBACK_TYPE f_dwCallbackType,
				 IN const DRM_VOID *f_pv) {
  return DRM_SUCCESS;
}

DRM_RESULT DecryptFile() {
  DRM_RESULT dr = DRM_SUCCESS;
  DRM_ENVELOPED_FILE_CONTEXT oEnvFile = { 0 };
  const DRM_CONST_STRING *apdcsRights[1] = { NULL };
  DRM_DECRYPT_CONTEXT oDecryptContext;
  DRM_AES_COUNTER_MODE_CONTEXT ctrContext = { 0 };

  ProcessEnvelope(g_inBuf, g_inSize, &oEnvFile);
  g_outBuf += oEnvFile.oEnvHeader.dwFileDataOffset;
  g_outSize -= oEnvFile.oEnvHeader.dwFileDataOffset;

  apdcsRights[0] = &g_dstrWMDRM_RIGHT_PLAYBACK;
  ChkDR(Drm_Reader_Bind(g_poAppContext, apdcsRights, NO_OF(apdcsRights), (DRMPFNPOLICYCALLBACK)BindCallback,
			NULL, &oDecryptContext));

  // Drm_Envelope_InitializeRead - needed for RC4, not for AES CTR
  MEMCPY((DRM_BYTE *)&oEnvFile.oDecrypt, (DRM_BYTE *)&oDecryptContext, SIZEOF(DRM_CIPHER_CONTEXT));
  ChkBOOL(oEnvFile.oDecrypt.eCipherType == eDRM_AES_COUNTER_CIPHER, DRM_E_UNSUPPORTEDALGORITHM);

  ChkDR(Drm_Reader_Commit(g_poAppContext, NULL, NULL));

  // Drm_Envelope_Read - oEnvFile.oEnvHeader.dwFileDataOffset
  ctrContext.qwBlockOffset = DRM_UI64(0 / DRM_AES_BLOCKLEN);
  ctrContext.bByteOffset = (DRM_BYTE)(0 % DRM_AES_BLOCKLEN);
  ctrContext.qwInitializationVector = oEnvFile.qwInitialCipherCounter;
  ChkArg(oEnvFile.oDecrypt.eCipherType == eDRM_AES_COUNTER_CIPHER);
  ChkDR(DRM_Aes_CtrProcessData(&oEnvFile.oDecrypt.cipher.cipherAES.aesKey, g_outBuf, g_outSize, &ctrContext));
	
ErrorExit:
  return dr;
}

DRM_RESULT ProcessResponse(DRM_BYTE *responseBuf, DRM_DWORD responseSize) {
  DRM_RESULT dr = DRM_SUCCESS;
  DRM_LICENSE_RESPONSE oLicenseResponse = { eUnknownProtocol, 0 };
  const DRM_CONST_STRING *rgpdstrRights[1] = { 0 };
  DRM_DECRYPT_CONTEXT oDecryptContext;
  
  ChkDR(Drm_LicenseAcq_ProcessResponse(g_poAppContext, NULL, NULL, responseBuf, responseSize, &oLicenseResponse));

  rgpdstrRights[ 0 ] = &g_dstrWMDRM_RIGHT_PLAYBACK;
  ChkDR(Drm_Reader_Bind(g_poAppContext, rgpdstrRights, NO_OF(rgpdstrRights),
			(DRMPFNPOLICYCALLBACK)BindCallback, NULL, &oDecryptContext));
  ChkDR(Drm_Reader_Commit(g_poAppContext, NULL, NULL));

ErrorExit:
  return dr;
}

DRM_RESULT AcquireLicense(DRM_BYTE *headerBuf, DRM_DWORD headerSize,
			  DRM_CHAR *charCustomData, DRM_DWORD customDataSize) {
  DRM_RESULT dr = DRM_SUCCESS;
  DRM_ENVELOPED_FILE_CONTEXT oEnvFile = { 0 };
  const DRM_CONST_STRING *rgpdstrRights[1] = { 0 };
  rgpdstrRights[ 0 ] = &g_dstrWMDRM_RIGHT_PLAYBACK;

  ChkDR(ProcessEnvelope(headerBuf, headerSize, &oEnvFile));
  dr = Drm_LicenseAcq_GenerateChallenge(g_poAppContext, rgpdstrRights, NO_OF(rgpdstrRights),
					NULL, charCustomData, customDataSize, NULL, &g_cchUrl,
					NULL, NULL, NULL, &g_cbChallenge);
  if (dr != DRM_E_BUFFERTOOSMALL) {
    goto ErrorExit;
  }

  ChkMem(g_pchUrl = (DRM_CHAR *)Oem_MemAlloc(g_cchUrl + 1));
  ChkMem(g_pbChallenge = (DRM_BYTE *)Oem_MemAlloc(g_cbChallenge + 1));
  ChkDR(Drm_LicenseAcq_GenerateChallenge(g_poAppContext, rgpdstrRights, NO_OF(rgpdstrRights),
					 NULL, charCustomData, customDataSize, g_pchUrl, &g_cchUrl,
					 NULL, NULL, g_pbChallenge, &g_cbChallenge));
  // NULL Terminate
  g_pbChallenge[g_cbChallenge] = '\0';
  g_pchUrl[g_cchUrl] = '\0';
ErrorExit:
  return dr;
}

DRM_RESULT InitDrm() {
  DRM_RESULT dr = DRM_SUCCESS;
  DRM_BYTE *pbRevocationBuffer = NULL;

  g_poAppContext = (DRM_APP_CONTEXT *)Oem_MemAlloc(SIZEOF(DRM_APP_CONTEXT));
  ZEROMEM(g_poAppContext, SIZEOF(DRM_APP_CONTEXT));
  if (g_poAppContext != NULL) {
    ChkDR(Drm_Initialize(g_poAppContext, (DRM_VOID *)&g_dstrDrmPath, &g_dstrStoreName));
#if DRM_SUPPORT_REVOCATION
    ChkMem(pbRevocationBuffer = (DRM_BYTE *)Oem_MemAlloc( REVOCATION_BUFFER_SIZE ));
    ChkDR(Drm_Revocation_SetBuffer(g_poAppContext, pbRevocationBuffer, REVOCATION_BUFFER_SIZE));
#endif
    // Remove old licenses
    ChkDR(Drm_StoreMgmt_CleanupStore(g_poAppContext, NULL, 0, NULL));
  }
ErrorExit:
  return dr;
}

void DeallocDrm() {
  if (g_poAppContext != NULL) {
    Drm_Uninitialize(g_poAppContext);
  }
  Oem_MemFree(g_poAppContext);
  g_poAppContext = NULL;
}

jint
Java_com_sezmi_playready_PlayReadyDRM_jniInit( JNIEnv* env,
					       jobject thiz,
					       jstring drmPath,
					       jstring storeName )
{
  DRM_RESULT dr = DRM_SUCCESS; 
  const char *ansiDrmPath = (*env)->GetStringUTFChars(env, drmPath, NULL);
  const char *ansiStoreName = (*env)->GetStringUTFChars(env, storeName, NULL);
  g_dstrStoreName = CharToConstDRMString(ansiStoreName);
  g_dstrDrmPath = CharToConstDRMString(ansiDrmPath);
  dr = InitDrm();
  (*env)->ReleaseStringUTFChars(env, drmPath, ansiDrmPath);
  (*env)->ReleaseStringUTFChars(env, storeName, ansiStoreName);
  return (jint)dr;
}

void
Java_com_sezmi_playready_PlayReadyDRM_jniDealloc( JNIEnv* env,
						  jobject thiz )
{
  DeallocDrm();
}

jint
Java_com_sezmi_playready_PlayReadyDRM_jniAcquireLicense( JNIEnv* env,
							 jobject thiz,
							 jbyteArray header,
							 jstring customData )
{
  DRM_RESULT dr = DRM_SUCCESS;
  char *charHeader = (*env)->GetByteArrayElements(env, header, NULL);
  int headerSize = (int)(*env)->GetArrayLength(env, header);
  char *charCustomData = NULL;
  int customDataSize = 0;
  if (customData != NULL) {
    charCustomData = (char *)(*env)->GetStringUTFChars(env, customData, NULL);
    customDataSize = (int)(*env)->GetStringUTFLength(env, customData);
  }
  dr = AcquireLicense((DRM_BYTE *)charHeader, (DRM_DWORD)headerSize,
		      (DRM_CHAR *)charCustomData, (DRM_DWORD)customDataSize);
  (*env)->ReleaseByteArrayElements(env, header, charHeader, 0);
  if (customData != NULL) {
    (*env)->ReleaseStringUTFChars(env, customData, charCustomData);
  }
  return (jint)dr;
}

jstring
Java_com_sezmi_playready_PlayReadyDRM_jniGetLicenseRequest( JNIEnv *env,
							    jobject thiz )
{
  jstring retval = NULL;
  if (g_pbChallenge != NULL) {
    retval = (*env)->NewStringUTF(env, (const char *)g_pbChallenge);
    Oem_MemFree(g_pbChallenge);
    g_pbChallenge = NULL;
    g_cbChallenge = 0;
  }
  return retval;
}

jstring
Java_com_sezmi_playready_PlayReadyDRM_jniGetLicenseURL( JNIEnv *env,
							jobject thiz )
{
  jstring retval = NULL;
  if (g_pchUrl != NULL) {
    retval = (*env)->NewStringUTF(env, (const char *)g_pchUrl);
    Oem_MemFree(g_pchUrl);
    g_pchUrl = NULL;
    g_cchUrl = 0;
  }
  return retval;
}

jint
Java_com_sezmi_playready_PlayReadyDRM_jniProcessResponse( JNIEnv *env,
							  jobject thiz,
							  jstring response )
{
  DRM_RESULT dr = DRM_SUCCESS;
  const char *ansiResponse = (*env)->GetStringUTFChars(env, response, NULL);
  int responseSize = (int)(*env)->GetStringLength(env, response);
  dr = ProcessResponse((DRM_BYTE *)ansiResponse, (DRM_DWORD)responseSize);
  (*env)->ReleaseStringUTFChars(env, response, ansiResponse);
  return (jint)dr;
}

jint
Java_com_sezmi_playready_PlayReadyDRM_jniDecryptBuffer( JNIEnv *env,
							jobject thiz,
							jbyteArray buf )
{
  DRM_RESULT dr = DRM_SUCCESS;
  g_inSize = (DRM_DWORD)(*env)->GetArrayLength(env, buf);
  g_inBuf = Oem_MemAlloc(g_inSize);
  (*env)->GetByteArrayRegion(env, buf, 0, (jsize)g_inSize, (DRM_BYTE *)g_inBuf);
  g_outBuf = g_inBuf;
  g_outSize = g_inSize;
  dr = DecryptFile();
  return (jint)dr;
}

jbyteArray
Java_com_sezmi_playready_PlayReadyDRM_jniGetDecrypted( JNIEnv *env,
						       jobject thiz )
{
  jbyteArray retval = NULL;
  if (g_outBuf != NULL) {
    retval = (*env)->NewByteArray(env, (jsize)g_outSize);
    (*env)->SetByteArrayRegion(env, retval, 0, (jsize)g_outSize, (const jbyte *)g_outBuf);
    Oem_MemFree(g_inBuf);
    g_inBuf = NULL;
    g_outBuf = NULL;
    g_inSize = 0;
    g_outSize = 0;
  }
  return retval;
}

