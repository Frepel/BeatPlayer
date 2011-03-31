package com.beatax;

import java.io.File;
import java.io.FilenameFilter;

public class BeatFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}
