package org.devio.as.proj.plugin

class LargeBitmapChecker {
    public static String BITMAP_CHECK_METHOD_STATEMENT = "{\n" +
            "if (drawable != null) {\n" +
            "            int drawableWidth = drawable.getIntrinsicWidth();\n" +
            "            int drawableHeight = drawable.getIntrinsicHeight();\n" +
            "            post(new Runnable() {\n" +
            "                @Override\n" +
            "                public void run() {\n" +
            "                    int width = getWidth();\n" +
            "                    int height = getHeight();\n" +
            "                    if (width > 0 && height > 0) {\n" +
            "                        if (drawableWidth >= 2 * width && drawableHeight >= 2 * height) {\n" +
            "                            android.util.Log.e(\"LargeBitmapChecker\", \"bitmap:[\" + drawableWidth + \",\" + drawableHeight + \"],view:[\" + width + \",\" + height + \"],className:\" + getContext().getClass().getSimpleName());\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            });\n" +
            "        }" +
            "}"
}