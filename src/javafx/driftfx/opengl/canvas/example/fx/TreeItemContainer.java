package javafx.driftfx.opengl.canvas.example.fx;

import javafx.application.Application;

public class TreeItemContainer {
    public String title;
    public Object channel;
    public Application dataSet;

    static final int TYPE_STRING = 0;
    static final int TYPE_DATASET = 1;
    static final int TYPE_CHANNEL = 2;

    public TreeItemContainer(String title) {
        this.title = title;
    }

    public TreeItemContainer(Object channel) {
        this.channel = channel;
        this.title = channel.toString();
    }

    public TreeItemContainer(Application dataSet) {
        this.dataSet = dataSet;
        this.title = dataSet.toString();
    }

    public int getType() {
        if (channel != null) {
          return TYPE_CHANNEL;
        }
        else if (dataSet != null) {
          return TYPE_DATASET;
        }

        return TYPE_STRING;
    }

    public String getName() {
        return title;
    }

}
