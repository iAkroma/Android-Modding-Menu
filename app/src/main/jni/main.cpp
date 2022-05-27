#include <pthread.h>
#include <jni.h>
#include <Includes/Utils.h>
#include <Substrate/SubstrateHook.h>
#include "KittyMemory/MemoryPatch.h"
#include <Icon.h>
#include <vector>


#include "Canvas/ESP.h"
#include "Canvas/Bools.h"
#include "Canvas/StructsCommon.h"


extern "C" {

char* libName = "libil2cpp.so";
ESP espOverlay;


JNIEXPORT jstring JNICALL
Java_il2cpp_Main_getTitle(JNIEnv *env, jobject activityObject) {
	return env->NewStringUTF("Modded by Wish | For Buy Hack -->> @Libfucker");
}



std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

void hexChange(bool &var, MemoryPatch &patch) {
	var = !var;
	if (var) {
		patch.Modify();
	} else {
		patch.Restore();
	}
}

std::string to_string(int param)
{
    std::string str = "";
    for(str = ""; param ; param /= 10)
        str += (char)('0' + param % 10);
    reverse(str.begin(), str.end());
	if (str == "") str = "0";
    return str;
}

std::string fto_string(float param) {
	std::string str = "";
	int num = (int) (param * 100);
	str = to_string(num);
	if (str.size() > 2) str.insert(str.size() - 2, ",");
	return str;
}

JNIEXPORT jobjectArray  JNICALL
Java_il2cpp_Main_getFeatures(JNIEnv *env, jobject activityObject) { jobjectArray ret;
    // PAGE_Name_Icon_Lines
	// SWITCH_Page_Line_Feature_Text_Size
	// SLIDER_Page_Line_Feature_Text_Min_Max_Current_Size
	// BUTTON_Page_Line_Feature_Text_Size
	// INPUT_Page_Line_Feature_Text_Size
	// TITLE_Page_Line_Text_Size
	// ARROW_Page_Line_Feature_Texts_Size
	
    const char *features[] = {
            "PAGE_Player_player.png_2",   // 0
			"PAGE_Visuals_visuals.png_2", // 1
			"PAGE_Other_cfg.png_1",
			"PAGE_Test_cfg.png_1",
			"PAGE_Config_cfg.png_1",// 2
			// ------ Player  > 0 ------  //
			//"TITLE_0_0_Weapon_13"
    };

    int Total_Feature = (sizeof features /
                         sizeof features[0]); //Now you dont have to manually update the number everytime;

    ret = (jobjectArray) env->NewObjectArray(Total_Feature, env->FindClass("java/lang/String"),
                                             env->NewStringUTF(""));
    int i;
    for (i = 0; i < Total_Feature; i++)
        env->SetObjectArrayElement(ret, i, env->NewStringUTF(features[i]));
    return (ret);
} 
bool logg, desync = false;

JNIEXPORT void JNICALL
Java_il2cpp_Main_OnChange(JNIEnv *env, jobject activityObject, jint feature, jint value, jstring strv, jboolean check) {jobjectArray ret;
	std::string str_value = jstring2string(env, strv);
	switch (feature) {
		
		
	}
}

using namespace std;std::string utf16le_to_utf8(const std::u16string &u16str) {    if (u16str.empty()) { return std::string(); }    const char16_t *p = u16str.data();    std::u16string::size_type len = u16str.length();    if (p[0] == 0xFEFF) {        p += 1;        len -= 1;    }    std::string u8str;    u8str.reserve(len * 3);    char16_t u16char;    for (std::u16string::size_type i = 0; i < len; ++i) {        u16char = p[i];        if (u16char < 0x0080) {            u8str.push_back((char) (u16char & 0x00FF));            continue;        }        if (u16char >= 0x0080 && u16char <= 0x07FF) {            u8str.push_back((char) (((u16char >> 6) & 0x1F) | 0xC0));            u8str.push_back((char) ((u16char & 0x3F) | 0x80));            continue;        }        if (u16char >= 0xD800 && u16char <= 0xDBFF) {            uint32_t highSur = u16char;            uint32_t lowSur = p[++i];            uint32_t codePoint = highSur - 0xD800;            codePoint <<= 10;            codePoint |= lowSur - 0xDC00;            codePoint += 0x10000;            u8str.push_back((char) ((codePoint >> 18) | 0xF0));            u8str.push_back((char) (((codePoint >> 12) & 0x3F) | 0x80));            u8str.push_back((char) (((codePoint >> 06) & 0x3F) | 0x80));            u8str.push_back((char) ((codePoint & 0x3F) | 0x80));            continue;        }        {            u8str.push_back((char) (((u16char >> 12) & 0x0F) | 0xE0));            u8str.push_back((char) (((u16char >> 6) & 0x3F) | 0x80));            u8str.push_back((char) ((u16char & 0x3F) | 0x80));            continue;        }    }    return u8str;}typedef struct _monoString {    void *klass;    void *monitor;    int length;    const char *toChars(){        u16string ss((char16_t *) getChars(), 0, getLength());        string str = utf16le_to_utf8(ss);        return str.c_str();    }    char chars[0];    char *getChars() {        return chars;    }    int getLength() {        return length;    }    std::string get_string() {                return std::string(toChars());    }} monoString;monoString *CreateMonoString(const char *str) {    monoString *(*String_CreateString)(void *instance, const char *str) = (monoString *(*)(void *, const char *))getAbsoluteAddress("libil2cpp.so", 0x17FDA04);     return String_CreateString(NULL, str);}



JNIEXPORT void JNICALL
Java_il2cpp_typefaces_Menu_DrawOn(JNIEnv *env, jclass type, jobject espView, jobject canvas) {
	espOverlay = ESP(env, espView, canvas);
	if (espOverlay.isValid()){
		
	}
} 

}
// ---------- Hooking ---------- //
 



void *hack_thread(void *) {
	
    ProcMap il2cppMap;
    do {
        il2cppMap = KittyMemory::getLibraryMap(libName);
        sleep(1);
    } while (!isLibraryLoaded(libName));
	
	//MSHookFunction((void *) getAbsoluteAddress("libil2cpp.so", 0x1239980), (void *) Auth, (void **) &Ticket); 
    return NULL;
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *globalEnv;
    vm->GetEnv((void **) &globalEnv, JNI_VERSION_1_6);
	
	_JavaVM* publicJVM = vm; 
	publicJVM->GetEnv((void **) &globalEnv, JNI_VERSION_1_6); 
	
    // Create a new thread so it does not block the main thread, means the game would not freeze
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);

    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM *vm, void *reserved) {}
