package me.lucyy.common.format.blocked;

import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public class BlockedGradient {
    private final TextColor[] cols;
    private final String[] names;
    public BlockedGradient(String[] names, TextColor... cols) {
        this.names = names;
        this.cols = cols;
    }

    public BlockedGradient(String name, TextColor... cols) {
        this.names = new String[]{name};
        this.cols = cols;
    }

    public String[] getNames() {
        return names;
    }

    public TextColor[] getCols() {
        return cols;
    }
}