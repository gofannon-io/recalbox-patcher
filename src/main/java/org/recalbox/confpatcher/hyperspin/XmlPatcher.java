package org.recalbox.confpatcher.hyperspin;

import java.util.regex.Pattern;

class XmlPatcher {
    
    public static String parse(String initialXml) {
        //@formatter:off
       return Pattern.compile(initialXml)
               .splitAsStream("(?=&)")
               .map(XmlPatcher::fix).collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
               .toString();
       //@formatter:on
    }
    
    static String fix(String xmlExtract ) {
        if( xmlExtract.matches("\\&[a-z]+;.*"))
            return xmlExtract;
        return xmlExtract.replace("&", "&amp;");
    }

}
