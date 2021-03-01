package org.devio.as.proj.hi_hotfix;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.devio.as.proj.hi_hotfix.ReflectUtil.findField;
import static org.devio.as.proj.hi_hotfix.ReflectUtil.findMethod;

public class HotFix {
    public static void fix(Context context, String dexFilePath) throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        ClassLoader loader = context.getClassLoader();
        Field pathListField = findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);

        ArrayList<File> files = new ArrayList<>();
        files.add(new File(dexFilePath));

        Object[] patchDexElements = makeDexElements(dexPathList, files, loader);
        expandFieldArray(dexPathList, patchDexElements);

        Oat2Dex.triggerPMDexOptOnDemand(context, dexFilePath, FileUtil.optimizedPathFor(new File(dexFilePath)));
    }

    private static Object[] makeDexElements(Object dexPathList, ArrayList<File> files, ClassLoader loader) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method makeDexElements = findMethod(dexPathList, "makeDexElements", List.class, File.class, List.class, ClassLoader.class);
        ArrayList<IOException> exceptions = new ArrayList<>();
        Object[] objects = (Object[]) makeDexElements.invoke(dexPathList, files, null, exceptions, loader);
        return objects;
    }

    private static void expandFieldArray(Object instance, Object[] extraElements) throws IllegalAccessException, NoSuchFieldException {
        Field dexElementsFiled = findField(instance, "dexElements");
        Object[] original = (Object[]) dexElementsFiled.get(instance);
        //构建新的element数组，先把修复bug的dexelement数组放进去，再把原来的放进去，最后更改到dexpathList对象的dexelement
        //getComponentType得到数组中元素的类型 ，我们知道是Element,但是无法直接访问到该类。
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length + extraElements.length);
        System.arraycopy(extraElements, 0, combined, 0, extraElements.length);
        System.arraycopy(original, 0, combined, extraElements.length, original.length);
        dexElementsFiled.set(instance, combined);
    }
}
