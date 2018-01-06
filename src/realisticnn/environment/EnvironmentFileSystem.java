package realisticnn.environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentFileSystem extends Environment { // incomplete, do not use. could cause catastrophic damage

    private boolean readOnly;
    private volatile boolean[] bits;
    private volatile List<File> listing;
    private volatile File opened;

    public EnvironmentFileSystem() {
        super();
        readOnly = true;
        bits = new boolean[8];
        listing = new ArrayList<>();
        opened = null;

    }

    @Override
    public synchronized void interact(int action) {
        switch (action) {
            case 0: // cases 0-7: toggle 8 bits
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                bits[action] = !bits[action];
                break;
            case 8: // add byte before selected
                break;
            case 9: // delete selected byte
                break;
            case 10: // move cursor left
               break;
            case 11: // move cursor right
                break;
            case 12: // move up listing
                break;
            case 13: // move down listing
                break;
            case 14: // move up a directory
                break;
            case 15: // open selected directory
                break;
            case 16: // open selected file
                break;
            case 17: // close selected file
                break;
            case 18: // edit selected directory or file name
                break;
            case 19: // delete selected file or directory
                break;
            case 20: // create file
                break;
            case 21: // create directory
                break;
            default: // do nothing
                break;
        }
    }

    @Override
    public synchronized double[] getStimuli() {
        return null; //TODO
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
