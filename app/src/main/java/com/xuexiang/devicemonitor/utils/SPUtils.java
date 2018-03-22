/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.devicemonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * SharedPreferences工具类
 * @author xuexiang
 * @date 2018/2/15 下午2:03
 */
public final class SPUtils {

    //==================================SharedPreferences实例获取==================================================//
    /**
     * 获取默认SharedPreferences实例
     * @return
     */
    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 获取SharedPreferences实例
     * @param spName
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context, String spName) {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    //=======================================键值保存==================================================//
    /**
     * 设置boolean值
     * @param sp SharedPreferences实例
     * @param key
     * @param value
     */
    public static boolean putBoolean(SharedPreferences sp, String key, boolean value) {
        return sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 设置float值
     * @param sp SharedPreferences实例
     * @param key
     * @param value
     */
    public static boolean putFloat(SharedPreferences sp, String key, float value) {
        return sp.edit().putFloat(key, value).commit();
    }

    /**
     * 设置long值
     * @param sp SharedPreferences实例
     * @param key
     * @param value
     */
    public static boolean putLong(SharedPreferences sp, String key, long value) {
        return sp.edit().putLong(key, value).commit();
    }

    /**
     * 设置String值
     * @param sp SharedPreferences实例
     * @param key
     * @param value
     */
    public static boolean putString(SharedPreferences sp, String key, String value) {
        return sp.edit().putString(key, value).commit();
    }

    /**
     * 设置int值
     * @param sp SharedPreferences实例
     * @param key
     * @param value
     */
    public static boolean putInt(SharedPreferences sp, String key, int value) {
        return sp.edit().putInt(key, value).commit();
    }

    //=======================================键值获取==================================================//
    /**
     * 根据key获取boolean值
     * @param sp SharedPreferences实例
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(SharedPreferences sp, String key, boolean defValue) {
        try {
            return sp.getBoolean(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 根据key获取long值
     * @param sp SharedPreferences实例
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(SharedPreferences sp, String key, long defValue) {
        try {
            return sp.getLong(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 根据key获取float值
     * @param sp SharedPreferences实例
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(SharedPreferences sp, String key, float defValue) {
        try {
            return sp.getFloat(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 根据key获取String值
     * @param sp SharedPreferences实例
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(SharedPreferences sp, String key, String defValue) {
        try {
            return sp.getString(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    /**
     * 根据key获取int值
     * @param sp SharedPreferences实例
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(SharedPreferences sp, String key, int defValue) {
        try {
            return sp.getInt(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(SharedPreferences sp, String key, Object defaultObject) {
        try {
            if (defaultObject instanceof String) {
                return sp.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sp.getLong(key, (Long) defaultObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultObject;
    }


    //=======================================公共方法==================================================//
    /**
     * 查询某个key是否已经存在
     * @param sp SharedPreferences实例
     * @param key
     * @return
     */
    public static boolean contains(SharedPreferences sp, String key) {
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     * @param sp SharedPreferences实例
     * @return
     */
    public static Map<String, ?> getAll(SharedPreferences sp) {
        try {
            return sp.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean remove(SharedPreferences sp, String key) {
        return sp.edit().remove(key).commit();
    }

    /**
     * 清空销毁
     * @param sp SharedPreferences实例
     */
    public static boolean clear(SharedPreferences sp) {
        return sp.edit().clear().commit();
    }

}
