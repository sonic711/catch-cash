package com.sean.batch.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.micrometer.common.util.StringUtils;

/**
 * 命令列執行流程
 */
public class CommandProcess {

    /** 指令解析 */
    private final static Pattern COMMAND_PATTERN = Pattern.compile("(-{1,2}\\w+)=?\"?([^\"]*)\"?");

    /** 是否為命令列執行 */
    public static boolean executable(final String... args) {
        if (args != null) {
            for (String arg : args) {
                if (StringUtils.isNotBlank(arg)) {
                    Matcher matcher = COMMAND_PATTERN.matcher(arg);
                    if (matcher.find()) {
                        String key = matcher.group(1);
                        String value = matcher.group(2);
                        return VersionCommand.executable(key);
                    }
                }
            }
        }
        return false;
    }

    /** 執行 */
    public static void execute(final String... args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (StringUtils.isNotBlank(arg)) {
                Matcher matcher = COMMAND_PATTERN.matcher(arg);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    if (VersionCommand.executable(key)) {
                        printHandler(i, VersionCommand.execute(key));
                        continue;
                    }
                }
            }
            printHandler(i, "Command Line parameter unrecognized");
        }
    }

    private static void printHandler(final int index, final String value) {
        System.out.println("Parameter-" + index + "  " + value);
    }
}
