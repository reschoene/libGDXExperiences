package br.com.reschoene.mariobros.util;

public class StrFmt {
    public static String format(String str, int... numbers){
        String res = str;
        for(int i: numbers)
            res = res.replaceFirst("%d", String.valueOf(i));
        return res;
    }

    public static String format(String str, String... strs){
        String res = str;
        for(String s: strs)
            res = res.replaceFirst("%s", s);
        return res;
    }

    public static String zeroPad(int number, int count){
        String str = String.valueOf(number);
        while(str.length() < count)
            str = "0" + str;

        return str;
    }
}
