/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssdownloader.utility;

/**
 *
 * @author Jie Yu
 */
public class Html {
    
    static public String removeTag(String str) {
        char buf[] = str.toCharArray();
        int strlen = str.length();
        str = "";
        int i = 0;
        boolean inTag = false;
        while (i < strlen) {
            if (buf[i] == '<') {
                inTag = true;
            } else if (buf[i] == '>') {
                inTag = false;
            } else {
                if (!inTag) {
                    str += buf[i];
                }
            }
            i++;
        }
        return str;
    }
    
}
