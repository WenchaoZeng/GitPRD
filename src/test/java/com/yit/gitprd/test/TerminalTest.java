package com.yit.gitprd.test;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: clive
 * @date: 2018/06/07
 * @since: 1.0
 */
public class TerminalTest {

    @Test
    public void openInfinder() {
        Process process = null;
        String command = "open /Users/clive/gitPRD/master";

        List<String> processList = new ArrayList<String>();

        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : processList) {
            System.out.println(line);
        }

    }
}
